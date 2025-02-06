package yqloss.yqlossclientmixinkt.util

import net.minecraft.item.ItemStack
import yqloss.yqlossclientmixinkt.util.scope.noexcept

val ItemStack?.skyBlockID: String?
    get() {
        return this?.run {
            noexcept {
                this.tagCompound.getCompoundTag("ExtraAttributes").getString("id")
            }
        }
    }
