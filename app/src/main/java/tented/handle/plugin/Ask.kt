package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Handler
import java.util.regex.Pattern

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
object Ask : Handler("问答系统", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |问答 [ASK] -> [ANSWER]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    fun asksSet( group : Long ) : Set<Any> = File.keySet(File.getPath("$group/Ask.cfg"))

    operator fun get( group : Long, key : String ) = File.read(File.getPath("$group/Ask.cfg"), key, "")
    operator fun set( group : Long, key : String, value : String ) = File.write(File.getPath("$group/Ask.cfg"), key, value)

    override fun handle(msg : PluginMsg) : Boolean
    {
        msg.addMsg(Type.MSG, get(msg.group, msg.msg))
        msg.send()
        msg.clearMsg()

        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)

            msg.member.master ->
            {
                when
                {
                    msg.msg.matches(Regex("问答 .+ -> .*")) ->
                    {
                        val matcher = Pattern.compile("问答 (.+) -> (.*)").matcher(msg.msg)
                        matcher.matches()       //必定成功

                        val ask = matcher.group(1)
                        val answer = matcher.group(2)

                        if( answer == null || answer == "" ) File.remove(File.getPath("${msg.group}/Ask.cfg"), ask)
                        else set(msg.group, ask, answer)

                        msg.addMsg(Type.MSG, "添加成功")
                    }
                }
            }
        }

        return true
    }
}