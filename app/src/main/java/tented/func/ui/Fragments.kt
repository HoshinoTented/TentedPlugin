package tented.func.ui

import android.app.Activity
import com.tented.demo.kotlin.R

/**
 * Fragments
 * @author Hoshino Tented
 * @date 2018/1/19 3:15
 */

//加这些是为了以后方便改Fragment的包
//因为Fragment有android.app.Fragment和android.support.v4.app.Fragment
//要是要改的话。。。可能要一个一个搞
//既然Kotlin有类似于C那样的typedef
//何用而不为呢?
typealias FragmentManager = android.app.FragmentManager
typealias FragmentTransaction = android.app.FragmentTransaction
typealias Fragment = android.app.Fragment

/**
 * 设置Fragment
 * @param activity 调用这个函数的Activity
 * @param layoutEntity 要设置的Fragment实例
 * @return 总是true
 */
fun setFragment( activity : Activity , layoutEntity : Fragment ) : Boolean
{
    val transaction : FragmentTransaction = activity.fragmentManager.beginTransaction()

    transaction.replace(R.id.context, layoutEntity)
    transaction.commit()

    return true
}