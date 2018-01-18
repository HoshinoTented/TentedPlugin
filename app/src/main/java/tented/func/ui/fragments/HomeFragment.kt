package tented.func.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tented.demo.kotlin.R
import tented.func.ui.Fragment

/**
 * HomeFragment
 * @author Hoshino Tented
 * @date 2018/1/19 3:35
 */
class HomeFragment :Fragment()
{
    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? =
        if( inflater != null )
        {
            inflater.inflate(R.layout.bottom_home_fragment, container, false)

            //TODO something...
        }
        else null
}