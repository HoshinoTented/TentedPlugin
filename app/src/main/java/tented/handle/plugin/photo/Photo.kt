package tented.handle.plugin.photo

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Plugin
import tented.handle.plugin.Main

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Photo : Plugin("百度搜图", "1.0")
{
    val message : String =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |搜图[KEYWORD]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
        }

        else if( msg.msg.matches(Regex("搜图.+")) )
        {
            val keyWord : String = msg.msg.substring(2)
            val url : String = Searcher.search(keyWord)

            msg.addMsg(Type.IMAGE, url)
        }
    }
}