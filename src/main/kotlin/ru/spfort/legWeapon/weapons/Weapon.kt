package ru.spfort.legWeapon.weapons

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

abstract class Weapon(
    val id: String,
    val name: String,
    val item: ItemStack,
    val cooldown: Long,
) {
    abstract fun damage(damager: Player, target: Entity)
    abstract fun use(player: Player)
    abstract fun kill(killer: Player, victim: Entity)
    abstract val craft: Recipe
}