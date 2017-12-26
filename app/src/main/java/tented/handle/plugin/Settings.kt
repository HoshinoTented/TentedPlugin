package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Plugin

/**
 * Created by Hoshino Tented on 2017/12/26.
 */
object Settings : Plugin("系统设置", "1.0")
{
    operator fun get( group : Long , key : String , default : String = "null") : String = File.read(File.getPath("$group/config.cfg"), key, default)
    operator fun set( group : Long , key : String , value : String ) = File.write(File.getPath("$group/config.cfg"), key, value)

    fun doInit( msg : PluginMsg ) : Boolean         //this function will invoke in loader
    {
        if( msg.member.master )
        {
            if( msg.msg.matches(Regex("(开机|关机)")) )
            {
                val action : Boolean = msg.msg == "开机"

                set(msg.group, "open", action.toString())

                msg.clearMsg()
                msg.addMsg(Type.MSG, msg.msg + "成功")
                msg.send()

                return action
            }
        }

        return get(msg.group, "open", "true") == "true"
    }


    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name ->
            {
                val message =
                        """
                            |$name
                            |${Main.splitChar * Main.splitTimes}
                            |(开|关)机  >>  ${if(get(msg.group, "open", "true") == "true") "开启" else "关闭"}
                            |${Main.splitChar * Main.splitTimes}
                        """.trimMargin()

                msg.addMsg(Type.MSG, message)
            }

            /*      以后再搞, 诶嘿嘿
            msg.member.master ->
                when
                {
                    msg.msg.matches(Regex("设置分隔符.+")) ->
                    {
                        val splitChar = msg.msg.substring(5)


                    }
                }
            */
        }
    }

}