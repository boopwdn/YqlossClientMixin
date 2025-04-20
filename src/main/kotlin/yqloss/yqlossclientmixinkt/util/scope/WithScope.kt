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
value class WithScopeContext(
    val resourceList: MutableList<AutoCloseable>,
) {
    inline fun <T : AutoCloseable> use(resource: T): T {
        return resource.also {
            resourceList += it
        }
    }

    inline fun use(crossinline cleanup: () -> Unit) {
        resourceList += AutoCloseable { cleanup() }
    }

    inline fun <T : AutoCloseable> T.using() = use(this)

    inline fun <T> T.using(crossinline cleanup: (T) -> Unit) = also { use { cleanup(this) } }
}

data class ResourceClosureException(
    val failures: List<Pair<Any?, Exception>>,
) : Exception()

inline fun <R> withscope(function: WithScopeContext.() -> R) {
    val resourceList = mutableListOf<AutoCloseable>()
    val context = WithScopeContext(resourceList)
    try {
        function(context)
    } finally {
        val exceptionList = mutableListOf<Pair<Any?, Exception>>()
        resourceList.reversed().forEach { resource ->
            noexcept({ exceptionList.add(resource to it) }) { resource.close() }
        }
        if (exceptionList.isNotEmpty()) {
            throw ResourceClosureException(exceptionList)
        }
    }
}
