package tented.handle.plugin.shop.system

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import org.json.JSONArray
import org.json.JSONObject
import tented.bag.Bag
import tented.extra.getPath
import tented.extra.times
import tented.handle.Plugin
import tented.handle.plugin.Main
import tented.handle.plugin.Money
import tented.io.Data
import tented.shop.Item
import tented.shop.Shop

/**
 * Created by Hoshino Tented on 2017/12/26.
 */
object SystemShop : Plugin("系统商店", "1.0")
{
    operator fun get(group : Long) : Shop = Shop(tented.file.File.getPath("$group/Shop/shop.json"))
    operator fun set(group : Long , shop : Shop) = Data.save(tented.file.File.getPath("$group/Shop/shop.json"), shop.toString())

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            val shop : Shop = get(msg.group)
            val builder : StringBuilder = StringBuilder("$name\n${Main.splitChar * Main.splitTimes}\n")

            for( item in shop ) builder.append(item.name).append("(${item.id})").append("\n").append(item.info["info"]).append("\n").append(Main.splitChar * Main.splitTimes).append("\n")

            builder.append("购买[ID] [COUNT]\n背包")

            msg.addMsg(Type.MSG, builder.toString())
        }

        else if( msg.msg.matches(Regex("购买.+ [0-9]+")) )
        {
            val bagPath : String = tented.file.File.getPath("${msg.group}/Bag/${msg.uin}.json")

            val bag : Bag = Bag(bagPath)
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
            val bag : Bag = Bag(tented.file.File.getPath("${msg.group}/Bag/${msg.uin}.json"))
            val items : JSONObject = bag.items

            val builder : StringBuilder = StringBuilder("${msg.uinName}背包中的物品有...\n${Main.splitChar * Main.splitTimes}\n")

            for( item in items.keys() )
            {
                val key : String = item.toString()

                builder.append("$key >> ${items[key]}\n")
            }

            builder.append(Main.splitChar * Main.splitTimes)

            msg.addMsg(Type.MSG, builder.toString())
        }

        else if( msg.msg.matches(Regex("使用.+")) )
        {
            val id : String = msg.msg.substring(2)
            val function : ((tented.member.Member) -> Unit)? = SystemItems.items[id]

            if( function == null ) msg.addMsg(Type.MSG, "哎呀, 好像这个物品无法使用呢...")
            else
            {
                val bagPath = tented.file.File.getPath("${msg.group}/Bag/${msg.uin}.json")
                val bag : Bag = Bag(bagPath)

                if( bag.items.has(id) && bag.items.getInt(id) > 0 )
                {

                    bag.remove(id, 1)

                    if( bag.items.getInt(id) == 0 ) bag.items.remove(id)        //clear zero item

                    Data.save(bagPath, bag.toString())

                    function(msg.member)

                    msg.addMsg(Type.MSG, "使用完毕")
                }
                else msg.addMsg(Type.MSG, "你没有这个物品了啦!!!")
            }
        }
    }
}