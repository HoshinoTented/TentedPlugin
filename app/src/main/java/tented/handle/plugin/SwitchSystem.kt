package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2017/12/31.
 */
object SwitchSystem : Handler("插件开关", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |[GLOBAL]开启插件 [NAME]
                |[GLOBAL]关闭插件 [NAME]
                |${Main.splitChar * Main.splitTimes}
                |如果要看了开启哪几个插件
                |请发送 插件版本
            """.trimMargin()

    operator fun get(clazz : String) : Boolean = tented.file.File.read(tented.file.File.getPath("switch.cfg"), clazz, "true") == "true"
    operator fun set(clazz : String, mod : Boolean) = tented.file.File.write(tented.file.File.getPath("switch.cfg"), clazz, mod.toString())

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("(开启|关闭)插件 .+")) ->
            {
                val pluginName = msg.msg.substring(5)
                val filterResult = tented.handle.PluginLoader.pluginArray.filter { it.name == pluginName }

                if( filterResult.isNotEmpty() )
                {
                    set(filterResult[0].javaClass.name, msg.msg.startsWith("开启"))

                    Settings.globalSuccess(msg)
                }
                else msg.addMsg(Type.MSG, "没有找到名为 $pluginName 的插件")
            }
        }

        return true
    }
}