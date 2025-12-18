package ru.spfort.legWeapon.weapons

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import ru.spfort.legWeapon.plugin

class AppleSword(
    name: String,
    id: String,
    item: ItemStack,
    cooldownTime: Long
) : Weapon(id, name, item, cooldownTime) {
    override fun damage(damager: Player, target: Entity) {}

    override fun use(player: Player) {}

    override fun kill(killer: Player, victim: Entity) {
        if (victim !is Player) return
        if (victim.killer != killer) return
        killer.getAttribute(Attribute.MAX_HEALTH)!!.baseValue += 2.0
    }

    override val craft: Recipe
        get() = ShapedRecipe(NamespacedKey(plugin, id), item).apply {
            shape(
                "ACA",
                "ADA",
                "AGA"
            )
            setIngredient('A', Material.APPLE)
            setIngredient('C', Material.ENCHANTED_GOLDEN_APPLE)
            setIngredient('G', Material.GOLDEN_APPLE)
            setIngredient('D', Material.DIAMOND_SWORD)
        }
}