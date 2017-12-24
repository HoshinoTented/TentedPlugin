package tented.member

import tented.extra.getPath
import tented.file.File
import java.io.Serializable

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
class Member ( val group : Long , val uin : Long , val name : String? = null ) : Serializable
{
    companion object
    {
        val EMPTY : Member = Member(0, 0)       //empty object
    }

    var money : Long
        get() = getMoney("money")
        set(value) = setMoney("money", value)

    var bank : Long
        get() = getMoney("bank")
        set(value) = setMoney("bank", value)

    var master : Boolean
        get() = get("master") == "true"
        set(value) = set("master", value.toString())

    operator fun set(key : String, value : Any?) = File.write(File.getPath("$group/$uin/config.cfg"), key, value.toString())
    operator fun get(key : String) : String = File.read(File.getPath("$group/$uin/config.cfg"), key, "null")

    private fun getMoney( type : String ) : Long = java.lang.Long.parseLong(File.read(File.getPath("$group/Money.cfg"), this.uin.toString(), "0"))
    private fun setMoney( type : String , value : Long ) = File.write(File.getPath("$group/Money.cfg"), this.uin.toString(), value.toString())
}