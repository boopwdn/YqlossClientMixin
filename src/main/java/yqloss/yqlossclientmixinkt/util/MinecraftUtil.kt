package yqloss.yqlossclientmixinkt.util

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.entity.Entity
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ResourceLocation
import yqloss.yqlossclientmixinkt.impl.mixincallback.yqlossclient.mcPartialTicks
import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.Vec3D
import yqloss.yqlossclientmixinkt.util.math.Vec3I
import yqloss.yqlossclientmixinkt.util.math.asFloat
import yqloss.yqlossclientmixinkt.ycLogger

private val logger = ycLogger("Minecraft Util")

val MC: Minecraft by lazy { Minecraft.getMinecraft() }

val partialTicks by ::mcPartialTicks

inline val BlockPos.asVec3I get() = Vec3I(x, y, z)

inline val Vec3I.asBlockPos get() = BlockPos(x, y, z)

inline val Entity.lastPos get() = Vec3D(prevPosX, prevPosY, prevPosZ)

inline val Entity.currPos get() = Vec3D(posX, posY, posZ)

inline val Entity.renderPos get() = lastPos + (currPos - lastPos) * partialTicks

val REGEX_STYLE = Regex("\\u00a7.")

inline val String.removeStyle get() = REGEX_STYLE.replace(this, "")

inline val String.trimStyle get() = removeStyle.trim()

inline fun mcRenderScope(
    mode: Int,
    vertexFormat: VertexFormat,
    function: WorldRenderer.() -> Unit,
) {
    val tessellator = Tessellator.getInstance()
    val worldRenderer = tessellator.worldRenderer
    worldRenderer.begin(mode, vertexFormat)
    try {
        function(worldRenderer)
    } finally {
        tessellator.draw()
    }
}

fun WorldRenderer.pos(vec: Vec3D): WorldRenderer = pos(vec.x, vec.y, vec.z)

fun WorldRenderer.tex(vec: Vec2D): WorldRenderer = tex(vec.x, vec.y)

fun WorldRenderer.color(color: YCColor): WorldRenderer = color(color.r.asFloat, color.g.asFloat, color.b.asFloat, color.a.asFloat)

fun printChat(message: String = "") {
    logger.info("[PRINT CHAT] $message")
    MC.theWorld?.let {
        MC.ingameGUI.chatGUI.printChatMessage(ChatComponentText(message))
    }
}

class CustomSound(
    private val argSoundLocation: ResourceLocation,
    private val argVolume: Double = 1.0,
    private val argPitch: Double = 1.0,
    private val argCanRepeat: Boolean = false,
    private val argRepeatDelay: Int = 0,
    private val argXPos: Double = 0.0,
    private val argYPos: Double = 0.0,
    private val argZPos: Double = 0.0,
    private val argAttenuationType: ISound.AttenuationType = ISound.AttenuationType.NONE,
) : ISound {
    override fun getSoundLocation() = argSoundLocation

    override fun canRepeat() = argCanRepeat

    override fun getRepeatDelay() = argRepeatDelay

    override fun getVolume() = argVolume.toFloat()

    override fun getPitch() = argPitch.toFloat()

    override fun getXPosF() = argXPos.toFloat()

    override fun getYPosF() = argYPos.toFloat()

    override fun getZPosF() = argZPos.toFloat()

    override fun getAttenuationType() = argAttenuationType
}
