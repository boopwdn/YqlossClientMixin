package yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import net.minecraft.client.gui.GuiScreen
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.impl.gui.GUIEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.NanoVGUIContext
import yqloss.yqlossclientmixinkt.util.partialTicks

fun updateCameraAndRenderPre(partialTicksIn: Double) {
    partialTicks = partialTicksIn
    YC.eventDispatcher(YCRenderEvent.Render.Pre)
}

fun updateCameraAndRenderRenderHUD() {
    GUIEvent.HUD
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
    GUIEvent.Screen
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

fun updateCameraAndRenderProxyScreen(
    screen: GuiScreen,
    mouseX: Int,
    mouseY: Int,
    partialTicks: Float,
) {
    YCRenderEvent.Screen
        .Proxy(screen)
        .also(YC.eventDispatcher)
        .mutableScreen
        .drawScreen(mouseX, mouseY, partialTicks)
}
