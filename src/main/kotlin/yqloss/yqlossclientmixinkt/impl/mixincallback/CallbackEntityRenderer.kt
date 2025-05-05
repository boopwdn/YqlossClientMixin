/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

package yqloss.yqlossclientmixinkt.impl.mixincallback

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import net.minecraft.client.gui.ScaledResolution
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.GUIEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.NanoVGUIContext
import yqloss.yqlossclientmixinkt.impl.oneconfiginternal.loadFonts
import yqloss.yqlossclientmixinkt.impl.oneconfiginternal.nvg
import yqloss.yqlossclientmixinkt.module.ssmotionblur.SSMotionBlurEvent
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.glStateScope
import yqloss.yqlossclientmixinkt.util.partialTicks

object CallbackEntityRenderer {
    object YqlossClient {
        fun updateCameraAndRenderPre(partialTicksIn: Double) {
            partialTicks = partialTicksIn
            YC.eventDispatcher(YCRenderEvent.Render.Pre)
        }

        fun updateCameraAndRenderRenderHUD() {
            GUIEvent.HUD
                .Post()
                .also(YC.eventDispatcher)
                .apply {
                    glStateScope {
                        val helper = NanoVGHelper.INSTANCE
                        helper.setupAndDraw { vg ->
                            nvg.loadFonts(vg)
                            val context = NanoVGUIContext(helper, vg)
                            helper.setAlpha(vg, 1.0F)
                            widgets.forEach { it.draw(context) }
                        }
                    }
                }
        }

        fun updateCameraAndRenderProxyScreenAtCondition() {
            if (MC.currentScreen !== null) return
            GUIEvent.Screen
                .Post()
                .also(YC.eventDispatcher)
                .apply {
                    glStateScope {
                        val helper = NanoVGHelper.INSTANCE
                        helper.setupAndDraw { vg ->
                            nvg.loadFonts(vg)
                            val context = NanoVGUIContext(helper, vg)
                            helper.setAlpha(vg, 1.0F)
                            widgets.forEach { it.draw(context) }
                        }
                    }
                }
        }
    }

    object SSMotionBlur {
        fun updateCameraAndRenderRenderMotionBlur() {
            YC.eventDispatcher(SSMotionBlurEvent.Render(MC.displayWidth, MC.displayHeight, ScaledResolution(MC)))
        }
    }
}
