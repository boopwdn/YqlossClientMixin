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

package yqloss.yqlossclientmixinkt.api

import kotlin.random.Random

interface YCTemplate {
    // setting a key multiple times is undefined
    operator fun set(
        key: String,
        value: Any?,
    )

    fun format(): String
}

typealias YCTemplateProvider = (String) -> YCTemplate

fun YCTemplate.setStyles() {
    "r0123456789abcdefklmno".forEach { this["$it"] = "\u00a7$it" }
}

data object RandomGenerator {
    val braille get() = Char(Random.nextInt(256) + 10240)
}

fun YCTemplate.setRandom() {
    this["rng"] = RandomGenerator
}

fun YCTemplate.setDefault() {
    setRandom()
    setStyles()
}
