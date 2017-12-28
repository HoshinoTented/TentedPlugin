package tented.handle

import com.saki.aidl.Type
import tented.extra.times
import tented.handle.plugin.Main

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

    private fun checkBan( msg : com.saki.aidl.PluginMsg ) : Boolean
    {
        if( msg.member.master ) return false                //Master will never be shuted up

        val touches : List<String> = tented.handle.plugin.ban.Banner.getWords(msg.group).filter { msg.msg.matches(Regex(".*$it.*")) }

        return  if( touches.isNotEmpty() )
                {
                    val time : Int = touches.size * tented.handle.plugin.ban.Banner[msg.group]
                    val builder : StringBuilder = StringBuilder("${msg.member.name}\n${"-" * 9}\n你触犯了以下违禁词...\n")

                    for( word in touches ) builder.append(">>$word<<\n")

                    builder.append("被禁言了${time}分钟\n${"-" * Main.splitTimes}")

                    msg.member.shut(time)

                    msg.clearMsg()
                    msg.addMsg(Type.MSG, builder.toString())
                    msg.send()

                    true
                }
                else false
    }

    fun handleMessage( msg : com.saki.aidl.PluginMsg )
    {
        if( checkBan(msg) || ! tented.handle.plugin.Settings.doInit(msg) ) return

        for ( handler in pluginList)
        {
            msg.clearMsg()
            handler.handle(msg)
            msg.send()
        }
    }
}