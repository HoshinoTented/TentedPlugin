package tented.game.chess.exceptions

import tented.game.chess.Pos

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
class HadChessException(pos : Pos) : Exception("Pos $pos had chess!!!")