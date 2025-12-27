package ru.spfort.legWeapon.weapons

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

abstract class Weapon(
    val id: String,
    val name: String,
    val item: ItemStack,
    val cooldown: Long,
    val craft: ShapedRecipe
) {
    open fun damage(damager: Player, target: Entity){}
    open fun use(player: Player){}
    open fun kill(killer: Player, victim: Entity){}
}