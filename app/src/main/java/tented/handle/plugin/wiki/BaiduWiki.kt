package tented.handle.plugin.wiki

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.times
import tented.handle.Handler
import tented.handle.plugin.Main
import java.util.regex.Pattern

/**
 * Created by Hoshino Tented on 2017/12/27.
 */
object BaiduWiki : Handler("百度百科", "1.0")
{

    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |百科[KEYWORD]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("百科.+")) ->
            {
                val result = Searcher.search(msg.msg.substring(2))
                val matcher = Pattern.compile("(?s).{0,300}").matcher(result)        //split result         if not do, some long message will not able be sent

                while(matcher.find())
                {
                    msg.clearMsg()
                    msg.addMsg(Type.MSG, matcher.group())
                    msg.send()
                }
            }
        }

        return true
    }

}