package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.gal.GalCompiler
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2018/1/5.
 */
object Gal : Handler("文字冒险", "1.0")
{
    private val compilers = HashMap<Long, GalCompiler>()
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |载入脚本
                |选择[CHOOSE]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg == "载入脚本" ->
            {
                compilers[msg.group] = GalCompiler.newInstance(File.read(File.getPath("${msg.group}/Gal/script.tgs")))

                msg.addMsg(Type.MSG, "载入完毕")
            }

            msg.msg.matches(Regex("选择.+")) ->
            {
                compilers[msg.group]?.handle(
                        msg.msg.substring(2),
                        {
                            script ->

                            val builder = StringBuilder("你选择了${script.title}...\n")

                            builder.appendln(script.context)

                            for( choose in script.chooses ) builder.appendln(">>" + choose)

                            builder.append("你")

                            builder.append(if( script.chooses.isNotEmpty() ) "的选择是:" else "没有选择")

                            msg.addMsg(Type.MSG, builder.toString())
                        }
                                            )
            }
        }

        return true
    }
}