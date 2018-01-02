package tented.game.chess

import kotlin.math.abs

/**
 * Created by Hoshino Tented on 2018/1/2.
 *     ^x
 *     |0 0 0
 *     |0 0 0
 *     |0 0 0
 *      ------------->y
 *
 * so x is vertical
 * y is horizontal
 */

class Pos ( val x : Int , val y : Int )
{
    companion object
    {
        val rightTiltPoses = arrayOf(
                Pos(2, 0),
                Pos(1, 1),
                Pos(0, 2)
                                   )

        val leftTiltPoses = arrayOf(
                Pos(0, 0),
                Pos(1, 1),
                Pos(2, 2)
                                    )

        fun newInstance( index : Int ) : Pos
        {
            val pos = index.toString(3)
            val isFirstLine = pos.length == 1

            val x = if(isFirstLine) 0 else pos[0].toString().toInt()         //check the pos is on the first line, if not, return the first number
            val y = (if(isFirstLine) pos[0] else pos[1]).toString().toInt()

            return Pos(x, y)
        }
    }

    val verticalPoses : Array<Pos>
        get()
        {
            val poses = ArrayList<Pos>()

            for( posX in 0..2 step 1 ) poses.add(Pos(posX, y))

            return poses.toTypedArray()
        }

    val horizontalPoses : Array<Pos>
        get()
        {
            val poses = ArrayList<Pos>()

            for( posY in 0..2 step 1 ) poses.add(Pos(x, posY))

            return poses.toTypedArray()
        }

    val isTilt : Boolean get() = isLeftTilt || isRightTilt

    val isLeftTilt : Boolean get() = x == y
    val isRightTilt : Boolean get() = (abs(x - y) == 2)

    operator fun get( chessMap : Array<CharArray> ) : Char = chessMap[x][y]
    operator fun set( chessMap : Array<CharArray> , char : Char )
    {
        chessMap[x][y] = char
    }

    fun isEmpty(chessMap : Array<CharArray>) : Boolean = get(chessMap) == ChessGame.CHESS_EMPTY
    fun isNotEmpty(chessMap : Array<CharArray>) : Boolean = ! isEmpty(chessMap)

    override fun toString() : String = "[$x][$y]"
}