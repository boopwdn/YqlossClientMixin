package yqloss.yqlossclientmixinkt.event.minecraft

import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.util.asBlockPos
import yqloss.yqlossclientmixinkt.util.math.Vec3I

sealed interface YCRenderEvent : YCEvent {
    sealed interface Render : YCRenderEvent {
        data object Pre : Render
    }

    sealed interface Entity : YCRenderEvent {
        data object Post : Entity
    }

    sealed interface GUI : YCRenderEvent {
        val widgets: MutableList<Widget<*>>

        sealed interface HUD : GUI {
            data class Post(
                override val widgets: MutableList<Widget<*>> = mutableListOf(),
            ) : HUD
        }

        sealed interface Screen : GUI {
            data class Post(
                override val widgets: MutableList<Widget<*>> = mutableListOf(),
            ) : Screen
        }
    }

    sealed interface Block : YCRenderEvent {
        // you must not modify an air block
        // be careful when modifying a block to a tile entity or vice versa
        data class ProcessBlockState(
            val blockAccess: IBlockAccess,
            val blockPos: Vec3I,
            val blockState: IBlockState = blockAccess.getBlockState(blockPos.asBlockPos),
            var mutableBlockState: IBlockState = blockState,
        ) : Block

        // you must not modify an air block
        // be careful when modifying a block to a tile entity or vice versa
        data class ProcessTileEntity(
            val blockAccess: IBlockAccess,
            val blockPos: Vec3I,
            val tileEntity: TileEntity? = blockAccess.getTileEntity(blockPos.asBlockPos),
            var mutableTileEntity: TileEntity? = tileEntity,
        ) : Block
    }
}
