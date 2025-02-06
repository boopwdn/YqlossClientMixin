package yqloss.yqlossclientmixinkt.module.corpsefinder

enum class CorpseType(
    val id: String,
    val armor: String,
    val option: CorpseOption,
) {
    LAPIS("lapis", "LAPIS_ARMOR_", CorpseFinder.options.lapis),
    UMBER("umber", "ARMOR_OF_YOG_", CorpseFinder.options.umber),
    TUNGSTEN("tungsten", "MINERAL_", CorpseFinder.options.tungsten),
    VANGUARD("vanguard", "VANGUARD_", CorpseFinder.options.vanguard),
    ;

    companion object {
        fun getByArmor(armor: String) = entries.firstOrNull { armor.startsWith(it.armor) }
    }
}
