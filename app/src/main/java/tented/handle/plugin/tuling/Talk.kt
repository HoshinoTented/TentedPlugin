package tented.handle.plugin.tuling

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.handle.Handler
import tented.handle.plugin.Main
import tented.util.getPath
import tented.util.times

/**
 * Talk
 * @author Hoshino Tented
 * @date 2018/1/24 3:00
 */
object Talk : Handler("图灵聊天", "1.0")
{
    private val keys : List<String> = tented.file.File.readLines(tented.file.File.getPath("Tuling.txt"))
    private val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |@机器人 [MESSAGE]
                |${Main.splitChar * Main.splitTimes}
                |使用图灵接口, 使用此功能前请先在主界面设置自己的apiKey(可设置多个)
            """.trimMargin()

    override fun handle(msg : PluginMsg) : Boolean
    {
        if( msg.msg == name ) msg.addMsg(Type.MSG, message)
        else if( msg.ats.isNotEmpty() && msg.ats[0].uin == PluginMsg(PluginMsg.TYPE_GET_LOGIN_ACCOUNT).send()?.uin )
        {
            for( it in keys )
            {
                if( it != "" )
                {
                    try
                    {
                        invoke(it, msg, handle)
                    }
                    catch (e : ApiKeyException)
                    {
                        continue
                    }
                    catch (e : TulingFormatException)
                    {
                        msg.addMsg(Type.MSG, "请求发生错误: 请联系作者(但她可能并不会来理这个bug)或自行下载源码修改")
                    }

                    break
                }
            }
        }

        return true
    }

}