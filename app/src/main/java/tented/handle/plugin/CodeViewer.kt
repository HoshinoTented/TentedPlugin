package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.times
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2017/12/28.
 */
object CodeViewer : Handler("代码查看", "1.0")           //xml和json代码的查看, 懒得写复读
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |暂无指令
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    private fun send( message : String )        //以后再搞群发送和控制台发送的切换, 反正现在是懒得要死
    {
        saki.demo.Demo.debug(message)
    }

    private fun doView( msg : PluginMsg )       //执行获取xml或json代码
    {
        val code =
                when
                {
                    msg.xml != "" -> msg.xml
                    msg.json != "" -> msg.json

                    else -> null
                }

        if( code != null ) send(code)
    }


    override fun handle(msg : PluginMsg) : Boolean
    {
        doView(msg)

        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
        }

        return true
    }

}