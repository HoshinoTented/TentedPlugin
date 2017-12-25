package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object PluginLoader
{
    val pluginList = arrayListOf(
                                            //TODO some handler
                                            tented.handle.plugin.Main,
                                            tented.handle.plugin.Master,
                                            tented.handle.plugin.Money,
                                            tented.handle.plugin.Timer,
                                            tented.handle.plugin.Manager,
                                            tented.handle.plugin.translate.Translate
                                        )

    fun pluginCount() : Int = pluginList.size

    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        for ( handler in pluginList)
        {
            msg.clearMsg()
            handler.handle(msg)
            msg.send()
        }
    }
}