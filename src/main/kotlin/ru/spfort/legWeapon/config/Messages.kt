package ru.spfort.legWeapon.config

import com.filkond.paperktlib.config.Config
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.Sound

@Serializable
class Messages : Config {
    val cooldownActionbar: String = "<gold><cooldown>"
    val youHaveCooldownMessage: String = "<red>Подождите перед следующим использованием!"
    val itemCrafted: String = "<yellow><player> скрафтил <item>"
    @Serializable(with = SimpleSoundKeySerializer::class)
    val itemCraftedSound: Key = Registry.SOUNDS.getKey(Sound.ENTITY_PLAYER_LEVELUP)!!


    private class SimpleSoundKeySerializer : KSerializer<Key> {
        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)
        override fun deserialize(decoder: Decoder): Key {
            return Registry.SOUNDS.getKey(Registry.SOUNDS.get(NamespacedKey(NamespacedKey.MINECRAFT_NAMESPACE, decoder.decodeString()))!!)!!
        }

        override fun serialize(encoder: Encoder, value: Key) {
            encoder.encodeString(value.value())
        }
    }
}