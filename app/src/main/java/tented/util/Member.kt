package tented.util

import com.saki.aidl.PluginMsg
import tented.bag.Bag
import tented.extra.description
import tented.extra.getPath
import tented.extra.toInt
import tented.file.File
import tented.io.Data
import java.io.Serializable
import java.util.Calendar

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
open class Member ( val group : Long , val uin : Long , val name : String? = null ) : Serializable       //copyable
{
    companion object
    {
        val EMPTY : Member = Member(0, 0)       //empty object, 1.5+ will be useless
    }

    var money : Long
        get() = getMoney("Money")
        set(value) = setMoney("Money", value)

    var bank : Long         //可能没有什么用处
        get() = getMoney("Bank")
        set(value) = setMoney("Bank", value)

    var master : Boolean
        get() = File.read(File.getPath("$group/Master.cfg"), this.uin.toString(), "false") == "true"
        set(value) = File.write(File.getPath("$group/Master.cfg"), this.uin.toString(), value.toString())

    var bag : Bag
        get() = Bag(File.getPath("$group/Bag/$uin.json"))
        set(value) = Data.save(File.getPath("$group/Bag/$uin.json"), value.toString())

    var vip : String
        get() = get("vip", "2018/01/04")            //别问我为什么是2018 01 04, 因为是在这一天写完vip系统的
        set(value) = set("vip", value)

    operator fun set(key : String, value : Any?) = File.write(File.getPath("$group/$uin/config.cfg"), key, value.toString())
    operator fun get(key : String, default : String = "null") : String = File.read(File.getPath("$group/$uin/config.cfg"), key, default)

    private fun getMoney( type : String ) : Long = File.read(File.getPath("$group/$type.cfg"), this.uin.toString(), "0").toLong()
    private fun setMoney( type : String , value : Long ) = File.write(File.getPath("$group/$type.cfg"), this.uin.toString(), value.toString())

    fun isVip() : Boolean
    {
        val timeArray = vip.split('/')

        val now = Calendar.getInstance()
        val time = Calendar.getInstance()

        time.set(timeArray[0].toInt(), timeArray[1].toInt(), timeArray[2].toInt())

        return now.after(time)
    }

    fun shut( time : Int ) = PluginMsg.send(type = PluginMsg.TYPE_SET_MEMBER_SHUTUP, group = group, uin = uin, value = time * 60)
    fun remove() = PluginMsg.send(type = PluginMsg.TYPE_DELETE_MEMBER, group = group, uin = uin)
    fun rename( newName : String ) = PluginMsg.send(type = PluginMsg.TYPE_SET_MEMBER_CARD, group = group, uin = uin, title = newName)
    fun favourite( times : Int = 10 ) = PluginMsg.send(type = PluginMsg.TYPE_FAVOURITE, group = group, uin = uin, value = times)

    fun shutGroup( mod : Boolean ) = PluginMsg.send(type = PluginMsg.TYPE_SET_GROUP_SHUTUP, group = group, value = (! mod).toInt())

    /**
     * 重写equals方法!!!
     */
    override fun equals(other : Any?) : Boolean =
            when(other)
            {
                is Member -> other.group == group && other.uin == uin

                else -> false
            }
}