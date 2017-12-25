package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getMembers
import tented.extra.getPath
import tented.extra.times
import tented.file.File
import tented.handle.Plugin
import java.io.BufferedWriter
import java.io.FileInputStream
import java.io.FileWriter

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object Money : Plugin("货币系统", "1.1")
{
    var moneyUnit : String
        get() = File.read(File.getPath("config.cfg"), "money::unit", "枚")
        set(value) = File.write(File.getPath("config.cfg"), "money::unit", value)

    var moneyName : String
        get() = File.read(File.getPath("config.cfg"), "money::name", "水晶")
        set(value) = File.write(File.getPath("config.cfg"), "money::name", value)

    val message : String
            get() =
                """
                        |$name
                        |${Main.splitChar * Main.splitTimes}
                        |钱包
                        |修改货币名称 [NAME]
                        |修改货币单位 [NAME]
                        |${Main.splitChar * Main.splitTimes}
                        |货币名称: $moneyName
                        |货币单位: $moneyUnit
                    """.trimMargin()

    private fun giveMoneyToEveryone( group : Long , change : Long )
    {
        val file : java.io.File = java.io.File(tented.file.File.getPath("$group/Money.cfg"))
        val properties : java.util.Properties = java.util.Properties()
        val builder : StringBuilder = StringBuilder("")
        val members : List<Long> = getMembers(group)

        if( ! file.exists() )
        {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        properties.load(FileInputStream(file))

        for( member in members )
        {
            val money : Long = java.lang.Long.parseLong(properties.getProperty(member.toString(), "0")) + change

            builder.append("$member=$money\n")     //add map to the builder
        }

        //do setWords

        val writer : BufferedWriter = BufferedWriter(FileWriter(file))

        writer.write(builder.toString())
        writer.close()
    }

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
        }

        else if( msg.msg.matches(Regex("钱包(@.+)?")) )
        {
            val member : tented.member.Member = if( msg.ats.isNotEmpty() ) msg.ats[0] else msg.member

            msg.addMsg(Type.MSG, "${member.name}的钱包...\n${Main.splitChar * 9}\n$moneyName: 有${member.money}${moneyUnit}呢")
        }

        else if( msg.member.master )
        {
            if (msg.msg.matches(Regex("修改货币(名称|单位) .+")))
            {
                val action : String = msg.msg.substring(4, 6)
                val value : String = msg.msg.substring(7)

                if( action == "名称" ) this.moneyName = value else this.moneyUnit = value

                msg.addMsg(Type.MSG, "操作成功!!!")
            }

            else if (msg.msg.matches(Regex("奖励全员-?[0-9]+")))
            {
                val change : Long = java.lang.Long.parseLong(msg.msg.substring(4))

                giveMoneyToEveryone(msg.group, change)

                msg.addMsg(Type.MSG, "操作完毕")
            }
        }
    }

}