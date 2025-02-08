package yqloss.yqlossclientmixinkt.module.miningprediction

import kotlin.math.max

enum class OreType(
    val offsetGetter: () -> Int,
    val overrideGetter: () -> Int?,
) {
    OTHER(
        { MiningPrediction.options.generalMiningSpeedOffset },
        { MiningPrediction.options.generalMiningSpeedOverride.takeIf { MiningPrediction.options.enableGeneralMiningSpeedOverride } },
    ),
    GEMSTONE(
        { MiningPrediction.options.gemstoneMiningSpeedOffset },
        { MiningPrediction.options.gemstoneMiningSpeedOverride.takeIf { MiningPrediction.options.enableGemstoneMiningSpeedOverride } },
    ),
    DWARVEN_METAL(
        { MiningPrediction.options.dwarvenMetalMiningSpeedOffset },
        {
            MiningPrediction.options.dwarvenMetalMiningSpeedOverride.takeIf {
                MiningPrediction.options.enableDwarvenMetalMiningSpeedOverride
            }
        },
    ),
    ;

    fun modifyMiningSpeed(miningSpeed: Int) = max(0, overrideGetter() ?: (miningSpeed + offsetGetter()))
}
