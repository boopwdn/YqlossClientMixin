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

package yqloss.yqlossclientmixinkt.util

import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.ISound
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.VertexFormat
import net.minecraft.entity.Entity
import net.minecraft.item.Item
import net.minecraft.util.BlockPos
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ResourceLocation
import yqloss.yqlossclientmixinkt.module.option.YCColor
import yqloss.yqlossclientmixinkt.util.math.*
import yqloss.yqlossclientmixinkt.ycLogger

private val logger = ycLogger("Minecraft Util")

val MC: Minecraft by lazy { Minecraft.getMinecraft() }

var tickCounter = 0L

var partialTicks = 0.0

var mousePosition = Vec2D(0.0, 0.0)

var guiScale = 1.0

inline val BlockPos.asVec3I get() = Vec3I(x, y, z)

inline val Vec3I.asBlockPos get() = BlockPos(x, y, z)

inline val Entity.lastPos get() = Vec3D(prevPosX, prevPosY, prevPosZ)

inline val Entity.currPos get() = Vec3D(posX, posY, posZ)

inline val Entity.renderPos get() = lerp(lastPos, currPos, partialTicks)

val REGEX_STYLE = Regex("\\u00a7.")

inline val String.removeStyle get() = REGEX_STYLE.replace(this, "")

inline val String.trimStyle get() = removeStyle.trim()

inline val Block.item: Item get() = Item.getItemFromBlock(this)

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

fun WorldRenderer.color(color: YCColor): WorldRenderer = color(color.r.float, color.g.float, color.b.float, color.a.float)

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

fun updateWorldRender() {
    MC.renderGlobal.loadRenderers()
}

fun updateWorldRenderArea(area: Area3I) {
    MC.renderGlobal.markBlockRangeForRenderUpdate(
        area.first.x,
        area.first.y,
        area.first.z,
        area.second.x - 1,
        area.second.y - 1,
        area.second.z - 1,
    )
}

fun updateWorldRenderBlock(pos: Vec3I) {
    MC.renderGlobal.markBlockRangeForRenderUpdate(pos.x, pos.y, pos.z, pos.x, pos.y, pos.z)
}
