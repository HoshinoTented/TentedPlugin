package tented.game.fishing

import tented.extra.randomTo
import tented.game.fishing.exceptions.NoFishException
import tented.game.fishing.exceptions.NoFishingException
import tented.member.Member
import kotlin.reflect.KProperty

/**
 * Created by Hoshino Tented on 2018/1/1.
 */
class Fisher private constructor( group : Long , uin : Long , name : String? ) : Member(group, uin, name)
{
    object FishingManager
    {
        operator fun getValue( _this : Any? , properties : KProperty<*> ) : Boolean =
                if( _this is Fisher )
                {
                    when (properties.name)
                    {
                        "isFishing" -> fishing
                        "hasFish" -> fish

                        else -> throw IllegalArgumentException("properties is not a field of tented.game.fishing.Fisher")
                    }[_this.group]?.get(_this.uin) ?: false
                }
                else throw IllegalArgumentException("object is not instance of tented.game.fishing.Fisher")

        operator fun setValue( _this : Any? , properties : KProperty<*> , value : Boolean ) =
                if( _this is Fisher )
                {
                    val map =
                        when(properties.name)
                        {
                            "isFishing" -> fishing
                            "hasFish" -> fish

                            else -> throw IllegalArgumentException("properties is not a field of tented.game.fishing.Fisher")
                        }

                    if( map[_this.group] == null ) map[_this.group] = HashMap()

                    map[_this.group]!![_this.uin] = value       //此处不可能为null
                }
                else throw IllegalArgumentException("object is not instance of tented.game.fishing.Fisher")
    }


    companion object
    {
        private val fishing = HashMap<Long, HashMap<Long, Boolean>>()
        private val fish = HashMap<Long, HashMap<Long, Boolean>>()

        fun newInstance( member : Member ) : Fisher = Fisher(member.group, member.uin, member.name)
    }

    private var isFishing : Boolean by FishingManager
    private var hasFish : Boolean by FishingManager

    /**
     * 必须自己限制在 成功钓鱼 之后才能调用
     */
    private fun waitingForFish()
    {
        Thread {
            Thread.sleep(5000 randomTo 30000)     //5~30秒之间

            hasFish = true

            tented.handle.plugin.game.Fishing.hadFish(this)

            waitingForTakingUp()
        }.start()
    }

    /**
     * 鱼已经上钩, 等待接收 起竿 指令
     * 由 waitingForFish() 调用
     */
    private fun waitingForTakingUp()
    {
        Thread {
            Thread.sleep(9999)      //延迟个接近10秒左右

            if( hasFish )
            {
                hasFish = false

                tented.handle.plugin.game.Fishing.lostFish(this)
            }
        }.start()
    }

    /**
     * 钓鱼
     */
    @Throws(NoFishException::class)
    fun fishing()
    {
        if( ! isFishing )
        {
            isFishing = true

            waitingForFish()
        }
        else throw NoFishException
    }

    /**
     * 起竿
     * @return 返回钓起来的Fish对象(其实是Item, 只是搞了个类型别名而已)
     */
    @Throws(NoFishException::class, NoFishingException::class)
    fun takeUp() : Fish
    {
        if( isFishing )
        {
            if( hasFish )
            {
                hasFish = false
                isFishing = false

                val shop = FishShop[group]

                return Fish(shop[(0 randomTo shop.size).toInt()].info)
            }
            else throw NoFishException
        }
        else throw NoFishingException
    }
}