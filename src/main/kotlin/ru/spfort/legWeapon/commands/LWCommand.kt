package ru.spfort.legWeapon.commands

import com.filkond.paperktlib.adventure.ext.deserialize
import dev.triumphteam.cmd.bukkit.annotation.Permission
import dev.triumphteam.cmd.core.BaseCommand
import dev.triumphteam.cmd.core.annotation.Command
import dev.triumphteam.cmd.core.annotation.SubCommand
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import ru.spfort.legWeapon.RecipesGui.openRecipes
import ru.spfort.legWeapon.messages
import ru.spfort.legWeapon.plugin
import ru.spfort.legWeapon.weapons.Weapon

@Suppress("unused")
@Command("legWeapon", alias = ["lw"])
class LWCommand: BaseCommand() {

    @Permission("lw.admin.command.get")
    @SubCommand("get")
    fun get(sender: CommandSender, weapon: Weapon) {
        if (sender !is Player) return
        sender.inventory.addItem(weapon.item)
    }

    @Permission("lw.admin.command.reload")
    @SubCommand("reload")
    fun reload(sender: CommandSender) {
        plugin.reloadWeaponConfig()
        sender.sendMessage(messages.configReloaded.deserialize())
    }

    @SubCommand("recipes")
    fun recipes(sender: CommandSender) {
        if (sender !is Player) return
        sender.openRecipes()
    }

}