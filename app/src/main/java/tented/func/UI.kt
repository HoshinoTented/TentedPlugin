package tented.func

import com.tented.demo.kotlin.R
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle

/**
 * Created by Hoshino Tented on 2017/11/5.
 */

class UI : Activity()
{
    var hasTentedDictionary : Boolean = false          //以后拿来写 与TentedDictionary 的交互, 现在先放在这里

    override fun onCreate(bundle: Bundle?)
    {
        super.onCreate(bundle)

        setContentView(R.layout.layout)

        finish()        //立即关闭界面, 千万不要以为是闪退噢

        val packageManager = packageManager
        val packages = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)

        hasTentedDictionary = packages.filter { it.packageName == "com.tented.dictionary.kotlin" }.isNotEmpty()

        println(hasTentedDictionary)
    }
}
