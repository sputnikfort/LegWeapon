package ru.spfort.legWeapon.config

import com.filkond.paperktlib.adventure.ext.deserialize
import com.filkond.paperktlib.config.ReloadableConfig
import de.tr7zw.nbtapi.NBT
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType
import ru.spfort.legWeapon.plugin
import ru.spfort.legWeapon.pluginKey
import ru.spfort.legWeapon.items.weapons.AppleSword
import ru.spfort.legWeapon.items.weapons.EmeraldHammer
import ru.spfort.legWeapon.items.weapons.IceKatana
import ru.spfort.legWeapon.items.weapons.Weapon
import ru.spfort.legWeapon.weaponsRegistry

@Serializable
open class WeaponsConfig(
    var settings: List<WeaponSettings> = listOf(
        IceKatanaSettings(),
        AppleSwordSettings(),
        EmeraldHammerSettings()
    )
) : ReloadableConfig {

    override fun preReload() {
        weaponsRegistry.forEach { plugin.server.removeRecipe(it.value.craft.key) }
    }

    companion object : WeaponsConfig()

    @Serializable
    class IceKatanaSettings(
        val id: String = "iceKatana",
        val name: String = "IceKatana",
        val customModelData: String = "icesword",
        val item: Material = Material.DIAMOND_SWORD,
        val abilityDuration: Long = 10,
        val recipeInfo: RecipeInfo = RecipeInfo(
            id,
            listOf(
                "IPI",
                "IDI",
                "IBI"
            ),
            mapOf(
                'I' to Material.DIAMOND,
                'D' to Material.DIAMOND_SWORD,
                'B' to Material.IRON_BLOCK,
                'P' to Material.POWDER_SNOW_BUCKET
            )
        ),
    ) : WeaponSettings(true, 30) {
        override fun getRegistry(): Weapon {
            val item = constructItem(name, id, customModelData, item)
            return IceKatana(
                name,
                id,
                item,
                abilityDuration,
                cooldown,
                recipeInfo.toRecipe(item)
            )
        }
    }

    @Serializable
    class AppleSwordSettings(
        val id: String = "appleSword",
        val name: String = "AppleSword",
        val customModelData: String = "applesword",
        val item: Material = Material.DIAMOND_SWORD,
        val recipeInfo: RecipeInfo = RecipeInfo(
            id,
            listOf(
                "ACA",
                "ADA",
                "AGA"
            ),
            mapOf(
                'A' to Material.APPLE,
                'C' to Material.ENCHANTED_GOLDEN_APPLE,
                'G' to Material.GOLDEN_APPLE,
                'D' to Material.DIAMOND_SWORD
            )
        )
    ) : WeaponSettings(true, 0) {
        override fun getRegistry(): Weapon {
            val item = constructItem(name, id, customModelData, item)
            return AppleSword(
                name,
                id,
                item,
                cooldown,
                recipeInfo.toRecipe(item)
            )
        }
    }

    @Serializable
    class EmeraldHammerSettings(
        val id: String = "emeraldHammer",
        val name: String = "EmeraldHammer",
        val customModelData: String = "emeraldhammer",
        val item: Material = Material.NETHERITE_AXE,
        val recipeInfo: RecipeInfo = RecipeInfo(
            id,
            listOf(
                "ELE",
                "EBE",
                " B "
            ),
            mapOf(
                'L' to Material.LODESTONE,
                'E' to Material.EMERALD_BLOCK,
                'B' to Material.BREEZE_ROD
            )
        )
    ) : WeaponSettings(true, 60) {
        override fun getRegistry(): Weapon {
            val item = constructItem(name, id, customModelData, item)
            return EmeraldHammer(
                name,
                id,
                item,
                cooldown,
                recipeInfo.toRecipe(item)
            )
        }

    }

    @Serializable
    sealed class WeaponSettings(val enabled: Boolean = true, val cooldown: Long) {
        abstract fun getRegistry(): Weapon
    }

    @Serializable
    class RecipeInfo(
        val id: String,
        val rows: List<String>,
        val materials: Map<Char, Material>
    ) {
        fun toRecipe(item: ItemStack): ShapedRecipe {
            val key = NamespacedKey(plugin, id)
            return ShapedRecipe(key, item).apply {
                shape(*rows.toTypedArray())
                materials.forEach { (key, value) ->
                    setIngredient(key, value)
                }
            }
        }
    }
}

private fun constructItem(name: String, id: String, customModelData: String, item: Material) = ItemBuilder.from(item)
    .name(name.deserialize().decoration(TextDecoration.ITALIC, false))
    .pdc { it.set(pluginKey, PersistentDataType.STRING, id) }
    .asItemStack().apply {
        NBT.modifyComponents(this) { components ->
            components.getOrCreateCompound("minecraft:custom_model_data")
                .getStringList("strings")
                .add(customModelData)
        }
    }