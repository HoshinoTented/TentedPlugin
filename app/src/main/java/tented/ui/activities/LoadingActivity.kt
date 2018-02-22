package tented.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.graphics.drawable.RoundedBitmapDrawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.tented.demo.kotlin.R
import java.lang.Math.max

/**
 * LoadingActivity
 * 伪加载
 * @author Hoshino Tented
 * @date 2018/1/27 3:40
 */

class LoadingActivity : AppCompatActivity()
{
    private lateinit var icon : ImageView

    private fun loadViews()
    {
        this.icon = findViewById(R.id.icon)
    }

    private fun viewsInit()
    {
        fun getBitmap( drawable : Drawable) : Bitmap
        {
            val width = drawable.intrinsicWidth
            val height = drawable.intrinsicHeight
            val config = if( drawable.opacity != PixelFormat.OPAQUE ) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565

            val bitmap = Bitmap.createBitmap(width, height, config)

            val canvas = Canvas(bitmap)

            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)

            return bitmap
        }

        //Header的头像圆化处理
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
        val image : RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        image.paint.isAntiAlias = true
        image.cornerRadius = max(bitmap.width, bitmap.height).toFloat()

        val newBitmap = getBitmap(image)        //把Drawable对象转化为Bitmap对象

        //绘画圆环
        val canvas = Canvas(newBitmap)
        val paint = Paint()

        val ringWidth = 20F

        paint.isAntiAlias = true            //抗锯齿
        paint.strokeWidth = 20F             //圆环宽度
        paint.color = getColor(R.color.title_color)          //圆环颜色
        paint.style = Paint.Style.STROKE    //设置为描边(不设置的话是填充, 会变成实心圆)

        canvas.drawArc(
                RectF(
                        0F + ringWidth / 2,
                        0F + ringWidth / 2,
                        newBitmap.width - ringWidth / 2,
                        newBitmap.height - ringWidth / 2
                     ),
                0F, 360F, false, paint
                      )

        icon.setImageDrawable(BitmapDrawable(resources, newBitmap))
    }

    /**
     * 拦截返回键
     */
    override fun onBackPressed() = Unit

    override fun onCreate(savedInstanceState : Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_layout)

        loadViews()
        viewsInit()

        Thread {
            Thread.sleep(3500)

            val intent = Intent()

            intent.setClass(this, MainActivity::class.java)
            startActivityForResult(intent, 9)

            finish()
        }.start()
    }
}