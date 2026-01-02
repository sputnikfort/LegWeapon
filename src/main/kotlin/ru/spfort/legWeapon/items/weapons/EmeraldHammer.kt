package ru.spfort.legWeapon.items.weapons

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Damageable
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import ru.spfort.legWeapon.plugin
import kotlin.math.sqrt

class EmeraldHammer(
    name: String,
    id: String,
    item: ItemStack,
    cooldownTime: Long,
    craft: ShapedRecipe
): Weapon(id, name, item, cooldownTime, craft) {
    override fun damage(damager: Player, target: Entity) {
    }

    override fun use(player: Player) {
        val radius = 6.0
        val knockbackStrength = 1.2
        val upward = 0.4

        val center: Location = player.location

        for (entity in player.world.getNearbyEntities(center, radius, radius, radius)) {
            if (entity.uniqueId == player.uniqueId) continue

            val dx = entity.location.x - center.x
            val dz = entity.location.z - center.z

            val (vx, vz) = if (dx == 0.0 && dz == 0.0) {
                Pair((Math.random() - 0.5) * 0.1, (Math.random() - 0.5) * 0.1)
            } else {
                val len = sqrt(dx * dx + dz * dz)
                Pair(dx / len * knockbackStrength, dz / len * knockbackStrength)
            }

            val velocity = entity.velocity
            velocity.x = vx
            velocity.y = upward
            velocity.z = vz
            entity.velocity = velocity

            entity.world.spawnParticle(
                Particle.BLOCK_CRUMBLE,
                entity.location.add(0.0, 1.0, 0.0),
                10,
                0.3, 0.3, 0.3,
                0.1,
                plugin.server.createBlockData("minecraft:stone")
            )
            entity.world.playSound(entity.location, Sound.ITEM_MACE_SMASH_GROUND, 1.0f, 1.2f)
            if (entity is Damageable) entity.damage(2.0, player)
        }

        player.world.spawnParticle(
            Particle.BLOCK_CRUMBLE,
            center.add(0.0, 1.0, 0.0),
            15,
            0.5, 0.5, 0.5,
            0.2,
            plugin.server.createBlockData("minecraft:stone")
        )
        player.world.playSound(center, Sound.ITEM_MACE_SMASH_GROUND, 1.0f, 1.0f)
    }

    override fun kill(killer: Player, victim: Entity) {
    }
}