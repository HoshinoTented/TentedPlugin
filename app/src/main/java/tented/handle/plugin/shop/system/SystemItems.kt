package tented.handle.plugin.shop.system

/**
 * Created by Hoshino Tented on 2017/12/26.
 */
object SystemItems
{
    val items : Map<String, (msg : com.saki.aidl.PluginMsg) -> Unit> =          //使用PluginMsg对象作为参数, 直接在lambda内处理
            mapOf(
                    "item:cirno" to
                            {
                                msg ->

                                msg.member.shut(9)
                                com.saki.aidl.PluginMsg.send(type = 0, group = msg.member.group, message = "${msg.member.name}使用了 琪露诺, 被琪露诺冻住了!!!\n禁言了⑨分钟")
                            }
                   )
}