package tented.handle.plugin.ban

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Handler
import tented.handle.plugin.Main

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Ban : Handler("违禁系统", "1.2")
{
    val message : String =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |[MASTER]添加违禁词[WORD]
                |[MASTER]删除违禁词[WORD]
                |[MASTER]设置违禁禁言时长[TIME]
                |违禁词列表
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    fun checkBan( msg : com.saki.aidl.PluginMsg ) : Boolean
    {
        if( msg.member.master ) return false                //Master will never be shut up

        val touches : List<String> = tented.handle.plugin.ban.Banner.getWords(msg.group).filter { msg.msg.matches(Regex("(?s)(?i).*$it.*")) }       //加入(?s)(?i)模式匹配符

        return  if( touches.isNotEmpty() )
        {
            val time : Int = touches.size * tented.handle.plugin.ban.Banner[msg.group]
            val builder = StringBuilder("${msg.member.name}\n${Main.splitChar * Main.splitTimes}\n你触犯了以下违禁词...\n")

            for( word in touches ) builder.append(">>$word<<\n")

            builder.append("被禁言了${time}分钟\n${Main.splitChar * Main.splitTimes}")

            msg.member.shut(time)

            msg.clearMsg()
            msg.addMsg(Type.MSG, builder.toString())
            msg.send()

            true
        }
        else false
    }

    override fun handle(msg : PluginMsg) : Boolean
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message + "\n本群违禁禁言时长为: ${Banner[msg.group]}")
        }

        else if( msg.msg == "违禁词列表" )
        {
            val builder : StringBuilder = StringBuilder("本群的违禁词如下...\n${Main.splitChar * Main.splitTimes}\n")

            for( word in Banner.getWords(msg.group) ) builder.append(">>$word<<\n")

            builder.append(Main.splitChar * Main.splitTimes)

            msg.addMsg(Type.MSG, builder.toString())
        }

        else if( msg.member.master )
        {
            if( msg.msg.matches(Regex("(?s)(添加|删除)违禁词.+")) )
            {
                val action : String = msg.msg.substring(0, 2)
                val word : String = msg.msg.substring(5)
                val list : ArrayList<String> = ArrayList(Banner.getWords(msg.group))

                if( action == "添加" )
                {
                    if (list.contains(word)) msg.addMsg(Type.MSG, "违禁词列表里已经有'$word'了哦")
                    else
                    {
                        list.add(word)
                        Banner.setWords(msg.group, list)

                        msg.addMsg(Type.MSG, "添加成功~")
                    }
                }

                else
                {
                    list.remove(word)
                    Banner.setWords(msg.group, list)

                    msg.addMsg(Type.MSG, "删除成功~")
                }
            }

            else if( msg.msg.matches(Regex("设置违禁禁言时长[0-9]{1,5}")) )
            {
                val time : Int = Integer.parseInt(msg.msg.substring(8))

                Banner[msg.group] = time

                msg.addMsg(Type.MSG, "设置完毕")
            }
        }

        return true
    }
}