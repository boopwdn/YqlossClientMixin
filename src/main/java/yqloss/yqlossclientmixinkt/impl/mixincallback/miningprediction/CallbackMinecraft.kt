package yqloss.yqlossclientmixinkt.impl.mixincallback.miningprediction

import net.minecraft.util.BlockPos
import yqloss.yqlossclientmixinkt.YC
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPredictionEvent
import yqloss.yqlossclientmixinkt.util.asVec3I

fun sendClickBlockToControllerMining(blockPos: BlockPos) {
    YC.eventDispatcher(MiningPredictionEvent.Mining(blockPos.asVec3I))
}

fun sendClickBlockToControllerNotMining() {
    YC.eventDispatcher(MiningPredictionEvent.NotMining)
}
