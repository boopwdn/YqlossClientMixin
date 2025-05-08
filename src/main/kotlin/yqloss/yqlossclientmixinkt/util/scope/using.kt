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

@file:Suppress("NOTHING_TO_INLINE")

package yqloss.yqlossclientmixinkt.util.scope

import yqloss.yqlossclientmixinkt.util.extension.type.takeTrue
import java.lang.AutoCloseable
import kotlin.Any
import kotlin.Exception
import kotlin.Pair
import kotlin.Suppress
import kotlin.Unit
import kotlin.also
import kotlin.collections.plusAssign
import kotlin.to

@JvmInline
value class UsingScopeContext(
    val resourceList: ArrayDeque<AutoCloseable>,
) {
    inline fun <T : AutoCloseable> using(resource: T): T {
        return resource.also {
            resourceList += it
        }
    }

    inline fun using(crossinline cleanup: () -> Unit) {
        resourceList += AutoCloseable { cleanup() }
    }

    inline val <T : AutoCloseable> T.using get() = using(this)

    inline fun <T> T.using(crossinline cleanup: (T) -> Unit) = also { this@UsingScopeContext.using { cleanup(this) } }

    inline fun <T : AutoCloseable> usingPre(resource: T): T {
        return resource.also {
            resourceList.addFirst(resource)
        }
    }

    inline fun usingPre(crossinline cleanup: () -> Unit) {
        resourceList.addFirst(AutoCloseable { cleanup() })
    }

    inline val <T : AutoCloseable> T.usingPre get() = usingPre(this)

    inline fun <T> T.usingPre(crossinline cleanup: (T) -> Unit) = also { this@UsingScopeContext.usingPre { cleanup(this) } }
}

data class ResourceClosureException(
    val failures: List<Pair<Any?, Exception>>,
) : Exception()

inline fun <R> usingScope(function: UsingScopeContext.() -> R): R {
    val resourceList = ArrayDeque<AutoCloseable>()
    try {
        return UsingScopeContext(resourceList).function()
    } finally {
        val exceptionList = mutableListOf<Pair<Any?, Exception>>()
        resourceList.asReversed().forEach { resource ->
            noExcept({ exceptionList.add(resource to it) }) { resource.close() }
        }
        exceptionList.isEmpty().takeTrue ?: throw ResourceClosureException(exceptionList)
    }
}
