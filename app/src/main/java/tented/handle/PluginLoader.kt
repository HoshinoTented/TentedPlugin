package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 *
 * 一个Handler子类的loader
 */
object PluginLoader
{
    val pluginList = arrayListOf(           //这里写上所有需要加载的Handler的子类, 以后写开关系统就容易多了
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
                                            tented.handle.plugin.CodeViewer,
                                            tented.handle.plugin.game.SpellCard
                                        )

    //fun pluginCount() : Int = pluginList.size       //获取插件数量, 目前用不到, 也没用...

    /**
     * 处理消息函数
     * @param msg 接受处理的PluginMsg对象
     */
    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        if( tented.handle.plugin.ban.Ban.checkBan(msg) || ! tented.handle.plugin.Settings.doInit(msg) ) return           //违禁词和开关机判断, 其实应该开关机放前面的...

        for ( handler in pluginList)        //迭代遍历所有的Handler子类
        {
            msg.clearMsg()          //清楚消息
            handler.handle(msg)     //执行处理
            msg.send()              //发送消息, 所以只要在处理里面添加消息就好了
        }
    }
}