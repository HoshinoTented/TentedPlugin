package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
interface MessageHandler
{
    fun handle( msg : com.saki.aidl.PluginMsg )
}