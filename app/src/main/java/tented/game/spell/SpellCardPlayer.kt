package tented.game.spell

import tented.extra.getPath
import tented.extra.randomTo
import tented.file.File
import tented.game.spell.cards.Card
import tented.game.spell.exceptions.NoCardFoundException
import tented.game.exceptions.PlayerDiedException
import tented.member.Member
import kotlin.reflect.KProperty

/**
 * Created by Hoshino Tented on 2017/12/29.
 */
class SpellCardPlayer private constructor(group : Long, uin : Long, name : String?) : Member(group, uin, name)
{
    inner class InfoManager
    {
        operator fun getValue( _this : Any? , properties : KProperty<*> ) : Int = Integer.parseInt(
                                                                                                        File.read
                                                                                                        (
                                                                                                            infoPath,
                                                                                                            properties.name,
                                                                                                            when(properties.name)       //根据properties name来决定默认值
                                                                                                            {
                                                                                                                "health", "maxHealth" -> "100"
                                                                                                                "missing", "killCount", "deathCount" -> "0"
                                                                                                                "reliveTime" -> "120"

                                                                                                                else -> throw IllegalArgumentException("${properties.name} is not a correct property name")
                                                                                                            }
                                                                                                        )
                                                                                                  )
        operator fun setValue( _this : Any? , properties : KProperty<*> , value : Int ) = File.write(infoPath, properties.name, value.toString())
    }

    companion object
    {
        fun newPlayer( member : Member ) : SpellCardPlayer = SpellCardPlayer(member.group, member.uin, member.name)
    }

    val infoPath = File.getPath("$group/SpellCardGame/Players/$uin.cfg")

    var health : Int by InfoManager()
    var maxHealth : Int by InfoManager()
    var missing : Int by InfoManager()
    var reliveTime : Int by InfoManager()
    var killCount : Int by InfoManager()
    var deathCount : Int by InfoManager()

    private fun isMissing() : Boolean = 1 randomTo 101 in 0..missing
    private fun relive()            //复活函数, 死亡时才会被调用
    {
        Thread(
                    Runnable
                    {
                        Thread.sleep(reliveTime * 1000L)

                        health = maxHealth
                    }
              ).start()
    }

    /**
     * fight with other player
     * @param other The other player
     * @param card Card object
     */
    @Throws(NoCardFoundException::class)
    fun useCard( other : SpellCardPlayer , card : Card ) : Map<String, String>
    {
        if( health > 0 && other.health > 0 )
        {
            val bag = this.bag

            if (bag.remove(card.id, 1))
            {
                this.bag = bag      //保存...

                return  if( ! other.isMissing() )
                        {
                            val hurt = card.lowHurt randomTo (card.highHurt + 1)

                            other.health -= hurt.toInt()

                            val death = other.health < 1

                            if(death)       //如果对方死亡
                            {
                                other.relive()    //调用relive复活函数
                                other.deathCount ++     //自增对方的deathCount

                                killCount ++      //自增本身的killCount
                            }

                            mapOf(
                                    "hurt" to hurt.toString(),
                                    "death" to death.toString()
                                 )
                        }
                        else mapOf( "missing" to "true" )
            }
            else throw NoCardFoundException(card.id)
        }
        else throw PlayerDiedException(this)
    }
}