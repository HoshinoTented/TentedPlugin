package tented.func.ui

import com.tented.demo.kotlin.R
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import org.json.JSONObject
import tented.util.random
import tented.func.ui.fragments.TuLingFragment
import tented.func.ui.fragments.MasterFragment
import tented.func.ui.fragments.OtherFragment
import tented.internet.Request

/**
 * Created by Hoshino Tented on 2017/11/5.
 */

class UI : AppCompatActivity()              //因为theme继承的是AppCompat的主题, 所以Activity也得更改成继承AppCompatActivity, 不然标题栏就会消失喵。。。
{
    companion object
    {
        var launched : Boolean = false
    }

    private lateinit var v8DownloadUrl : String

    private lateinit var masterFragment : MasterFragment        //调单例模式(object)就会弹出个什么。。。constructor must be public的错误。。。还得打注解, 很难看...所以就手动单例了
    private lateinit var tuLingFragment : TuLingFragment
    private lateinit var otherFragment : OtherFragment

    //所有的控件对象

    private lateinit var jump : Button
    private lateinit var melon : Button

    private lateinit var navigation : BottomNavigationView

    private var hasTentedDictionary : Boolean = false          //以后拿来写 与TentedDictionary 的交互, 现在先放在这里
    private var hasV8 : Boolean = false

    private var clickedBack = false         //是否进入二次退出确认

    private fun doInit()
    {
        this.masterFragment = MasterFragment()
        this.tuLingFragment = TuLingFragment()
        this.otherFragment = OtherFragment()

        setFragment(this, masterFragment)

        this.navigation = findViewById(R.id.navigation)

        this.melon = findViewById(R.id.melon)
        this.jump = findViewById(R.id.jump)

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
        val packages = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES)

        hasTentedDictionary = containsPackage(packages, "com.tented.dictionary.kotlin")
        hasV8 = containsPackage(packages, "com.setqq")

        Toast.makeText(this,
                (if( hasTentedDictionary ) hasTentedDictionaryAnswers else hasNotTentedDictionaryAnswers).random(),
                Toast.LENGTH_SHORT
                      ).show()
    }

    private fun doTips()
    {
        if( ! hasV8 )
        {
            val alert = AlertDialog.Builder(this).create()

            alert.setTitle("无法找到v8...")
            alert.setMessage(hasNotV8Answers.random())

            alert.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    "下载",
                    {
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
                    {
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

            /*
            val jumpRequest = Intent()

            jumpRequest.setClassName("com.setqq", "saki.ui.LoginActivity")
            startActivity(jumpRequest)
            */

            val alert = AlertDialog.Builder(this).create()

            alert.setTitle("DO NOT TOUCH ME!")
            alert.setMessage("不要碰我！！！")

            alert.show()

            jump.text = jumpV8Answers.random()
        }

        navigation.setOnNavigationItemSelectedListener {
            when( it.itemId )
            {
                R.id.master -> setFragment(this, masterFragment)
                R.id.home -> setFragment(this, tuLingFragment)
                R.id.other ->
                {
                    Toast.makeText(this@UI, fragmentChangeAnswers.random(), Toast.LENGTH_SHORT).show()

                    setFragment(this, otherFragment)
                }

                else -> false
            }
        }

        melon.setOnClickListener {
            Toast.makeText(this, clickedMelonAnswers.random(), Toast.LENGTH_SHORT).show()
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
                Thread.sleep(2500)

                clickedBack = false
            }.start()
        }

        if( ! clickedBack )
        {
            Toast.makeText(this, willExit.random(), Toast.LENGTH_SHORT).show()

            clickedBack = true

            waitingSecondClick()
        }

        else
        {
            clickedBack = false

            Toast.makeText(this, exited.random(), Toast.LENGTH_SHORT).show()

            finish()            //仅仅退出界面, 因为还有Service什么的。。。
        }
    }

    override fun onCreate(bundle: Bundle?)
    {
        super.onCreate(bundle)
        //requestWindowFeature(Window.FEATURE_NO_TITLE)     去除标题栏

        setContentView(R.layout.main_layout)

        launched = true

        requestPermission(this)     //申请必要的权限

        noPermission(this).forEach {        //验证权限
            requestAgain(this, it)
        }

        //finish()        //立即关闭界面, 千万不要以为是闪退噢
        //Toast.makeText(this, "哎呀。。。出现了一点小故障呜。。\n联系一下插件作者吧？不过还是先看下源码比较好吧。。。\n", Toast.LENGTH_LONG).show()                //报出虚假的信息

        doInit()
        doLoad()
        doTips()
        doUI()
        doSetListener()
    }
}
