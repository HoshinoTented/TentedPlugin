package tented.handle.plugin.wiki

import tented.internet.Request
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * Created by Hoshino Tented on 2017/12/27.
 */
object Searcher
{
    private val pattern = Pattern.compile("(?s)<ul style=\"display:none\" data-soundtitle=\"百科名片\" data-index=\"百科名片\" class=\"sound-list\" id=\"J-card-sound\">(.+?)</ul>")
    private val api = "https://wapbaike.baidu.com/item/"

    private fun format( page : String ) : String
    {
        println(page)
        val matcher = pattern.matcher(page)
        return if(matcher.find()) matcher.group(1) .replace(Regex("<.+?>"), "") else "not found"
    }

    fun search( keyWord : String ) : String = format(Request(api + URLEncoder.encode(keyWord, "UTF-8")).doGet())
}