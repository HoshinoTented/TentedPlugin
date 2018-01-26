package tented.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tented.demo.kotlin.R
import tented.ui.Fragment

/**
 * OtherFragment
 * @author Hoshino Tented
 * @date 2018/1/24 4:09
 */
class OtherFragment : Fragment()
{
    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? =
            if( inflater != null )
            {
                val view = inflater.inflate(R.layout.bottom_other_fragment, container, false)

                //TODO something

                view
            }
            else null
}