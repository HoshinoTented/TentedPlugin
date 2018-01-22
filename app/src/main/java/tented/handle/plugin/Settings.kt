package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.getPath
import tented.util.isNumber
import tented.util.times
import tented.file.File
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2017/12/26.
 */
object Settings : Handler("系统设置", "1.1")
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

    fun globalSuccess( msg : PluginMsg ) = msg.addMsg(Type.MSG, "操作成功, 重启插件后生效\n此次操作为全局操作")


    override fun handle(msg : PluginMsg) : Boolean
    {
        when            //使用when可能更好?毕竟Kotlin的when支持布尔值
        {
            msg.msg == name ->
            {
                val message =
                        """
                            |$name
                            |${Main.splitChar * Main.splitTimes}
                            |(开|关)机  >>  ${if(get(msg.group, "open", "true") == "true") "开启" else "关闭"}
                            |[GLOBAL]设置分隔符符号[CHAR]  >> ${Main.splitChar}
                            |[GLOBAL]设置分隔符数量[COUNT]  >> ${Main.splitTimes}
                            |[GLOBAL]设置刷屏警告上限[NUMBER]  >> ${Main.warningCount}
                            |[GLOBAL]设置刷屏禁言上限[NUMBER]  >> ${Main.shutUpCount}
                            |设置艾特提醒  >>  ${if(get(msg.group, "at") == "true") "开启" else "关闭"}
                            |退出插件
                            |${Main.splitChar * Main.splitTimes}
                        """.trimMargin()

                msg.addMsg(Type.MSG, message)
            }

            msg.member.master ->
                when
                {
                    msg.msg.matches(Regex("设置分隔符(符号|数量).+")) ->
                    {
                        val value = msg.msg.substring(7)
                        val action = msg.msg.substring(5, 7)
                        val key = if( action == "符号" ) "sc" else "st"

                        if( key == "st" && ! value.isNumber() ) msg.addMsg(Type.MSG, "修改失败")
                        else
                        {
                            File.write(File.getPath("config.cfg"), key, value)

                            globalSuccess(msg)
                        }
                    }

                    msg.msg.matches(Regex("设置刷屏(警告|禁言)上限[0-9]+")) ->
                    {
                        val value = msg.msg.substring(8)
                        val action = msg.msg.subSequence(4, 6)
                        val key = if (action == "警告") "wc" else "suc"

                        Main[key] = value

                        globalSuccess(msg)
                    }

                    msg.msg == "设置艾特提醒" ->
                    {
                        val action = get(msg.group, "at") == "true"

                        set(msg.group, "at", (! action).toString())

                        msg.addMsg(Type.MSG, "成功${if(action) "关闭" else "开启"}艾特提醒")
                    }

                    msg.msg == "退出插件" -> System.exit(0)
                }
        }

        return true
    }

}