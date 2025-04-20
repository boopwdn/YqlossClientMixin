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
import yqloss.yqlossclientmixinkt.util.once
import yqloss.yqlossclientmixinkt.util.onceUnary
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Trigger<T, TV>(
    private val function: (TV) -> T,
    private val versionGetter: () -> TV,
    private var initialized: Boolean,
    private var value: T?,
) : ReadOnlyProperty<Any?, T> {
    private var version: Box<TV>? = null

    fun reset() {
        initialized = false
        version = null
        value = null
    }

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>,
    ): T {
        val currentVersion = versionGetter().inBox
        return if (initialized && version == currentVersion) {
            value!!
        } else {
            val newValue = function(currentVersion.value)
            initialized = true
            version = currentVersion
            value = newValue
            newValue
        }
    }
}

fun <T, TV> trigger(
    versionGetter: () -> TV,
    function: (TV) -> T,
) = Trigger(function, versionGetter, false, null)

fun <T, TV> trigger(
    initial: T,
    versionGetter: () -> TV,
    function: (TV) -> T,
) = Trigger(function, versionGetter, true, initial)

fun <T, TV> triggerOnce(
    versionGetter: () -> TV,
    function: () -> T,
) = Trigger({ once(function) }, versionGetter, false, null)

fun <TA, T, TV> triggerOnceUnary(
    versionGetter: () -> TV,
    function: (TA) -> T,
) = Trigger({ onceUnary(function) }, versionGetter, false, null)
