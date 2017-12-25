package tented.handle.plugin.ban

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Plugin
import tented.handle.plugin.Main

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Ban : Plugin("违禁系统", "1.1")
{
    val message : String =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |[MASTER]添加违禁词[WORD]
                |[MASTER]删除违禁词[WORD]
                |违禁词列表
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
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
        }
    }
}