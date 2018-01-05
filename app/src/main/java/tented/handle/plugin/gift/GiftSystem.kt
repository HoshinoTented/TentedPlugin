package tented.handle.plugin.gift

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import org.json.JSONArray
import org.json.JSONObject
import tented.extra.getPath
import tented.extra.randomTo
import tented.extra.times
import tented.file.File
import tented.handle.Handler
import tented.handle.plugin.Main
import tented.handle.plugin.Money
import tented.shop.Item
import tented.shop.Shop

/**
 * Created by Hoshino Tented on 2018/1/5.
 */
object GiftSystem : Handler("礼包系统", "1.0")
{
    /**
     * {
     *      "id":"id", "name":"name", "price":price, "info":"info", "gifts":
     *      [
     *          {"id":"id", "probability":probability(0~100), "count":count(max count)}
     *      ]
     * }
     */
    class Gift(jsonObject : JSONObject) : Item(jsonObject)
    {
        val price : Long get() = info.getLong("price")
        val gifts : JSONArray get() = info.getJSONArray("gifts")
    }

    object GiftShop : Handler("礼包商店", "1.0")
    {
        operator fun get( group : Long ) : Shop = Shop(File.getPath("$group/Shop/gifts.json"))

        override fun handle(msg : PluginMsg)
        {
            when
            {
                msg.msg.matches(Regex("查看礼包.+")) ->
                {
                    val id = msg.msg.substring(4)
                    val shop = get(msg.group)
                    val item = shop.getItemById(id)

                    if( item != null )
                    {
                        val pack = Gift(item.info)
                        val gifts = pack.gifts

                        val builder = StringBuilder("${pack.name}(${pack.id})的礼包详情...\n${pack.info.getString("info")}\n价格: ${pack.info.getLong("price")}\n可能的礼物有...\n")

                        var index = -1

                        while( ++ index < gifts.length() )
                        {
                            val gift = gifts.getJSONObject(index)

                            builder.appendln(">>${gift.getString("id")}(1~${gift.getInt("count")})  几率: ${gift.getInt("probability")}%")
                        }

                        msg.addMsg(Type.MSG, builder)
                    }
                    else msg.addMsg(Type.MSG, "查看礼包失败: 礼包商店内没有此礼包")
                }

                msg.msg.matches(Regex("购买礼包.+")) ->
                {
                    val id = msg.msg.substring(4)
                    val shop = get(msg.group)
                    val item = shop.getItemById(id)

                    if( item != null )
                    {
                        val money = item.info.getLong("price")
                        val newMoney = msg.member.money - money

                        if( newMoney > -1 )
                        {
                            msg.member.money = newMoney

                            val bag = msg.member.bag

                            bag.add(id, 1)

                            msg.member.bag = bag

                            msg.addMsg(Type.MSG, "购买成功了呢~")
                        }
                        else msg.addMsg(Type.MSG, "你没有足够的$money${Money.moneyUnit}${Money.moneyName}")
                    }
                    else msg.addMsg(Type.MSG, "没有在礼包商店找到id为${id}的礼包")
                }
            }
        }
    }

    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |礼包商店
                |使用礼包[ID]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg == "礼包商店" ->
            {
                val shop : Shop = GiftShop[msg.group]
                val builder = StringBuilder("${msg.msg}...\n${Main.splitChar * Main.splitTimes}\n")

                for( gift in shop ) builder.appendln(gift.id)

                builder.append("${Main.splitChar * Main.splitTimes}\n查看礼包[ID]\n购买礼包[ID]")

                msg.addMsg(Type.MSG, builder)
            }

            msg.msg.matches(Regex("使用礼包.+")) ->
            {
                val id = msg.msg.substring(4)
                val bag = msg.member.bag

                if( bag.remove(id, 1) )
                {
                    val gift = GiftShop[msg.group].getItemById(id)

                    if( gift != null )
                    {
                        val giftItem = Gift(gift.info)
                        val gifts = giftItem.gifts
                        val builder = StringBuilder("使用成功!获得了...\n")

                        var index = -1

                        while( ++ index < gifts.length() )
                        {
                            val item = gifts.getJSONObject(index)

                            if( ( 0 randomTo 100 ) in 1..item.getInt("probability") )
                            {
                                val count = 1 randomTo (item.getInt("count") + 1)
                                val itemId = item.getString("id")

                                bag.add(itemId, count)
                                builder.appendln(">>$itemId * $count")
                            }
                        }

                        msg.member.bag = bag

                        msg.addMsg(Type.MSG, builder)
                    }
                    else msg.addMsg(Type.MSG, "这个东西不是礼包了啦, 反正你都用了, 不会还你的就是了")
                }
                else msg.addMsg(Type.MSG, "使用失败: 背包里没有id为${id}的物品哦")
            }
        }
    }
}