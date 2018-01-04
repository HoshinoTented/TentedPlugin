package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.encoder.MD5Encoder
import tented.extra.times
import tented.handle.Handler

/**
 * Created by Hoshino Tented on 2018/1/2.
 */
object Encode : Handler("加密系统", "1.0")
{
    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |加密 [WORD] [KEY]
                |解密 [WORD] [KEY]
                |MD5 [WORD]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    private fun encode( str : String , key : Char ) : String
    {
        fun xorEncode(str : String, key : Char) : String
        {
            val builder = StringBuilder()

            for (char in str) builder.append((char.toInt() xor key.toInt()).toChar())

            return builder.toString()
        }

        return xorEncode(str, key)
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.msg.matches(Regex("[加解]密 .+ .")) ->
            {
                val word = msg.msg.substring(3, msg.msg.length - 2)
                val key = msg.msg[msg.msg.length - 1]

                msg.addMsg(Type.MSG, encode(word, key))
            }

            msg.msg.matches(Regex("(?s)(?i)md5 .+")) ->
            {
                val word = msg.msg.substring(4)

                msg.addMsg(Type.MSG, MD5Encoder.encode(word).toUpperCase())
            }
        }
    }
}