package yqloss.yqlossclientmixinkt.module.rawinput

import net.minecraft.util.MouseHelper
import yqloss.yqlossclientmixinkt.util.math.asInt

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
        if (RawInput.options.enabled) {
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
