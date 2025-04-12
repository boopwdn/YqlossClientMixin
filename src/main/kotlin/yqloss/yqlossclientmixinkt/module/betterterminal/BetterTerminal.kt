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

import net.minecraft.client.gui.inventory.GuiChest
import org.lwjgl.opengl.GL11.glScaled
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.event.YCEventRegistry
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.*
import yqloss.yqlossclientmixinkt.module.betterterminal.terminal.*
import yqloss.yqlossclientmixinkt.module.option.invoke
import yqloss.yqlossclientmixinkt.util.*
import yqloss.yqlossclientmixinkt.util.scope.longrun
import kotlin.random.Random

val INFO_BETTER_TERMINAL = moduleInfo<BetterTerminalOptions>("better_terminal", "Better Terminal")

private val TERMINAL_FACTORIES =
    listOf(
        TerminalOrder,
        TerminalStart,
        TerminalColor,
        TerminalPanes,
        TerminalRubix,
        TerminalAlign,
    )

object BetterTerminal : YCModuleBase<BetterTerminalOptions>(INFO_BETTER_TERMINAL) {
    data class QueueData(
        val slotID: Int,
        val button: Int,
        val wrong: Boolean,
    )

    data class TerminalData(
        val terminal: Terminal,
        val enableQueue: Boolean,
        var windowID: Int,
        val states: ArrayDeque<List<Int>>,
        val queue: ArrayDeque<QueueData>,
        var clickDelay: Int,
    ) {
        val state get() = states[states.size - 1]
    }

    var data: TerminalData? = null

    private val clickQueue = mutableListOf<Pair<Int, Int>>()

    private fun validateData() {
        val data = data ?: return
        if (data.queue.count { !it.wrong } != data.states.size - 1) {
            this.data = null
        }
    }

    fun reloadTerminal() {
        data = null
    }

    fun forceNonQueue() {
        val data = data ?: return
        this.data =
            TerminalData(
                data.terminal,
                false,
                data.windowID,
                ArrayDeque(listOf(data.state)),
                ArrayDeque(),
                -1,
            )
    }

    fun onClick(
        slotID: Int,
        button: Int,
    ) {
        val data = data ?: return
        val button = if (data.terminal.enableRightClick) button else 0
        val result = data.terminal.predict(data.state, slotID, button)
        if (result.clickType !== ClickType.NONE) {
            if (data.enableQueue) {
                val (click, wrong) =
                    when (result.clickType) {
                        ClickType.CORRECT ->
                            true.also {
                                ClickType.CORRECT.notify(logger)
                            } to false

                        ClickType.WRONG ->
                            (!options.preventMisclick).also {
                                (if (it) ClickType.WRONG else ClickType.CANCELED).notify(logger)
                            } to true

                        ClickType.FAIL ->
                            (!options.preventMisclick && !options.preventFail).also {
                                (if (it) ClickType.FAIL else ClickType.CANCELED).notify(logger)
                            } to true

                        else -> false to false
                    }
                if (click) {
                    if (data.queue.isEmpty()) {
                        data.clickDelay = randomDelay
                    }
                    data.queue.addLast(QueueData(slotID, result.button, wrong))
                    if (!wrong) {
                        data.states.addLast(result.state)
                    }
                }
            } else {
                when (result.clickType) {
                    ClickType.WRONG -> {
                        if (options.preventMisclick) {
                            ClickType.CANCELED.notify(logger)
                            return
                        }
                    }

                    ClickType.FAIL -> {
                        if (options.preventMisclick || options.preventFail) {
                            ClickType.CANCELED.notify(logger)
                            return
                        }
                    }

                    else -> {}
                }
                clickSlot(slotID, result.button)
                ClickType.NORMAL.notify(logger)
            }
        }
    }

    private val randomDelay
        get(): Int {
            var from = options.clickDelayFrom
            var until = options.clickDelayUntil
            if (from < 0) from = 0
            if (until <= from) until = from + 1
            return Random.nextInt(from, until)
        }

    private var lastTick = -1L

    private fun updateChest(
        chest: GuiChest,
        data: TerminalData?,
    ) {
        validateData()
        val tick = lastTick != tickCounter
        lastTick = tickCounter
        val windowID = chest.inventorySlots.windowId
        val title =
            YC.api
                .get_GuiChest_lowerChestInventory(chest)
                .name.trimStyle
        val terminal = TERMINAL_FACTORIES.firstNotNullOfOrNull { it.createIfMatch(title) } ?: return
        Screen.setScreen(chest)
        val inventory = YC.api.get_GuiChest_lowerChestInventory(chest)
        val items = (0..<inventory.sizeInventory).map { inventory.getStackInSlot(it) }
        val state = terminal.parse(items)
        data?.takeIf { it.terminal == terminal }?.let {
            if (tick && data.clickDelay >= 0) {
                if (state !== null && state notEqualTo data.states[0]) {
                    if (options.reloadOnMismatch) {
                        this.data = null
                    } else {
                        this.data =
                            TerminalData(
                                terminal,
                                false,
                                windowID,
                                ArrayDeque(listOf(state)),
                                ArrayDeque(),
                                -1,
                            )
                    }
                    return@let
                }
                if (data.clickDelay == 0) {
                    if (state === null) {
                        ++data.clickDelay
                    } else if (data.queue.isNotEmpty()) {
                        val click = data.queue[0]
                        if (click.wrong) {
                            data.queue.removeFirst()
                            data.clickDelay = randomDelay + 1
                        }
                        clickQueue.add(click.slotID to click.button)
                    }
                }
                --data.clickDelay
            }

            state?.let {
                if (!data.enableQueue) {
                    this.data =
                        TerminalData(
                            terminal,
                            false,
                            windowID,
                            ArrayDeque(listOf(state)),
                            ArrayDeque(),
                            -1,
                        )
                } else if (data.queue.isEmpty()) {
                    this.data =
                        TerminalData(
                            terminal,
                            true,
                            windowID,
                            ArrayDeque(listOf(state)),
                            ArrayDeque(),
                            -1,
                        )
                } else {
                    val equalID = windowID == data.windowID
                    val equalState = state equalTo data.states[0]

                    when {
                        !equalID && !equalState -> {
                            data.states.removeFirst()
                            data.queue.removeFirst()
                            data.clickDelay = randomDelay
                            this.data = data
                        }

                        equalID && !equalState -> {
                            this.data =
                                TerminalData(
                                    terminal,
                                    false,
                                    windowID,
                                    ArrayDeque(listOf(state)),
                                    ArrayDeque(),
                                    -1,
                                )
                        }

                        else -> this.data = data
                    }
                }
            } ?: run {
                this.data = data
            }
        } ?: run {
            state?.let {
                this.data =
                    TerminalData(
                        terminal,
                        options.enableQueue && terminal.enableQueue,
                        windowID,
                        ArrayDeque(listOf(state)),
                        ArrayDeque(),
                        -1,
                    )
            } ?: run {
                this.data =
                    TerminalData(
                        TerminalLoading(terminal.lines, terminal.beginLine, terminal.chestLines, terminal.title),
                        options.enableQueue && terminal.enableQueue,
                        windowID,
                        ArrayDeque(listOf(listOf())),
                        ArrayDeque(),
                        -1,
                    )
            }
        }
        validateData()
    }

    private fun clickSlot(
        slotID: Int,
        button: Int,
    ) {
        val chest = MC.currentScreen as? GuiChest ?: return
        if (button == 1) {
            MC.playerController.windowClick(
                chest.inventorySlots.windowId,
                slotID,
                1,
                0,
                MC.thePlayer,
            )
        } else {
            MC.playerController.windowClick(
                chest.inventorySlots.windowId,
                slotID,
                2,
                3,
                MC.thePlayer,
            )
        }
        options.onActualClick(logger) {
            this["slot"] = slotID
            this["button"] = button
        }
    }

    override fun registerEvents(registry: YCEventRegistry) {
        registry.run {
            register<YCRenderEvent.Screen.Proxy> { event ->
                longrun {
                    val data = data
                    Screen.proxiedScreen = null
                    this@BetterTerminal.data = null

                    ensureEnabled()

                    if (!options.forceEnabled) {
                        ensureSkyBlock()
                    }

                    val chest = event.screen as? GuiChest ?: return@register
                    updateChest(chest, data)

                    this@BetterTerminal.data ?: return@register

                    event.mutableScreen = Screen
                }
            }

            register<BetterTerminalEvent.DrawDefaultBackground> { event ->
                longrun {
                    ensureNotCanceled(event)
                    ensureEnabled()
                    event.canceled = event.screen === Screen.proxiedScreen
                }
            }

            register<BetterTerminalEvent.RenderTooltip> { event ->
                longrun {
                    ensureNotCanceled(event)
                    ensureEnabled()
                    event.canceled = event.screen === Screen.proxiedScreen
                }
            }
        }
    }

    object Screen : YCProxyScreen<GuiChest>() {
        override fun drawScreen(
            mouseX: Int,
            mouseY: Int,
            partialTicks: Float,
        ) {
            if (options.showChest) {
                glMatrixScope {
                    glScaled(options.chestScale, options.chestScale, 1.0)
                    proxiedScreen?.drawScreen(mouseX, mouseY, partialTicks)
                }
            }
        }

        override fun handleInput() {
            val data = data
            proxiedScreen = null
            BetterTerminal.data = null
            val chest = MC.currentScreen as? GuiChest ?: return
            updateChest(chest, data)
            clickQueue.forEach {
                clickSlot(it.first, it.second)
            }
            clickQueue.clear()
        }
    }
}
