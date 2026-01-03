package ru.spfort.legWeapon.items.weapons

import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class AppleSword(
    name: String,
    id: String,
    item: ItemStack,
    cooldownTime: Long,
    craft: ShapedRecipe,
) : Weapon(id, name, item, cooldownTime, craft) {

    override fun kill(player: Player, victim: Entity) {
        if (victim !is Player) return
        if (victim.killer != player) return
        player.getAttribute(Attribute.MAX_HEALTH)!!.baseValue += 2.0
    }
}