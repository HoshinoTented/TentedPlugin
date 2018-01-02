package tented.game.exceptions

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
class PlayerNotEnoughException(leastCount : Int) : Exception("The player count must be at least $leastCount")