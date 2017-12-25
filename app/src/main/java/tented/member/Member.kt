package tented.member

import com.saki.aidl.PluginMsg
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

    var bank : Long         //可能没有什么用处
        get() = getMoney("bank")
        set(value) = setMoney("bank", value)

    var master : Boolean
        get() = File.read(File.getPath("$group/Master.cfg"), this.uin.toString(), "false") == "true"
        set(value) = File.write(File.getPath("$group/Master.cfg"), this.uin.toString(), value.toString())

    operator fun set(key : String, value : Any?) = File.write(File.getPath("$group/$uin/config.cfg"), key, value.toString())
    operator fun get(key : String) : String = File.read(File.getPath("$group/$uin/config.cfg"), key, "null")

    private fun getMoney( type : String ) : Long = java.lang.Long.parseLong(File.read(File.getPath("$group/Money.cfg"), this.uin.toString(), "0"))
    private fun setMoney( type : String , value : Long ) = File.write(File.getPath("$group/Money.cfg"), this.uin.toString(), value.toString())

    fun shut( time : Int ) = PluginMsg.send(type = PluginMsg.TYPE_SET_MEMBER_SHUTUP, group = group, uin = uin, value = time * 60)
    fun remove() = PluginMsg.send(type = PluginMsg.TYPE_DELETE_MEMBER, group = group, uin = uin)
    fun rename( newName : String ) = PluginMsg.send(type = PluginMsg.TYPE_SET_MEMBER_CARD, group = group, uin = uin, title = newName)
    fun favourite( times : Int = 10 ) = PluginMsg.send(type = PluginMsg.TYPE_FAVOURITE, group = group, uin = uin, value = times)
}