package tented.handle.plugin

import com.saki.aidl.PluginMsg
import tented.util.isNumber
import tented.handle.Handler
import tented.handle.PluginLoader

/**
 * NumberCall
 * @author Hoshino Tented
 * @date 2018/1/14 21:41
 */
object NumberCall : Handler("数字召唤", "1.0")
{
    val message =
            """
                |这个就不写message了, 毕竟也没啥功能...
            """.trimMargin()

    private fun doNumberCall( msg : PluginMsg ) : Boolean
    {
        return if( msg.msg.isNumber() )
        {
            val index = msg.msg.toInt() - 1

            if (index < PluginLoader.pluginList.size)
            {
                val handler = PluginLoader.pluginList[index]

                msg.msg = handler.name

                msg.clearMsg()
                handler.handle(msg)
                msg.send()

                true
            }
            else false
        }
        else false
    }

    override fun handle(msg : PluginMsg) : Boolean
    {
        if( msg.msg.isNumber() )
        {
            doNumberCall(msg)

            msg.clearMsg()
        }

        return true
    }
}