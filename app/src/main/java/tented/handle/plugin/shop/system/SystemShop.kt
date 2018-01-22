package tented.handle.plugin.shop.system

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import org.json.JSONObject
import tented.bag.Bag
import tented.util.getPath
import tented.util.times
import tented.handle.Handler
import tented.handle.plugin.Main
import tented.handle.plugin.Money
import tented.io.Data
import tented.shop.Item
import tented.shop.Shop

/**
 * Created by Hoshino Tented on 2017/12/26.
 *
 * 系统商店
 * item json基本格式
 * {
 *      "id":"id", "name":"name",
 *      "price":Long[,
 *      ...more properties]
 * }
 */
object SystemShop : Handler("系统商店", "1.1")
{
    operator fun get(group : Long) : Shop = Shop(tented.file.File.getPath("$group/Shop/shop.json"))
    operator fun set(group : Long , shop : Shop) = Data.save(tented.file.File.getPath("$group/Shop/shop.json"), shop.toString())

    override fun handle(msg : PluginMsg) : Boolean
    {
        if( msg.msg == name )
        {
            val shop : Shop = get(msg.group)
            val builder = StringBuilder("$name\n${Main.splitChar * Main.splitTimes}\n")

            for( item in shop )
                builder.append(
                                    """
                                        |${item.name}(${item.id})
                                        |价格: ${item.info.getLong("price")}
                                        |${item.info.getString("info")}
                                        |${Main.splitChar * Main.splitTimes}
                                        |
                                    """.trimMargin()
                              )
            builder.append("购买[ID] [COUNT]\n使用物品[ID] [ARGS]\n背包")

            msg.addMsg(Type.MSG, builder.toString())
        }

        else if( msg.msg.matches(Regex("购买.+ [0-9]+")) )
        {
            val bagPath : String = tented.file.File.getPath("${msg.group}/Bag/${msg.uin}.json")

            val bag = Bag(bagPath)
            val shop : Shop = get(msg.group)

            val space : Int = msg.msg.lastIndexOf(' ')
            val id : String = msg.msg.substring(2, space)
            val count : Long = java.lang.Long.parseLong(msg.msg.substring(space + 1))

            val item : Item? = shop[id]

            if( item != null )
            {
                val price : Long = item.info.getLong("price")

                if( msg.member.money < (price * count) ) msg.addMsg(Type.MSG, "${Money.moneyName}不足${price * count}噢")
                else
                {
                    msg.member.money -= (price * count)

                    bag.add(item.id, count)
                    Data.save(bagPath, bag.toString())

                    msg.addMsg(Type.MSG, "购买成功")
                }
            }
            else msg.addMsg(Type.MSG, "id指向的item不存在")
        }

        else if( msg.msg == "背包" )
        {
            val bag = Bag(tented.file.File.getPath("${msg.group}/Bag/${msg.uin}.json"))
            val items : JSONObject = bag.items

            val builder = StringBuilder("${msg.uinName}背包中的物品有...\n${Main.splitChar * Main.splitTimes}\n")

            for( item in items.keys() )
            {
                val key : String = item.toString()

                builder.append("$key >> ${items[key]}\n")
            }

            builder.append(Main.splitChar * Main.splitTimes)

            msg.addMsg(Type.MSG, builder.toString())
        }

        else if( msg.msg.matches(Regex("使用物品.+")) )
        {
            val space : Int = msg.msg.indexOf(' ')
            val id : String = msg.msg.substring(4, if( space == -1 ) msg.msg.length else space)     //截取id, 到最近的一个空格, 按照规定, id不能包含空格( 使用_代替 ), 这个是为了适应某些道具使用需要参数...
            val function : ((PluginMsg) -> Unit)? = SystemItems.items[id]

            if( function == null ) msg.addMsg(Type.MSG, "哎呀, 好像这个物品无法使用呢...")
            else
            {
                val bagPath = tented.file.File.getPath("${msg.group}/Bag/${msg.uin}.json")
                val bag = Bag(bagPath)

                if( bag.remove(id, 1) )     //更新了ShopSystem, remove具有返回值, 是否删除成功, 所以直接这样写就好了, 而且还会自动清理0count的item
                {
                    //bag.remove(id, 1)

                    //if( bag.items.getInt(id) == 0 ) bag.items.remove(id)        //clear zero item

                    Data.save(bagPath, bag.toString())

                    function(msg)       //进行使用处理

                    msg.addMsg(Type.MSG, "使用完毕")
                }
                else msg.addMsg(Type.MSG, "你没有这个物品了啦!!!")
            }
        }

        return true
    }
}