package ru.spfort.legWeapon

import com.comphenix.protocol.ProtocolLibrary
import com.filkond.paperktlib.config.ext.JsonConfigManager
import com.filkond.paperktlib.config.ext.load
import com.filkond.paperktlib.config.ext.loadCompanion
import com.filkond.paperktlib.config.ext.reloadAll
import com.filkond.paperktlib.config.ext.saveAll
import dev.triumphteam.cmd.bukkit.BukkitCommandManager
import org.bukkit.NamespacedKey
import org.bukkit.plugin.java.JavaPlugin
import ru.spfort.legWeapon.commands.LWCommand
import ru.spfort.legWeapon.config.Messages
import ru.spfort.legWeapon.config.WeaponsConfig
import ru.spfort.legWeapon.items.LegItem
import ru.spfort.legWeapon.listener.PlayerListener
import ru.spfort.legWeapon.items.weapons.Weapon

lateinit var plugin: LegWeapon

lateinit var messages: Messages

lateinit var pluginKey: NamespacedKey

val protocolManager = ProtocolLibrary.getProtocolManager()!!
private val registries = mutableMapOf<String, LegItem>()
val weaponsRegistry: Map<String, LegItem>
    get() = registries

class LegWeapon : JavaPlugin() {

    val configManager = JsonConfigManager(dataFolder)

    override fun onEnable() {
        configManager.saveAll()
        plugin = this

        pluginKey = NamespacedKey(plugin, "LegWeaponID")

        messages = configManager.load<Messages>("messages.json")

        configManager.loadCompanion<WeaponsConfig>("weapons.json")

        loadWeapons()

        server.pluginManager.registerEvents(PlayerListener(), this)

        val commandManager = BukkitCommandManager.create(this)
        commandManager.registerArgument(Weapon::class.java){ _, arg -> weaponsRegistry[arg] }
        commandManager.registerSuggestion(Weapon::class.java){ _, _ -> weaponsRegistry.keys.toList() }
        commandManager.registerCommand(LWCommand())
    }

    private fun loadWeapons(){
        WeaponsConfig.settings.filter { it.enabled }
            .map { it.getRegistry() }
            .forEach {
                registries[it.id] = it
                plugin.server.addRecipe(it.craft)
            }
    }

    fun reloadWeaponConfig(){
        configManager.reload(WeaponsConfig::class)
        registries.clear()
        loadWeapons()
    }

    override fun onDisable() {
        configManager.reloadAll()
        configManager.saveAll()
    }
}
