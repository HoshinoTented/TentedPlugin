package tented.handle.plugin.game

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.bag.Bag
import tented.extra.times
import tented.game.fishing.Fish
import tented.game.fishing.FishShop
import tented.game.fishing.Fisher
import tented.game.fishing.exceptions.NoFishException
import tented.game.fishing.exceptions.NoFishingException
import tented.handle.Handler
import tented.handle.plugin.Main
import tented.handle.plugin.Money
import tented.util.Member

/**
 * Created by Hoshino Tented on 2018/1/1.
 */
object Fishing : Handler("钓鱼游戏", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |钓鱼
                |起竿
                |卖鱼[ID]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    fun hadFish( member : Member )
    {
        val msg = com.saki.aidl.PluginMsg()

        msg.group = member.group
        msg.addMsg(com.saki.aidl.Type.AT, "${member.uin}@${member.name}")
        msg.addMsg(Type.MSG, "\n鱼上钩啦~\n快点发送 起竿 把鱼钓起来吧")

        msg.send()
    }

    fun lostFish( member : Member )
    {
        val msg = com.saki.aidl.PluginMsg()

        msg.group = member.group
        msg.addMsg(com.saki.aidl.Type.AT, "${member.uin}@${member.name}")
        msg.addMsg(Type.MSG, "\n唔...鱼...跑掉了...")

        msg.send()
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg == "钓鱼" ->
            {
                val fisher = Fisher.newInstance(msg.member)

                try
                {
                    fisher.fishing()

                    msg.addMsg(Type.MSG, "开始钓鱼啦~等待水面的反应吧~")
                }

                catch ( e : NoFishException )
                {
                    msg.addMsg(Type.MSG, "你已经在钓鱼了噢!")
                }
            }

            msg.msg == "起竿" ->
            {
                val fisher = Fisher.newInstance(msg.member)

                try
                {
                    val fish : Fish = fisher.takeUp()
                    val bag : Bag = msg.member.bag

                    bag.add(fish.id, 1)

                    msg.member.bag = bag        //记得保存_(>_<)_

                    msg.addMsg(Type.MSG, "成功钓到一条${fish.name}!!!\n已放入你的背包")
                }

                catch ( e: NoFishException )
                {
                    msg.addMsg(Type.MSG, "还没有鱼上钩的说...等一等啦...")
                }

                catch ( e : NoFishingException )
                {
                    msg.addMsg(Type.MSG, "还没有开始钓鱼呢, 快发送 钓鱼 开始钓鱼吧~")
                }
            }

            msg.msg.matches(Regex("卖鱼.+")) ->
            {
                val id = msg.msg.substring(2)
                val bag = msg.member.bag

                if( bag.remove(id, 1) )
                {
                    val fish = FishShop[msg.group].getItemById(id)

                    if( fish != null )
                    {
                        val price = fish.info.getLong("price")

                        msg.member.money += price

                        msg.addMsg(Type.MSG, "出售成功~获得了$price${Money.moneyUnit}${Money.moneyName}呐")
                    }
                    else msg.addMsg(Type.MSG, "你怎么可以笨到把别的东西当成鱼来卖啊...\n虽然卖出去了...可是没有拿到钱啊...")

                    msg.member.bag = bag        //因为删除失败了, 所以下面的那个就不需要保存了, 优化性能
                }
                else msg.addMsg(Type.MSG, "你背包里没有id为${id}的鱼噢...")
            }
        }
    }
}