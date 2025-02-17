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

class WithScopeContext(
    private val resourceList: MutableList<AutoCloseable>,
    private val cleanupList: MutableList<() -> Unit>,
) {
    fun <T : AutoCloseable> use(resource: T): T {
        return resource.also {
            if (resource !in resourceList) {
                resourceList.add(it)
            }
        }
    }

    fun use(cleanup: () -> Unit) {
        if (cleanup !in cleanupList) {
            cleanupList.add(cleanup)
        }
    }

    fun <T : AutoCloseable> T.using() = use(this)

    fun <T> T.using(cleanup: () -> Unit) = this@using.also { use(cleanup) }
}

data class ResourceClosureException(
    val failures: List<Pair<Any?, Exception>>,
) : Exception()

inline fun <R> withscope(function: WithScopeContext.() -> R) {
    val resourceList = mutableListOf<AutoCloseable>()
    val cleanupList = mutableListOf<() -> Unit>()
    val context = WithScopeContext(resourceList, cleanupList)
    try {
        function(context)
    } finally {
        val exceptionList = mutableListOf<Pair<Any?, Exception>>()
        resourceList.forEach { resource ->
            noexcept({ exceptionList.add(resource to it) }) { resource.close() }
        }
        cleanupList.forEach { cleanup ->
            noexcept({ exceptionList.add(cleanup to it) }) { cleanup() }
        }
        if (exceptionList.isNotEmpty()) {
            throw ResourceClosureException(exceptionList)
        }
    }
}
