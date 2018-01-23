package tented.handle.plugin.tuling

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import org.json.JSONArray
import org.json.JSONObject
import tented.handle.plugin.Main
import tented.internet.Request
import tented.util.times

/**
 * Tuling
 * @author Hoshino Tented
 * @date 2018/1/24 3:02
 */

const val API = "http://www.tuling123.com/openapi/apihttp://www.tuling123.com/openapi/api"       //post真的是很迷

val handle : (JSONObject, PluginMsg) -> Unit =
        {
            it, msg ->

            msg.reAt()
            when( it.getInt("code") )
            {
                100000 -> msg.addMsg(Type.MSG, it.getString("text"))
                200000 -> msg.addMsg(Type.MSG, it.getString("text") + it.getString("url"))
                302000 ->
                {
                    val news : JSONArray = it.getJSONArray("list")

                    for( i in 0 until news.length() )
                    {
                        val new : JSONObject = news.getJSONObject(i)
                        val article : String = new.getString("article")
                        val source : String = new.getString("source")
                        val url : String = new.getString("detailurl")

                        msg.addMsg(Type.MSG, "标题: $article($source)\n正文: $url\n")
                    }
                }

                308000 ->
                {
                    val menu : JSONObject = it.getJSONArray("list").getJSONObject(0)

                    val name : String = menu.getString("name")
                    val info : String = menu.getString("info")
                    val image : String = menu.getString("icon")
                    val url : String = menu.getString("detailurl")

                    msg.addMsg(Type.IMAGE, image)
                    msg.addMsg(
                            Type.MSG,
                            """
                                |$name
                                |${Main.splitChar * Main.splitTimes}
                                |$info
                                |正文: $url
                                |${Main.splitChar * Main.splitTimes}
                            """.trimMargin()
                              )
                }

                40001, 40004 -> throw ApiKeyException
                40002, 40007 -> throw TulingFormatException

                else -> throw Exception("不可能的错误发生了")
            }
        }

fun invoke( key : String , msg : PluginMsg , handle : (JSONObject, PluginMsg) -> Unit)=
        handle(
                JSONObject(
                        Request("$API?key=$key&info=${msg.msg.replace("@${msg.ats[0].name}", "")}&userid=${msg.group}${msg.uin}")        //这里使用group和uin混合作为userId
                                .doGet()
                          ),
                msg
              )