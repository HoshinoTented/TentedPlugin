package tented.handle.plugin.guessing

import tented.util.random

/**
 * Fingers
 * @author Hoshino Tented
 * @date 2018/1/22 15:15
 */

private val fingerMap : Map<String, Finger> =
        mapOf(
                "剪刀" to Scissors,
                "布" to Paper,
                "石头" to Stone
             )

abstract class Finger : Comparable<Finger>
{
    abstract val value : Int

    override fun compareTo(other : Finger) : Int
    {
        val compareResult = value - other.value

        return when(compareResult)
        {
            -2 -> 1
            2 -> -1

            else -> compareResult
        }
    }

    override fun toString() : String =
            when(value)
            {
                0 -> "石头"
                1 -> "布"
                2 -> "剪刀"

                else -> throw Exception("不被预知的错误发生了")
            }
}

object Paper : Finger()
{
    override val value : Int get() = 1
}

object Scissors : Finger()
{
    override val value : Int get() = 2
}

object Stone : Finger()
{
    override val value : Int get() = 0
}

fun forName( name : String ) : Finger = fingerMap[name] ?: throw IllegalArgumentException("Finger name is not in the range: $name")
fun randomFinger() : String = listOf("剪刀", "石头", "布").random()