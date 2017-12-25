package tented.func

import tented.extra.getPath
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
        val file : java.io.File = java.io.File(tented.file.File.getPath("Timer.cfg"))

        if( file.exists() )
        {
            val properties : Properties = Properties()

            properties.load(FileInputStream(file))

            properties.keys.map { if( properties.getProperty(it.toString(), "false") == "true" ) tented.handle.plugin.Timer.groupSet.add(java.lang.Long.parseLong(it.toString())) }
        }
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