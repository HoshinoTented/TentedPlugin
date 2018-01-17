package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Handler
import tented.handle.PluginLoader
import tented.handle.plugin.ban.Banner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern

/**
 * Created by Hoshino Tented on 2017/12/24.
 *
 * 其实这个东西就放一些比较杂的
 * 例如什么at提醒啊之类的
 * 虽然是Main
 * 可是...功能并不Main
 */
object Main : Handler("插件版本", "1.7")
{
    data class MessageCount( val uin : Long , var count : Int )      //只作为一个数据存储类而已

    private val msgMap = HashMap<Long, MessageCount>()      //存储各群上一条消息的发送者, 作为一个缓存吧
    private val middleIcon : String = File.read(File.getPath("config.cfg"), "midIcon", 10024.toChar())

    //像这些什么分隔符之类的, 都是一开始就载入, 不然每一次get都要读取一次, 很卡的
    val splitTimes : Long = File.read(File.getPath("config.cfg"), "st", "9").toLong()
    val splitChar : String = File.read(File.getPath("config.cfg"), "sc", "-")

    //刷屏检测的警告线和禁言线
    val warningCount : Int = get("wc", "5").toInt()
    val shutUpCount : Int = get("suc", "10").toInt()

    val message : String
            get()
            {
                val builder = StringBuilder("$name\n${splitChar * splitTimes}\n")

                builder.append("插件作者: 星野 天忆\n")

                for( plugin in PluginLoader.pluginArray ) builder.append("(${ if( PluginLoader.pluginList.contains(plugin) ) "已" else "未" }开启)${plugin.name} 版本: ${plugin.version}\n")       //添加功能版本

                builder.append(splitChar * splitTimes + "\n已开启的插件: ${PluginLoader.pluginList.size}/${PluginLoader.pluginArray.size}\n本插件源码仓库: https://github.com/LimbolRain/TentedPlugin.git\n如果可以的话star一下啦...")

                return builder.toString()
            }

    operator fun get( key : String , default : String = "null " ) : String = File.read(File.getPath("config.cfg"), key, default)
    operator fun set( key : String , value : String ) = File.write(File.getPath("config.cfg"), key, value)

    private fun makeMenu() : String
    {
        val builder = StringBuilder("")

        for( (index, element) in tented.handle.PluginLoader.pluginList.withIndex() )
        {
            builder.append("${index + 1}.${element.name}")

            if( index % 2 == 0 ) builder.append(middleIcon)
            else builder.append("\n")
        }

        return builder.substring(0, builder.length - 1)
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == "菜单" || msg.msg == "功能" || msg.msg == "帮助" -> msg.addMsg (Type.MSG, Main.makeMenu())
            msg.msg == name ->
            {
                val matcher = Pattern.compile("(.+(\n)?){1,10}").matcher(message)

                while (matcher.find())
                {
                    msg.addMsg(Type.MSG, matcher.group())
                    msg.send()
                    msg.clearMsg()
                }
            }

            msg.member.master ->
            {
                when
                {
                    msg.msg == "重置" ->
                    {
                        msg.send("开始删除本群的所有配置...(不包括全局配置)")

                        File.delete(File.getPath(msg.group.toString()))

                        msg.addMsg(Type.MSG, "删除完毕")
                    }
                }
            }

            msg.ats.isNotEmpty() ->
            {
                if( Settings[msg.group, "at"] == "true" )
                {
                    val message =
                        """
                            |****Tented*Handler****
                            |${msg.uinName}在${SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(Date())}at了你
                            |内容为:
                            |${msg.msg}
                            |****Tented*Handler****
                        """.trimMargin()

                    PluginMsg.send(type = PluginMsg.TYPE_SESS_MSG, group = msg.group, uin = msg.ats[0].uin, message = message)
                }
            }
        }

        //刷屏检测

        val message : MessageCount? = msgMap[msg.group]

        if( msg.uin == message?.uin )       //如果发送者为上个消息包的发送者(隐含null判断)
        {                                   //为何隐含null判断?
                                            //msg.uin是一个Int, 而message?.uin是一个Int?
                                            //如果msg.uin == message?.uin, 就说明message?.uin是一个Int
                                            //因此包含了null判断
            message.count ++        //消息包计数 + 1

            if( message.count >= Main.shutUpCount )     //警告线和禁言线判断
            {
                msg.member.shut(Banner[msg.group])

                msg.reAt()
                msg.addMsg(Type.MSG, "\n你已经连续发了${message.count}条消息了!静一静...")
            }
            else if( message.count >= Main.warningCount ) msg.addMsg(Type.MSG, "小心一点噢!已经连续发送了${message.count}条消息了!")
        }
        else msgMap[msg.group] = MessageCount(msg.uin, 1)       //如果不是就覆盖
    }

}