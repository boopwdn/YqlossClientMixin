package yqloss.yqlossclientmixinkt.module.miningprediction

import net.minecraft.client.renderer.DestroyBlockProgress
import yqloss.yqlossclientmixinkt.event.YCEvent
import yqloss.yqlossclientmixinkt.util.math.Vec3I

sealed interface MiningPredictionEvent : YCEvent {
    data class Mining(
        val pos: Vec3I,
    ) : MiningPredictionEvent

    data object NotMining : MiningPredictionEvent

    data class RenderBlockDamage(
        val damages: List<DestroyBlockProgress>,
        var mutableDamages: MutableList<DestroyBlockProgress> = damages.toMutableList(),
    ) : MiningPredictionEvent
}
