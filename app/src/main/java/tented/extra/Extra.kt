package tented.extra

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import com.saki.aidl.PluginMsg
import tented.file.File
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.regex.Pattern


/**
 * Created by Hoshino Tented on 2017/12/24.
 */

infix fun Number.randomTo( to : Number ) : Long = this.toLong() + (Math.random() * (to.toLong() - this.toLong())).toLong()

fun Boolean.toInt() = if(this) 1 else 0

fun Int.toBoolean() = this != 0

fun java.io.File.createFiles() = File.createFiles(this)

fun Any?.toUnit() = Unit        //用于把任何对象转化为Unit...因为setters如果使用简单写法的话, 而且那一条表达式还有返回值的话, 就会爆炸

fun String.isNumber() : Boolean = this.matches(Regex("[+\\-]?[0-9]+"))
fun String.matchGroups( regex : String ) : Array<String>
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


fun File.getPath(more : String) : String = android.os.Environment.getExternalStorageDirectory().toString() + "/Tented/TentedPlugin/$more"
fun File.delete( fileName : String )
{
    val file = java.io.File(fileName)

    if( file.exists() )
    {
        if( file.isDirectory )
        {
            val files = file.listFiles()

            for( f in files ) f.delete()

            file.delete()
        }
        else file.delete()
    }
}

fun getMembers( group : Long ) : List<Long> = PluginMsg.send(type = PluginMsg.TYPE_GET_GROUP_MEMBER, group = group)!!.data["member"]!!.map { java.lang.Long.parseLong(it) }

fun deepClone( obj : Any ) : Any
{
    val bo = ByteArrayOutputStream()
    val oo = ObjectOutputStream(bo)

    oo.writeObject(obj)

    val bi = ByteArrayInputStream(bo.toByteArray())
    val oi = ObjectInputStream(bi)

    return oi.readObject()
}

fun doDraw(input : String, textSize : Float = 54F, textColor : Int = Color.rgb(0x33, 0xb5, 0xe5))
{
    val bitmap = Bitmap.createBitmap((textSize * 10).toInt(), textSize.toInt() + 9, Bitmap.Config.ARGB_4444)
    val canvas = Canvas(bitmap)
    val paint = Paint()

    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

    paint.textAlign = Paint.Align.LEFT
    paint.color = textColor
    paint.textSize = textSize

    canvas.drawText(input, 0F, textSize, paint)
    canvas.save(Canvas.ALL_SAVE_FLAG)
    canvas.restore()

    val file = java.io.File(tented.file.File.getPath("temp/${input.hashCode()}.png"))

    file.createFiles()

    val out = FileOutputStream(file)

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)

    out.flush()
    out.close()
}


operator fun String.times( times : Number ) : String
{
    if( times.toLong() < 0 ) throw IllegalArgumentException("times: $times can not lower than zero")

    val builder = StringBuilder("")

    for( i in 1..times.toLong() ) builder.append(this)

    return builder.toString()
}