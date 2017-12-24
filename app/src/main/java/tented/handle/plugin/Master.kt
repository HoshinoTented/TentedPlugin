package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.MessageHandler

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
class Master : MessageHandler
{
    companion object
    {
        val name : String = "主人系统"

        val message : String =
                """
                    |$name
                    |${"-" * 9}
                    |[MASTER]添加主人@
                    |[MASTER]删除主人@
                    |主人列表
                """.trimMargin()

        init
        {
            Main.list.add(name)
        }


    }

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
        }

        else if( msg.msg == "主人列表" )
        {

        }
    }

}