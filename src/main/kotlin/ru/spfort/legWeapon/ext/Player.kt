package ru.spfort.legWeapon.ext

import org.bukkit.entity.Player
import ru.spfort.legWeapon.weapons.Weapon

private val cooldowns = mutableMapOf<Player, MutableList<Cooldown>>()

fun Player.setCooldown(weapon: Weapon) {
    if (cooldowns[this] == null) cooldowns[this] = mutableListOf()
    val cooldown = cooldowns[this]!!.find { it.weapon == weapon }
    if (cooldown != null) {
        cooldown.cooldown = System.currentTimeMillis() + (weapon.cooldown * 1000)
    }else{
        cooldowns[this]!!.add(Cooldown(weapon, System.currentTimeMillis() + (weapon.cooldown * 1000)))
    }

}

fun Player.hasCooldown(weapon: Weapon): Boolean {
    val cooldownsList = cooldowns[this] ?: return false
    return cooldownsList.find { it.weapon == weapon }?.let { it.cooldown > System.currentTimeMillis() } == true
}

fun Player.getCooldown(weapon: Weapon): Long {
    val cooldownsList = cooldowns[this]?.toMutableList() ?: return 0
    return cooldownsList.find { it.weapon == weapon }?.let {
        val cd = it.cooldown - System.currentTimeMillis()
        if (cd < 0 ) cooldownsList.remove(it)
        return cd
    } ?: 0
}

private data class Cooldown(
    val weapon: Weapon,
    var cooldown: Long,
)