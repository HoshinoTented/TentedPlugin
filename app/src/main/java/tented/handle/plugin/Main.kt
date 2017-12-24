package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.handle.MessageHandler

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object Main : MessageHandler
{
    val splitTimes : Long = 9L
    val list : ArrayList<String> = ArrayList()

    fun makeMenu() : String
    {
        val builder : StringBuilder = StringBuilder("")

        for( (index, element) in list.withIndex() )
        {
            builder.append(element)

            if( index % 2 == 0 ) builder.append("   ")
            else builder.append("\n")
        }

        return builder.substring(0, builder.length - 1)
    }

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == "菜单" ) msg.addMsg(Type.MSG, Main.makeMenu())
    }

}