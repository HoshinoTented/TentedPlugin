package saki.demo

import android.content.ServiceConnection
import com.saki.aidl.AppServiceInterface

class SQConnection : ServiceConnection
{
    internal var service : AppServiceInterface? = null

    override fun onServiceConnected(name: android.content.ComponentName, service: android.os.IBinder)
    {
        this.service = AppServiceInterface.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: android.content.ComponentName) {}
}
