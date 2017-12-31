package tented.game.fishing

import tented.extra.getPath
import tented.file.File
import tented.shop.Shop

/**
 * Created by Hoshino Tented on 2018/1/1.
 */
object FishShop
{
    operator fun get( group : Long ) : Shop = Shop(File.getPath("$group/Fishing/Shop/items.json"))
}