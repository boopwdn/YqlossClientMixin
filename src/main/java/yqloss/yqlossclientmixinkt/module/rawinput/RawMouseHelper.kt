package yqloss.yqlossclientmixinkt.module.rawinput

import net.minecraft.util.MouseHelper
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.util.asInt

class RawMouseHelper(
    private val parent: MouseHelper,
) : MouseHelper() {
    var x = 0.0
    var y = 0.0

    override fun grabMouseCursor() {
        parent.grabMouseCursor()
    }

    override fun ungrabMouseCursor() {
        parent.ungrabMouseCursor()
    }

    override fun mouseXYChange() {
        if (YC.rawInput.options.enabled) {
            deltaX = x.asInt
            deltaY = -y.asInt
            x -= deltaX
            y += deltaY
        } else {
            x = 0.0
            y = 0.0
            parent.mouseXYChange()
            deltaX = parent.deltaX
            deltaY = parent.deltaY
        }
    }
}
