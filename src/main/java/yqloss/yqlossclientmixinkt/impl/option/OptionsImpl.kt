package yqloss.yqlossclientmixinkt.impl.option

import cc.polyfrost.oneconfig.config.Config
import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.CustomOption
import cc.polyfrost.oneconfig.config.core.ConfigUtils
import cc.polyfrost.oneconfig.config.data.Mod
import cc.polyfrost.oneconfig.config.elements.BasicOption
import cc.polyfrost.oneconfig.config.elements.OptionPage
import cc.polyfrost.oneconfig.config.elements.SubConfig
import cc.polyfrost.oneconfig.config.migration.Migrator
import yqloss.yqlossclientmixinkt.module.YCModuleInfo
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.general.inBox
import java.lang.reflect.Field
import java.lang.reflect.Method

abstract class OptionsImpl(
    info: YCModuleInfo<*>,
) : SubConfig(info.name, info.configFile),
    YCModuleOptions {
    init {
        super.enabled = false
    }

    override val enabled get() = YqlossClientConfig.enabled && super.enabled

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean,
    ): BasicOption? {
        field.isAccessible = true
        return handleExtensionOption(this, { field[this] }, annotation, page, mod, "${field.name}.")
    }
}

private val internalOptionClass: Class<out Annotation> by lazy {
    Class.forName("cc.polyfrost.oneconfig.internal.config.annotations.Option").inBox.cast()
}

private val internalAddOptionToPageMethod: Method by lazy {
    ConfigUtils::class.java
        .getDeclaredMethod(
            "addOptionToPage",
            OptionPage::class.java,
            internalOptionClass,
            Field::class.java,
            Object::class.java,
            Migrator::class.java,
        ).apply { isAccessible = true }
}

private fun addOptions(
    config: Config,
    instance: Any,
    type: Class<*>,
    page: OptionPage,
    mod: Mod,
    prefix: String,
) {
    type.superclass
        .takeIf { it !== Object::class.java }
        ?.let { addOptions(config, instance, it, page, mod, prefix) }

    type.declaredFields.forEach { field ->
        field.isAccessible = true
        val optionName = "$prefix${field.name}"

        ConfigUtils.findAnnotation(field, internalOptionClass)?.also { annotation ->
            config.optionNames[optionName] =
                internalAddOptionToPageMethod(null, page, annotation, field, instance, null) as BasicOption
        } ?: ConfigUtils.findAnnotation(field, CustomOption::class.java)?.also { annotation ->
            handleExtensionOption(
                config,
                { field[instance] },
                annotation,
                page,
                mod,
                "$prefix${field.name}.",
            )?.let { basicOption ->
                config.optionNames[optionName] = basicOption
            }
        }
    }

    type.declaredMethods.forEach { method ->
        method.isAccessible = true
        ConfigUtils.findAnnotation(method, Button::class.java)?.let {
            config.optionNames["$prefix${method.name}"] = ConfigUtils.addOptionToPage(page, method, instance)
        }
    }
}

private fun handleExtensionOption(
    config: Config,
    fieldGetter: () -> Any,
    annotation: CustomOption,
    page: OptionPage,
    mod: Mod,
    prefix: String,
): BasicOption? {
    val args = annotation.id.split(":")
    return when (args[0]) {
        "extract" -> handleExtractOption(config, fieldGetter, page, mod, prefix)
        else -> null
    }
}

// usage: @Extract
// recursive extraction is supported
private fun handleExtractOption(
    config: Config,
    fieldGetter: () -> Any,
    page: OptionPage,
    mod: Mod,
    prefix: String,
): BasicOption? {
    val value = fieldGetter()
    addOptions(config, value, value::class.java, page, mod, prefix)
    return null
}
