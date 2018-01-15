package tented.handle.plugin.ban

import tented.extra.getPath
import tented.file.File

/**
 * Created by Hoshino Tented on 2017/12/25.
 */
object Banner
{
    //这个set和get都是群的一个禁言的时长
    //影响范围是违禁系统的刷屏检测
    operator fun get( group : Long ) : Int = Integer.parseInt(File.read(File.getPath("$group/config.cfg"), "ban", "10"))
    operator fun set( group : Long , time : Int ) = File.write(File.getPath("$group/config.cfg"), "ban", time.toString())

    fun setWords(group : Long, words : List<String>)
    {
        val builder : StringBuilder = StringBuilder("")

        for( word in words ) builder.append(word + "\n")

        File.write(File.getPath("$group/Ban.cfg"), builder.toString())
    }

    fun getWords( group : Long ) : List<String> = File.readLines(File.getPath("$group/Ban.cfg")).filter { it != "" }
}