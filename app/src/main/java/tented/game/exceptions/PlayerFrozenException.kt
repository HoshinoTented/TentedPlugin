package tented.game.exceptions

import tented.util.Member

/**
 * Created by Hoshino Tented on 2017/12/29.
 */
class PlayerFrozenException(player : Member) : Exception("player ( ${player.uin} in ${player.group}) was frozen")