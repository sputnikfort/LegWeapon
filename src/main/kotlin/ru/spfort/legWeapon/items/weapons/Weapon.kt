package ru.spfort.legWeapon.items.weapons

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import ru.spfort.legWeapon.items.LegItem

@Suppress("UNUSED")
abstract class Weapon(
    id: String,
    name: String,
    item: ItemStack,
    cooldown: Long,
    craft: ShapedRecipe
) : LegItem(id, name, item, cooldown, craft){
    open fun held(player: Player){}
}