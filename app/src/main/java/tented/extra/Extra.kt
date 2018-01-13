package tented.extra

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import com.saki.aidl.PluginMsg
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * 这个文件就放一些普通的拓展而已
 */

inline fun <reified T : Any> T.description() : String
{
    val jsonObj = JSONObject()

    javaClass.declaredFields.forEach {
        if( it != null )
        {
            it.isAccessible = true
            jsonObj.put(it.name, it.get(this))
        }
    }

    return jsonObj.toString()
}

inline fun <reified T : Any> T.deepClone( obj : Any ) : T
{
    val bo = ByteArrayOutputStream()
    val oo = ObjectOutputStream(bo)

    oo.writeObject(obj)

    val bi = ByteArrayInputStream(bo.toByteArray())
    val oi = ObjectInputStream(bi)

    return oi.readObject() as T
}

fun getMembers( group : Long ) : List<Long> = PluginMsg.send(type = PluginMsg.TYPE_GET_GROUP_MEMBER, group = group)!!.data["member"]!!.map { java.lang.Long.parseLong(it) }

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

