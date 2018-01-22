package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.getPath
import tented.util.times
import tented.file.File
import tented.handle.Handler
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Created by Hoshino Tented on 2018/1/4.
 */
object VIPSystem : Handler("贵族系统", "1.0")
{
    private val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |购买贵族[MONTH]
                |贵族特权
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    private val vip =
            """
                |贵族特权...
                |${Main.splitChar * Main.splitTimes}
                |签到经验和金币翻倍
                |签到获得额外VIP礼包
                |井字棋游戏强制先手
                |井字棋金币翻倍
                |符卡半价购买
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()


    operator fun get( group : Long ) : Long = File.read(File.getPath("$group/Vip.cfg"), "money", 10000).toLong()
    operator fun set( group : Long , value : Long ) = File.read(File.getPath("$group/Vip.cfg"), "money", value)

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message + "\n过期时间:${msg.member.vip}\n一个月贵族${get(msg.group)}${Money.moneyUnit}${Money.moneyName}哦！")
            msg.msg == "贵族特权" -> msg.addMsg(Type.MSG, vip)
            msg.msg.matches(Regex("购买贵族[0-9]+")) ->
            {
                val month = msg.msg.substring(4).toInt()
                val used = month * get(msg.group)
                val newMoney = msg.member.money - used

                if( newMoney > -1 )
                {
                    msg.member.money = newMoney

                    val calendar : Calendar = Calendar.getInstance()
                    calendar.add(Calendar.MONTH, month)

                    msg.member.vip = SimpleDateFormat("yyyy/MM/dd").format(calendar.time)

                    msg.addMsg(Type.MSG, "花费$used${Money.moneyUnit}${Money.moneyName}购买了${month}个月的vip")
                }
                else msg.addMsg(Type.MSG, "${Money.moneyName}不够$used${Money.moneyUnit}哦")
            }
        }

        return true
    }
}