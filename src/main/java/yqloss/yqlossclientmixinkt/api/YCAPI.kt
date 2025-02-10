package yqloss.yqlossclientmixinkt.api

interface YCAPI {
    val hypixelLocation: YCHypixelLocation?
    val templateProvider: YCTemplateProvider
}

inline fun YCAPI.format(
    template: String,
    placeholder: YCTemplate.() -> Unit,
): String {
    return templateProvider(template).also(placeholder).format()
}
