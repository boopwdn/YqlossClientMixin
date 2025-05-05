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

package yqloss.yqlossclientmixinkt.module.ssmotionblur

import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11.*
import yqloss.yqlossclientmixinkt.event.YCEventRegistry
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.extension.double
import yqloss.yqlossclientmixinkt.util.glStateScope
import yqloss.yqlossclientmixinkt.util.mcRenderScope
import yqloss.yqlossclientmixinkt.util.scope.longRun
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

val INFO_SS_MOTION_BLUR = moduleInfo<SSMotionBlurOptions>("ss_motion_blur", "SS Motion Blur")

private const val FRAME_TIME_256 = 1000000000.0 / 256.0

object SSMotionBlur : YCModuleBase<SSMotionBlurOptions>(INFO_SS_MOTION_BLUR) {
    private var lastWidth = -1
    private var lastHeight = -1
    private var widthFactor = 1.0
    private var heightFactor = 1.0
    private var textureId: Int? = null
    private var lastNanos = System.nanoTime()

    private fun setupTexture(
        texture: Int,
        width: Int,
        height: Int,
    ) {
        val maxWH = max(width, height)
        var size = 16
        while (size < maxWH) size *= 2

        widthFactor = width.double / size
        heightFactor = height.double / size

        glBindTexture(GL_TEXTURE_2D, texture)

        ByteBuffer.allocateDirect(size * size * 4).let { buffer ->
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_RGB,
                size,
                size,
                0,
                GL_RGB,
                GL_UNSIGNED_BYTE,
                buffer,
            )
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
        }

        logger.info("created texture $texture ($size*$size) for $width*$height")
    }

    private fun allocate(
        allocateTexture: Boolean,
        width: Int,
        height: Int,
    ) {
        textureId?.let { texture ->
            GlStateManager.deleteTexture(texture)
            logger.info("deleted texture $texture")
            textureId = null
        }

        lastWidth = width
        lastHeight = height

        if (allocateTexture) {
            val texture = GlStateManager.generateTexture()
            textureId = texture
            setupTexture(texture, width, height)
        }
    }

    private fun takeScreenShot(
        width: Int,
        height: Int,
    ) {
        if (!options.enabled) return

        if (textureId === null || lastWidth != width || lastHeight != height) {
            allocate(true, width, height)
        }

        textureId?.let { texture ->
            glBindTexture(GL_TEXTURE_2D, texture)
            glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height)
        }
    }

    private fun getAlpha(): Double {
        var alpha = options.strength

        if (options.balanced) {
            val currentNanos = System.nanoTime()
            val diffNanos = currentNanos - lastNanos
            lastNanos = currentNanos

            alpha = alpha.pow(diffNanos / FRAME_TIME_256)
        }

        return max(0.0, min(1.0, alpha))
    }

    private fun renderMotionBlur(
        screenWidth: Int,
        screenHeight: Int,
        scaledResolution: ScaledResolution,
    ) {
        MC.entityRenderer.setupOverlayRendering()

        glStateScope {
            if (textureId === null || lastWidth != screenWidth || lastHeight != screenHeight) {
                allocate(true, screenWidth, screenHeight)
            }

            textureId?.let { texture ->
                glEnable(GL_TEXTURE_2D)
                glBindTexture(GL_TEXTURE_2D, texture)
                glEnable(GL_BLEND)
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
                glDisable(GL_DEPTH_TEST)
                glDisable(GL_ALPHA_TEST)
                glDisable(GL_CULL_FACE)
                glDisable(GL_LIGHTING)
                glColor4d(1.0, 1.0, 1.0, getAlpha())

                val scaledWidth = scaledResolution.scaledWidth_double
                val scaledHeight = scaledResolution.scaledHeight_double

                mcRenderScope(GL_QUADS, DefaultVertexFormats.POSITION_TEX) {
                    pos(0.0, scaledHeight, 0.0).tex(0.0, 0.0).endVertex()
                    pos(scaledWidth, scaledHeight, 0.0).tex(widthFactor, 0.0).endVertex()
                    pos(scaledWidth, 0.0, 0.0).tex(widthFactor, heightFactor).endVertex()
                    pos(0.0, 0.0, 0.0).tex(0.0, heightFactor).endVertex()
                }
            }

            takeScreenShot(screenWidth, screenHeight)
        }
    }

    override fun registerEvents(registry: YCEventRegistry) {
        registry.run {
            register<YCRenderEvent.Render.Pre> {
                MC.theWorld ?: run {
                    lastNanos = System.nanoTime()
                    glStateScope {
                        allocate(false, -1, -1)
                    }
                }
            }

            register<SSMotionBlurEvent.Render> { event ->
                longRun {
                    ensureEnabled()

                    renderMotionBlur(event.screenWidth, event.screenHeight, event.scaledResolution)
                }
            }
        }
    }
}
