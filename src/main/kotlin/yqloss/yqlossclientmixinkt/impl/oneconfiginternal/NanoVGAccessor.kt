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

package yqloss.yqlossclientmixinkt.impl.oneconfiginternal

import cc.polyfrost.oneconfig.renderer.font.Font
import yqloss.yqlossclientmixinkt.util.accessor.provideDelegate
import yqloss.yqlossclientmixinkt.util.accessor.refs.lateVal
import yqloss.yqlossclientmixinkt.util.accessor.value

var nvg: NanoVGAccessor by lateVal()

private var fontLoaded = false

val fontMedium = lateVal<Font>()
val fontSemiBold = lateVal<Font>()

fun NanoVGAccessor.loadFonts(vg: Long) {
    if (!fontLoaded) {
        fontLoaded = true
        fontMedium.value = loadFont(vg, "montserrat/medium.ttf")
        fontSemiBold.value = loadFont(vg, "montserrat/semibold.ttf")
    }
}

class NanoVGImageCache {
    private val cache = mutableMapOf<Any?, MutableMap<Int, Int>>()
    private val imagesToDelete = mutableSetOf<Int>()

    operator fun get(key: Any?) = NanoVGImageCacheEntry(this, cache.getOrPut(key, ::mutableMapOf))

    fun cleanup(
        accessor: NanoVGAccessor,
        vg: Long,
    ) {
        accessor.deleteImages(vg, imagesToDelete)
        imagesToDelete.clear()
    }

    fun clear() {
        cache.values.forEach { entry ->
            entry.values.forEach(imagesToDelete::add)
        }
        cache.clear()
    }

    fun clear(key: Any?) {
        cache[key]?.values?.forEach(imagesToDelete::add)
        cache.remove(key)
    }
}

class NanoVGImageCacheEntry(
    val parent: NanoVGImageCache,
    val cache: MutableMap<Int, Int>,
) {
    fun cleanup(
        accessor: NanoVGAccessor,
        vg: Long,
    ) {
        parent.cleanup(accessor, vg)
    }
}

interface NanoVGAccessor {
    fun loadFont(
        vg: Long,
        name: String,
    ): Font

    fun deleteImages(
        vg: Long,
        images: Set<Int>,
    )

    fun drawRingArc(
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
    )

    fun drawRoundedPlayerAvatar(
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
    )
}
