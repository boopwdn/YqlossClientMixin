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

package yqloss.yqlossclientmixinkt.util.property

import yqloss.yqlossclientmixinkt.util.general.Box
import yqloss.yqlossclientmixinkt.util.general.inBox
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LateLet<T> : ReadWriteProperty<Any?, T> {
    private var box: Box<T>? = null

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        return box?.cast() ?: throw IllegalStateException()
    }

    override fun setValue(
        thisRef: Any?,
        property: KProperty<*>,
        value: T,
    ) {
        box?.let { throw IllegalStateException() }
        box = value.inBox
    }
}

fun <T> latelet() = LateLet<T>()
