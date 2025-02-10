package yqloss.yqlossclientmixinkt.module.option

import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import yqloss.yqlossclientmixinkt.util.scope.noexcept

interface YCBlockOption {
    val id: String
    val meta: Int
}

val YCBlockOption.blockState: IBlockState
    get() = noexcept { Block.getBlockFromName(id).getStateFromMeta(meta) } ?: Blocks.air.defaultState
