package yqloss.yqlossclientmixinkt.api

import kotlin.random.Random

interface YCTemplate {
    // setting a key multiple times is undefined
    operator fun set(
        key: String,
        value: Any?,
    )

    fun format(): String
}

typealias YCTemplateProvider = (String) -> YCTemplate

fun YCTemplate.setStyles() {
    "r0123456789abcdefklmno".forEach { this["$it"] = "\u00a7$it" }
}

data object RandomGenerator {
    val braille get() = Char(Random.nextInt(256) + 10240)
}

fun YCTemplate.setRandom() {
    this["rng"] = RandomGenerator
}

fun YCTemplate.setDefault() {
    setRandom()
    setStyles()
}
