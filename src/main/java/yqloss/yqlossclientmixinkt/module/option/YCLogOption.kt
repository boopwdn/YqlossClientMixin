package yqloss.yqlossclientmixinkt.module.option

import org.apache.logging.log4j.Level
import org.apache.logging.log4j.Logger
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.api.YCTemplate

interface YCLogOption {
    val enabled: Boolean
    val level: Int
    val text: String
}

inline operator fun YCLogOption.invoke(
    logger: Logger,
    placeholder: YCTemplate.() -> Unit,
) {
    if (enabled) {
        logger.log(
            when (level) {
                0 -> Level.FATAL
                1 -> Level.ERROR
                2 -> Level.WARN
                3 -> Level.INFO
                4 -> Level.DEBUG
                else -> Level.TRACE
            },
            YC.api
                .templateProvider(text)
                .also(placeholder)
                .format(),
        )
    }
}
