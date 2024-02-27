package me.outspending.core.mining

import me.outspending.core.format
import me.outspending.core.helpers.FormatHelper.Companion.parse
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*

private const val UPDATE_INDEX: UByte = 50u

private val BROKEN_KEY = NamespacedKey("stats", "blocks_broken")

class MetaStorage internal constructor(var player: Player, var meta: ItemMeta) {
    companion object {
        val metaData: WeakHashMap<Player, MetaStorage> = WeakHashMap()
    }

    private var currentIndex: UByte = 0u
    var data: PersistentDataContainer = meta.persistentDataContainer

    init {
        metaData[player] = this
    }

    fun canUpdate(): Boolean = currentIndex >= UPDATE_INDEX

    /** This method is to grab a value from the PDC */
    fun <P, C : Any> grabValueWithDefault(
        key: NamespacedKey,
        type: PersistentDataType<P, C>,
        defaultValue: C
    ): C = data.getOrDefault(key, type, defaultValue)

    fun <P, C : Any> grabValue(key: NamespacedKey, type: PersistentDataType<P, C>): C? =
        data.get(key, type)

    /** This method is to add a value with the given NamespacedKey */
    fun addValue(key: NamespacedKey, value: Int) {
        val current = grabValueWithDefault(key, PersistentDataType.INTEGER, 0)
        data.set(key, PersistentDataType.INTEGER, current + value)
    }

    private fun updateLore() {
        // Only need to update Lore line of 4
        val lore = meta.lore() ?: return

        addValue(BROKEN_KEY, currentIndex.toInt())
        val broken = grabValue(BROKEN_KEY, PersistentDataType.INTEGER)!!

        lore[4] = " <second><b>|</b> <gray>ʙʟᴏᴄᴋꜱ ʙʀᴏᴋᴇɴ: <white>${broken.format()}".parse()

        meta.lore(lore)
    }

    private fun giveItem() {
        val item = player.inventory.itemInMainHand

        item.itemMeta = meta
        player.inventory.setItemInMainHand(item)
    }

    fun forceUpdate() {
        updateLore()
        giveItem()

        currentIndex = 0u
    }

    fun update(heldItem: ItemStack) {
        meta.lore(heldItem.lore())

        heldItem.itemMeta = meta
        player.inventory.setItemInMainHand(heldItem)
    }

    fun run() {
        if (!canUpdate()) currentIndex++ else forceUpdate()
    }
}
