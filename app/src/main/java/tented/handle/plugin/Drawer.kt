package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.doDraw
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
object Drawer : Handler("绘图系统", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |转图片[TEXT]
                |清除图片缓存
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg == "清除图片缓存" ->
            {
                File.delete(File.getPath("temp"))

                msg.addMsg(Type.MSG, "清除完毕")
            }

            msg.msg.matches(Regex("转图片.+")) ->
            {
                val text = msg.msg.substring(3)
                val hashCode = text.hashCode().toString()

                doDraw(text)

                msg.addMsg(Type.IMAGE, File.getPath("temp/$hashCode.png"))
            }
        }
    }
}