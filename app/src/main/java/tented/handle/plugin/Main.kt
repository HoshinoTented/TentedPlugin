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

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object Main : Handler("插件版本", "1.6")
{
    data class MessageCount( val uin : Long , var count : Int )      //只作为一个数据存储类而已

    private val msgMap = HashMap<Long, MessageCount>()      //存储各群上一条消息的发送者, 作为一个缓存吧

    //像这些什么分隔符之类的, 都是一开始就载入, 不然每一次get都要读取一次, 很卡的
    val splitTimes : Long = File.read(File.getPath("config.cfg"), "st", "9").toLong()
    val splitChar : String = File.read(File.getPath("config.cfg"), "sc", "-")

    //刷屏检测的警告线和禁言线
    val warningCount : Int = get("wc", "5").toInt()
    val shutUpCount : Int = get("suc", "10").toInt()

    val menuSet : HashSet<String> = HashSet()          //菜单来着

    val message : String
            get()
            {
                val builder = StringBuilder("$name\n${splitChar * splitTimes}\n")

                builder.append("插件作者: 星野 天忆\n")

                for( plugin in PluginLoader.pluginList ) builder.append("${plugin.name} 版本: ${plugin.version}\n")       //添加功能版本

                builder.append(splitChar * splitTimes + "\n本插件源码仓库: https://github.com/LimbolRain/TentedPlugin.git\n如果可以的话star一下啦...")

                return builder.toString()
            }

    init
    {
        menuSet.add(name)
    }

    operator fun get( key : String , default : String = "null " ) : String = File.read(File.getPath("config.cfg"), key, default)
    operator fun set( key : String , value : String ) = File.write(File.getPath("config.cfg"), key, value)

    private fun makeMenu() : String
    {
        val builder = StringBuilder("")

        for( (index, element) in menuSet.withIndex() )
        {
            builder.append(element)

            if( index % 2 == 0 ) builder.append("   ")
            else builder.append("\n")
        }

        return builder.substring(0, builder.length - 1)
    }

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == "菜单" -> msg.addMsg (Type.MSG, Main.makeMenu())
            msg.msg == name -> msg.addMsg(Type.MSG, message)
            msg.ats.isNotEmpty() ->
            {
                val message =
                        """
                            |****Tented*Plugin****
                            |${msg.uinName}在${SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(Date())}at了你
                            |内容为:
                            |${msg.msg}
                            |****Tented*Plugin****
                        """.trimMargin()

                PluginMsg.send(type = PluginMsg.TYPE_SESS_MSG, group = msg.group, uin = msg.ats[0].uin, message = message)
            }
        }

        //刷屏检测

        val message : MessageCount? = msgMap[msg.group]

        if( msg.uin == message?.uin )       //如果发送者为上个消息包的发送者(隐含null判断
        {
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