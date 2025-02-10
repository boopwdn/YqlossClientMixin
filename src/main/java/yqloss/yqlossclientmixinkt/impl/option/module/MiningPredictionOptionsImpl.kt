package yqloss.yqlossclientmixinkt.impl.option.module

import cc.polyfrost.oneconfig.config.annotations.Button
import cc.polyfrost.oneconfig.config.annotations.Color
import cc.polyfrost.oneconfig.config.annotations.HUD
import cc.polyfrost.oneconfig.config.annotations.Header
import cc.polyfrost.oneconfig.config.annotations.Info
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.annotations.Text
import cc.polyfrost.oneconfig.config.data.InfoType
import yqloss.yqlossclientmixinkt.impl.option.OptionsImpl
import yqloss.yqlossclientmixinkt.impl.option.YCHUD
import yqloss.yqlossclientmixinkt.impl.option.adapter.Extract
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerAtOwnRisk
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerLegit
import yqloss.yqlossclientmixinkt.impl.option.disclaimer.DisclaimerRequireHypixelModAPI
import yqloss.yqlossclientmixinkt.impl.option.gui.GUIBackground
import yqloss.yqlossclientmixinkt.impl.option.impl.BlockOption
import yqloss.yqlossclientmixinkt.impl.option.impl.NotificationOption
import yqloss.yqlossclientmixinkt.impl.util.Colors
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
        name = "Mining Speed Offset When Mining Gemstone",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var gemstoneMiningSpeedOffsetOption = 0

    @Number(
        name = "Mining Speed Offset When Mining Dwarven Metal",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var dwarvenMetalMiningSpeedOffsetOption = 0

    @Number(
        name = "Mining Speed Offset When Mining Other Blocks",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var generalMiningSpeedOffsetOption = 0

    @Info(
        text = "Override means the FINAL Mining Speed used for calculation, not the specialized part.",
        type = InfoType.WARNING,
        size = 2,
    )
    var warningOverride = false

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

    @Switch(
        name = "Other Blocks Mining Speed Override",
        size = 1,
    )
    var enableGeneralMiningSpeedOverrideOption = false

    @Number(
        name = "Other Blocks Mining Speed",
        min = -Float.MAX_VALUE,
        max = Float.MAX_VALUE,
        step = 1,
        size = 1,
    )
    var generalMiningSpeedOverrideOption = 0

    @Header(
        text = "Replacement Block For Destroyed Blocks",
        size = 2,
    )
    var headerReplacement = false

    @Extract
    var destroyedBlockOption = BlockOption()

    @Header(
        text = "Notification on Breaking Block",
        size = 2,
    )
    var headerBreakBlockNotification = false

    @Extract
    var onBreakBlockOption = NotificationOption()

    @HUD(
        name = "Progress HUD",
    )
    var hud = YCHUD()

    @Extract
    var background = GUIBackground()

    @Number(
        name = "Width",
        min = 1.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var width = 80.0F

    @Number(
        name = "Progress Bar Height",
        min = 1.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var progressHeight = 4.0F

    @Color(
        name = "Progress Bar Color When Digging",
        size = 1,
    )
    var progressForeground = Colors.GREEN

    @Color(
        name = "Progress Bar Color When Block Destroyed",
        size = 1,
    )
    var progressForegroundOnBreak = Colors.YELLOW

    @Color(
        name = "Progress Bar Background Color",
        size = 1,
    )
    var progressBackground = Colors.LIGHT

    @Number(
        name = "Font Size",
        min = 1.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var textSize = 8.0F

    @Color(
        name = "Left Text Color",
        size = 1,
    )
    var textColorLeft = Colors.LIGHT

    @Color(
        name = "Right Text Color",
        size = 1,
    )
    var textColorRight = Colors.LIGHT

    @Text(
        name = "Left Text",
        size = 1,
    )
    var textLeft = "<ore.displayName>"

    @Text(
        name = "Right Text",
        size = 1,
    )
    var textRight = "<normalizedProgress> / <ticks>"

    @Number(
        name = "Gap Between Text And Progress Bar",
        min = 1.0F,
        max = Float.MAX_VALUE,
        size = 1,
    )
    var textProgressGap = 4.0F

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

    @Switch(
        name = "Force Enabled",
        size = 1,
    )
    var forceEnabledOption = false

    @Switch(
        name = "Force Example",
        size = 1,
    )
    var forceExample = false

    override val onBreakBlock by ::onBreakBlockOption
    override val durationOffset by ::offsetOption
    override val destroyedBlock by ::destroyedBlockOption
    override val generalMiningSpeedOffset by ::generalMiningSpeedOffsetOption
    override val gemstoneMiningSpeedOffset by ::gemstoneMiningSpeedOffsetOption
    override val dwarvenMetalMiningSpeedOffset by ::dwarvenMetalMiningSpeedOffsetOption
    override val enableGeneralMiningSpeedOverride by ::enableGeneralMiningSpeedOverrideOption
    override val generalMiningSpeedOverride by ::generalMiningSpeedOverrideOption
    override val enableGemstoneMiningSpeedOverride by ::enableGemstoneMiningSpeedOverrideOption
    override val gemstoneMiningSpeedOverride by ::gemstoneMiningSpeedOverrideOption
    override val enableDwarvenMetalMiningSpeedOverride by ::enableDwarvenMetalMiningSpeedOverrideOption
    override val dwarvenMetalMiningSpeedOverride by ::dwarvenMetalMiningSpeedOverrideOption
    override val forceEnabled by ::forceEnabledOption
}
