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

package yqloss.yqlossclientmixinkt.module.betterterminal

import org.apache.logging.log4j.Logger
import yqloss.yqlossclientmixinkt.module.option.YCNotificationOption
import yqloss.yqlossclientmixinkt.module.option.invoke

enum class ClickType(
    val notificationGetter: () -> YCNotificationOption?,
) {
    NONE({ null }),
    CORRECT({ BetterTerminal.options.onCorrectClick }),
    CANCELED({ BetterTerminal.options.onCanceledClick }),
    WRONG({ BetterTerminal.options.onWrongClick }),
    FAIL({ BetterTerminal.options.onFailClick }),
    NORMAL({ BetterTerminal.options.onNonQueuedClick }),
    ;

    fun notify(logger: Logger) {
        notificationGetter()?.invoke(logger) {}
    }
}
