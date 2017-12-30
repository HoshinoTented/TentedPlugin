package tented.handle.plugin.translate

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Handler
import tented.handle.plugin.Main

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Translate : Handler("百度翻译", "1.0")
{
    val message : String =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |翻译[QUERY]
                |语言代码[QUERY]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
        }

        else if ( msg.msg.matches(Regex("翻译.+")) )
        {
            val query : String = msg.msg.substring(2)
            val result : String = Translation.translate(query)

            msg.addMsg(Type.MSG, "百度翻译...\n请求: $query\n${Main.splitChar * Main.splitTimes}\n返回: $result")
        }

        else if ( msg.msg.matches(Regex("语言代码.+")) )
        {
            val query : String = msg.msg.substring(4)
            val result : String = Translation.getLanguage(query)

            msg.addMsg(Type.MSG, "语言代码...\n请求: $query\n${Main.splitChar * Main.splitTimes}\n返回: $result")
        }
    }
}