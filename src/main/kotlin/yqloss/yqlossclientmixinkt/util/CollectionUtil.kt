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

package yqloss.yqlossclientmixinkt.util

infix fun <T> Collection<T>.prepend(element: T): List<T> {
    return buildList(this.size + 1) {
        add(element)
        addAll(this@prepend)
    }
}

infix fun <T> T.prependTo(collection: Collection<T>): List<T> {
    return buildList(collection.size + 1) {
        add(this@prependTo)
        addAll(collection)
    }
}

infix fun <T> List<T>.equalTo(list: List<T>): Boolean {
    return this === list || (size == list.size && indices.all { this[it] == list[it] })
}

infix fun <T> List<T>.notEqualTo(list: List<T>) = !(this equalTo list)
