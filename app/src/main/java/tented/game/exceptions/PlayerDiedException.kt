package tented.game.exceptions

import tented.member.Member

/**
 * Created by Hoshino Tented on 2017/12/29.
 */
class PlayerDiedException( player : Member) : Exception("${player.uin} in group ${player.group} died, can not join the game")