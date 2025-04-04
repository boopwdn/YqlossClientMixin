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

package yqloss.yqlossclientmixinkt.module.betterterminal.terminal

import net.minecraft.item.ItemStack
import yqloss.yqlossclientmixinkt.module.betterterminal.ClickType
import yqloss.yqlossclientmixinkt.module.betterterminal.Terminal

data class TerminalLoading(
    override val lines: Int,
    override val beginLine: Int,
    override val chestLines: Int,
    override val title: String,
) : Terminal {
    override val enableQueue = false
    override val enableRightClick = false

    override fun parse(items: List<ItemStack?>) = listOf<Int>()

    override fun draw(state: List<Int>) = listOf<Terminal.SlotRenderInfo>()

    override fun predict(
        state: List<Int>,
        slotID: Int,
        button: Int,
    ) = Terminal.Prediction(state, ClickType.NONE, button)
}
