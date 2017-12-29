package tented.game.spell.exceptions

/**
 * Created by Hoshino Tented on 2017/12/29.
 */
class NoCardFoundException( cardId : String ) : Exception("Card $cardId not found")