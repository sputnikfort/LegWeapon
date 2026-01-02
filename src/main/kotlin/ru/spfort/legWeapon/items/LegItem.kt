package ru.spfort.legWeapon.items

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

@Suppress("UNUSED")
abstract class LegItem(
    val id: String,
    val name: String,
    val item: ItemStack,
    val cooldown: Long,
    val craft: ShapedRecipe
) {
    open fun use(player: Player){}
    open fun kill(player: Player, victim: Entity){}
    open fun ownerKilled(victim: Player, killer: Player?){}
    open fun damage(player: Player, target: Entity){}
    open fun ownerDamaged(damager: Player, owner: Player){}

}