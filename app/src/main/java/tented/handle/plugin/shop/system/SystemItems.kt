package tented.handle.plugin.shop.system

/**
 * Created by Hoshino Tented on 2017/12/26.
 */
object SystemItems
{
    val items : Map<String, (tented.member.Member) -> Unit> =
            mapOf(
                    "item:cirno" to
                            {
                                member -> member.shut(9)

                                com.saki.aidl.PluginMsg.send(type = 0, group = member.group, message = "${member.name}使用了 琪露诺, 被琪露诺冻住了!!!\n禁言了⑨分钟")
                            }
                   )
}