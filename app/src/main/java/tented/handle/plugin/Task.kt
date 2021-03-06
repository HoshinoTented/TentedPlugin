package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.hold
import tented.util.times
import tented.handle.Handler
import java.util.Calendar
import java.util.Timer

/**
 * Task
 * @author Hoshino Tented
 * @date 2018/1/17 16:38
 */

object Task : Handler("延时提醒", "1.0")
{
    class TimerTask( val member : tented.util.Member , val message : String ) : java.util.TimerTask()
    {
        override fun run()
        {
            val msg = PluginMsg()

            msg.group = member.group

            msg.addMsg(Type.AT, "${member.uin}@${member.name}")
            msg.addMsg(Type.MSG, " $message")

            msg.send()
        }
    }

    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |设置任务 [YYYY] [MM] [DD] [HH] [MM] [SS] [MESSAGE]
                |${Main.splitChar * Main.splitTimes}
                |一次设置消耗10000${Money.moneyUnit}${Money.moneyName}哦~~~
            """.trimMargin()

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("设置任务 \\d{4} \\d{2} \\d{2} \\d{2} \\d{2} \\d{2} .+")) ->
            {
                val newMoney = msg.member.money - 10000

                if (newMoney > - 1)
                {
                    val arguments = msg.msg.split(" ")
                    val message = arguments.subList(7, arguments.size)

                    val calendar = Calendar.getInstance()
                    calendar.set(arguments[1].toInt(), arguments[2].toInt() - 1, arguments[3].toInt(), arguments[4].toInt(), arguments[5].toInt(), arguments[6].toInt())

                    if( calendar.after(Calendar.getInstance()) )
                    {
                        msg.member.money = newMoney

                        Timer().schedule(TimerTask(msg.member, message.hold()), calendar.time)

                        msg.addMsg(Type.MSG, "设置成功啦~")
                    }
                    else msg.addMsg(Type.MSG, "唔。。时间为什么会比现在还早呢。。？")
                }
                else msg.addMsg(Type.MSG, "你没有足够的${Money.moneyName}来着。。。")
            }
        }

        return true
    }
}