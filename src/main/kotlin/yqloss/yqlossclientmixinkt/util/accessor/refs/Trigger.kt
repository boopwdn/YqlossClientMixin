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

package yqloss.yqlossclientmixinkt.util.accessor.refs

import yqloss.yqlossclientmixinkt.util.accessor.Out
import yqloss.yqlossclientmixinkt.util.accessor.outs.Box
import yqloss.yqlossclientmixinkt.util.accessor.outs.inBox
import yqloss.yqlossclientmixinkt.util.accessor.outs.value
import yqloss.yqlossclientmixinkt.util.functional.eval
import yqloss.yqlossclientmixinkt.util.functional.once
import yqloss.yqlossclientmixinkt.util.functional.onceUnary

data class Trigger<out T, TV>(
    private val function: (TV) -> T,
    private val versionGetter: () -> TV,
    private var holder: Box<T>?,
) : Out<T> {
    private var version: Box<TV>? = null

    override fun get(): T {
        val currentVersion = versionGetter().inBox
        val holder = holder
        return if (holder?.eval { version == currentVersion } == true) {
            holder.value
        } else {
            val newValue = function(currentVersion.value)
            version = currentVersion
            this.holder = newValue.inBox
            newValue
        }
    }
}

fun <T, TV> trigger(
    versionGetter: () -> TV,
    function: (TV) -> T,
) = Trigger(function, versionGetter, null)

fun <T, TV> trigger(
    initial: T,
    versionGetter: () -> TV,
    function: (TV) -> T,
) = Trigger(function, versionGetter, initial.inBox)

fun <T, TV> triggerOnce(
    versionGetter: () -> TV,
    function: () -> T,
) = Trigger({ once(function) }, versionGetter, null)

fun <TA, T, TV> triggerOnceUnary(
    versionGetter: () -> TV,
    function: (TA) -> T,
) = Trigger({ onceUnary<() -> TA, T> { function(it()) } }, versionGetter, null)
