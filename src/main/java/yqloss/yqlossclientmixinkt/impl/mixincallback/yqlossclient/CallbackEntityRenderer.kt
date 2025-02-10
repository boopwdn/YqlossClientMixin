package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.NanoVGUIContext
import yqloss.yqlossclientmixinkt.util.partialTicks

fun updateCameraAndRenderPre(partialTicksIn: Double) {
    partialTicks = partialTicksIn
    YC.eventDispatcher(YCRenderEvent.Render.Pre)
}

fun updateCameraAndRenderRenderHUD() {
    YCRenderEvent.GUI.HUD
        .Post()
        .also(YC.eventDispatcher)
        .apply {
            val helper = NanoVGHelper.INSTANCE
            helper.setupAndDraw { vg ->
                val context = NanoVGUIContext(helper, vg)
                helper.setAlpha(vg, 1.0F)
                widgets.forEach { it.draw(context) }
            }
        }
}

fun updateCameraAndRenderRenderScreen() {
    YCRenderEvent.GUI.Screen
        .Post()
        .also(YC.eventDispatcher)
        .apply {
            val helper = NanoVGHelper.INSTANCE
            helper.setupAndDraw { vg ->
                val context = NanoVGUIContext(helper, vg)
                helper.setAlpha(vg, 1.0F)
                widgets.forEach { it.draw(context) }
            }
        }
}
