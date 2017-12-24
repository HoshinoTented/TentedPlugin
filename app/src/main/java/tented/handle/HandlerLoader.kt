package tented.handle

import tented.handle.plugin.Master

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object HandlerLoader
{
    private val handlerList = arrayListOf<MessageHandler>   (
                                                                //TODO some handler
                                                                Master
                                                            )

    fun pluginCount() : Int = handlerList.size

    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        for ( handler in handlerList )
        {
            msg.clearMsg()
            handler.handle(msg)
            msg.send()
        }
    }
}