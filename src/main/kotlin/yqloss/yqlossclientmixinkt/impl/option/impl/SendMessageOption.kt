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

package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.annotations.Number
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.module.option.SendMessagePool
import yqloss.yqlossclientmixinkt.module.option.YCSendMessageOption
import yqloss.yqlossclientmixinkt.util.printChat

class SendMessageOption : YCSendMessageOption {
    @Switch(
        name = "Enable Send Message Notification",
        size = 1,
    )
    var enabledOption = false

    @Text(
        name = "Message",
        size = 1,
    )
    var textOption = ""

    @Checkbox(
        name = "Enable Interval",
        size = 1,
    )
    var enableIntervalOption = false

    @Number(
        name = "Interval Since Last Message (in ticks)",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var intervalOption = 0

    @Text(
        name = "Interval Pool",
        size = 1,
    )
    var intervalPoolOption = "default"

    @Number(
        name = "Max Pool Size",
        min = 0.0F,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var maxPoolSizeOption = 2147483647

    override val enabled by ::enabledOption
    override val text by ::textOption
    override val enableInterval by ::enableIntervalOption
    override val interval by ::intervalOption
    override val intervalPool by ::intervalPoolOption
    override val maxPoolSize by ::maxPoolSizeOption

    @Transient
    @Extract
    val clearPool =
        @Button(
            name = "Clear this Pool",
            text = "Clear",
            size = 1,
        )
        {
            SendMessagePool.clear(intervalPool)
            printChat("cleared pool $intervalPool")
        }

    @Transient
    @Extract
    val viewPoolSize =
        @Button(
            name = "Print Pool Sizes",
            text = "Print",
            size = 1,
        )
        {
            printChat()
            printChat("Pool Sizes:")
            SendMessagePool.poolMap.forEach { (pool, list) ->
                if (list.isNotEmpty()) {
                    printChat("    $pool: ${list.size}")
                }
            }
            printChat()
        }
}
