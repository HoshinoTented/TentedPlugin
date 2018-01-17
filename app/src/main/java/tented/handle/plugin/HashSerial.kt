package tented.handle.plugin

import android.os.Build
import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.handle.Handler
import java.lang.reflect.Modifier

/**
 * HashSerial
 * @author Hoshino Tented
 * @date 2018/1/17 21:58
 */
object HashSerial : Handler("哈希序列", "1.0")
{
    private fun makeHashSerialNumber() : String
    {
        val serialCode = StringBuilder()

        Build::class.java.declaredFields.forEach {
            if(Modifier.isStatic(it.modifiers) && Modifier.isPublic(it.modifiers)) serialCode.append((31 * it.get(null).hashCode()).toString(16) + " ")
        }

        return serialCode.toString()
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, makeHashSerialNumber())
        }
    }
}