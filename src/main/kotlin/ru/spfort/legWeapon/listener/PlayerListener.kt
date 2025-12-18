package ru.spfort.legWeapon.listener

import com.filkond.paperktlib.adventure.ext.deserialize
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.persistence.PersistentDataType
import ru.spfort.legWeapon.ext.getCooldown
import ru.spfort.legWeapon.ext.hasCooldown
import ru.spfort.legWeapon.ext.setCooldown
import ru.spfort.legWeapon.messages
import ru.spfort.legWeapon.plugin
import ru.spfort.legWeapon.pluginKey
import ru.spfort.legWeapon.weaponsRegistry
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class PlayerListener : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val damager = event.damager as Player

        val weaponID =
            damager.inventory.itemInMainHand.persistentDataContainer.get(pluginKey, PersistentDataType.STRING) ?: return

        weaponsRegistry[weaponID]?.damage(damager, event.entity)
    }

    private val interactActions = listOf(Action.RIGHT_CLICK_BLOCK, Action.RIGHT_CLICK_AIR)

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!interactActions.contains(event.action)) return
        val weaponID =
            event.player.inventory.itemInMainHand.persistentDataContainer.get(pluginKey, PersistentDataType.STRING)
                ?: return

        val weapon = weaponsRegistry[weaponID] ?: return


        if (event.player.hasCooldown(weapon)) {
            event.player.sendMessage(messages.youHaveCooldownMessage.deserialize())
            return
        }

        event.player.setCooldown(weapon)

        weapon.use(event.player)

        plugin.server.asyncScheduler.runAtFixedRate(
            plugin,
            {
                val cd = event.player.getCooldown(weapon)
                if (cd <= 0) {
                    it.cancel()
                    return@runAtFixedRate
                }
                event.player.sendActionBar(
                    messages.cooldownActionbar.deserialize(
                        Placeholder.parsed(
                            "cooldown",
                            ceil(cd / 1000.0).toInt().toString()
                        )
                    )
                )
            },
            0L,
            1L,
            TimeUnit.SECONDS
        )
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        if (event.player.killer == null) return
        val weaponID =
            event.player.killer!!.inventory.itemInMainHand.persistentDataContainer.get(
                pluginKey,
                PersistentDataType.STRING
            ) ?: return

        weaponsRegistry[weaponID]?.kill(event.player.killer!!, event.entity)
    }


    @EventHandler(ignoreCancelled = true)
    fun onCraftItem(event: CraftItemEvent) {
        val weaponID =
            event.recipe.result.persistentDataContainer.get(pluginKey, PersistentDataType.STRING)
                ?: return
        val weapon = weaponsRegistry[weaponID] ?: return
        Bukkit.broadcast(
            messages.itemCrafted.deserialize(
                Placeholder.parsed("player", event.whoClicked.name), Placeholder.parsed("item", weapon.name)
            )
        )
        Bukkit.getOnlinePlayers().forEach {
            it.playSound(Sound.sound(messages.itemCraftedSound, Sound.Source.MASTER, 1f, 1f))
        }
        plugin.server.removeRecipe(NamespacedKey(plugin, weapon.id))
    }

}