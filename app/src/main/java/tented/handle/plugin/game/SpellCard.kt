package tented.handle.plugin.game

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.times
import tented.game.exceptions.PlayerDiedException
import tented.game.exceptions.PlayerFrozenException
import tented.game.spell.SpellCardPlayer
import tented.game.spell.SpellCardShop
import tented.game.spell.cards.Card
import tented.game.spell.exceptions.NoCardFoundException
import tented.handle.Plugin
import tented.handle.plugin.Main

/**
 * Created by Hoshino Tented on 2017/12/29.
 */
object SpellCard : Plugin("符卡游戏", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |使用符卡[ID] [@]
                |购买符卡[ID]
                |符卡商店
                |我的符卡信息
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    /**
     * 处理拓展技能
     * @param extra 拓展的Map映射
     * @param card 使用的符卡
     * @param msg 被处理的消息包
     */
    private fun extraHandle( other : SpellCardPlayer , extra : Map<*, *> , card : Card , msg : PluginMsg )
    {
        if( extra["freeze"] != null ) msg.addMsg(Type.MSG, "\n${other.name}被你冻结了${extra["freeze"]}秒")
        if( extra["health"] != null ) msg.addMsg(Type.MSG, "\n${other.name}被你治疗了${extra["health"]}点生命值")
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("使用符卡.+ @.+")) ->
            {
                if( msg.ats.isNotEmpty() )
                {
                    val cardId = msg.msg.substring(4, msg.msg.indexOf(' '))         //id will not has space character
                    val item = SpellCardShop[msg.group].getItemById(cardId)

                    if( item != null )
                    {
                        val card = Card(item.info)      //will throw a exception, but we can not catch it0.0

                        val thisPlayer = SpellCardPlayer.newPlayer(msg.member)
                        val otherPlayer = SpellCardPlayer.newPlayer(msg.ats[0])

                        try
                        {
                            val result = thisPlayer.useCard(otherPlayer, card)

                            if( result["missing"] == null )     //没有missing键, 代表对方没有躲避成功
                            {
                                msg.addMsg(Type.MSG, "你造成了${result["hurt"]}点伤害\n${otherPlayer.name}的生命值: ${otherPlayer.health}/${otherPlayer.maxHealth}")

                                if(result["death"].toString() == "true")
                                {
                                    msg.addMsg(Type.MSG, "\n你使用${card.name}击倒了${otherPlayer.name}!!!")       //以后再写赏金吧...

                                    //TODO give money or other item....
                                }

                                val extra = result["extra"]

                                if( extra is Map<*, *> ) extraHandle(otherPlayer, extra, card, msg)    //判断类型, 隐式判断null
                            }
                            else msg.addMsg(Type.MSG, "${otherPlayer.name}闪避了你的符卡攻击")
                        }

                        catch ( e : PlayerDiedException )
                        {
                            msg.addMsg(Type.MSG, "玩家已死亡, 无法继续游戏")
                        }

                        catch ( e : NoCardFoundException )
                        {
                            msg.addMsg(Type.MSG, "你没有id为 $cardId 的符卡噢...")
                        }

                        catch ( e : PlayerFrozenException )
                        {
                            msg.addMsg(Type.MSG, "你被冻结了!无法使用符卡")
                        }
                    }
                    else msg.addMsg(Type.MSG, "未找到id为: $cardId 的符卡")
                }
            }

            msg.msg.matches(Regex("购买符卡.+")) ->
            {
                val cardId = msg.msg.substring(4)
                val item = SpellCardShop[msg.group].getItemById(cardId)

                if(item != null)
                {
                    val bag = msg.member.bag
                    bag.add(cardId, 1)

                    msg.member.bag = bag
                    msg.member.money -= item.info.getLong("price")

                    msg.addMsg(Type.MSG, "购买成功!")
                }
                else msg.addMsg(Type.MSG, "符卡商店里没有id为$cardId 的符卡噢")
            }

            msg.msg == "符卡商店" ->
            {
                val builder = StringBuilder("符卡商店...\n${Main.splitChar * Main.splitTimes}\n")
                val shop = SpellCardShop[msg.group]

                for( item in shop )
                {
                    val card = Card(item.info)

                    builder.append(
                                        """
                                            |${card.name}(${card.id})
                                            |伤害范围: ${card.lowHurt}~${card.highHurt}
                                            |${card.info.getString("info")}
                                            |${Main.splitChar * Main.splitTimes}
                                            |
                                        """.trimMargin()
                                  )
                }

                builder.append("发送 购买符卡[ID] 即可购买相应的符卡")

                msg.addMsg(Type.MSG, builder.toString())
            }

            msg.msg == "我的符卡信息" ->
            {
                val player = SpellCardPlayer.newPlayer(msg.member)
                val info =
                        """
                            |${player.name}的符卡游戏信息
                            |${Main.splitChar * Main.splitTimes}
                            |生命值: ${player.health}/${player.maxHealth}
                            |击倒了 ${player.killCount} 个玩家
                            |被击倒了 ${player.deathCount} 次...
                        """.trimMargin()

                msg.addMsg(Type.MSG, info)
            }
        }
    }
}