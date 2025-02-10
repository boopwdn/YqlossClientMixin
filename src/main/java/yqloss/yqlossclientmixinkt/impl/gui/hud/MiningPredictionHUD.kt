package yqloss.yqlossclientmixinkt.impl.gui.hud

import cc.polyfrost.oneconfig.renderer.font.Fonts
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.format
import yqloss.yqlossclientmixinkt.event.RegistryEventDispatcher
import yqloss.yqlossclientmixinkt.event.register
import yqloss.yqlossclientmixinkt.impl.gui.YCModuleHUDBase
import yqloss.yqlossclientmixinkt.impl.nanovgui.Transformation
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.impl.nanovgui.widget.TextWidget
import yqloss.yqlossclientmixinkt.impl.nanovgui.widget.progressBarWidget
import yqloss.yqlossclientmixinkt.impl.option.module.MiningPredictionOptionsImpl
import yqloss.yqlossclientmixinkt.module.ensure
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPrediction
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPredictionEvent
import yqloss.yqlossclientmixinkt.util.math.Fraction
import yqloss.yqlossclientmixinkt.util.math.Vec2D
import yqloss.yqlossclientmixinkt.util.math.asDouble
import yqloss.yqlossclientmixinkt.util.partialTicks
import kotlin.math.min

object MiningPredictionHUD :
    YCModuleHUDBase<MiningPredictionOptionsImpl, MiningPrediction>(MiningPrediction, { options.hud }) {
    override val width get() = options.width.asDouble

    override val height get() = (options.textSize + options.textProgressGap + options.progressHeight).asDouble

    override val fadeOut = 3000000000L

    override val example get() = super.example || options.forceExample

    override fun ensureShow() {
        ensureHUDEnabled()

        if (!example) {
            ensure { module.isAvailable && module.breakingBlock !== null }
        }
    }

    override fun draw(
        widgets: MutableList<Widget<*>>,
        box: Vec2D,
        tr: Transformation,
    ) {
        val progress =
            if (example) {
                0.5
            } else {
                min(1.0, module.breakingProgress.asDouble + partialTicks * module.getProgressPerTick().asDouble)
            }

        val textLeft =
            if (example) {
                "Bedrock"
            } else {
                YC.api.format(options.textLeft, module::setPlaceholders)
            }

        val textRight =
            if (example) {
                "5 / 10"
            } else {
                YC.api.format(options.textRight, module::setPlaceholders)
            }

        val foreground =
            if (example) {
                options.progressForeground.rgb
            } else if (module.breakingProgress >= Fraction.ONE) {
                options.progressForegroundOnBreak.rgb
            } else {
                options.progressForeground.rgb
            }

        options.background.addTo(widgets, tr, box)

        widgets.add(
            progressBarWidget(
                progress,
                tr pos Vec2D(0.0, box.y - options.progressHeight.asDouble),
                tr pos box,
                foreground,
                options.progressBackground.rgb,
            ),
        )

        widgets.add(
            TextWidget(
                textLeft,
                tr pos Vec2D(0.0, 0.0),
                options.textColorLeft.rgb,
                tr size options.textSize.asDouble,
                Fonts.SEMIBOLD,
                Vec2D(0.0, 0.0),
            ),
        )

        widgets.add(
            TextWidget(
                textRight,
                tr pos Vec2D(box.x, 0.0),
                options.textColorRight.rgb,
                tr size options.textSize.asDouble,
                Fonts.SEMIBOLD,
                Vec2D(1.0, 0.0),
            ),
        )
    }

    override fun registerEvents(registry: RegistryEventDispatcher) {
        super.registerEvents(registry)
        registry.run {
            register<MiningPredictionEvent.BreakBlock> {
                val box = size
                val tr = transformation
                animation.update(true, box, tr, fadeOut)
                redraw(box, tr)
            }
        }
    }
}
