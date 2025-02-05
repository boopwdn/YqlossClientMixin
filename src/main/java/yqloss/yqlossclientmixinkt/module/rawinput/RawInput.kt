package yqloss.yqlossclientmixinkt.module.rawinput

import net.java.games.input.Controller
import net.java.games.input.ControllerEnvironment
import net.java.games.input.Mouse
import yqloss.yqlossclientmixinkt.event.RegistrationEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCMinecraftEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.moduleInfo
import yqloss.yqlossclientmixinkt.util.MC
import yqloss.yqlossclientmixinkt.util.property.latelet
import yqloss.yqlossclientmixinkt.util.scope.noexcept

val INFO_RAW_INPUT = moduleInfo<RawInputOptions>("raw_input", "Raw Input")

object RawInput : YCModuleBase<RawInputOptions>(INFO_RAW_INPUT) {
    private var mouseHelper: RawMouseHelper by latelet()
    private var savedMouse: Mouse? = null

    override fun RegistrationEventDispatcher.registerEvents() {
        register<YCMinecraftEvent.Load.Post> {
            mouseHelper = RawMouseHelper(MC.mouseHelper)
            MC.mouseHelper = mouseHelper
        }

        register<YCMinecraftEvent.Loop.Pre> {
            if (!options.enabled) return@register

            noexcept(logger::catching) {
                if (savedMouse === null) {
                    logger.info("trying to find a mouse")
                    for (controller in ControllerEnvironment.getDefaultEnvironment().controllers) {
                        run {
                            noexcept(logger::catching) {
                                if (controller.type === Controller.Type.MOUSE) {
                                    val mouse = controller as Mouse
                                    mouse.poll()
                                    if (mouse.x.pollData !in -0.1..0.1 || mouse.y.pollData !in -0.1..0.1) {
                                        savedMouse = mouse
                                        logger.info("found mouse $mouse")
                                        return@run null
                                    }
                                }
                            }
                        } ?: break
                    }
                }

                if (savedMouse === null) {
                    logger.info("failed to find a mouse")
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
