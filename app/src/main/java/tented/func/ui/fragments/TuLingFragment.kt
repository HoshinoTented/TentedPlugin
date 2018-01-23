package tented.func.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tented.demo.kotlin.R
import tented.file.File
import tented.func.ui.Fragment
import tented.util.getPath

/**
 * TuLingFragment
 * @author Hoshino Tented
 * @date 2018/1/19 3:35
 */
class TuLingFragment : Fragment()
{
    private lateinit var edit :EditText
    private lateinit var commit : Button
    private lateinit var keys : Button

    private fun loadViews( view : View )
    {
        this.edit = view.findViewById(R.id.apiKey)
        this.commit = view.findViewById(R.id.commit)
        this.keys = view.findViewById(R.id.keys)
    }

    private fun setListeners()
    {
        fun <T> List<T>.listToString() : String
        {
            val builder = StringBuilder()

            forEach {
                builder.appendln(it)
            }

            return builder.toString()
        }

        this.commit.setOnClickListener {
            val key = edit.text.toString()

            Toast.makeText(
                    activity,
                    if( key.matches(Regex("[0-9a-z]{32}")) )
                    {
                        val keys = File.readLines(File.getPath("Tuling.txt")).toMutableList()

                        if (keys.contains(key)) keys.remove(key) else keys.add(key)

                        File.write(File.getPath("Tuling.txt"), keys.listToString())

                        "成功了喵~"
                    }
                    else "Key的格式不对了啦!"
                    , Toast.LENGTH_SHORT).show()

        }

        this.keys.setOnClickListener {
            val keys = File.readLines(File.getPath("Tuling.txt"))

            val alert = AlertDialog.Builder(activity).create()

            alert.setTitle("目前的所有Key...")
            alert.setMessage(keys.listToString())
            alert.show()
        }
    }

    override fun onCreateView(inflater : LayoutInflater?, container : ViewGroup?, savedInstanceState : Bundle?) : View? =
        if( inflater != null )
        {
            val view = inflater.inflate(R.layout.bottom_tuling_fragment, container, false)

            loadViews(view)
            setListeners()

            view
        }
        else null
}