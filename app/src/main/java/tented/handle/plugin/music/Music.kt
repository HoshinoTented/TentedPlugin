package tented.handle.plugin.music

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import saki.demo.Demo
import tented.extra.times
import tented.handle.Handler
import tented.handle.plugin.Main

/**
 * Music
 * @author Hoshino Tented
 * @date 2018/1/13 17:37
 */
object Music : Handler("点歌系统", "1.0")
{
    private val results = HashMap<Long, List<MusicSearcher.Song>>()

    val message =
            """
                |$name
                |${Main.splitChar * Main.splitTimes}
                |搜索 [KEYWORD]
                |点歌 [INDEX]
                |${Main.splitChar * Main.splitTimes}
            """.trimMargin()

    override fun handle(msg : PluginMsg)
    {
        when
        {
            msg.msg == name -> msg.addMsg(Type.MSG, message + "\n${if (results[msg.group] == null) "无" else "有"}搜索结果")
            msg.msg.matches(Regex("搜索 .+")) ->
            {
                val result = MusicSearcher.search(msg.msg.substring(3))
                val builder = StringBuilder("点歌结果如下...\n${Main.splitChar * Main.splitTimes}\n")

                if( result.isNotEmpty() ) this.results[msg.group] = result

                for( (index, song) in result.withIndex() )builder.appendln("${index + 1} : ${song.name}(${song.singerName})")

                builder.append(Main.splitChar * Main.splitTimes)

                msg.addMsg(Type.MSG, builder)
            }

            msg.msg.matches(Regex("点歌 [1-4]")) ->
            {
                val buffer = results[msg.group]

                if( buffer != null )
                {
                    val index = msg.msg.substring(3).toInt()

                    if( index - 1 < buffer.size ) msg.addMsg(Type.XML, Demo.debug(MusicSearcher.makeXml (buffer[index - 1])).toString()) else msg.addMsg(Type.MSG, "点歌失败: 序号超出范围")
                }
                else msg.addMsg(Type.MSG, "点歌失败: 没有搜索结果")
            }
        }
    }
}