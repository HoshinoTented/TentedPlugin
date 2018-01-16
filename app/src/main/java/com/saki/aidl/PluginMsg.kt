package com.saki.aidl

import java.util.ArrayList
import java.util.HashMap

import android.os.Parcel
import android.os.Parcelable

import org.json.JSONObject

import saki.demo.Demo
import tented.annotations.NotProperty
import java.io.Serializable

class PluginMsg : Parcelable, Serializable
{
    companion object
    {
        val EMPTY = PluginMsg()

        //必须提供一个名为CREATOR的static final属性 该属性需要实现android.os.Parcelable.Creator<T>接口
        @JvmField
        val CREATOR : Parcelable.Creator<PluginMsg> = object : Parcelable.Creator<PluginMsg>
        {
            override fun createFromParcel(source : Parcel) : PluginMsg = PluginMsg(source)
            override fun newArray(size : Int) : Array<PluginMsg?> = arrayOfNulls(size)
        }

        fun send(type : Int = 0, group : Long = 0, uin : Long = 0, msgType : Type = Type.MSG, message : String = "", value : Int = 0 , title : String? = null) : PluginMsg?
        {
            val msg = PluginMsg(type)

            msg.group = group
            msg.uin = uin
            msg.value = value
            msg.title = title
            msg.code = group

            msg.addMsg(msgType, message)
            return msg.send()
        }

        val TYPE_DEBUG = -1 //控制台消息
        val TYPE_GROUP_MSG = 0 //群消息
        val TYPE_BYDDY_MSG = 1 //好友消息
        val TYPE_DIS_MSG = 2 //讨论组消息
        val TYPE_SESS_MSG = 4 //临时消息
        val TYPE_SYS_MSG = 3 //系统消息
        val TYPE_GET_GROUP_LIST = 5 //群列表
        val TYPE_GET_GROUP_INFO = 6 //群信息
        val TYPE_GET_GROUP_MEMBER = 7 //群成员
        val TYPE_GET_MEMBER_INFO = 14 //成员信息
        val TYPE_FAVOURITE = 8 //点赞
        val TYPE_SET_MEMBER_CARD = 9 //设置群名片
        val TYPE_SET_MEMBER_SHUTUP = 10 //成员禁言
        val TYPE_SET_GROUP_SHUTUP = 11 //群禁言
        val TYPE_DELETE_MEMBER = 12 //删除群成员
        val TYPE_AGREE_JOIN = 13 //同意入群
        val TYPE_GET_LOGIN_ACCOUNT = 15 //获取机器人QQ
        val TYPE_MEMBER_DELETE = 16 // 退群
        val TYPE_ADMIN_CHANGE = 17 // 管理员变更
        val TYPE_STOP = 18 //插件停止（重载）
    }

    var type : Int = 0
    var group : Long = 0
    var code : Long = 0
    var uin : Long = 0
    var time : Long = 0
    var value : Int = 0
    var groupName : String? = null
    var uinName : String? = null
    var title : String? = null
    var data = HashMap<String, ArrayList<String>>()

    @NotProperty lateinit var msg : String
    @NotProperty lateinit var xml : String
    @NotProperty lateinit var json : String

    @NotProperty lateinit var member : tented.util.Member//使用lateinit关键字       // = tented.member.Member.EMPTY          //干脆搞一个member对象下去得了, 不然Kotlin的null判断很烦, 而且就算放一个空Member下去也改不了什么...
    @NotProperty var ats : List<tented.util.Member> = arrayListOf()
    var textMsg : String
        get() = getTextMsg(Type.MSG)
        set(value) = addMsg(Type.MSG, value)

    constructor(source : Parcel)
    {
        readFromParcel(source)
    }

    constructor(type : Int)
    {
        this.type = type
    }

    constructor()

    override fun describeContents() : Int = 0

    fun clearMsg()
    {
        data = HashMap()
    }

    fun getTextMsg(type : Type) : String = getTextMsg(type.toString().toLowerCase())
    fun getTextMsg(type : String) : String
    {
        val build = StringBuilder("")
        val list = data[type]

        if (list != null)
        {
            for (m in list) build.append(m)
        }

        return build.toString()
    }


    fun addMsg(type : Type, text : CharSequence) = addMsg(type.toString().toLowerCase(), text.toString())
    fun addMsg(key : String, text : String)
    {
        var index : ArrayList<String>? = data["index"]
        if (index == null)
        {
            index = ArrayList()
            data.put("index", index)
        }
        index.add(key)
        var list : ArrayList<String>? = data[key]
        if (list == null)
        {
            list = ArrayList()
            data.put(key, list)
        }
        list.add(text)
    }

    override fun writeToParcel(dest : Parcel, flags : Int)
    {
        dest.writeInt(type)
        dest.writeLong(group)
        dest.writeLong(code)
        dest.writeLong(uin)
        dest.writeLong(time)
        dest.writeInt(value)
        dest.writeString(groupName)
        dest.writeString(uinName)
        dest.writeString(title)
        dest.writeMap(data)
    }

    fun readFromParcel(source : Parcel)
    {
        type = source.readInt()
        group = source.readLong()
        code = source.readLong()
        uin = source.readLong()
        time = source.readLong()
        value = source.readInt()
        groupName = source.readString()
        uinName = source.readString()
        title = source.readString()
        source.readMap(data, javaClass.classLoader)

        msg = this.textMsg
        xml = this.getTextMsg(com.saki.aidl.Type.XML)
        json = this.getTextMsg(com.saki.aidl.Type.JSON)

        ats = this.data["at"]?.map {
                                        val index : Int = it.indexOf('@')
                                        val uin : Long = java.lang.Long.parseLong(it.substring(0, index))
                                        val name : String = it.substring(index + 1)

                                        tented.util.Member(this.group, uin, name)
                                    } ?: ats
        member = tented.util.Member(group, uin, uinName)
    }

    fun send() : PluginMsg? = if( type == 0 && ! ( getTextMsg(Type.MSG) != "" || getTextMsg(Type.XML) != "" || getTextMsg(Type.JSON) != "") ) null else Demo.send(this)        //如果是群消息而且消息为空的话就不发送了(免得别人刷屏然后机器人被ban了
    fun send( message : String )
    {
        addMsg(Type.MSG, message)
        send()
        clearMsg()
    }
    fun reAt() = this.addMsg(Type.AT, uin.toString() + "@" + uinName)

    override fun toString() : String
    {
        val jsonObj = JSONObject()

        jsonObj.put("type", type)
        jsonObj.put("group", group)
        jsonObj.put("code", code)
        jsonObj.put("uin", uin)
        jsonObj.put("time", time)
        jsonObj.put("value", value)
        jsonObj.put("groupName", groupName)
        jsonObj.put("uinName", uinName)
        jsonObj.put("title", title)
        jsonObj.put("data", data)

        return jsonObj.toString()
    }
}
