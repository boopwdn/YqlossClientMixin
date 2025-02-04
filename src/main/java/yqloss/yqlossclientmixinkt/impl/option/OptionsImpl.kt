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
import yqloss.yqlossclientmixinkt.impl.MainConfig
import yqloss.yqlossclientmixinkt.module.YCModuleInfo
import yqloss.yqlossclientmixinkt.module.YCModuleOptions
import yqloss.yqlossclientmixinkt.util.inBox
import java.lang.reflect.Field
import java.lang.reflect.Method

abstract class OptionsImpl(
    info: YCModuleInfo<*>,
) : SubConfig(info.name, info.configFile),
    YCModuleOptions {
    init {
        super.enabled = false
    }

    override val enabled get() = MainConfig.enabled && super.enabled

    override fun getCustomOption(
        field: Field,
        annotation: CustomOption,
        page: OptionPage,
        mod: Mod,
        migrate: Boolean,
    ): BasicOption? {
        return handleExtensionOption(this, field, annotation, page, mod)
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
        val optionName = "$prefix${field.name}"

        ConfigUtils.findAnnotation(field, internalOptionClass)?.let { annotation ->
            config.optionNames[optionName] =
                internalAddOptionToPageMethod(null, page, annotation, field, instance, null) as BasicOption
        }
    }

    type.declaredMethods.forEach { method ->
        ConfigUtils.findAnnotation(method, Button::class.java)?.let {
            config.optionNames["$prefix${method.name}"] = ConfigUtils.addOptionToPage(page, method, instance)
        }
    }
}

private fun handleExtensionOption(
    config: Config,
    field: Field,
    annotation: CustomOption,
    page: OptionPage,
    mod: Mod,
): BasicOption? {
    val args = annotation.id.split(":")
    return when (args[0]) {
        "extract" -> handleExtractOption(config, field, page, mod, args)
        else -> null
    }
}

// usage: @CustomOption(id = "extract:OPTION_KEY")
// currently cannot handle recursive @CustomOption
private fun handleExtractOption(
    config: Config,
    field: Field,
    page: OptionPage,
    mod: Mod,
    args: List<String>,
) = null.also { addOptions(config, field.get(config), field.type, page, mod, "${args[1]}.") }
