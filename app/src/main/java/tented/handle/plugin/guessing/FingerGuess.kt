package tented.handle.plugin.guessing

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.times
import tented.handle.Handler
import tented.handle.plugin.Main
import tented.handle.plugin.Money

/**
 * FingerGuess
 * @author Hoshino Tented
 * @date 2018/1/22 15:11
 */
object FingerGuess : Handler("猜拳游戏", "1.0")
{
    private const val leastMoney : Long = 500L
    private const val highestMoney : Long = 999999L

    private val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |猜拳 [剪刀|石头|布] [MONEY]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("猜拳 (剪刀|石头|布) [0-9]+")) ->
            {
                val money = msg.msg.substring(msg.msg.lastIndexOf(' ') + 1).toLong()

                if( money in leastMoney..highestMoney )
                {
                    if( msg.member.enoughMoney(money) )
                    {
                        val name = msg.msg.substring(3, msg.msg.lastIndexOf(' '))
                        val robotFingerName = randomFinger()

                        val finger = forName(name)
                        val robotFinger = forName(robotFingerName)

                        msg.reAt()
                        when (finger.compareTo(robotFinger))
                        {
                            1 ->
                            {
                                msg.member.money += money

                                msg.addMsg(Type.MSG, "\n恭喜你!你获胜了, 赢得了$money${Money.moneyUnit}${Money.moneyName}\n你出的是$name, 我出的是$robotFinger")
                            }

                            0 -> msg.addMsg(Type.MSG, "\n平局了, 你没有损失任何东西...")

                            - 1 ->
                            {
                                msg.member.money -= money

                                msg.addMsg(Type.MSG, "\n很遗憾, 你失败了, 损失了$money${Money.moneyUnit}${Money.moneyName}\n你出的是$name, 我出的是$robotFinger")
                            }
                        }
                    }
                    else msg.addMsg(Type.MSG, "你的${Money.moneyName}不够哦...")
                }
                else msg.addMsg(Type.MSG, "猜拳所用的${Money.moneyName}只能在$leastMoney~${highestMoney}的范围内哦")
            }
        }

        return true
    }
}