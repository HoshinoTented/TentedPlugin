package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2018/1/2.
 *
 * 这个。。。其实得算是一个杂项
 * 不过可能以后还要写其他的
 * 所以就
 * 先搞成一个Handler子类吧
 */
object Member : Handler("个人信息", "Beta")
{
    private val levelName = arrayOf(
            "无名之辈",
            "有名之辈",
            "有姓之辈",
            "小有名气",
            "中有名气",
            "大有名气",
            "小佬",
            "中佬",
            "大佬",
            "超大佬",
            "巨佬"
                           )

    private fun level( experience : Long ) : Int =
            when(experience)
            {
                in 0..50 -> 0
                in 51..200 -> 1
                in 201..500 -> 2
                in 501..1200 -> 3
                in 1201..2500 -> 4
                in 2501..5000 -> 5
                in 5001..12500 -> 6
                in 12501..50000 -> 7
                in 50000..125200 -> 8
                in 125201..500000 -> 9
                in 500000..Long.MAX_VALUE -> 10

                else -> throw IllegalArgumentException("$experience is not in the range")
            }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name ->
            {
                val exp = msg.member["exp", "0"].toLong()
                val level = level(exp)
                val message =
                        """
                            |$name
                            |${Main.splitChar * Main.splitTimes}
                            |钱包: ${msg.member.money}${Money.moneyUnit}${Money.moneyName}
                            |经验: $exp
                            |等级: Lv.${level + 1} ${levelName[level]}
                            |${ if( msg.member.isVip() ) "[VIP贵族]过期时间: ${msg.member.vip}" else "[普通用户]无特权" }
                            |${Main.splitChar * Main.splitTimes}
                        """.trimMargin()

                msg.addMsg(Type.MSG, message)
            }
        }
    }

}