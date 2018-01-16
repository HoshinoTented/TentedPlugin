package tented.handle.plugin

import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import tented.extra.getMembers
import tented.extra.getPath
import tented.extra.random
import tented.extra.randomTo
import tented.extra.times
import tented.extra.toInt
import tented.file.File
import tented.handle.Handler
import java.io.BufferedWriter
import java.io.FileInputStream
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

/**
 * Created by Hoshino Tented on 2017/12/24.
 */
object Money : Handler("货币系统", "1.2")
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
                    |[GLOBAL]修改货币名称 [NAME]
                    |[GLOBAL]修改货币单位 [NAME]
                    |金币排行榜
                    |${Main.splitChar * Main.splitTimes}
                    |货币名称: $moneyName
                    |货币单位: $moneyUnit
                """.trimMargin()

    private fun giveMoneyToEveryone( group : Long , change : Long )
    {
        val file : java.io.File = java.io.File(tented.file.File.getPath("$group/Money.cfg"))        //get group money file
        val properties : java.util.Properties = java.util.Properties()      //new a Properties object
        val builder = StringBuilder("")                     //new a builder
        val members : List<Long> = getMembers(group)            //get group members

        if( ! file.exists() )       //if the file is not exists
        {                           //create parent dictionaries and file
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        properties.load(FileInputStream(file))          //load properties

        for( member in members )            //iterate members
        {
            val money : Long = properties.getProperty(member.toString(), "0").toLong() + change        //get money and add change money

            builder.append("$member=$money\n")     //add map to the builder
        }

        //do setWords

        val writer = BufferedWriter(FileWriter(file))

        writer.write(builder.toString())
        writer.close()      //close, this step is very important!!! if not do, the money chance will no write into the file
    }

    /**
     * 排行榜
     * @param properties 选择传入Properties对象而不是group : Long是为了节省资源, 免得二次实例化对象
     */
    private fun rank( properties : Properties ) : List<Any>
    {
        fun ArrayList<Any>.swap( i :Int , j : Int )
        {
            val temp = get(i)

            set(i, get(j))
            set(j, temp)
        }

        fun ArrayList<Any>.rank( properties : Properties )
        {
            var n = size - 2

            while( n > -1 )
            {
                (0..n).forEach {
                    if( properties.getProperty(get(it).toString(), "0").toLong() > properties.getProperty(get(it + 1).toString(), "0").toLong() )
                        swap(it, it + 1)
                }

                n --
            }
        }

        val members = ArrayList(properties.keys)

        members.rank(properties)

        var from = members.size - 10

        if( from < 0 ) from = 0

        return members.subList(from, members.size)
    }

    override fun handle(msg : PluginMsg)
    {
        if( msg.msg == name )
        {
            msg.addMsg(Type.MSG, message)
        }

        else if( msg.msg.matches(Regex("钱包(@.+)?")) )
        {
            val member : tented.util.Member = if( msg.ats.isNotEmpty() ) msg.ats[0] else msg.member

            msg.addMsg(Type.MSG, "${member.name}的钱包...\n${Main.splitChar * 9}\n$moneyName: 有${member.money}${moneyUnit}呢")
        }

        else if( msg.msg == "签到")
        {
            val date = SimpleDateFormat("yyyy/MM/dd").format(Date())

            if( msg.member["check"] != date )
            {
                val random = (0 randomTo 10000) * (msg.member.isVip().toInt() + 1)
                val experience = (0 randomTo 50) * (msg.member.isVip().toInt() + 1)

                msg.member.money += random
                msg.member["exp"] = msg.member["exp", "0"].toLong() + experience
                msg.member["check"] = date

                msg.addMsg(Type.MSG, "签到成功~\n获得了...诶...$random$moneyUnit${moneyName}呢\n获得经验$experience")

                if( msg.member.isVip() )
                {
                    val bag = msg.member.bag

                    bag.add("VIP礼包", 1)

                    msg.member.bag = bag

                    msg.addMsg(Type.MSG, "\n[VIP]金币和经验翻倍啦！！\n[VIP]获得额外礼包")
                }
            }
            else msg.addMsg(Type.MSG, listOf("你签到过了啦。。呜。。", "我才不会去理重复签到的人呢。。哼~", "签到成功~\n诶不对。。。失败了。。", "不理你。。。不理你。。。").random())
        }

        else if( msg.msg == "金币排行榜" )
        {
            val file = java.io.File(File.getPath("${msg.group}/Money.cfg"))
            val properties = Properties()

            if (file.exists())
            {
                properties.load(FileInputStream(file))

                val builder = StringBuilder()

                for ((index, uin) in rank(properties).reversed().withIndex())
                {
                    builder.appendln("第${index + 1}名 : $uin  >>  ${properties.getProperty(uin.toString(), "0")}")
                }

                msg.addMsg(Type.MSG, builder)
            }
            else msg.addMsg(Type.MSG, "没有数据")
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