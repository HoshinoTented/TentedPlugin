package tented.game.spell.cards

import org.json.JSONObject
import tented.extra.toUnit
import tented.shop.Item

/**
 * Created by Hoshino Tented on 2017/12/29.
 *
 * {
 *      "id":"id", "name":"name", "price":"price", "info":"info", "lowHurt":"lowHurt", "highHurt":"highHurt"[,
 *      ....more properties]
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