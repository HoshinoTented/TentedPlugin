package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.MessageHandler
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Manager : MessageHandler
{
    val name : String = "群管系统"

    val message : String =
            """
                |$name
                |${"-" * Main.splitTimes}
                |禁@ [TIME]
                |踢@
                |改@
                |赞
            """.trimMargin()

    init
    {
        Main.list.add(name)
    }

    private fun succfulMessage( msg : PluginMsg )
    {
        msg.addMsg(Type.MSG, "操作成功")
    }

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
        }

        else if( msg.member.master )
        {
            if( msg.ats.isNotEmpty() )
            {
                when
                {
                    msg.msg.matches(Regex("禁@.+ [0-9]+")) ->
                    {
                        val time : Int = Integer.parseInt(msg.msg.substring(msg.msg.lastIndexOf(' ') + 1))

                        msg.ats[0].shut(time)

                        Manager.succfulMessage(msg)
                    }

                    msg.msg.matches(Regex("踢@.+")) ->
                    {
                        msg.ats[0].remove()

                        Manager.succfulMessage(msg)
                    }

                    msg.msg.matches(Regex("改@.+ .+")) ->
                    {
                        val member : tented.member.Member = msg.ats[0]
                        val index : Int = (member.name?.length ?: 0) +  2

                        val newName : String = msg.msg.substring(index)

                        member.rename(newName)

                        Manager.succfulMessage(msg)
                    }
                }
            }

            if( msg.msg.matches(Regex("赞(自己|我)?")) )
            {
                val date : String = SimpleDateFormat("yyyy/MM/dd").format(Date())

                if( msg.member["favourite"] != date )
                {
                    msg.member.favourite()
                    msg.member["favourite"] = date

                    Manager.succfulMessage(msg)
                }
            }
        }
    }
}