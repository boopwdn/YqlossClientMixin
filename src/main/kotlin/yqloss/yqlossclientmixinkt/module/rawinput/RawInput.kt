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
 *
 *
 * Adapted some code from https://github.com/xCuri0/RawInputMod
 *
 * MIT License
 *
 * Copyright (c) 2020 Curi0
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package yqloss.yqlossclientmixinkt.module.rawinput

import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Mouse
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.general.intervalAction
import yqloss.yqlossclientmixinkt.util.property.latelet
import yqloss.yqlossclientmixinkt.util.scope.longrun
import yqloss.yqlossclientmixinkt.util.scope.noexcept

val INFO_RAW_INPUT = moduleInfo<RawInputOptions>("raw_input", "Raw Input")

object RawInput : YCModuleBase<RawInputOptions>(INFO_RAW_INPUT) {
    private var mouseHelper: RawMouseHelper by latelet()
    private var savedMouse: Mouse? = null

    private val findMouse =
        intervalAction(1000000000L) {
            logger.info("trying to find a mouse")

            noexcept(logger::catching) {
                for (controller in ControllerEnvironment.getDefaultEnvironment().controllers) {
                    noexcept(logger::catching) {
                        if (controller.type === Controller.Type.MOUSE) {
                            val mouse = controller as Mouse
                            mouse.poll()
                            if (mouse.x.pollData !in -0.1..0.1 || mouse.y.pollData !in -0.1..0.1) {
                                savedMouse = mouse
                                logger.info("found mouse $mouse")
                                return@intervalAction
                            }
                        }
                    }
                }
            }

            logger.info("failed to find a mouse")
        }

    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<YCMinecraftEvent.Load.Post> {
                mouseHelper = RawMouseHelper(MC.mouseHelper)
                MC.mouseHelper = mouseHelper
            }

            register<YCMinecraftEvent.Loop.Pre> {
                longrun {
                    ensureEnabled()

                    savedMouse ?: findMouse()

                    savedMouse?.let { mouse ->
                        mouse.poll()
                        MC.currentScreen ?: run {
                            mouseHelper.x += mouse.x.pollData
                            mouseHelper.y += mouse.y.pollData
                        }
                    }
                }
            }
        }
    }
}
