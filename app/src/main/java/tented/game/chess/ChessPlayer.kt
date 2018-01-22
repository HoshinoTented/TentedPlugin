package tented.game.chess

import tented.util.getPath
import tented.util.toBoolean
import tented.file.File
import tented.game.chess.exceptions.HadChessException
import tented.game.chess.exceptions.NoEmptyChessException
import tented.util.Member

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
class ChessPlayer private constructor( group : Long , uin : Long , name : String? ) : Member(group, uin, name)
{
    companion object
    {
        fun newInstance( member : Member ) : ChessPlayer = ChessPlayer(member.group, member.uin, member.name)
    }

    lateinit var game : ChessGame

    var chess : Char
        get() = File.read(File.getPath("$group/Chess/$uin.cfg"), "chess", if( game.gamingPlayers.indexOf(this).toBoolean() ) ChessGame.CHESS_1 else ChessGame.CHESS_0)[0]
        set(value) = File.write(File.getPath("$group/Chess/$uin.cfg"), "chess", value)
    val now : Boolean get() = game.players.indexOf(this).toBoolean() == game.nowGaming

    fun join( game : ChessGame ) : Boolean =
        if( game.addPlayer(this) )
        {
            this.game = game

            true
        }
        else false

    @Throws(HadChessException::class, IllegalArgumentException::class, NoEmptyChessException::class)
    fun setChess( pos : Pos ) : Boolean =
        if(pos.isEmpty(game.chessMap))
        {
            if(chess == game.chess0 || chess == game.chess1)
            {
                pos[game.chessMap] = chess
                game.next()

                game.checkPos(pos)
            }
            else throw IllegalArgumentException("Chess $chess is not a chess")
        }
        else throw HadChessException(pos)
}