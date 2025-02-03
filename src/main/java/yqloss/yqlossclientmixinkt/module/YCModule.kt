package yqloss.yqlossclientmixinkt.module

interface YCModule<TOptions : YCModuleOptions> {
    val id: String
    val name: String
    val options: TOptions
}
