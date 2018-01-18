package tented.func.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tented.demo.kotlin.R
import tented.extra.isNumber
import tented.func.ui.Fragment
import tented.util.Member

/**
 * MasterFragment
 * @author Hoshino Tented
 * @date 2018/1/19 3:19
 */
class MasterFragment : Fragment()
{
    private lateinit var master : Button

    private lateinit var group : EditText
    private lateinit var uin : EditText

    private fun loadViews( view : View )
    {
        this.master = view.findViewById(R.id.master)

        this.group = view.findViewById(R.id.group)
        this.uin = view.findViewById(R.id.uin)
    }

    private fun setListeners()
    {
        master.setOnClickListener {
            _ ->

            val group = group.text.toString()
            val uin = uin.text.toString()

            if( group.isNumber() && uin.isNumber() )
            {
                val member = Member(group.toLong(), uin.toLong(), null)

                member.master = ! member.master

                Toast.makeText(activity, "修改完毕", Toast.LENGTH_SHORT).show()
            }
            else Toast.makeText(activity, "群号或主人QQ号未填写或数据错误", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? =
            if( inflater != null )
            {
                val view : View = inflater.inflate(R.layout.bottom_master_fragment, container, false)

                loadViews(view)
                setListeners()

                view
            }
            else null

}