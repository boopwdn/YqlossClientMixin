package yqloss.yqlossclientmixinkt.util

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import yqloss.yqlossclientmixinkt.util.scope.noexcept
import java.util.UUID

val ItemStack?.skyBlockID: String?
    get() {
        return this?.run {
            noexcept {
                this.tagCompound.getCompoundTag("ExtraAttributes").getString("id")
            }
        }
    }

val ItemStack?.skyBlockUUID: UUID?
    get() {
        return this?.run {
            noexcept {
                UUID.fromString(this.tagCompound.getCompoundTag("ExtraAttributes").getString("uuid"))
            }
        }
    }

val SKYBLOCK_MINING_TOOLS by lazy {
    setOf(
        Items.wooden_pickaxe,
        Items.stone_pickaxe,
        Items.golden_pickaxe,
        Items.iron_pickaxe,
        Items.diamond_pickaxe,
        Items.skull,
        Items.prismarine_shard,
    )
}
