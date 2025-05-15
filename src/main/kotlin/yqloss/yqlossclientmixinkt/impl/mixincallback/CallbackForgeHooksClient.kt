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

import net.minecraft.client.gui.GuiScreen
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.impl.nanovgui.GUIEvent

object CallbackForgeHooksClient {
    object YqlossClient {
        private var drawYCScreenAtPost = false

        private fun drawYCScreen() {
            GUIEvent.Screen
                .Post()
                .also(YC.eventDispatcher)
                .render()
        }

        fun drawScreenPre(
            screen: GuiScreen,
            mouseX: Int,
            mouseY: Int,
            partialTicks: Float,
            ci: CallbackInfo,
        ) {
            YCRenderEvent.Screen
                .Proxy(screen)
                .also(YC.eventDispatcher)
                .mutableScreen
                ?.run {
                    ci.cancel()
                    drawScreen(mouseX, mouseY, partialTicks)
                    drawYCScreen()
                }
                ?: run {
                    drawYCScreenAtPost = true
                }
        }

        fun drawScreenPost() {
            if (drawYCScreenAtPost) {
                drawYCScreen()
            }
        }
    }
}
