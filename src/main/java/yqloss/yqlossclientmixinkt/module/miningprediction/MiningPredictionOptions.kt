package yqloss.yqlossclientmixinkt.module.miningprediction

import yqloss.yqlossclientmixinkt.module.option.YCBlockOption
import yqloss.yqlossclientmixinkt.module.option.YCModuleOptions
import yqloss.yqlossclientmixinkt.module.option.YCNotificationOption

interface MiningPredictionOptions : YCModuleOptions {
    val onBreakBlock: YCNotificationOption
    val durationOffset: Int
    val destroyedBlock: YCBlockOption
    val generalMiningSpeedOffset: Int
    val gemstoneMiningSpeedOffset: Int
    val dwarvenMetalMiningSpeedOffset: Int
    val enableGeneralMiningSpeedOverride: Boolean
    val generalMiningSpeedOverride: Int
    val enableGemstoneMiningSpeedOverride: Boolean
    val gemstoneMiningSpeedOverride: Int
    val enableDwarvenMetalMiningSpeedOverride: Boolean
    val dwarvenMetalMiningSpeedOverride: Int
    val forceEnabled: Boolean
}
