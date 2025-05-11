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

package yqloss.yqlossclientmixinkt.impl.oneconfiginternal.lwjglmanagerimplloaded

import cc.polyfrost.oneconfig.renderer.font.Font
import org.lwjgl.nanovg.NVGColor
import org.lwjgl.nanovg.NVGPaint
import org.lwjgl.nanovg.NanoVG.*
import org.lwjgl.nanovg.NanoVGGL3.NVG_IMAGE_NODELETE
import org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle
import yqloss.yqlossclientmixinkt.impl.oneconfiginternal.NanoVGAccessor
import yqloss.yqlossclientmixinkt.impl.oneconfiginternal.NanoVGImageCacheEntry
import yqloss.yqlossclientmixinkt.impl.oneconfiginternal.nvg
import yqloss.yqlossclientmixinkt.util.extension.float
import yqloss.yqlossclientmixinkt.util.math.convertARGBToDoubleArray
import yqloss.yqlossclientmixinkt.util.scope.usingScope
import java.nio.ByteBuffer
import java.util.*
import kotlin.math.*

private fun NVGColor.fill(argb: Int): NVGColor {
    val (rv, gv, bv, av) = convertARGBToDoubleArray(argb)
    r(rv.float)
    g(gv.float)
    b(bv.float)
    a(av.float)
    return this
}

object NanoVGAccessorImpl : NanoVGAccessor {
    init {
        nvg = this
    }

    override fun loadFont(
        vg: Long,
        name: String,
    ): Font {
        val byteArray = javaClass.getResource("/assets/yqlossclientmixin/font/$name")!!.readBytes()
        val byteBuffer =
            ByteBuffer.allocateDirect(byteArray.size).apply {
                put(byteArray)
                flip()
            }
        val fontFace = UUID.randomUUID().toString()
        nvgCreateFontMem(vg, fontFace, byteBuffer, 0)
        return object : Font(fontFace, "") {
            val byteBuffer = byteBuffer
        }
    }

    override fun addFallbackFont(
        vg: Long,
        fontFace: String,
        fallbackFontFace: String,
    ) {
        nvgAddFallbackFont(vg, fontFace, fallbackFontFace)
    }

    override fun deleteImages(
        vg: Long,
        images: Set<Int>,
    ) {
        images.forEach {
            nvgDeleteImage(vg, it)
        }
    }

    override fun drawRingArc(
        vg: Long,
        x: Double,
        y: Double,
        outerRadius: Double,
        innerRadius: Double,
        fromRadian: Double,
        toRadian: Double,
        arcPaddingFrom: Double,
        arcPaddingTo: Double,
        color: Int,
    ) {
        usingScope {
            val nvgColor = NVGColor.calloc().using.fill(color)

            val lpo = asin(arcPaddingFrom / outerRadius)
            val lpi = asin(arcPaddingFrom / innerRadius)
            val rpo = asin(arcPaddingTo / outerRadius)
            val rpi = asin(arcPaddingTo / innerRadius)

            nvgBeginPath(vg)
            nvgArc(
                vg,
                x.float,
                y.float,
                outerRadius.float,
                (fromRadian + lpo).float,
                (toRadian - rpo).float,
                NVG_CW,
            )
            nvgArc(
                vg,
                x.float,
                y.float,
                innerRadius.float,
                (toRadian - rpi).float,
                (fromRadian + lpi).float,
                NVG_CCW,
            )
            nvgClosePath(vg)
            nvgFillColor(vg, nvgColor)
            nvgFill(vg)
        }
    }

    private fun drawRoundedImage(
        vg: Long,
        image: Int,
        imageXRel: Double,
        imageYRel: Double,
        imageWRel: Double,
        imageHRel: Double,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    ) {
        usingScope {
            val nvgPaint = NVGPaint.calloc().using
            val imageW = width / imageWRel
            val imageH = height / imageHRel
            val imageX = x - imageW * imageXRel
            val imageY = y - imageH * imageYRel
            nvgImagePattern(
                vg,
                imageX.float,
                imageY.float,
                imageW.float,
                imageH.float,
                0.0F,
                image,
                alpha.float,
                nvgPaint,
            )
            nvgBeginPath(vg)
            nvgRoundedRect(
                vg,
                x.float,
                y.float,
                width.float,
                height.float,
                radius.float,
            )
            nvgFillPaint(vg, nvgPaint)
            nvgFill(vg)
        }
    }

    override fun drawRoundedPlayerAvatar(
        vg: Long,
        imageCache: NanoVGImageCacheEntry,
        texture: Int,
        hat: Boolean,
        scaleHat: Boolean,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        alpha: Double,
        radius: Double,
    ) {
        imageCache.cleanup(this, vg)

        val nvgImage =
            imageCache.cache.getOrPut(texture) {
                nvglCreateImageFromHandle(vg, texture, 64, 64, NVG_IMAGE_NEAREST or NVG_IMAGE_NODELETE)
                    .also { if (it == -1) return@drawRoundedPlayerAvatar }
            }

        drawRoundedImage(
            vg,
            nvgImage,
            0.125,
            0.125,
            0.125,
            0.125,
            x,
            y,
            width,
            height,
            alpha,
            radius,
        )

        if (hat) {
            val offset = if (scaleHat) 1.0 / 144.0 else 0.0

            drawRoundedImage(
                vg,
                nvgImage,
                0.625 + offset,
                0.125 + offset,
                0.125 - 2.0 * offset,
                0.125 - 2.0 * offset,
                x,
                y,
                width,
                height,
                alpha,
                radius,
            )
        }
    }

    private fun calculateAngles(
        x: Double,
        y: Double,
        angle1: Double,
        angle2: Double,
        unswapped: Boolean = false,
    ): Pair<Double, Double> {
        var a1 = angle1 + PI / 2.0
        var a2 = angle2 - PI / 2.0
        val pi2 = PI * 2.0
        val ccw = ((a2 - a1) % pi2 + pi2) % pi2 >= PI
        if (ccw) {
            run {
                val tmp = a1
                a1 = a2
                a2 = tmp
            }
            a1 += PI
            a2 += PI
        }
        a2 = a1 + ((a2 - a1) % pi2 + pi2) % pi2
        return if (ccw && unswapped) a2 to a1 else a1 to a2
    }

    private fun isCCW(
        angle1: Double,
        angle2: Double,
    ): Boolean {
        val a1 = angle1 + PI / 2.0
        val a2 = angle2 - PI / 2.0
        val pi2 = PI * 2.0
        return ((a2 - a1) % pi2 + pi2) % pi2 >= PI
    }

    override fun drawTrailLine(
        vg: Long,
        x1: Double,
        y1: Double,
        x2: Double,
        y2: Double,
        angle1: Double,
        angle2: Double,
        radiusInner: Double,
        radiusOuter: Double,
        color: Int,
    ) {
        usingScope {
            val angle = atan2(y2 - y1, x2 - x1)
            val paint2 =
                nvgLinearGradient(
                    vg,
                    (x1 + radiusInner * sin(angle)).float,
                    (y1 - radiusInner * cos(angle)).float,
                    (x1 + radiusOuter * sin(angle)).float,
                    (y1 - radiusOuter * cos(angle)).float,
                    NVGColor.calloc().using.fill(color),
                    NVGColor.calloc().using.fill(0),
                    NVGPaint.calloc().using,
                )
            val paint1 =
                nvgLinearGradient(
                    vg,
                    (x1 - radiusInner * sin(angle)).float,
                    (y1 + radiusInner * cos(angle)).float,
                    (x1 - radiusOuter * sin(angle)).float,
                    (y1 + radiusOuter * cos(angle)).float,
                    NVGColor.calloc().using.fill(color),
                    NVGColor.calloc().using.fill(0),
                    NVGPaint.calloc().using,
                )
            val (a1u, a2u) = calculateAngles(x1, y1, angle1, angle, true)
            val (b1u, b2u) = calculateAngles(x2, y2, angle + PI, angle2, true)
            val ap1x = x1 + radiusOuter * cos(a2u)
            val ap1y = y1 + radiusOuter * sin(a2u)
            val ar = radiusOuter / cos((a1u - a2u) / 2.0)
            val aa = (a1u + a2u) / 2.0
            val ap2x = x1 - ar * cos(aa)
            val ap2y = y1 - ar * sin(aa)
            val bp1x = x2 + radiusOuter * cos(b1u)
            val bp1y = y2 + radiusOuter * sin(b1u)
            val br = radiusOuter / cos((b1u - b2u) / 2.0)
            val ba = (b1u + b2u) / 2.0
            val bp2x = x2 - br * cos(ba)
            val bp2y = y2 - br * sin(ba)
            val a1x: Double
            val a1y: Double
            val a2x: Double
            val a2y: Double
            val b1x: Double
            val b1y: Double
            val b2x: Double
            val b2y: Double
            if (isCCW(angle1, angle)) {
                a1x = ap1x
                a1y = ap1y
                a2x = ap2x
                a2y = ap2y
            } else {
                a1x = ap2x
                a1y = ap2y
                a2x = ap1x
                a2y = ap1y
            }
            if (isCCW(angle + PI, angle2)) {
                b1x = bp1x
                b1y = bp1y
                b2x = bp2x
                b2y = bp2y
            } else {
                b1x = bp2x
                b1y = bp2y
                b2x = bp1x
                b2y = bp1y
            }
            if (doubleArrayOf(a1x, a1y, b1x, b1y).all { it.isFinite() }) {
                nvgBeginPath(vg)
                nvgMoveTo(vg, x1.float, y1.float)
                nvgLineTo(vg, a1x.float, a1y.float)
                nvgLineTo(vg, b1x.float, b1y.float)
                nvgLineTo(vg, x2.float, y2.float)
                nvgClosePath(vg)
                nvgFillPaint(vg, paint1)
                nvgFill(vg)
            }
            if (doubleArrayOf(a2x, a2y, b2x, b2y).all { it.isFinite() }) {
                nvgBeginPath(vg)
                nvgMoveTo(vg, x1.float, y1.float)
                nvgLineTo(vg, a2x.float, a2y.float)
                nvgLineTo(vg, b2x.float, b2y.float)
                nvgLineTo(vg, x2.float, y2.float)
                nvgClosePath(vg)
                nvgFillPaint(vg, paint2)
                nvgFill(vg)
            }
        }
    }

    override fun drawTrailCorner(
        vg: Long,
        x: Double,
        y: Double,
        angle1: Double,
        angle2: Double,
        radiusInner: Double,
        radiusOuter: Double,
        color: Int,
    ) {
        usingScope {
            val paint =
                nvgRadialGradient(
                    vg,
                    x.float,
                    y.float,
                    radiusInner.float,
                    radiusOuter.float,
                    NVGColor.calloc().using.fill(color),
                    NVGColor.calloc().using.fill(0),
                    NVGPaint.calloc().using,
                )
            nvgBeginPath(vg)
            val (a1, a2) = calculateAngles(x, y, angle1, angle2)
            nvgArc(vg, x.float, y.float, radiusOuter.float, a1.float, a2.float, NVG_CW)
            nvgLineTo(vg, x.float, y.float)
            nvgClosePath(vg)
            nvgFillPaint(vg, paint)
            nvgFill(vg)
        }
    }

    override fun drawTrailCircle(
        vg: Long,
        x: Double,
        y: Double,
        radiusInner: Double,
        radiusOuter: Double,
        color: Int,
    ) {
        usingScope {
            val paint =
                nvgRadialGradient(
                    vg,
                    x.float,
                    y.float,
                    radiusInner.float,
                    radiusOuter.float,
                    NVGColor.calloc().using.fill(color),
                    NVGColor.calloc().using.fill(0),
                    NVGPaint.calloc().using,
                )
            nvgBeginPath(vg)
            nvgCircle(vg, x.float, y.float, radiusOuter.float)
            nvgFillPaint(vg, paint)
            nvgFill(vg)
        }
    }
}
