package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 */

abstract class Plugin ( name : String , version : String  ) : Handler(name, version)    //1.1+将此接口换成抽象类
{
    init
    {
        tented.handle.plugin.Main.list.add(name)
    }
}