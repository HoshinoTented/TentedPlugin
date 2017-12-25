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

fun getMembers( group : Long ) : List<Long> = PluginMsg.send(type = PluginMsg.TYPE_GET_GROUP_MEMBER, group = group)!!.getData()["member"]!!.map { java.lang.Long.parseLong(it) }

operator fun String.times( times : Number ) : String
{
    if( times.toLong() < 0 ) throw IllegalArgumentException("times: $times can not lower than zero")

    val builder : StringBuilder = StringBuilder("")

    for( i in 1..times.toLong() ) builder.append(this)

    return builder.toString()
}