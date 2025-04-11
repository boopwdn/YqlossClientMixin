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

package yqloss.yqlossclientmixinkt.util.scope

import yqloss.yqlossclientmixinkt.util.LONG_RETURN_STACKTRACE
import yqloss.yqlossclientmixinkt.util.general.Box
import yqloss.yqlossclientmixinkt.util.general.inBox
import yqloss.yqlossclientmixinkt.util.loop
import yqloss.yqlossclientmixinkt.util.property.threadlocal

var frameCounter by threadlocal { 0 }

data class LongReturn(
    val frame: Int,
    val value: Any?,
) : Throwable() {
    inline val next: LongReturn
        get() = if (frame < 0) LongReturn(frame + 1, value) else this

    override fun fillInStackTrace(): Throwable {
        return if (LONG_RETURN_STACKTRACE) {
            super.fillInStackTrace()
        } else {
            this
        }
    }
}

class LongReturnContext1(
    private val value: Any?,
) {
    infix fun scope(frame: Int) {
        throw LongReturn(frame, value)
    }

    infix fun times(count: Int) {
        throw LongReturn(1 - count, value)
    }
}

class LongReturnContext2(
    private val box: Box<*>,
) {
    fun <T> v() = box.cast<T>()
}

class LongReturnContext3(
    val function: (Int) -> Unit,
) {
    inline infix fun onreturn(returnValue: LongReturnContext2.() -> Unit) {
        frameCounter += 1
        val currentFrame = frameCounter
        try {
            function(currentFrame)
        } catch (longReturn: LongReturn) {
            if (longReturn.frame != 0 && longReturn.frame != currentFrame) {
                throw longReturn.next
            }
            returnValue(LongReturnContext2(longReturn.value.inBox))
        } finally {
            frameCounter -= 1
        }
    }
}

inline fun <T> longret(getter: () -> T) = LongReturnContext1(getter())

inline fun <T> longreturn(
    scope: Int = 0,
    getter: () -> T,
) {
    val value = getter()
    throw LongReturn(scope, value)
}

fun longscope(function: (Int) -> Unit) = LongReturnContext3(function)

inline fun <R> longrun(function: (Int) -> R): R {
    frameCounter += 1
    val currentFrame = frameCounter
    try {
        return function(currentFrame)
    } catch (longReturn: LongReturn) {
        if (longReturn.frame != 0 && longReturn.frame != currentFrame) {
            throw longReturn.next
        }
        return longReturn.value.inBox.cast()
    } finally {
        frameCounter -= 1
    }
}

inline val Int.longreturnTimes get() = 1 - this

inline fun <R> longloop(function: (Int) -> Unit): R {
    return longrun<R> {
        loop {
            function(it)
        }
    }
}
