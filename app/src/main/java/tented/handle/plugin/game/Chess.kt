package tented.handle.plugin.game

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.util.randomTo
import tented.util.times
import tented.util.toInt
import tented.game.chess.ChessGame
import tented.game.chess.ChessPlayer
import tented.game.chess.Pos
import tented.game.chess.exceptions.NoEmptyChessException
import tented.game.exceptions.PlayerNotEnoughException
import tented.handle.Handler
import tented.game.exceptions.HadGameException
import tented.handle.plugin.Main
import tented.handle.plugin.Money

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
object Chess : Handler("井字之棋", "1.1")
{
    private val games = HashMap<Long, ChessGame>()

    private val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |创建井字棋
                |删除井字棋
                |加入井字棋
                |开始井字棋
                |显示棋盘
                |下棋[1-9]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    @Throws(HadGameException::class)
    private fun createNewGame( group : Long )
    {
        if( ! games.containsKey(group) ) games[group] = ChessGame()
        else throw HadGameException
    }

    override fun handle(msg : PluginMsg) : Boolean
    {
        when
        {
            msg.msg == name ->
            {
                val builder = StringBuilder(message)
                val game = games[msg.group]

                builder.append("\n游戏")

                if( game != null )
                {
                    builder.append("存在\n")
                    builder.append(Main.splitChar * Main.splitTimes)
                    builder.append("\n玩家如下:\n")

                    for( player in game.players ) builder.append(">> ${player.uin}\n")
                }
                else builder.append(Type.MSG, "不存在\n")

                builder.append(Main.splitChar * Main.splitTimes)

                msg.addMsg(Type.MSG, builder.toString())
            }

            msg.msg == "创建井字棋" ->
            {
                try
                {
                    createNewGame(msg.group)

                    msg.addMsg(Type.MSG, "游戏创建成功")
                }

                catch ( e : HadGameException)
                {
                    msg.addMsg(Type.MSG, "游戏创建失败: 游戏已存在")
                }
        }

            msg.msg == "删除井字棋" ->
            {
                val game = games[msg.group]

                if( game != null )
                {
                    if (game.contains(msg.member))
                    {
                        games.remove(msg.group)

                        msg.addMsg(Type.MSG, "删除游戏完毕")
                    }
                    else msg.addMsg(Type.MSG, "删除游戏失败: 你不是游戏中的玩家")
                }
                else msg.addMsg(Type.MSG, "删除游戏失败: 游戏未创建")
            }

            msg.msg == "加入井字棋" ->
            {
                val player = ChessPlayer.newInstance(msg.member)

                val game = games[msg.group]

                if( game != null )
                {
                    if(player.join(game)) msg.addMsg(Type.MSG, "加入游戏成功")
                    else msg.addMsg(Type.MSG, "加入游戏失败: 游戏玩家已满或你已加入游戏")
                }
                else msg.addMsg(Type.MSG, "加入游戏失败: 游戏不存在")
            }

            msg.msg == "开始井字棋" ->
            {
                val game = games[msg.group]

                if( game != null )
                {
                    if(game.contains(ChessPlayer.newInstance(msg.member)))
                    {
                        try
                        {
                            game.start()

                            msg.addMsg(Type.MSG, "开始游戏成功\n玩家${game.nowGamingPlayer().uin}先手")
                        }

                        catch ( e : PlayerNotEnoughException )
                        {
                            msg.addMsg(Type.MSG, "玩家人数不足")
                        }
                    }
                    else msg.addMsg(Type.MSG, "开始游戏失败: 玩家未进入游戏")
                }
                else msg.addMsg(Type.MSG, "开始游戏失败: 游戏未创建")
            }

            msg.msg == "显示棋盘" ->
            {
                val game = games[msg.group]

                if( game != null ) msg.addMsg(Type.MSG, game.toString() + "轮到${game.nowGamingPlayer().uin}下棋")
                else msg.addMsg(Type.MSG, "加载棋盘失败: 游戏不存在")
            }

            msg.msg.matches(Regex("下棋[1-9]")) ->
            {
                val game = games[msg.group]

                if( game != null )
                {
                    if( game.contains(msg.member) )
                    {
                        if( game.isStarted )
                        {
                            val player = game.nowGamingPlayer()

                            if (msg.member == player)
                            {
                                val pos = Pos.newInstance(msg.msg[2].toString().toInt() - 1)
                                var wim = false

                                try
                                {
                                    wim = player.setChess(pos)
                                }

                                catch ( e : NoEmptyChessException )
                                {
                                    games.remove(msg.group)

                                    msg.addMsg(Type.MSG, "哎呀...平局呢\n")
                                }

                                msg.addMsg(Type.MSG, "下棋成功!\n" + game)

                                if (wim)
                                {
                                    msg.addMsg(Type.MSG, "你胜利了")

                                    games.remove(msg.group)

                                    val vip = msg.member.isVip()
                                    val money = (0 randomTo 1000) * (vip.toInt() + 1)
                                    msg.member.money += money

                                    msg.addMsg(Type.MSG, "\n获得了$money${Money.moneyUnit}${Money.moneyName}")

                                    if( vip ) msg.addMsg(Type.MSG, "\n[VIP]井字棋游戏金币翻倍")
                                }
                            }
                            else msg.addMsg(Type.MSG, "下棋失败: 尚未轮到你下棋")
                        }
                        else msg.addMsg(Type.MSG, "下棋失败: 游戏尚未开始")
                    }
                    else msg.addMsg(Type.MSG, "下棋失败: 你未参加游戏")
                }
                else msg.addMsg(Type.MSG, "下棋失败: 游戏不存在")
            }
        }

        return true
    }
}