package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.handle.MessageHandler
import java.io.FileInputStream
import java.util.Properties

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Master : MessageHandler
{
    val name : String = "主人系统"

    val message : String =
            """
                |$name
                |${"-" * 9}
                |[MASTER]添加主人@
                |[MASTER]删除主人@
                |主人列表
            """.trimMargin()

    init
    {
        Main.list.add(name)
    }

    fun getMasterList( group : Long ) : List<Any>
    {
        val file : java.io.File = java.io.File(tented.file.File.getPath("$group/Master.cfg"))

        if( file.exists() )
        {
            val properties : Properties = Properties()

            properties.load(FileInputStream(file))

            return properties.keys.filter { properties.getProperty(it.toString(), "false") == "true" }
        }

        return arrayListOf()
    }

    override fun handle(msg : PluginMsg)
    {
        if (msg.msg == name)
        {
            msg.addMsg(Type.MSG, message)
        }
        else if (msg.msg == "主人列表")
        {
            val list : List<Any> = getMasterList(msg.group)
            val builder : StringBuilder = StringBuilder("主人列表如下\n${"-" * 9}")

            for (element in list)
            {
                builder.append(">>$element<<\n")
            }

            builder.append("-" * 9)

            msg.addMsg(Type.MSG, builder.toString())
        }
        else if (msg.member.master)
        {
            if (msg.msg.matches(Regex("(添加|删除)主人@.+")))
            {
                if (msg.ats.isNotEmpty())      //防止文本at导致出错
                {
                    val action : String = msg.msg.substring(0, 2)       //获取动作

                    msg.ats[0].master = action == "添加"

                    msg.addMsg(Type.MSG, "${action}成功")
                }
            }
        }

    }
}