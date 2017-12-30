package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 *
 * 消息处理抽象类
 * 需实现handle方法
 * 为什么删掉Plugin是吗...
 * 因为Handler和Plugin分开的原因原本是因为Main类里面需要添加各个插件的名称
 * 可是...
 * 完全可以从pluginList里面取
 * 所以就删掉
 *
 * 为什么删掉Plugin而不是Handler呢?
 * 因为...删掉Plugin之后就有个bug, 好像是因为类的问题...
 * 因为Plugin之前是继承于Handler类的
 * 所以
 * 就会爆炸
 */

abstract class Handler(val name : String, val version : String)
{
    abstract fun handle(msg : com.saki.aidl.PluginMsg)
}