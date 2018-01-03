package tented.extra

import java.util.regex.Pattern

/**
 * Created by Hoshino Tented on 2018/1/3.
 *
 * 一些类的拓展。。
 */

infix fun Number.randomTo( to : Number ) : Long = this.toLong() + (Math.random() * (to.toLong() - this.toLong())).toLong()

fun Boolean.toInt() = if(this) 1 else 0
fun Int.toBoolean() = this != 0
fun Any?.toUnit() = Unit        //用于把任何对象转化为Unit...因为setters如果使用简单写法的话, 而且那一条表达式还有返回值的话, 就会爆炸
fun String.isNumber() : Boolean = this.matches(Regex("[+\\-]?[0-9]+"))
fun String.matchGroups(regex : String) : Array<String>
{
    val matcher = Pattern.compile(regex).matcher(this)

    return if(matcher.matches())
    {
        Array(
                matcher.groupCount() + 1,
                {
                    matcher.group(it)
                }
                                  )
    }
    else arrayOf()
}

operator fun String.times(times : Number) : String
{
    if( times.toLong() < 0 ) throw IllegalArgumentException("times: $times can not lower than zero")

    val builder = StringBuilder("")

    for( i in 1..times.toLong() ) builder.append(this)

    return builder.toString()
}