package yqloss.yqlossclientmixinkt.impl.mixincallback.miningprediction

import net.minecraft.client.renderer.DestroyBlockProgress
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPredictionEvent

private var savedDamageMap: Map<Int, DestroyBlockProgress> = mapOf()

fun drawBlockDamageTexturePre(damageMap: MutableMap<Int, DestroyBlockProgress>) {
    val damages = damageMap.toMap()
    MiningPredictionEvent
        .RenderBlockDamage(damages)
        .also(YC.eventDispatcher)
        .let { event ->
            savedDamageMap = damageMap.toMap()
            damageMap.clear()
            event.mutableDamages.forEach { (i, v) -> damageMap[i] = v }
        }
}

fun drawBlockDamageTexturePost(damageMap: MutableMap<Int, DestroyBlockProgress>) {
    damageMap.clear()
    damageMap.putAll(savedDamageMap)
    savedDamageMap = mapOf()
}
