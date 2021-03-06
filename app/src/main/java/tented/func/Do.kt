package tented.func

import tented.util.getPath
import tented.file.File
import java.io.FileInputStream
import java.util.Properties

/**
 * Created by Hoshino Tented on 2017/11/5.
 */
object Do
{
    init
    {
        tented.handle.plugin.Timer.thread.start()       //start the thread
    }

    fun serviceOnCreate()
    {
        tented.shop.Shop.rootPath = File.getPath("")

        val file : java.io.File = java.io.File(tented.file.File.getPath("Timer.cfg"))

        if( file.exists() )
        {
            val properties : Properties = Properties()

            properties.load(FileInputStream(file))

            properties.keys.map { if( properties.getProperty(it.toString(), "false") == "true" ) tented.handle.plugin.Timer.groupSet.add(java.lang.Long.parseLong(it.toString())) }
        }

        tented.handle.PluginLoader.insertPlugins()      //在一开始才载入
    }

    private fun create()
    {
        //TODO
    }

    private fun destroy()
    {
        //TODO
    }

    fun onSet( bool : Boolean ) = if( bool ) create() else destroy()
}