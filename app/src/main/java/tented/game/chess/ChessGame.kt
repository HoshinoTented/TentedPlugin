package tented.game.chess

import tented.extra.toInt
import tented.game.chess.exceptions.NoEmptyChessException
import tented.game.exceptions.PlayerNotEnoughException
import tented.member.Member
import java.util.Random

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
class ChessGame
{
    companion object
    {
        val CHESS_EMPTY = '□'
        //这个是默认的chess
        var CHESS_0 = '○'
        var CHESS_1 = '×'
    }

    lateinit var gamingPlayers : ArrayList<ChessPlayer>

    //这个是玩家的ches
    var chess0 = '○'
    var chess1 = '×'

    val players = HashSet<ChessPlayer>()        //a set of players
    val chessMap = Array(
            3,
            { CharArray(3, { CHESS_EMPTY }) }
                        )

    var nowGaming = false       //false is the index of zero, true is the index of one
    var isStarted = false

    /**
     * 判断目标坐标的所有水平坐标对应的字符是否相同
     * @param pos 所需判断的坐标
     * @return 如果相同, 返回其布尔值
     */
    private fun checkHorizontal( pos : Pos ) : Boolean
    {
        val horizontalPoses = pos.horizontalPoses
        val firstPos = horizontalPoses[0]

        return firstPos[chessMap] == horizontalPoses[1][chessMap] && firstPos[chessMap] == horizontalPoses[2][chessMap]
    }

    /**
     * 判断目标坐标的所有垂直坐标对应的字符是否相同
     * @param pos 所需判断的坐标
     * @return 如果相同, 返回其布尔值
     */
    private fun checkVertical( pos : Pos ) : Boolean
    {
        val verticalPoses = pos.verticalPoses
        val firstPos = verticalPoses[0]

        return firstPos[chessMap] == verticalPoses[1][chessMap] && firstPos[chessMap] == verticalPoses[2][chessMap]
    }

    /**
     * 判断目标坐标所能构造的所有直线上的坐标的字符是否相同
     * @param pos 所需判断的坐标
     * @return 如果至少有一条直线上的坐标字符相同, 返回true, 反之false
     */
    @Throws(NoEmptyChessException::class)
    fun checkPos( pos : Pos ) : Boolean
    {
        /**
         * 需要注意的是
         * 所有的pos都至少需要判断两条直线, 所在水平线和所在垂直线
         * 随后需要判断 是否符合 斜线
         * 如果符合, 则继续判断可构造的斜线上的所有坐标对应的字符
         * 否则 false
         *
         * 然而写这么多其实是为了不想用笨笨的方法(为了性能)
         * 烦躁
         *
         * 结果首先需要判断棋子是否下完。。。（空格为0）
         */

        /**
         * 判断目标坐标的所有能构造的斜线上的坐标对应的字符是否相同
         * @param pos 所需判断的坐标
         * @return 如果相同, 返回其布尔值
         */
        fun checkTilt( pos : Pos ) : Boolean
        {
            /**
             * 首先就是需要先判断是否为中心点(1, 1)
             * 如果是, 就需要判断左斜线和右斜线
             * 否则就只判断一条斜线
             */

            fun checkLeftTile() : Boolean
            {
                val poses = Pos.leftTiltPoses
                val firstPos = poses[0]

                return firstPos[chessMap] == poses[1][chessMap] && firstPos[chessMap] == poses[2][chessMap]
            }

            fun checkRightTilt() : Boolean
            {
                val poses = Pos.rightTiltPoses
                val firstPos = poses[0]

                return firstPos[chessMap] == poses[1][chessMap] && firstPos[chessMap] == poses[2][chessMap]
            }

            return (if( pos.isLeftTilt ) checkLeftTile() else false) || (if( pos.isRightTilt ) checkRightTilt() else false)
        }

        var emptyChessCount = 0

        chessMap.map { emptyChessCount += it.filter { it == CHESS_EMPTY }.size }

        if( emptyChessCount == 0 ) throw NoEmptyChessException

        return checkHorizontal(pos) || checkVertical(pos) || (if( pos.isTilt ) checkTilt(pos) else false)
    }

    @Throws(PlayerNotEnoughException::class)
    fun start()
    {
        if (players.size == 2)
        {
            gamingPlayers = ArrayList(players)
            isStarted = true

            if( ! gamingPlayers[0].isVip() && gamingPlayers[1].isVip() || Random().nextBoolean()) gamingPlayers.reverse()       //如果第二个玩家是vip则强制先手
                                                                                                                                //所以第一个玩家是不是vip决定了第二个玩家是否先手
                                                                                                                                //如果两者都不是vip, 则进行随机玩家
                                                                                                                                //所以第一个玩家还是有优势的

            chess0 = gamingPlayers[0].chess
            chess1 = gamingPlayers[1].chess
        }
        else throw PlayerNotEnoughException(2)
    }

    /**
     * 添加玩家
     * @param player 井字棋玩家对象
     * @return 如果添加成功则返回true, 否则false
     *          添加成功的条件为: 玩家未满 且 玩家列表内不包含该玩家
     */
    fun addPlayer( player : ChessPlayer ) : Boolean = players.size < 2 && ! contains(player) && players.add(player)

    /**
     * 迭代
     */
    fun next()
    {
        nowGaming = !nowGaming
    }

    fun nowGamingPlayer() : ChessPlayer = gamingPlayers[nowGaming.toInt()]

    fun contains( player : Member ) : Boolean = players.filter { it == player }.isNotEmpty()

    override fun toString() : String
    {
        val builder = StringBuilder()

        for( array in chessMap )
        {
            for( chess in array )
            {
                builder.append(chess)
                builder.append(' ')
            }

            builder.append('\n')
        }

        return builder.toString()
    }
}