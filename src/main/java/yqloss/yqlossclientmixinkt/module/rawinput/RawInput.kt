package yqloss.yqlossclientmixinkt.module.rawinput

import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Mouse
import yqloss.yqlossclientmixinkt.event.YCEventRegistration
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.module.buildRegisterEventEntries
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.latelet
import yqloss.yqlossclientmixinkt.util.noexcept
import yqloss.yqlossclientmixinkt.ycLogger

val INFO_RAW_INPUT = moduleInfo<RawInputOptions>("raw_input", "Raw Input")

private val LOGGER = ycLogger(INFO_RAW_INPUT.name)

class RawInput :
    YCModule<RawInputOptions> by INFO_RAW_INPUT,
    YCEventRegistration {
    private var mouseHelper: RawMouseHelper by latelet()
    private var savedMouse: Mouse? = null

    override val eventEntries =
        buildRegisterEventEntries {
            register<YCMinecraftEvent.Load.Post> {
                mouseHelper = RawMouseHelper(MC.mouseHelper)
                MC.mouseHelper = mouseHelper
            }

            register<YCMinecraftEvent.Loop.Pre> {
                if (!options.enabled) return@register

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
}
