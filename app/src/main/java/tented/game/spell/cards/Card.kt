package tented.game.spell.cards

import org.json.JSONObject
import tented.util.toUnit
import tented.shop.Item

/**
 * Created by Hoshino Tented on 2017/12/29.
 *
 * item编写方式
 * {
 *      "code":200,
 *      "items":
 *      [
 *          {
 *              "id":"id", "name":"name", "info":"info", "price":price, "lowHurt":lowHurt, "highHurt":highHurt[,
 *              "freeze":冻结时间, "health":治疗的生命值, "hardAttack":暴击几率(百分比)]
 *          }[,
 *          ...more items]
 *      ]
 * }
 */
class Card : Item
{
    companion object
    {
        val LOW_HURT = "lowHurt"
        val HIGH_HURT = "highHurt"
    }

    constructor(id : String , name : String , price : Long , info : String, lHurt : Int , hHurt : Int) : super(id, name)
    {
        this.info.put("info", info)
        this.info.put("price", price)
        this.info.put(LOW_HURT, lHurt)
        this.info.put(HIGH_HURT, hHurt)
    }

    constructor(jsonObject : JSONObject) : super(jsonObject)
    {
        if (! (jsonObject.has("price") && jsonObject.has(LOW_HURT) && jsonObject.has(HIGH_HURT) && jsonObject.has("info"))) throw IllegalArgumentException("$jsonObject\n is not a card json")
    }

    var price : Long
        get() = info.getLong("price")
        set(value) = info.put("price", value).toUnit()

    var lowHurt : Int
        get() = info.getInt(LOW_HURT)
        set(value) = info.put(LOW_HURT, value).toUnit()

    var highHurt : Int
        get() = info.getInt(HIGH_HURT)
        set(value) = info.put(HIGH_HURT, value).toUnit()
}