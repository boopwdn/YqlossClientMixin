package yqloss.yqlossclientmixinkt.event.minecraft

import net.minecraft.client.multiplayer.WorldClient
import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface YCMinecraftEvent : YCEvent {
    sealed interface Load : YCMinecraftEvent {
        data object Post : Load
    }

    sealed interface Loop : YCMinecraftEvent {
        data object Pre : Loop
    }

    sealed interface Tick : YCMinecraftEvent {
        data object Pre : Tick
    }

    sealed interface LoadWorld : YCMinecraftEvent {
        data class Pre(
            val world: WorldClient?,
        ) : LoadWorld
    }
}
