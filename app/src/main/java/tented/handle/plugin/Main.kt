package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Handler
import tented.handle.PluginLoader

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object Main : Handler("插件版本", "1.0")
{
    val splitTimes : Long = 9L
    val list : HashSet<String> = HashSet()

    val message : String
            get()
            {
                val builder : StringBuilder = StringBuilder("$name\n${"-" * splitTimes}\n")

                builder.append("插件作者: 星野 天忆\n")

                for( plugin in PluginLoader.pluginList ) builder.append("${plugin.name} 版本: ${plugin.version}\n")

                builder.append("-" * splitTimes)

                return builder.toString()
            }

    init
    {
        list.add(name)
    }

    private fun makeMenu() : String
    {
        val builder : StringBuilder = StringBuilder("")

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
        if( msg.msg == "菜单" ) msg.addMsg(Type.MSG, Main.makeMenu())
        else if( msg.msg == name ) msg.addMsg(Type.MSG, message)
    }

}