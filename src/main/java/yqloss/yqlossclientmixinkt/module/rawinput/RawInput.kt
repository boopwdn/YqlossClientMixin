package yqloss.yqlossclientmixinkt.module.rawinput

import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Mouse
import yqloss.yqlossclientmixinkt.YqlossClient
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.event.buildEventEntries
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.event.registerEventEntries
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.noexcept
import yqloss.yqlossclientmixinkt.ycLogger

private val LOGGER = ycLogger("Raw Input")

class RawInput(
    private val yc: YqlossClient,
) : YCModule<RawInputOptions>,
    YCEventRegistration {
    override val id = "raw_input"
    override val name = "Raw Input"
    override val options by lazy { yc.getOptionsImpl(RawInputOptions::class) }

    override val eventEntries =
        buildEventEntries {
            register<YCMinecraftEvent.Load.Post> { onEvent(it) }
            register<YCMinecraftEvent.Loop.Pre> { onEvent(it) }
        }

    init {
        registerEventEntries(yc.eventDispatcher)
    }

    private val mouseHelper = RawMouseHelper(this)
    private var savedMouse: Mouse? = null

    private fun onEvent(event: YCMinecraftEvent.Load.Post) {
        MC.mouseHelper = mouseHelper
    }

    private fun onEvent(event: YCMinecraftEvent.Loop.Pre) {
        if (!options.enabled) return

        noexcept(LOGGER::catching) {
            if (savedMouse === null) {
                LOGGER.info("trying to find a mouse")
                for (controller in ControllerEnvironment.getDefaultEnvironment().controllers) {
                    run {
                        noexcept(LOGGER::catching) {
                            if (controller.type === Controller.Type.MOUSE) {
                                val mouse = controller as Mouse
                                mouse.poll()
                                if (mouse.x.pollData !in -0.1..0.1 || mouse.y.pollData !in -0.1..0.1) {
                                    savedMouse = mouse
                                    LOGGER.info("found mouse $mouse")
                                    return@run null
                                }
                            }
                        }
                    } ?: break
                }
            }

            if (savedMouse === null) {
                LOGGER.info("failed to find a mouse")
            }

            savedMouse?.let { mouse ->
                mouse.poll()
                if (MC.currentScreen === null) {
                    mouseHelper.x += mouse.x.pollData
                    mouseHelper.y += mouse.y.pollData
                }
            }
        }
    }
}
