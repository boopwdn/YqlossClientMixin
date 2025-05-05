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
import yqloss.yqlossclientmixinkt.util.accessor.outs.Box
import yqloss.yqlossclientmixinkt.util.accessor.outs.inBox
import yqloss.yqlossclientmixinkt.util.extension.castTo
import yqloss.yqlossclientmixinkt.util.extension.type.asFailure
import yqloss.yqlossclientmixinkt.util.extension.type.asSuccess
import yqloss.yqlossclientmixinkt.util.extension.type.boxedOrNull
import yqloss.yqlossclientmixinkt.util.functional.loop

sealed interface Scope {
    class FrameIdentifier

    @JvmInline
    value class Count(
        val count: Int,
    ) : Scope

    @JvmInline
    value class Frame(
        val identifier: FrameIdentifier = FrameIdentifier(),
    ) : Scope

    companion object {
        val LAST = Count(1)
    }
}

data class LongResult(
    val scope: Scope,
    val result: Result<Any?>,
) : Throwable() {
    inline val next: LongResult
        get() {
            return when (scope) {
                is Scope.Count -> LongResult(Scope.Count(scope.count - 1), result)
                is Scope.Frame -> this
            }
        }

    override fun fillInStackTrace(): Throwable {
        return if (LONG_RETURN_STACKTRACE) {
            super.fillInStackTrace()
        } else {
            this
        }
    }
}

@JvmInline
value class LongResultContext(
    val result: Result<*>,
) {
    @Suppress("NOTHING_TO_INLINE")
    inline fun <T> value() = result.getOrThrow().castTo<T>()

    @Suppress("NOTHING_TO_INLINE")
    inline fun <T> valueOrNull() = result.getOrNull().castTo<T?>()

    @Suppress("NOTHING_TO_INLINE")
    inline fun <T> boxed() = result.getOrThrow().inBox.castTo<Box<T>>()

    @Suppress("NOTHING_TO_INLINE")
    inline fun <T> boxedOrNull() = result.boxedOrNull.castTo<Box<T>?>()
}

inline fun longResult(
    scope: Scope = Scope.LAST,
    getter: () -> Result<*>,
): Nothing = throw LongResult(scope, getter())

inline fun <T> longReturn(
    scope: Scope = Scope.LAST,
    getter: () -> T,
): Nothing = longResult(scope) { getter().asSuccess }

inline fun longThrow(
    scope: Scope = Scope.LAST,
    getter: () -> Throwable,
): Nothing = longResult(scope) { getter().asFailure }

inline fun <R> longScope(
    onResult: LongResultContext.() -> R,
    function: (Scope.Frame) -> R,
): R {
    val frame = Scope.Frame()
    try {
        return function(frame)
    } catch (longResult: LongResult) {
        val scope = longResult.scope
        when {
            scope is Scope.Count && scope.count > 1 -> throw longResult.next
            scope is Scope.Frame && scope.identifier === frame.identifier -> throw longResult.next
        }
        return onResult(LongResultContext(longResult.result))
    }
}

inline fun longScope(function: (Scope.Frame) -> Unit): Result<*>? {
    val frame = Scope.Frame()
    try {
        function(frame)
        return null
    } catch (longResult: LongResult) {
        val scope = longResult.scope
        when {
            scope is Scope.Count && scope.count > 1 -> throw longResult.next
            scope is Scope.Frame && scope.identifier === frame.identifier -> throw longResult.next
        }
        return LongResultContext(longResult.result).result
    }
}

inline fun <R> longRun(function: (Scope.Frame) -> R): R {
    longScope({ return value() }) {
        return function(it)
    }
}

inline fun <R> longLoop(function: (Scope.Frame) -> Unit): R {
    return longRun<R> {
        loop {
            function(it)
        }
    }
}
