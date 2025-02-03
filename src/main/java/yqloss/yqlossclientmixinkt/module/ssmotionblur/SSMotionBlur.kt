package yqloss.yqlossclientmixinkt.module.ssmotionblur

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11
import yqloss.yqlossclientmixinkt.MC
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.event.buildEventEntries
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.event.registerEventEntries
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.util.asDouble
import yqloss.yqlossclientmixinkt.util.glStateScope
import yqloss.yqlossclientmixinkt.util.mcRenderScope
import yqloss.yqlossclientmixinkt.util.noexcept
import yqloss.yqlossclientmixinkt.ycLogger
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

private val LOGGER = ycLogger("SS Motion Blur")

private const val FRAME_TIME_256 = 1000000000.0 / 256.0

class SSMotionBlur(
    private val yc: YqlossClient,
) : YCModule<SSMotionBlurOptions>,
    YCEventRegistration {
    override val id = "ss_motion_blur"
    override val name = "SS Motion Blur"
    override val options by lazy { yc.getOptionsImpl(SSMotionBlurOptions::class) }

    override val eventEntries =
        buildEventEntries {
            register<YCRenderEvent.Pre> { onEvent(it) }
            register<SSMotionBlurEvent.Render> { onEvent(it) }
        }

    init {
        registerEventEntries(yc.eventDispatcher)
    }

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

        widthFactor = width.asDouble / size
        heightFactor = height.asDouble / size

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)

        ByteBuffer.allocateDirect(size * size * 4).let { buffer ->
            GL11.glTexImage2D(
                GL11.GL_TEXTURE_2D,
                0,
                GL11.GL_RGB,
                size,
                size,
                0,
                GL11.GL_RGB,
                GL11.GL_UNSIGNED_BYTE,
                buffer,
            )
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        }

        LOGGER.info("created texture $texture ($size*$size) for $width*$height")
    }

    private fun allocate(
        allocateTexture: Boolean,
        width: Int,
        height: Int,
    ) {
        textureId?.let { texture ->
            GlStateManager.deleteTexture(texture)
            LOGGER.info("deleted texture $texture")
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

        noexcept(LOGGER::catching) {
            if (textureId === null || lastWidth != width || lastHeight != height) {
                allocate(true, width, height)
            }

            textureId?.let { texture ->
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
                GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height)
            }
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

    private fun onEvent(event: YCRenderEvent.Pre) {
        noexcept(LOGGER::catching) {
            if (MC.theWorld === null) {
                lastNanos = System.nanoTime()
                glStateScope {
                    allocate(false, -1, -1)
                }
            }
        }
    }

    private fun onEvent(event: SSMotionBlurEvent.Render) {
        if (!options.enabled) return

        noexcept(LOGGER::catching) {
            MC.entityRenderer.setupOverlayRendering()

            glStateScope {
                if (textureId === null || lastWidth != event.screenWidth || lastHeight != event.screenHeight) {
                    allocate(true, event.screenWidth, event.screenHeight)
                }

                textureId?.let { texture ->
                    GL11.glEnable(GL11.GL_TEXTURE_2D)
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
                    GL11.glEnable(GL11.GL_BLEND)
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
                    GL11.glDisable(GL11.GL_DEPTH_TEST)
                    GL11.glDisable(GL11.GL_ALPHA_TEST)
                    GL11.glColor4d(1.0, 1.0, 1.0, getAlpha())

                    val scaledWidth = event.scaledResolution.scaledWidth_double
                    val scaledHeight = event.scaledResolution.scaledHeight_double

                    mcRenderScope(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX) {
                        pos(0.0, scaledHeight, 0.0).tex(0.0, 0.0).endVertex()
                        pos(scaledWidth, scaledHeight, 0.0).tex(widthFactor, 0.0).endVertex()
                        pos(scaledWidth, 0.0, 0.0).tex(widthFactor, heightFactor).endVertex()
                        pos(0.0, 0.0, 0.0).tex(0.0, heightFactor).endVertex()
                    }
                }

                takeScreenShot(event.screenWidth, event.screenHeight)
            }
        }
    }
}
