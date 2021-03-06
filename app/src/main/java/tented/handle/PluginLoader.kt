package tented.handle

import tented.util.createFiles
import tented.util.getPath

/**
 * Created by Hoshino Tented on 2017/12/24.
 *
 * 一个Handler子类的loader
 */
object PluginLoader
{
    val pluginArray : Array<Handler> = arrayOf(     //所有的Handler子类的数组
                                                    tented.handle.plugin.Main,
                                                    tented.handle.plugin.Master,
                                                    tented.handle.plugin.Money,
                                                    tented.handle.plugin.Timer,
                                                    tented.handle.plugin.Manager,
                                                    //tented.handle.plugin.translate.Translate,             出现接口异常, 暂时停止使用
                                                    tented.handle.plugin.ban.Ban,
                                                    //tented.handle.plugin.photo.Photo,                     同异常, 估计是处理异常, 不想去修复
                                                    tented.handle.plugin.shop.system.SystemShop,
                                                    tented.handle.plugin.Settings,
                                                    tented.handle.plugin.wiki.BaiduWiki,
                                                    tented.handle.plugin.CodeViewer,
                                                    tented.handle.plugin.game.SpellCard,
                                                    tented.handle.plugin.SwitchSystem,
                                                    tented.handle.plugin.game.Fishing,
                                                    tented.handle.plugin.Member,
                                                    tented.handle.plugin.game.Chess,
                                                    tented.handle.plugin.Ask,
                                                    tented.handle.plugin.Encode,
                                                    tented.handle.plugin.Drawer,
                                                    tented.handle.plugin.shop.chess.ChessShop,
                                                    tented.handle.plugin.VIPSystem,
                                                    tented.handle.plugin.Gal,
                                                    tented.handle.plugin.gift.GiftSystem,
                                                    tented.handle.plugin.gift.GiftSystem.GiftShop,
                                                    tented.handle.plugin.PingAddress,
                                                    tented.handle.plugin.music.Music,
                                                    tented.handle.plugin.Task,
                                                    tented.handle.plugin.HashSerial,
                                                    tented.handle.plugin.guessing.FingerGuess,
                                                    tented.handle.plugin.tuling.Talk,
                                                    tented.handle.plugin.NumberCall     //这个数字召唤得放最后, 一定!!!
                                              )

    val pluginList = ArrayList(pluginArray.toList())       //实际加载的一个集合, 和数组分开主要是实现开关系统

    //fun pluginCount() : Int = pluginList.size       //获取插件数量, 目前用不到, 也没用...

    /**
     * 虽然说是关闭了插件, 可是实际上只是关闭了处理消息的通道而已
     * ...
     * 所以没什么用嘛!
     * 不过也可以关闭它本身噢(SwitchSystem)
     */
    fun insertPlugins()
    {
        val file = java.io.File(tented.file.File.getPath("switch.cfg"))
        val properties = java.util.Properties()

        file.createFiles()

        properties.load(java.io.FileInputStream(file))
        properties.keys.map { if(properties.getProperty(it.toString(), "true") == "false") pluginList.remove(Class.forName(it.toString()).getField("INSTANCE").get(null) as? Handler) }
    }

    /**
     * 处理消息函数
     * @param msg 接受处理的PluginMsg对象
     */
    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        tented.test.test(msg)           //测试用的函数

        if( ! tented.handle.plugin.Settings.doInit(msg) || tented.handle.plugin.ban.Ban.checkBan(msg) ) return

        for ( handler in pluginList )        //迭代遍历所有的Handler子类
        {
            msg.clearMsg()          //清除消息
            val `continue` = handler.handle(msg)     //执行处理
            /*if(msg.send() != null) return*/ msg.send()              //发送消息, 所以只要在处理里面添加消息就好了
            //加这个判断主要是为了减少不必要的资源消耗。。。
            //感觉加了好傻。。还是不加了

            if( ! `continue` ) break        //接受 Handler.handle( msg : PluginMsg ) 的返回值, 判断是否继续
        }
    }
}