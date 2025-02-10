package yqloss.yqlossclientmixinkt.impl.gui

import yqloss.yqlossclientmixinkt.impl.nanovgui.Transformation
import yqloss.yqlossclientmixinkt.impl.nanovgui.Widget
import yqloss.yqlossclientmixinkt.util.math.ExponentialSmooth
import yqloss.yqlossclientmixinkt.util.math.Vec2D

class WindowAnimation {
    private var lastShow = false
    private var smoothAlpha = ExponentialSmooth(0.0)
    private var smoothScale = ExponentialSmooth(0.0)
    private var box = Vec2D(0.0, 0.0)
    private var tr = Transformation()
    private var lastShowFadeOut = false
    private var lastSwitch: Long? = null

    private fun doUpdate(show: Boolean): Boolean {
        if (show) {
            if (!lastShow) {
                smoothAlpha.set(0.1)
                smoothScale.set(0.9)
            }
            if (smoothAlpha.approach(1.1, 0.5) > 0.9999) {
                smoothAlpha.set(1.0)
            }
            if (smoothScale.approach(1.1, 0.5) > 0.9999) {
                smoothScale.set(1.0)
            }
        } else {
            if (lastShow) {
                smoothAlpha.set(1.0)
                smoothScale.set(1.0)
            }
            smoothAlpha.approach(0.0, 0.5)
            smoothScale.approach(0.9, 0.5)
        }
        lastShow = show
        return show
    }

    fun update(
        show: Boolean,
        box: Vec2D,
        tr: Transformation,
        fadeOut: Long = 0,
    ): Boolean {
        val time = System.nanoTime()
        var toUpdate = true
        this.box = box
        this.tr = tr
        if (!show && fadeOut > 0) {
            if (lastShowFadeOut) {
                lastSwitch = time
                toUpdate = false
            } else if (lastSwitch?.let { time - it < fadeOut } == true) {
                toUpdate = false
            }
        }
        lastShowFadeOut = show
        return toUpdate and doUpdate(show || !toUpdate)
    }

    fun mapWidgets(
        widgets: List<Widget<*>>,
        eventWidgets: MutableList<Widget<*>>,
    ) {
        if (smoothAlpha.value > 0.1) {
            eventWidgets.addAll(
                widgets.map {
                    it
                        .alphaScale(smoothAlpha.value)
                        .scale(smoothScale.value, tr pos (box / 2.0))
                },
            )
        }
    }

    fun setShow(show: Boolean) {
        if (show) {
            smoothAlpha.set(1.0)
            smoothScale.set(1.0)
        } else {
            smoothAlpha.set(0.0)
            smoothScale.set(0.9)
        }
        lastSwitch = null
        lastShow = show
        lastShowFadeOut = show
    }
}
