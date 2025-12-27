package ru.spfort.legWeapon.weapons

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.BlockPosition
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.scheduler.BukkitRunnable
import ru.spfort.legWeapon.plugin
import ru.spfort.legWeapon.protocolManager
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class IceKatana(
    name: String,
    id: String,
    item: ItemStack,
    val abilityDuration: Long,
    cooldownTime: Long,
    craft: ShapedRecipe
) : Weapon(id, name, item, cooldownTime, craft) {
    override fun damage(damager: Player, target: Entity) {
        var i = 0
        plugin.server.asyncScheduler.runAtFixedRate(
            plugin,
            {
                target.freezeTicks = 180
                i++
                if (i > 10) it.cancel()
            },
            0L,
            1L,
            TimeUnit.SECONDS,
        )
    }

    override fun use(player: Player) {
        val task = plugin.server.scheduler.runTaskTimer(
            plugin,
            Runnable{
                val block = player.location.add(0.0, -1.0, 0.0).block
                if (block.type == Material.WATER || block.type == Material.AIR) {
                    block.type = Material.BLUE_ICE

                    val fakeId = Random.nextInt()
                    object : BukkitRunnable() {
                        var stage = 0
                        override fun run() {
                            if (stage > 9) {
                                if (block.type == Material.BLUE_ICE) {
                                    block.type = Material.AIR
                                }
                                cancel()
                                return
                            }

                            for (viewer in block.world.getNearbyPlayers(block.location, 16.0)) {
                                sendBreakAnimation(viewer, block.x, block.y, block.z, stage, fakeId)
                            }

                            stage++
                        }
                    }.runTaskTimer(plugin, 0L, 2L)
                }
            },
            0L,
            1L
        )

        plugin.server.asyncScheduler.runDelayed(plugin, {task.cancel()}, abilityDuration, TimeUnit.SECONDS)
    }

    override fun kill(killer: Player, victim: Entity) {}

    private fun sendBreakAnimation(player: Player, x: Int, y: Int, z: Int, stage: Int, fakeId: Int) {
        val packet: PacketContainer = protocolManager.createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION)
        packet.integers.write(0, fakeId)
        packet.blockPositionModifier.write(0, BlockPosition(x, y, z))
        packet.integers.write(1, stage) // 0..9
        protocolManager.sendServerPacket(player, packet)
    }
}