package yqloss.yqlossclientmixinkt.module.rawinput

import yqloss.yqlossclientmixinkt.module.YCModuleOptions

interface RawInputOptions : YCModuleOptions {
    val nativeRawInput: Boolean
}
