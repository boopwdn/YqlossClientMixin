package yqloss.yqlossclientmixinkt.impl.gui

import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget

sealed interface GUIEvent : YCEvent {
    val widgets: MutableList<Widget<*>>

    sealed interface HUD : GUIEvent {
        data class Post(
            override val widgets: MutableList<Widget<*>> = mutableListOf(),
        ) : HUD
    }

    sealed interface Screen : GUIEvent {
        data class Post(
            override val widgets: MutableList<Widget<*>> = mutableListOf(),
        ) : Screen
    }
}
