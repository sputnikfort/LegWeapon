package ru.spfort.legWeapon.ext

import com.filkond.paperktlib.adventure.ext.deserialize
import dev.triumphteam.gui.paper.Gui
import dev.triumphteam.gui.paper.builder.item.ItemBuilder
import dev.triumphteam.gui.paper.kotlin.builder.buildGui
import dev.triumphteam.gui.paper.kotlin.builder.chestContainer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.inventory.Recipe
import org.bukkit.inventory.ShapedRecipe
import ru.spfort.legWeapon.RecipesGui.openRecipes

fun Recipe.getGuiFromRecipe(customName: Component? = null) = getRecipeGui(this, customName)


private fun getRecipeGui(recipe: Recipe, customName: Component?): Gui {

    return when (recipe) {
        is ShapedRecipe -> {
            val shapeSize = recipe.shape.size
            buildGui {
                containerType = chestContainer {
                    rows = shapeSize
                }

                if (customName != null) title(customName) else title(
                    recipe.result.displayName()
                )

                val offset = recipe.shape.maxOf { it.length - 1 }

                statelessComponent { container ->
                    container.setItem(13, ItemBuilder.from(Material.CRAFTING_TABLE).asGuiItem())
                    container.setItem(15, ItemBuilder.from(recipe.result).asGuiItem())
                    container.setItem(
                        shapeSize, 9,
                        ItemBuilder.from(Material.ARROW)
                            .name("Назад".deserialize().decoration(TextDecoration.ITALIC, false))
                            .asGuiItem { player, _ ->
                                player.openRecipes()
                            }
                    )
                    @Suppress("DEPRECATION")
                    recipe.shape.map { i -> i.toCharArray().map { recipe.ingredientMap[it] } }
                        .forEachIndexed { i, list ->
                            list.forEachIndexed { j, item ->
                                if (item == null) return@forEachIndexed
                                container.setItem(i + 1, j + (3 - offset), ItemBuilder.from(item).asGuiItem())
                            }
                        }
                }
            }
        }

        else -> {
            buildGui { title("not implemented yet ;(".deserialize()) }
        }
    }


}