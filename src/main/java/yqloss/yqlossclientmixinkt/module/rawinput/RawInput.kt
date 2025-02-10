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
