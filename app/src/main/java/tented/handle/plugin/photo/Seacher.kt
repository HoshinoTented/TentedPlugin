package tented.handle.plugin.photo

import tented.extra.randomTo
import tented.internet.Request
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Seacher
{
    private val api : String = "https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=index&fr=&hs=0&xthttps=111111&sf=1&fmq=&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word="
    private val pattern : Pattern = Pattern.compile("\"objURL\":\"(.+?)\"")

    private fun format( page : String ) : List<String>
    {
        val matcher : Matcher = pattern.matcher(page)
        val result : ArrayList<String> = arrayListOf()

        while( matcher.find() ) result.add(matcher.group(1))

        return result
    }

    fun search( keyWord : String ) : String
    {
        val request : Request = Request(api + URLEncoder.encode(keyWord, "UTF-8"))
        val result : List<String> = format(request.doGet())

        return result[(0 randomTo result.size).toInt()]
    }
}