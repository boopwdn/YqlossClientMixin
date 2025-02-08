package yqloss.yqlossclientmixinkt.impl.option.impl

import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.data.InfoType
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerLegit
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerRequireHypixelModAPI
import yqloss.yqlossclientmixinkt.impl.option.notification.NotificationOption
import yqloss.yqlossclientmixinkt.module.miningprediction.INFO_MINING_PREDICTION
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPrediction
import yqloss.yqlossclientmixinkt.module.miningprediction.MiningPredictionOptions

class MiningPredictionOptionsImpl :
    OptionsImpl(INFO_MINING_PREDICTION),
    MiningPredictionOptions {
    @Extract
    var disclaimer = DisclaimerAtOwnRisk()

    @Extract
    var legit = DisclaimerLegit()

    @Extract
    var requireHypixelModAPI = DisclaimerRequireHypixelModAPI()

    @Header(
        text = "Mining Prediction",
        size = 2,
    )
    var headerModule = false

    @Info(
        text =
            "This applies to all pickaxes, prismarine shards, skulls on these islands:",
        type = InfoType.WARNING,
        size = 2,
    )
    var warningUsage = false

    @Info(
        text = "Gold Mine, Deep Caverns, Dwarven Mines, Crystal Hollows, Mineshaft, The End, Crimson Isle.",
        type = InfoType.WARNING,
        size = 2,
    )
    var warningIslands = false

    @Info(
        text = "If Mining Speed Override is not enabled, Mining Speed stat is required to show in tab for this to work.",
        type = InfoType.WARNING,
        size = 2,
    )
    var warningWidget = false

    @Number(
        name = "Breaking Time Offset",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var offsetOption = 0

    @Number(
        name = "General Mining Speed Offset",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var generalMiningSpeedOffsetOption = 0

    @Number(
        name = "Gemstone Mining Speed Offset",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var gemstoneMiningSpeedOffsetOption = 0

    @Number(
        name = "Dwarven Metal Mining Speed Offset",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var dwarvenMetalMiningSpeedOffsetOption = 0

    @Switch(
        name = "General Mining Speed Override",
        size = 1,
    )
    var enableGeneralMiningSpeedOverrideOption = false

    @Number(
        name = "General Mining Speed",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var generalMiningSpeedOverrideOption = 0

    @Switch(
        name = "Gemstone Mining Speed Override",
        size = 1,
    )
    var enableGemstoneMiningSpeedOverrideOption = false

    @Number(
        name = "Gemstone Mining Speed",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var gemstoneMiningSpeedOverrideOption = 0

    @Switch(
        name = "Dwarven Metal Mining Speed Override",
        size = 1,
    )
    var enableDwarvenMetalMiningSpeedOverrideOption = false

    @Number(
        name = "Dwarven Metal Mining Speed",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var dwarvenMetalMiningSpeedOverrideOption = 0

    @Header(
        text = "Notification on Breaking Block",
        size = 2,
    )
    var headerBreakBlockNotification = false

    @Extract
    var onBreakBlockOption = NotificationOption()

    @Header(
        text = "Debug",
        size = 2,
    )
    var headerDebug = false

    @Button(
        name = "Print Debug Info",
        text = "Print",
        size = 1,
    )
    fun printDebugInfo() {
        MiningPrediction.printDebugInfo()
    }

    override val onBreakBlock by ::onBreakBlockOption
    override val durationOffset by ::offsetOption
    override val generalMiningSpeedOffset by ::generalMiningSpeedOffsetOption
    override val gemstoneMiningSpeedOffset by ::gemstoneMiningSpeedOffsetOption
    override val dwarvenMetalMiningSpeedOffset by ::dwarvenMetalMiningSpeedOffsetOption
    override val enableGeneralMiningSpeedOverride by ::enableGeneralMiningSpeedOverrideOption
    override val generalMiningSpeedOverride by ::generalMiningSpeedOverrideOption
    override val enableGemstoneMiningSpeedOverride by ::enableGemstoneMiningSpeedOverrideOption
    override val gemstoneMiningSpeedOverride by ::gemstoneMiningSpeedOverrideOption
    override val enableDwarvenMetalMiningSpeedOverride by ::enableDwarvenMetalMiningSpeedOverrideOption
    override val dwarvenMetalMiningSpeedOverride by ::dwarvenMetalMiningSpeedOverrideOption
}
