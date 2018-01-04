package tented.encoder

import java.security.MessageDigest

object MD5Encoder
{
    private val md5Encoder : MessageDigest = MessageDigest.getInstance("MD5")

    fun encode( input : String ) : String
    {
        md5Encoder.update(input.toByteArray())

        return hash2Hex(md5Encoder.digest())
    }

    private fun hash2Hex( hashCode : ByteArray ) : String
    {
        val buffer = StringBuffer("")

        for( code in hashCode )
        {
            var intValue : Int = code.toInt()

            if( intValue < 0 ) intValue = intValue and 0xFF
            if( intValue < 16 ) buffer.append(0)

            buffer.append(Integer.toHexString(intValue))
        }

        return buffer.toString()
    }
}