package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.handle.Plugin

/**
 * Created by Hoshino Tented on 2017/12/28.
 */
object CodeViewer : Plugin("代码查看", "1.0")           //xml和json代码的查看, 懒得写复读
{
    override fun handle(msg : PluginMsg)
    {
        val code =
            when
            {
                msg.xml != "" -> msg.xml
                msg.json != "" -> msg.json

                else -> null
            }

        if( code != null ) saki.demo.Demo.debug(code)
    }

}