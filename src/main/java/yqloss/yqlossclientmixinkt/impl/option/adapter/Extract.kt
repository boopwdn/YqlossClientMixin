package yqloss.yqlossclientmixinkt.impl.option.adapter

import cc.polyfrost.oneconfig.config.annotations.CustomOption

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@CustomOption(id = "extract")
annotation class Extract
