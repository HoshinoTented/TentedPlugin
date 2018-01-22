package tented.game.spell

import tented.util.getPath
import tented.file.File
import tented.shop.Shop

/**
 * Created by Hoshino Tented on 2017/12/29.
 */
object SpellCardShop
{
    operator fun get(group : Long) : Shop = Shop(File.getPath("$group/SpellCardGame/Shop/items.json"))
}