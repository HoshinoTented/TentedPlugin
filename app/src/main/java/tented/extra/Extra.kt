package tented.extra

import tented.file.File
import org.apache.http.util.EntityUtils.toByteArray
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

operator fun String.times( times : Int ) : String
{
    if( times < 0 ) throw IllegalArgumentException("times: $times can not lower than zero")

    val builder : StringBuilder = StringBuilder("")

    for( i in 1..times ) builder.append(this)

    return builder.toString()
}