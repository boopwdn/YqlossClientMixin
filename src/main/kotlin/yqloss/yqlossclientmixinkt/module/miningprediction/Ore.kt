/*
 * Copyright (C) 2025 Yqloss
 *
 * This file is part of Yqloss Client (Mixin).
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 (GPLv2)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Yqloss Client (Mixin). If not, see <https://www.gnu.org/licenses/old-licenses/gpl-2.0.html>.
 */

package yqloss.yqlossclientmixinkt.module.miningprediction

import net.minecraft.block.Block
import net.minecraft.init.Blocks

enum class Ore(
    val type: OreType,
    val displayName: String,
    val block: Block,
    val strength: Int,
    val instaMine: Int,
    val blockMeta: Int = 0,
) {
    STONE(OreType.OTHER, "Stone", Blocks.stone, 15, 450),
    HARD_STONE(OreType.OTHER, "Hard Stone", Blocks.monster_egg, 50, 1500, 0),
    COBBLESTONE(OreType.OTHER, "Cobblestone", Blocks.cobblestone, 20, 600),
    END_STONE(OreType.OTHER, "End Stone", Blocks.end_stone, 30, 1801),
    OBSIDIAN(OreType.OTHER, "Obsidian", Blocks.obsidian, 500, 30001),
    NETHERRACK(OreType.OTHER, "Netherrack", Blocks.netherrack, 4, 241),
    ICE(OreType.OTHER, "Ice", Blocks.ice, 5, 301),
    COAL_ORE(OreType.OTHER, "Coal", Blocks.coal_ore, 30, 1801),
    IRON_ORE(OreType.OTHER, "Iron", Blocks.iron_ore, 30, 1801),
    GOLD_ORE(OreType.OTHER, "Gold", Blocks.gold_ore, 30, 1801),
    LAPIS_LAZULI_ORE(OreType.OTHER, "Lapis", Blocks.lapis_ore, 30, 1801),
    REDSTONE_ORE(OreType.OTHER, "Redstone", Blocks.redstone_ore, 30, 1801),
    EMERALD_ORE(OreType.OTHER, "Emerald", Blocks.emerald_ore, 30, 1801),
    DIAMOND_ORE(OreType.OTHER, "Diamond", Blocks.diamond_ore, 30, 1801),
    NETHER_QUARTZ_ORE(OreType.OTHER, "Quartz", Blocks.quartz_ore, 30, 1801),
    PURE_COAL(OreType.OTHER, "Pure Coal", Blocks.coal_block, 600, 36001),
    PURE_IRON(OreType.OTHER, "Pure Iron", Blocks.iron_block, 600, 36001),
    PURE_GOLD(OreType.OTHER, "Pure Gold", Blocks.gold_block, 600, 36001),
    PURE_LAPIS(OreType.OTHER, "Pure Lapis", Blocks.lapis_block, 600, 36001),
    PURE_REDSTONE(OreType.OTHER, "Pure Redstone", Blocks.redstone_block, 600, 36001),
    PURE_EMERALD(OreType.OTHER, "Pure Emerald", Blocks.emerald_block, 600, 36001),
    PURE_DIAMOND(OreType.OTHER, "Pure Diamond", Blocks.diamond_block, 600, 36001),
    MITHRIL_GRAY_1(OreType.DWARVEN_METAL, "Mithril", Blocks.wool, 500, 30001, 7),
    MITHRIL_GRAY_2(OreType.DWARVEN_METAL, "Mithril", Blocks.stained_hardened_clay, 500, 30001, 9),
    MITHRIL_PRISMARINE_1(OreType.DWARVEN_METAL, "Mithril", Blocks.prismarine, 800, 48001, 0),
    MITHRIL_PRISMARINE_2(OreType.DWARVEN_METAL, "Mithril", Blocks.prismarine, 800, 48001, 1),
    MITHRIL_PRISMARINE_3(OreType.DWARVEN_METAL, "Mithril", Blocks.prismarine, 800, 48001, 2),
    MITHRIL_BLUE(OreType.DWARVEN_METAL, "Mithril", Blocks.wool, 1500, 90001, 3),
    TITANIUM(OreType.DWARVEN_METAL, "Titanium", Blocks.stone, 2000, 120001, 4),
    RUBY_GEMSTONE_BLOCK(OreType.GEMSTONE, "Ruby", Blocks.stained_glass, 2300, 138001, 14),
    RUBY_GEMSTONE_PANE(OreType.GEMSTONE, "Ruby", Blocks.stained_glass_pane, 2300, 138001, 14),
    AMBER_GEMSTONE_BLOCK(OreType.GEMSTONE, "Amber", Blocks.stained_glass, 3000, 180001, 1),
    AMBER_GEMSTONE_PANE(OreType.GEMSTONE, "Amber", Blocks.stained_glass_pane, 3000, 180001, 1),
    AMETHYST_GEMSTONE_BLOCK(OreType.GEMSTONE, "Amethyst", Blocks.stained_glass, 3000, 180001, 10),
    AMETHYST_GEMSTONE_PANE(OreType.GEMSTONE, "Amethyst", Blocks.stained_glass_pane, 3000, 180001, 10),
    JADE_GEMSTONE_BLOCK(OreType.GEMSTONE, "Jade", Blocks.stained_glass, 3000, 180001, 5),
    JADE_GEMSTONE_PANE(OreType.GEMSTONE, "Jade", Blocks.stained_glass_pane, 3000, 180001, 5),
    SAPPHIRE_GEMSTONE_BLOCK(OreType.GEMSTONE, "Sapphire", Blocks.stained_glass, 3000, 180001, 3),
    SAPPHIRE_GEMSTONE_PANE(OreType.GEMSTONE, "Sapphire", Blocks.stained_glass_pane, 3000, 180001, 3),
    OPAL_GEMSTONE_BLOCK(OreType.GEMSTONE, "Opal", Blocks.stained_glass, 3000, 180001, 0),
    OPAL_GEMSTONE_PANE(OreType.GEMSTONE, "Opal", Blocks.stained_glass_pane, 3000, 180001, 0),
    TOPAZ_GEMSTONE_BLOCK(OreType.GEMSTONE, "Topaz", Blocks.stained_glass, 3800, 228001, 4),
    TOPAZ_GEMSTONE_PANE(OreType.GEMSTONE, "Topaz", Blocks.stained_glass_pane, 3800, 228001, 4),
    JASPER_GEMSTONE_BLOCK(OreType.GEMSTONE, "Jasper", Blocks.stained_glass, 4800, 288001, 2),
    JASPER_GEMSTONE_PANE(OreType.GEMSTONE, "Jasper", Blocks.stained_glass_pane, 4800, 288001, 2),
    GLACITE(OreType.DWARVEN_METAL, "Glacite", Blocks.packed_ice, 6000, 360001),
    UMBER_1(OreType.DWARVEN_METAL, "Umber", Blocks.hardened_clay, 5600, 336001),
    UMBER_2(OreType.DWARVEN_METAL, "Umber", Blocks.stained_hardened_clay, 5600, 336001, 12),
    UMBER_3(OreType.DWARVEN_METAL, "Umber", Blocks.double_stone_slab2, 5600, 336001, 8),
    TUNGSTEN_1(OreType.DWARVEN_METAL, "Tungsten", Blocks.monster_egg, 5600, 336001, 1),
    TUNGSTEN_2(OreType.DWARVEN_METAL, "Tungsten", Blocks.clay, 5600, 336001),
    ONYX_GEMSTONE_BLOCK(OreType.GEMSTONE, "Onyx", Blocks.stained_glass, 5200, 312001, 15),
    ONYX_GEMSTONE_PANE(OreType.GEMSTONE, "Onyx", Blocks.stained_glass_pane, 5200, 312001, 15),
    AQUAMARINE_GEMSTONE_BLOCK(OreType.GEMSTONE, "Aquamarine", Blocks.stained_glass, 5200, 312001, 11),
    AQUAMARINE_GEMSTONE_PANE(OreType.GEMSTONE, "Aquamarine", Blocks.stained_glass_pane, 5200, 312001, 11),
    CITRINE_GEMSTONE_BLOCK(OreType.GEMSTONE, "Citrine", Blocks.stained_glass, 5200, 312001, 12),
    CITRINE_GEMSTONE_PANE(OreType.GEMSTONE, "Citrine", Blocks.stained_glass_pane, 5200, 312001, 12),
    PERIDOT_GEMSTONE_BLOCK(OreType.GEMSTONE, "Peridot", Blocks.stained_glass, 5200, 312001, 13),
    PERIDOT_GEMSTONE_PANE(OreType.GEMSTONE, "Peridot", Blocks.stained_glass_pane, 5200, 312001, 13),
    SULPHUR(OreType.OTHER, "Sulphur", Blocks.sponge, 500, 30001),
    ;

    fun canInstaMine(miningSpeed: Int) = miningSpeed >= instaMine

    fun getTicksOriginal(miningSpeed: Int): Int {
        return if (canInstaMine(miningSpeed)) {
            0
        } else if (miningSpeed <= 0) {
            2147483647
        } else {
            (30 * strength + miningSpeed / 2) / miningSpeed
        }
    }

    fun getTicksActual(miningSpeed: Int): Int {
        return if (canInstaMine(miningSpeed)) {
            0
        } else {
            getTicksOriginal(miningSpeed).takeIf { it > 4 } ?: 4
        }
    }

    companion object {
        fun getByBlockState(
            block: Block,
            meta: Int,
        ): Ore? = entries.firstOrNull { it.block === block && it.blockMeta == meta }
    }
}
