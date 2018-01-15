package tented.handle.plugin.music

import org.json.JSONObject
import tented.internet.Request
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * MusicSearcher
 * @author Hoshino Tented
 * @date 2018/1/13 17:11
 */
object MusicSearcher
{
    data class Song(val name : String, val singerName : String, val mid : String, val photoLink : String)

    private val API = "https://c.y.qq.com/soso/fcgi-bin/client_search_cp?ct=24&qqmusic_ver=1298&new_json=1&remoteplace=txt.yqq.song&searchid=58096188722391125&t=0&aggr=1&cr=1&catZhida=1&lossless=0&flag_qc=0&p=1&n=20&g_tk=5381&jsonpCallback=MusicJsonCallback908412425734962&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&w="
    private val PHOTO_API = "https://y.qq.com/n/yqq/song/"

    /**
     * 获取图片链接
     * 因为这个歌曲列表接口没有附带图片。。
     * 所以只能去歌曲主页提取html了。。。
     * @param mid 歌曲的mid
     */
    private fun getPhotoLink( mid : String ) : String
    {
        val request = Request(PHOTO_API + "$mid.html")
        val page = request.doGet()

        val matcher = Pattern.compile("y\\.gtimg\\.cn/music/photo_new/(.+?)\\.jpg\\?max_age=2592000").matcher(page)

        return if( matcher.find() ) matcher.group() else ""     //找不到就容错
    }


    private fun format(page : String) : List<Song>
    {
        val jsonText = page.substring(page.indexOf('(') + 1, page.length - 1)

        val json = JSONObject(jsonText)
        val data = json.getJSONObject("data")
        val song = data.getJSONObject("song")
        val list = song.getJSONArray("list")

        val result = ArrayList<Song>()

        var index = -1

        while( ++ index < 4 && index < list.length() )
        {
            val jsonObj = list.getJSONObject(index)
            val singers = jsonObj.getJSONArray("singer")

            val singerName = StringBuilder("")

            (0..(singers.length() - 1)).forEach {
                singerName.append(singers.getJSONObject(it).getString("name") + " ")
            }

            val mid = jsonObj.getString("mid")
            val name = jsonObj.getString("name")

            result.add(Song(name, singerName.toString(), mid, getPhotoLink(mid)))
        }

        return result
    }

    fun makeXml( song : Song ) : String =
            """
                |<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>
                |<msg serviceID="2" templateID="1" action="web" brief="未知的链接" sourceMsgId="0" url="" flag="0" adverSign="0" multiMsgFlag="0">
                |<item main_layout="2">
                |<audio cover="https://${song.photoLink}" src="http://dl.stream.qqmusic.qq.com/C100${song.mid}.m4a?fromtag=1" />
                |<title>${song.name}</title>
                |<summary>${song.singerName}</summary>
                |</item>
                |<source name="QQ音乐" icon="https://i.gtimg.cn/open/app_icon/01/07/98/56/1101079856_100_m.png?date=20180113" url="http://web.p.qq.com/qqmpmobile/aio/app.html?id=1101079856" action="app" a_actionData="com.tencent.qqmusic" i_actionData="tencent1101079856://" appid="1101079856" />
                |</msg>
            """.trimMargin()

    fun search(keyWord : String) : List<Song> = format(Request(API + URLEncoder.encode(keyWord, "UTF-8")).doGet())
}