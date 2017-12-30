package tented.handle

/**
 * Created by Hoshino Tented on 2017/12/24.
 *
 * Handler的抽象子类, 只是多了个构造器而已(为了和一些特殊的Handler有区别, 例如Main)
 */

abstract class Plugin ( name : String , version : String  ) : Handler(name, version)    //1.1+将此接口换成抽象类
{
    init
    {
        tented.handle.plugin.Main.list.add(name)
    }
}