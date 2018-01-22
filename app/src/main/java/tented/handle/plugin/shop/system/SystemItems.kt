package tented.handle.plugin.shop.system

import com.saki.aidl.PluginMsg
import tented.util.randomTo
import tented.handle.plugin.Money

/**
 * Created by Hoshino Tented on 2017/12/26.
 */
object SystemItems
{
    val items : Map<String, (msg : com.saki.aidl.PluginMsg) -> Unit> =          //使用PluginMsg对象作为参数, 直接在lambda内处理
            mapOf   (
                        "item:cirno" to
                                {
                                    msg ->

                                    msg.member.shut(9)
                                    PluginMsg.send(type = 0, group = msg.member.group, message = "${msg.member.name}使用了 琪露诺, 被琪露诺冻住了!!!\n禁言了⑨分钟")
                                },
                        "item:badapple" to
                                {
                                    msg ->

                                    val member = if( msg.ats.isNotEmpty() ) msg.ats[0] else msg.member

                                    if( member.money > 10000 )          //保证"受害者"的金币超过1w, 虽然只随机0~999, 但是还是搞高一点吧
                                    {
                                        val lose = 0 randomTo 1000
                                        member.money -= lose     //0~999            这个地方有可能使金币变负

                                        PluginMsg.send(group = msg.group, message = "${msg.member.name}对${member.name}使用了 烂苹果!!!\n损失了$lose${Money.moneyUnit}${Money.moneyName}")
                                    }
                                    else PluginMsg.send(group = msg.group, message = "使用失败了!!!而且 烂苹果 也消失不见了...")        //因为是先扣除, 再使用, 然而我又懒得去改, 所以
                                },
                        "item:theworld" to
                                {
                                    msg ->

                                    Thread {
                                        msg.member.shutGroup(true)

                                        Thread.sleep(3000)

                                        msg.member.shutGroup(false)
                                    }.start()

                                    PluginMsg.send(group = msg.group, message = "${msg.uinName}使用了 時符「ザ　ワールド」！！")
                                },
                        "item:money" to
                                {
                                    msg ->

                                    val money =  0 randomTo 1000

                                    msg.member.money += money

                                    PluginMsg.send(group = msg.group, message = "${msg.uinName}使用了金币礼包, 获得了$money${Money.moneyUnit}${Money.moneyName}")
                                }
                    )
}