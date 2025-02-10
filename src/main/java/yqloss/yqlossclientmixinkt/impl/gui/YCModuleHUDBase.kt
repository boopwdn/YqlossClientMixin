package yqloss.yqlossclientmixinkt.impl.gui

import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.minecraft.YCRenderEvent
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.nanovgui.Transformation
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.impl.option.YCHUD
import yqloss.yqlossclientmixinkt.module.YCModule
import yqloss.yqlossclientmixinkt.module.YCModuleBase
import yqloss.yqlossclientmixinkt.module.ensureEnabled
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.general.inBox
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.scope.longrun

abstract class YCModuleHUDBase<TO : YCModuleOptions, TM : YCModule<in TO>>(
    protected val module: TM,
    protected val hudGetter: YCModuleHUDBase<TO, TM>.() -> YCHUD,
) : YCModuleBase<TO>(module.inBox.cast()) {
    protected abstract val width: Double
    protected abstract val height: Double
    protected open val fadeOut = 0L

    protected abstract fun ensureShow()

    protected abstract fun draw(
        widgets: MutableList<Widget<*>>,
        box: Vec2D,
        tr: Transformation,
    )

    protected open fun doesShow(): Boolean {
        var show = false
        longrun {
            ensureShow()
            show = true
        }
        return show
    }

    protected open val size get() = Vec2D(width, height)
    protected open val hud get() = hudGetter()
    protected open val example get() = hud.isExample
    protected open val transformation
        get() = Transformation().scaleMC().translate(hud.renderPos).scale(hud.renderScale)

    protected open var widgets = mutableListOf<Widget<*>>()
    protected open val animation = WindowAnimation()

    protected open fun redraw(
        box: Vec2D,
        tr: Transformation,
    ) {
        widgets.clear()
        draw(widgets, box, tr)
    }

    override fun registerEvents(registry: RegistryEventDispatcher) {
        registry.run {
            register<YCHUD.GetWidthEvent> { it.width = width * hud.renderScale }

            register<YCHUD.GetHeightEvent> { it.height = height * hud.renderScale }

            register<YCRenderEvent.GUI.HUD.Post> { event ->
                val show = doesShow()
                val box = size
                val tr = transformation
                if (example) {
                    if (show) {
                        redraw(box, tr)
                    }
                    event.widgets.addAll(widgets)
                } else {
                    if (animation.update(show, box, tr, fadeOut)) {
                        redraw(box, tr)
                    }
                    animation.mapWidgets(widgets, event.widgets)
                }
            }
        }
    }

    protected open fun ensureHUDEnabled(frame: Int = 0) = ensureEnabled(frame) { hud.isEnabled }
}
