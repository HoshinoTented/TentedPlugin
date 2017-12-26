package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Handler
import tented.handle.PluginLoader
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object Main : Handler("插件版本", "1.5")
{
    val splitTimes : Long = java.lang.Long.parseLong(File.read(File.getPath("config.cfg"), "st", "9"))
    val splitChar : String = File.read(File.getPath("config.cfg"), "sc", "-")
    val list : HashSet<String> = HashSet()

    val message : String
            get()
            {
                val builder = StringBuilder("$name\n${splitChar * splitTimes}\n")

                builder.append("插件作者: 星野 天忆\n")

                for( plugin in PluginLoader.pluginList ) builder.append("${plugin.name} 版本: ${plugin.version}\n")

                builder.append(splitChar * splitTimes + "\n本插件源码仓库: https://github.com/LimbolRain/TentedPlugin.git\n如果可以的话star一下啦...")

                return builder.toString()
            }

    init
    {
        list.add(name)
    }

    private fun makeMenu() : String
    {
        val builder = StringBuilder("")

        for( (index, element) in list.withIndex() )
        {
            builder.append(element)

            if( index % 2 == 0 ) builder.append("   ")
            else builder.append("\n")
        }

        return builder.substring(0, builder.length - 1)
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == "菜单" -> msg.addMsg (Type.MSG, Main.makeMenu())
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.ats.isNotEmpty() ->
            {
                val message =
                        """
                            |****Tented*Plugin****
                            |${msg.uinName}在${SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(Date())}at了你
                            |内容为:
                            |${msg.msg}
                            |****Tented*Plugin****
                        """.trimMargin()

                PluginMsg.send(type = PluginMsg.TYPE_SESS_MSG, group = msg.group, uin = msg.ats[0].uin, message = message)
            }
        }
    }

}