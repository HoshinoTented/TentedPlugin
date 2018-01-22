package tented.util

import java.util.Calendar
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

fun <T> List<T>.random() : T =
        if( isNotEmpty() ) get((0 randomTo size).toInt())
        else throw IllegalArgumentException("List should be not empty")

fun <T> Iterable<T>.toStringList() : List<String> = map { it.toString() }
fun <T> Iterable<T>.hold() : String
{
    val builder = StringBuilder()

    forEach {
        builder.append(it.toString())
    }

    return builder.toString()
}

/**
 * 只比较日期
 * 我也不知道为什么要写这个方法
 * @param calendar 比较的指定对象
 * @return 日期是否相同(年月日)
 */
fun Calendar.dateEquals( calendar : Calendar ) : Boolean
{
    setOf(
            Calendar.YEAR,
            Calendar.MONTH,
            Calendar.DAY_OF_MONTH
         ).forEach {
        if( get(it) != calendar.get(it) ) return false
    }

    return true
}

fun getCalendarInstance( date :String ) : Calendar
{
    val matcher = Pattern.compile("([0-9]{4})/([0-9]{2})/([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})").matcher(date)

    return if( matcher.matches() )
    {
        val calendar = Calendar.getInstance()

        calendar.set(matcher.group(1).toInt(), matcher.group(2).toInt() - 1, matcher.group(3).toInt(), matcher.group(4).toInt(), matcher.group(5).toInt(), matcher.group(6).toInt())
        calendar
    }
    else throw NumberFormatException("format is wrong")
}

operator fun String.times(times : Number) : String
{
    if( times.toLong() < 0 ) throw IllegalArgumentException("times: $times can not lower than zero")

    val builder = StringBuilder("")

    for( i in 1..times.toLong() ) builder.append(this)

    return builder.toString()
}