package tented.extra

import tented.file.File

/**
 * Created by Hoshino Tented on 2018/1/3.
 *
 * 关于文件的拓展
 */
fun java.io.File.createFiles() = File.createFiles(this)

fun File.getPath(more : String) : String = android.os.Environment.getExternalStorageDirectory().toString() + "/Tented/TentedPlugin/$more"
fun File.delete(fileName : String)
{
    val file = java.io.File(fileName)

    if( file.exists() )
    {
        if( file.isDirectory )
        {
            val files = file.listFiles()

            for( f in files ) f.delete()

            file.delete()
        }
        else file.delete()
    }
}