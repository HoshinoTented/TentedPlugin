package tented.handle.plugin.shop.chess

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.bag.Bag
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Handler
import tented.handle.plugin.Main
import tented.handle.plugin.Money
import tented.io.Data
import tented.shop.Shop

/**
 * Created by Hoshino Tented on 2018/1/4.
 */
object ChessShop : Handler("井棋商店", "1.0")
{
    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name ->
            {
                val shop = Shop(File.getPath("${msg.group}/Shop/chess.json"))
                val builder = StringBuilder("$name\n${Main.splitChar * Main.splitTimes}\n")

                for( item in shop )
                {
                    builder.appendln(item.name + "(${item.id})")
                    builder.appendln("价格: ${item.info["price"]}")
                    builder.appendln(Main.splitChar * Main.splitTimes)
                }

                builder.append("购买井字棋[ID]\n使用井字棋[ID]\n井字棋背包\n你的棋子:${File.read(File.getPath("${msg.group}/Chess/${msg.uin}.cfg"), "chess", "无")}")

                msg.addMsg(Type.MSG, builder.toString())
            }

            msg.msg == "井字棋背包" ->
            {
                val shop = Shop(File.getPath("${msg.group}/Shop/chess.json"))
                val bag = Bag(File.getPath("${msg.group}/Chess/Bag/${msg.uin}.json"))
                val builder = StringBuilder("${msg.uinName}的井字棋背包...\n")

                for( item in bag.items.keys() )
                {
                    val name = shop.getItemById(item.toString())!!.name

                    builder.append(name)
                    builder.appendln("($item)")
                }

                builder.append("使用井字棋[ID]")

                msg.addMsg(Type.MSG, builder.toString())
            }

            msg.msg.matches(Regex("购买井字棋.+")) ->
            {
                val shop = Shop(File.getPath("${msg.group}/Shop/chess.json"))
                val id = msg.msg.substring(5)
                val item = shop.getItemById(id)

                if( item != null )
                {
                    val money = msg.member.money
                    val newMoney = money - item.info["price"].toString().toLong()

                    if( newMoney > -1 )
                    {
                        val bag = Bag(File.getPath("${msg.group}/Chess/Bag/${msg.uin}.json"))

                        msg.member.money = newMoney

                        bag.add(item.id, 1)
                        Data.save(File.getPath("${msg.group}/Chess/Bag/${msg.uin}.json"), bag.toString())       //记得保存qaq

                        msg.addMsg(Type.MSG, "购买成功")
                    }
                    else msg.addMsg(Type.MSG, "购买失败, 你没有足够的${Money.moneyName}")
                }
                else msg.addMsg(Type.MSG, "购买失败: 商店里不存在id为${id}的棋子")
            }

            msg.msg.matches(Regex("使用井字棋.+")) ->
            {
                val shop = Shop(File.getPath("${msg.group}/Shop/chess.json"))
                val id = msg.msg.substring(5)
                val bag = Bag(File.getPath("${msg.group}/Chess/Bag/${msg.uin}.json"))

                if( bag.items.has(id) )
                {
                    val chess = shop.getItemById(id)!!.name[0]
                    File.write(File.getPath("${msg.group}/Chess/${msg.uin}.cfg"), "chess", chess)

                    msg.addMsg(Type.MSG, "使用成功")
                }
                else msg.addMsg(Type.MSG, "使用井字棋失败: 背包内没有id为${id}的棋子")
            }
        }

        return true
    }
}