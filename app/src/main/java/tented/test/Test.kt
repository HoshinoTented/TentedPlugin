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
    saki.demo.Demo.debug(msg.description())
}