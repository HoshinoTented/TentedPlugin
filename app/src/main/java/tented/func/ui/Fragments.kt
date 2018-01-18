package tented.func.ui

import android.app.Activity
import com.tented.demo.kotlin.R
import tented.func.ui.fragments.MasterFragment

        /**
 * Fragments
 * @author Hoshino Tented
 * @date 2018/1/19 3:15
 */

typealias FragmentManager = android.app.FragmentManager
typealias FragmentTransaction = android.app.FragmentTransaction
typealias Fragment = android.app.Fragment

fun setFragment( activity : Activity , layoutEntity : Fragment ) : Boolean
{
    val manager : FragmentManager = activity.fragmentManager
    val transaction : FragmentTransaction = manager.beginTransaction()

    transaction.replace(R.id.context, layoutEntity)
    transaction.commit()

    return true
}