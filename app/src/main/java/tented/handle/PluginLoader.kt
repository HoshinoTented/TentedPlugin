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
                                            tented.handle.plugin.translate.Translate,
                                            tented.handle.plugin.ban.Ban,
                                            tented.handle.plugin.photo.Photo,
                                            tented.handle.plugin.shop.system.SystemShop,
                                            tented.handle.plugin.Settings,
                                            tented.handle.plugin.wiki.BaiduWiki,
                                            tented.handle.plugin.CodeViewer
                                        )

    fun pluginCount() : Int = pluginList.size

    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        if( tented.handle.plugin.ban.Ban.checkBan(msg) || ! tented.handle.plugin.Settings.doInit(msg) ) return           //违禁词和开关机判断, 其实应该开关机放前面的...

        for ( handler in pluginList)
        {
            msg.clearMsg()
            handler.handle(msg)
            msg.send()
        }
    }
}