package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/25.
 *
 * Handler处理类
 * 需要实现里面的handle方法
 */
abstract class Handler( val name : String , val version : String )
{
    abstract fun handle( msg : com.saki.aidl.PluginMsg )        //这个function推荐还是用when编写吧, 毕竟都是一些if else-if
}