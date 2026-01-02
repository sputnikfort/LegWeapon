package ru.spfort.legWeapon.ext

import org.bukkit.entity.Player
import ru.spfort.legWeapon.items.LegItem

private val cooldowns = mutableMapOf<Player, MutableList<Cooldown>>()

fun Player.setCooldown(item: LegItem) {
    if (cooldowns[this] == null) cooldowns[this] = mutableListOf()
    val cooldown = cooldowns[this]!!.find { it.item == item }
    if (cooldown != null) {
        cooldown.cooldown = System.currentTimeMillis() + (item.cooldown * 1000)
    }else{
        cooldowns[this]!!.add(Cooldown(item, System.currentTimeMillis() + (item.cooldown * 1000)))
    }

}

fun Player.hasCooldown(item: LegItem): Boolean {
    val cooldownsList = cooldowns[this] ?: return false
    return cooldownsList.find { it.item == item }?.let { it.cooldown > System.currentTimeMillis() } == true
}

fun Player.getCooldown(item: LegItem): Long {
    val cooldownsList = cooldowns[this]?.toMutableList() ?: return 0
    return cooldownsList.find { it.item == item }?.let {
        val cd = it.cooldown - System.currentTimeMillis()
        if (cd < 0 ) cooldownsList.remove(it)
        return cd
    } ?: 0
}

private data class Cooldown(
    val item: LegItem,
    var cooldown: Long,
)