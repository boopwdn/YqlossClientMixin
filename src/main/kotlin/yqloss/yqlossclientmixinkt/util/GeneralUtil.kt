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

package yqloss.yqlossclientmixinkt.util

inline infix fun <T1, T2> T1.sameNotNull(other: T2) = this !== null && this === other

inline infix fun <R> Boolean.ifTake(supplier: () -> R): R? = if (this) supplier() else null

inline infix fun <R> Boolean.unlessTake(supplier: () -> R): R? = if (this) null else supplier()

inline val Boolean.takeTrue get() = if (this) true else null

inline val Boolean.takeFalse get() = if (this) null else false

inline val Boolean.unitTrue get() = if (this) Unit else null

inline val Boolean.unitFalse get() = if (this) null else Unit
