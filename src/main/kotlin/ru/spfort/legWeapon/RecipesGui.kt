package ru.spfort.legWeapon

import com.filkond.paperktlib.adventure.ext.deserialize
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import dev.triumphteam.gui.paper.kotlin.builder.buildGui
import dev.triumphteam.gui.paper.kotlin.builder.chestContainer
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import ru.spfort.legWeapon.ext.getGuiFromRecipe
import kotlin.math.ceil

object RecipesGui {

    private val gui = buildGui {
        containerType = chestContainer {
            rows = ceil(weaponsRegistry.size / 9.0).toInt()
        }

        title("Крафты".deserialize())
        statelessComponent { container ->
            var i = 0
            weaponsRegistry.forEach {
                container[i] = ItemBuilder.from(it.value.craft.result)
                    .name(
                        it.value.craft.result.displayName().decoration(TextDecoration.ITALIC, false)
                    ).asGuiItem { player, _ ->
                        it.value.craft.getGuiFromRecipe().open(player)
                    }
                i++
            }
        }

    }

    fun Player.openRecipes() {
        gui.open(this)
    }
}