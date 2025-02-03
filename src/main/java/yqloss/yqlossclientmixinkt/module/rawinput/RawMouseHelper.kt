package yqloss.yqlossclientmixinkt.module.rawinput

import net.minecraft.util.MouseHelper
import yqloss.yqlossclientmixinkt.util.asInt

class RawMouseHelper(
    private val rawInput: RawInput,
) : MouseHelper() {
    var x = 0.0
    var y = 0.0

    override fun mouseXYChange() {
        if (rawInput.options.enabled) {
            deltaX = x.asInt
            deltaY = -y.asInt
            x -= deltaX
            y += deltaY
        } else {
            x = 0.0
            y = 0.0
            super.mouseXYChange()
        }
    }
}
