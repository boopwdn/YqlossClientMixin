package yqloss.yqlossclientmixinkt.impl.api

import org.stringtemplate.v4.DateRenderer
import org.stringtemplate.v4.NumberRenderer
import org.stringtemplate.v4.ST
import org.stringtemplate.v4.STGroup
import org.stringtemplate.v4.StringRenderer
import yqloss.yqlossclientmixinkt.api.YCTemplate
import yqloss.yqlossclientmixinkt.util.scope.noexcept
import java.util.Date
import java.util.WeakHashMap

private val cache = WeakHashMap<String, ST>()

private val group =
    STGroup('<', '>').apply {
        registerRenderer(Number::class.java, NumberRenderer())
        registerRenderer(String::class.java, StringRenderer())
        registerRenderer(Date::class.java, DateRenderer())
    }

class YCTemplateImpl(
    private val template: String,
) : YCTemplate {
    private val st =
        noexcept {
            ST(
                cache.getOrPut(template) {
                    ST(group, template)
                },
            )
        }

    override fun set(
        key: String,
        value: Any?,
    ) {
        st?.add(key, value)
    }

    override fun format() = noexcept { st?.render() } ?: template
}
