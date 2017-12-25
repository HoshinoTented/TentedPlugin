package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object HandlerLoader
{
    private val handlerList = arrayListOf(
                                            //TODO some handler
                                            tented.handle.plugin.Main,
                                            tented.handle.plugin.Master,
                                            tented.handle.plugin.Money,
                                            tented.handle.plugin.Timer
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