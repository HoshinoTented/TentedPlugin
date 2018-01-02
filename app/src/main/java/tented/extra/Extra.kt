package tented.extra

import com.saki.aidl.PluginMsg
import tented.file.File
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


/**
 * Created by Hoshino Tented on 2017/12/24.
 */

infix fun Number.randomTo( to : Number ) : Long = this.toLong() + (Math.random() * (to.toLong() - this.toLong())).toLong()

fun Boolean.toInt() = if(this) 1 else 0
fun Int.toBoolean() = this != 0

fun Any?.toUnit() = Unit        //用于把任何对象转化为Unit...因为setters如果使用简单写法的话, 而且那一条表达式还有返回值的话, 就会爆炸

fun String.isNumber() : Boolean = this.matches(Regex("[+\\-]?[0-9]+"))

fun File.getPath(more : String) : String = android.os.Environment.getExternalStorageDirectory().toString() + "/Tented/TentedPlugin/$more"

fun deepClone( obj : Any ) : Any
{
    val bo = ByteArrayOutputStream()
    val oo = ObjectOutputStream(bo)

    oo.writeObject(obj)

    val bi = ByteArrayInputStream(bo.toByteArray())
    val oi = ObjectInputStream(bi)

    return oi.readObject()
}

fun getMembers( group : Long ) : List<Long> = PluginMsg.send(type = PluginMsg.TYPE_GET_GROUP_MEMBER, group = group)!!.data["member"]!!.map { java.lang.Long.parseLong(it) }

operator fun String.times( times : Number ) : String
{
    if( times.toLong() < 0 ) throw IllegalArgumentException("times: $times can not lower than zero")

    val builder = StringBuilder("")

    for( i in 1..times.toLong() ) builder.append(this)

    return builder.toString()
}