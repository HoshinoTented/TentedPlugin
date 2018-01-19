package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Handler
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * PingAddress
 * @author Hoshino Tented
 * @date 2018/1/12 22:19
 */
object PingAddress : Handler("Ping!", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |PING [IP]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("(?i)ping .+")) ->
            {
                val runtime = Runtime.getRuntime()
                val process = runtime.exec("ping -c 3 ${msg.msg.substring(5)}")
                val reader = BufferedReader(InputStreamReader(process.inputStream))

                val builder = StringBuilder()

                while( true )
                {
                    val buffer :String? = reader.readLine()

                    if( buffer != null ) builder.append(buffer) else break
                }

                process.waitFor()

                msg.addMsg(Type.MSG, builder)
            }
        }

        return true
    }

}