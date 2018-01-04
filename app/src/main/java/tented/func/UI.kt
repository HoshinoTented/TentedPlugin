package tented.func

import com.tented.demo.kotlin.R
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import org.json.JSONObject
import tented.extra.isNumber
import tented.internet.Request
import tented.member.Member

/**
 * Created by Hoshino Tented on 2017/11/5.
 */

class UI : Activity()
{

    private lateinit var v8DownloadUrl : String

    //所有的控件对象

    private lateinit var jump : Button
    private lateinit var master : Button

    private lateinit var group : EditText
    private lateinit var uin : EditText


    private var hasTentedDictionary : Boolean = false          //以后拿来写 与TentedDictionary 的交互, 现在先放在这里
    private var hasV8 : Boolean = false

    private var clickedBack = false         //是否进入二次退出确认

    private fun doInit()
    {
        this.jump = findViewById(R.id.jump) as Button
        this.master = findViewById(R.id.master) as Button

        this.group = findViewById(R.id.group) as EditText
        this.uin = findViewById(R.id.uin) as EditText

        Thread {
            val request = Request("http://setqq.oss-cn-shanghai.aliyuncs.com/v8/update.json")
            val jsonObj = JSONObject(request.doGet())

            v8DownloadUrl = jsonObj.getString("down")
        }.start()
    }

    private fun doLoad()
    {
        fun containsPackage( packages : List<PackageInfo> , packageName : String ) : Boolean = packages.filter { it.packageName == packageName }.isNotEmpty()

        val packageManager = packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)

        hasTentedDictionary = containsPackage(packages, "com.tented.dictionary.kotlin")
        hasV8 = containsPackage(packages, "com.setqq")
    }

    private fun doTips()
    {
        if( ! hasV8 )
        {
            val alert = AlertDialog.Builder(this).create()

            alert.setTitle("无法找到v8...")
            alert.setMessage("我们无法在你的应用列表里找到SQV8主程序\n是否要立即下载?")

            alert.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    "下载",
                    DialogInterface.OnClickListener {
                        _, _ ->

                        val intent = Intent()

                        intent.action = "android.intent.action.VIEW"
                        intent.data = Uri.parse(v8DownloadUrl)

                        startActivity(intent)

                        System.exit(0)          //不管怎样还是要退出
                    }
                           )

            alert.setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    "退出",
                    DialogInterface.OnClickListener {
                        _, _ ->

                        System.exit(0)          //直接退出, 毫不留情
                    }
                           )

            alert.setCancelable(false)      //设置无法非正常退出
            alert.show()
        }
    }

    private fun doUI()
    {
        //TODO something
    }

    private fun doSetListener()
    {
        jump.setOnClickListener {
            _ ->

            val jumpRequest = Intent()

            jumpRequest.setClassName("com.setqq", "saki.ui.LoginActivity")
            startActivity(jumpRequest)
        }

        master.setOnClickListener {
            _ ->

            val group = this@UI.group.text.toString()
            val uin = this@UI.uin.text.toString()

            if( group.isNumber() && uin.isNumber() )
            {
                val member = Member(group.toLong(), uin.toLong(), null)

                member.master = ! member.master

                Toast.makeText(this@UI, "修改完毕", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(this@UI, "群号或主人QQ号未填写或数据错误", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed()
    {
        /**
         * 二次退出确认
         */

        fun waitingSecondClick()
        {
            Thread {
                Thread.sleep(3000)

                clickedBack = false
            }.start()
        }

        if( ! clickedBack )
        {
            Toast.makeText(this, "再按一次返回键退出", Toast.LENGTH_SHORT).show()

            clickedBack = true

            waitingSecondClick()
        }

        else
        {
            clickedBack = false

            finish()
        }
    }

    override fun onCreate(bundle: Bundle?)
    {
        super.onCreate(bundle)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout)

        //finish()        //立即关闭界面, 千万不要以为是闪退噢
        //Toast.makeText(this, "哎呀。。。出现了一点小故障呜。。\n联系一下插件作者吧？不过还是先看下源码比较好吧。。。\n", Toast.LENGTH_LONG).show()                //报出虚假的信息
                                                                                                                                                                    //好皮啊我。。。
        doInit()
        doLoad()

        doTips()

        doUI()

        doSetListener()
    }
}
