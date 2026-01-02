package ru.spfort.legWeapon.items.armors

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import ru.spfort.legWeapon.items.LegItem

@Suppress("UNUSED")
abstract class Armor(
    id: String,
    name: String,
    item: ItemStack,
    cooldown: Long,
    craft: ShapedRecipe
) : LegItem(id, name, item, cooldown, craft ) {
    open fun equip(player: Player){}
    open fun unequip(player: Player){}
}