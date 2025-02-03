package yqloss.yqlossclientmixinkt.event.minecraft

import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface YCMinecraftEvent : YCEvent {
    sealed interface Load : YCMinecraftEvent {
        data object Post : Load
    }

    sealed interface Loop : YCMinecraftEvent {
        data object Pre : Loop
    }
}
