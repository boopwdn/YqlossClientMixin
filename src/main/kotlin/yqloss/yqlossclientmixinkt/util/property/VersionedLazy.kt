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

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class VersionedLazy<T, TV : Any>(
    private val function: () -> T,
    private val versionGetter: () -> TV,
    private var initialized: Boolean,
    private var value: T?,
) : ReadOnlyProperty<Any?, T> {
    private var version: TV? = null

    fun reset() {
        initialized = false
        version = null
        value = null
    }

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        val currentVersion = versionGetter()
        return if (initialized && version == currentVersion) {
            value!!
        } else {
            val newValue = function()
            initialized = true
            version = currentVersion
            value = newValue
            newValue
        }
    }
}

fun <T, TV : Any> versionedLazy(
    versionGetter: () -> TV,
    function: () -> T,
) = VersionedLazy(function, versionGetter, false, null)

fun <T, TV : Any> versionedLazy(
    initial: T,
    versionGetter: () -> TV,
    function: () -> T,
) = VersionedLazy(function, versionGetter, true, initial)
