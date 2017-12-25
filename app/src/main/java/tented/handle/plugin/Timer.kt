package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.MessageHandler
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Timer : MessageHandler
{
    val name : String = "整点报时"
    val groupSet : HashSet<Long> = HashSet()        /*为什么要使用Set?
                                                     * Set集合的特性是 不可重复值
                                                     * 而这个报时系统的缓存正是需要这一点
                                                     * 即使Set无序也不会怎么样, 报时系统还需要顺序???
                                                     */

    val message : String
            get() =
                """
                    |$name
                    |${"-" * Main.splitTimes}
                    |开启报时
                    |关闭报时
                    |${"-" * Main.splitTimes}
                """.trimMargin()

    val thread : Thread = Thread(
                                    Runnable{
                                                while( true )
                                                {
                                                    val date : Date = Date()

                                                    if( SimpleDateFormat("mm:ss").format(date) == "00:00")
                                                    {
                                                        val time : String = SimpleDateFormat("现在时间: yyyy年MM月dd日 HH时mm分ss秒").format(date)
                                                        val msg : PluginMsg = PluginMsg()

                                                        msg.addMsg(Type.MSG, time)

                                                        for( group in groupSet)
                                                        {
                                                            msg.group = group
                                                            msg.send()
                                                        }
                                                    }

                                                    Thread.sleep(1000)
                                                }
                                            }
                                )

    init
    {
        Main.list.add(name)
    }

    operator fun get( group : Long ) : Boolean = File.read(File.getPath("Timer.cfg"), group.toString(), "false") == "true"
    operator fun set( group : Long , mode : Boolean )
    {
        File.write(File.getPath("Timer.cfg"), group.toString(), mode.toString())

        groupSet.add(group)
    }

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message + "\n本群${if(get(msg.group)) "已" else "未"}开启报时\n报时进程状态: ${thread.isAlive}")
        }

        else if( msg.member.master )
        {
            if( msg.msg.matches(Regex("(开启|关闭)报时")) )
            {
                val action : String = msg.msg.substring(0, 2)

                set(msg.group, action == "开启")

                msg.addMsg(Type.MSG, "报时已$action")
            }
        }
    }
}