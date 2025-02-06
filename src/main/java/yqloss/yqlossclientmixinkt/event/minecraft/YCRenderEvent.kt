package yqloss.yqlossclientmixinkt.event.minecraft

import yqloss.yqlossclientmixinkt.event.YCEvent

sealed interface YCRenderEvent : YCEvent {
    sealed interface Render : YCRenderEvent {
        data object Pre : Render
    }

    sealed interface Entity : YCRenderEvent {
        data object Post : Entity
    }
}
