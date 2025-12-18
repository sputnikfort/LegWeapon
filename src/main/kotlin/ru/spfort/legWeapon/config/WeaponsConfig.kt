package ru.spfort.legWeapon.config

import com.filkond.paperktlib.adventure.ext.deserialize
import com.filkond.paperktlib.config.Config
import de.tr7zw.nbtapi.NBT
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.persistence.PersistentDataType
import ru.spfort.legWeapon.weapons.IceKatana
import ru.spfort.legWeapon.weapons.Weapon
import ru.spfort.legWeapon.pluginKey
import ru.spfort.legWeapon.weapons.AppleSword
import ru.spfort.legWeapon.weapons.EmeraldHammer

@Serializable
class WeaponsConfig(
    val settings: List<WeaponSettings> = listOf(
        IceKatanaSettings(),
        AppleSwordSettings(),
        EmeraldHammerSettings()
    )
) : Config {
    @Serializable
    class IceKatanaSettings(
        val id: String = "iceKatana",
        val name: String = "IceKatana",
        val customModelData: String = "icesword",
        val item: Material = Material.DIAMOND_SWORD,
        val abilityDuration: Long = 10,
    ) : WeaponSettings(true, 30) {
        override fun getRegistry(): Weapon {
            return IceKatana(
                name,
                id,
                ItemBuilder.from(item)
                    .name(name.deserialize().decoration(TextDecoration.ITALIC, false))
                    .pdc { it.set(pluginKey, PersistentDataType.STRING, id) }
                    .asItemStack().apply {
                        NBT.modifyComponents(this) { components ->
                            components.getOrCreateCompound("minecraft:custom_model_data")
                                .getStringList("strings")
                                .add(customModelData)
                        }
                    },
                abilityDuration,
                cooldown
            )
        }
    }
    @Serializable
    class AppleSwordSettings(
        val id: String = "appleSword",
        val name: String = "AppleSword",
        val customModelData: String = "applesword",
        val item: Material = Material.DIAMOND_SWORD,
    ) : WeaponSettings(true, 0) {
        override fun getRegistry(): Weapon {
            return AppleSword(
                name,
                id,
                ItemBuilder.from(item)
                    .name(name.deserialize().decoration(TextDecoration.ITALIC, false))
                    .pdc { it.set(pluginKey, PersistentDataType.STRING, id) }
                    .asItemStack().apply {
                        NBT.modifyComponents(this) { components ->
                            components.getOrCreateCompound("minecraft:custom_model_data")
                                .getStringList("strings")
                                .add(customModelData)
                        }
                    },
                cooldown
            )
        }
    }
    @Serializable
    class EmeraldHammerSettings(
        val id: String = "emeraldHammer",
        val name: String = "EmeraldHammer",
        val customModelData: String = "emeraldhammer",
        val item: Material = Material.NETHERITE_AXE,
    ): WeaponSettings(true, 60) {
        override fun getRegistry(): Weapon {
            return EmeraldHammer(
                name,
                id,
                ItemBuilder.from(item)
                    .name(name.deserialize().decoration(TextDecoration.ITALIC, false))
                    .pdc { it.set(pluginKey, PersistentDataType.STRING, id) }
                    .asItemStack().apply {
                        NBT.modifyComponents(this) { components ->
                            components.getOrCreateCompound("minecraft:custom_model_data")
                                .getStringList("strings")
                                .add(customModelData)
                        }
                    },
                cooldown
            )
        }

    }

    @Serializable
    sealed class WeaponSettings(val enabled: Boolean = true, val cooldown: Long) {
        abstract fun getRegistry(): Weapon
    }
}