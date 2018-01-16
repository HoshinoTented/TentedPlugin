package tented.test

import com.saki.aidl.PluginMsg
import tented.extra.description

/**
 * Test
 * @author Hoshino Tented
 * @date 2018/1/14 16:03
 */

fun test( msg : PluginMsg )
{
    /*
    PluginMsg(PluginMsg.TYPE_GET_GROUP_LIST).send()?.data?.get("troop")?.forEach {
        println("[GROUP]$it")
    }
    */

    /*
    msg.msg.forEach {
        println(it.toInt())
    }
    */

    saki.demo.Demo.debug(msg.description())
}