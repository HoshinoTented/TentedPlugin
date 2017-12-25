package saki.demo

import java.io.ByteArrayOutputStream

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.IBinder
import android.os.RemoteException

import com.tented.demo.kotlin.R
import com.saki.aidl.AppInterface.Stub
import com.saki.aidl.PluginMsg
import com.saki.aidl.Type
import java.util.Random

class Demo : Service()
{
    companion object
    {

        private val JUMP = false

        private val AUTHOR = "星野天忆"
        private val INFO = "Tented Plugin"

        private var connection : SQConnection? = null

        private var checkCode : Long? = null

        fun debug(obj: Any?) : Any?
        {
            PluginMsg.send(PluginMsg.TYPE_DEBUG, message = obj.toString())
            return obj
        }

        fun doCheck( msg : PluginMsg )
        {
            if( msg.msg == "获取验证码" && checkCode == null )
            {
                this.checkCode = debug(Random().nextLong()) as? Long

                msg.clearMsg()
                msg.addMsg(Type.MSG, "验证码已发送到运行日志")
                msg.send()
            }

            else if( msg.msg.matches(Regex("-?[0-9]+")) && checkCode.toString() == msg.msg)
            {
                msg.member.master = true
                checkCode = null            //置null

                msg.clearMsg()
                msg.addMsg(Type.MSG, "验证成功")
                msg.send()
            }
        }

        /**
         * 对主程序发送一个消息包
         * @param msg 消息包内容
         * @return 对于需要返回的消息包如:【获取群列表】时获得返回，否则为null
         * @throws RemoteException
         */
        @Throws(RemoteException::class)
        fun send(msg: PluginMsg): PluginMsg? =
                if (connection != null && connection!!.service != null) connection!!.service!!.handlerMessage(msg)
                else null
    }

    //AIDL接口实现
    private val stub = object : Stub()
    {

        /**
         * 由主程序接收消息时调用
         * @param msg 消息包体
         */
        @Throws(RemoteException::class)
        override fun onMessageHandler(msg: PluginMsg)
        {
            if (msg.type == 18) //主程序请求停止插件
            {
                unbindService(connection)//解绑
                stopSelf()//停止插件
                return
            }

            doCheck(msg)
            tented.handle.PluginLoader.handleMessage(msg)
        }

        /**
         * 界面跳转开关，插件无界面请返回false
         */
        @Throws(RemoteException::class)
        override fun jump(): Boolean = Demo.JUMP

        /**
         * 插件相关简要信息说明
         */
        @Throws(RemoteException::class)
        override fun info(): String = Demo.INFO

        /**
         * 插件作者信息
         */
        @Throws(RemoteException::class)
        override fun author(): String = Demo.AUTHOR

        /**
         * 插件图标
         */
        @Throws(RemoteException::class)
        override fun icon(): ByteArray
        {
            val bit = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher)
            val out = ByteArrayOutputStream()
            bit.compress(Bitmap.CompressFormat.PNG, 100, out)
            return out.toByteArray()
        }
    }

    fun onSet(bool: Boolean) = tented.func.Do.onSet(bool)

    override fun onCreate() = tented.func.Do.serviceOnCreate()

    /**
     * 该方法由主程序绑定插件时调起，请回绑主程序插件服务
     * @param intent
     * @return 返回AIDL接口
     */
    override fun onBind(intent: Intent) : IBinder?
    {
        println("OnBind")
        //回绑
        val i = Intent("com.setqq.v8.service")
        i.`package` = "com.setqq"
        Demo.connection = SQConnection()
        bindService(i, Demo.connection, Context.BIND_AUTO_CREATE)
        //返回插件接口实现对象
        return stub
    }
}
