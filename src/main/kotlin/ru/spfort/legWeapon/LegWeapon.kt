package ru.spfort.legWeapon

import com.comphenix.protocol.ProtocolLibrary
import com.filkond.paperktlib.config.ext.JsonConfigManager
import com.filkond.paperktlib.config.ext.load
import dev.triumphteam.cmd.bukkit.BukkitCommandManager
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import ru.spfort.legWeapon.commands.LWCommand
import ru.spfort.legWeapon.config.Messages
import ru.spfort.legWeapon.config.WeaponsConfig
import ru.spfort.legWeapon.listener.PlayerListener
import ru.spfort.legWeapon.weapons.Weapon

lateinit var plugin: LegWeapon

lateinit var weaponsConfig: WeaponsConfig
lateinit var messages: Messages

lateinit var pluginKey: NamespacedKey

val protocolManager = ProtocolLibrary.getProtocolManager()!!
private val registries = mutableMapOf<String, Weapon>()
val weaponsRegistry: Map<String, Weapon>
    get() = registries

class LegWeapon : JavaPlugin() {
    override fun onEnable() {
        plugin = this

        pluginKey = NamespacedKey(plugin, "LegWeaponID")

        val configManager = JsonConfigManager(dataFolder)
        messages = configManager.load<Messages>("messages.json")
        weaponsConfig = configManager.load<WeaponsConfig>("weapons.json")

        weaponsConfig.settings.filter { it.enabled }
            .map { it.getRegistry() }
            .forEach {
                registries[it.id] = it
                plugin.server.addRecipe(it.craft)
            }

        server.pluginManager.registerEvents(PlayerListener(), this)

        val commandManager = BukkitCommandManager.create(this)
        commandManager.registerArgument(Weapon::class.java){ _, arg -> weaponsRegistry[arg] }
        commandManager.registerSuggestion(Weapon::class.java){ _, _ -> weaponsRegistry.keys.toList() }
        commandManager.registerCommand(LWCommand())
    }
}
