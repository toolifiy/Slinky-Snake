package com.example.slinkysnake.data

import com.example.slinkysnake.model.Accessory
import com.example.slinkysnake.model.Achievement
import com.example.slinkysnake.model.BoardTheme
import com.example.slinkysnake.model.FoodCategory
import com.example.slinkysnake.model.FoodTemplate
import com.example.slinkysnake.model.LevelConfig
import com.example.slinkysnake.model.LevelTheme
import com.example.slinkysnake.model.Pattern
import com.example.slinkysnake.model.Position
import com.example.slinkysnake.model.Skin

object GameData {

    val SNAKE_SKINS: List<Skin> = listOf(
        Skin(
            id = "slinky",
            name = "Slinky Green",
            primaryColor = 0xFF4ADE80,
            secondaryColor = 0xFF22C55E,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.NONE,
            pattern = Pattern.SOLID,
            description = "The friendly, happy default snake. Loves red juicy apples!",
            price = 0
        ),
        Skin(
            id = "glam",
            name = "Princess Peach",
            primaryColor = 0xFFF472B6,
            secondaryColor = 0xFFDB2777,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.CROWN,
            pattern = Pattern.SPOTS,
            description = "A fabulous pink snake with a shiny gold crown. Royalty!",
            price = 50
        ),
        Skin(
            id = "zippy",
            name = "Cool Breeze",
            primaryColor = 0xFF38BDF8,
            secondaryColor = 0xFF0284C7,
            eyeColor = 0xFFFACC15,
            accessory = Accessory.SUNGLASSES,
            pattern = Pattern.STRIPES,
            description = "Rocking cool black sunglasses. Cool, calm, and fast!",
            price = 50
        ),
        Skin(
            id = "ninja",
            name = "Shadow Ninja",
            primaryColor = 0xFF475569,
            secondaryColor = 0xFF1E293B,
            eyeColor = 0xFFEF4444,
            accessory = Accessory.BANDANA,
            pattern = Pattern.SOLID,
            description = "A master of stealth wearing a vibrant red ninja head bandana.",
            price = 80
        ),
        Skin(
            id = "mustache",
            name = "Sir Reginald",
            primaryColor = 0xFFF97316,
            secondaryColor = 0xFFC2410C,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.MUSTACHE,
            pattern = Pattern.STRIPES,
            description = "A distinguished gentleman snake with an elegant vintage mustache.",
            price = 80
        ),
        Skin(
            id = "candy",
            name = "Candy Gent",
            primaryColor = 0xFFEC4899,
            secondaryColor = 0xFFFFFFFF,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.MUSTACHE,
            pattern = Pattern.STRIPES,
            description = "Dapper peppermint candy cane design with a distinguished mustache!",
            price = 100
        ),
        Skin(
            id = "angry_viper",
            name = "Angry Viper",
            primaryColor = 0xFFDC2626,
            secondaryColor = 0xFF7F1D1D,
            eyeColor = 0xFFFBBF24,
            accessory = Accessory.BANDANA,
            pattern = Pattern.STRIPES,
            description = "Extremely short-tempered! Don't mess with him, he strikes with fury!",
            price = 100
        ),
        Skin(
            id = "goofy_charlie",
            name = "Goofy Charlie",
            primaryColor = 0xFFFBBF24,
            secondaryColor = 0xFFD97706,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.MUSTACHE,
            pattern = Pattern.SPOTS,
            description = "Always playing pranks and making silly faces. Sweet and funny!",
            price = 120
        ),
        Skin(
            id = "poison",
            name = "Neon Poison",
            primaryColor = 0xFF10B981,
            secondaryColor = 0xFF8B5CF6,
            eyeColor = 0xFFFACC15,
            accessory = Accessory.NONE,
            pattern = Pattern.SPOTS,
            description = "Bioluminescent green and venom purple. Exquisite but highly lethal!",
            price = 150
        ),
        Skin(
            id = "retro",
            name = "Retro Player",
            primaryColor = 0xFF059669,
            secondaryColor = 0xFF1E293B,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.SUNGLASSES,
            pattern = Pattern.STRIPES,
            description = "Direct from the nostalgic green screen of the retro brick phone.",
            price = 150
        ),
        Skin(
            id = "toxic_shroom",
            name = "Mad Scientist",
            primaryColor = 0xFFA855F7,
            secondaryColor = 0xFF10B981,
            eyeColor = 0xFFF43F5E,
            accessory = Accessory.SUNGLASSES,
            pattern = Pattern.SPOTS,
            description = "Mixing weird potions inside the radioactive garden.",
            price = 180
        ),
        Skin(
            id = "cosmic",
            name = "Cosmic Star",
            primaryColor = 0xFFA855F7,
            secondaryColor = 0xFF3B82F6,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.NONE,
            pattern = Pattern.GLOW,
            description = "A cosmic glow snake that shifts colors between nebula purple and space blue.",
            price = 200
        ),
        Skin(
            id = "ruby",
            name = "Ruby Crimson",
            primaryColor = 0xFFDC2626,
            secondaryColor = 0xFFF43F5E,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.BANDANA,
            pattern = Pattern.GLOW,
            description = "Wearing a warrior bandana and glistening with diamond-hard ruby scales.",
            price = 220
        ),
        Skin(
            id = "phantom",
            name = "Midnight Ghost",
            primaryColor = 0xFF1E1B4B,
            secondaryColor = 0xFF6366F1,
            eyeColor = 0xFF60A5FA,
            accessory = Accessory.SUNGLASSES,
            pattern = Pattern.GLOW,
            description = "A cool specter of the night who plays best when shifting dimensions.",
            price = 250
        ),
        Skin(
            id = "royal_sir",
            name = "Sir Emperor",
            primaryColor = 0xFF1E3A8A,
            secondaryColor = 0xFFFACC15,
            eyeColor = 0xFFFFFFFF,
            accessory = Accessory.CROWN,
            pattern = Pattern.STRIPES,
            description = "Highly sophisticated blue bloodline with an outstanding sense of authority.",
            price = 300
        ),
        Skin(
            id = "dragon",
            name = "Golden Dragon",
            primaryColor = 0xFFF59E0B,
            secondaryColor = 0xFFEF4444,
            eyeColor = 0xFFEF4444,
            accessory = Accessory.CROWN,
            pattern = Pattern.GLOW,
            description = "A legendary dragon serpent coated in solid gold. Breath of stars!",
            price = 350
        ),
        Skin(
            id = "fire",
            name = "Ghostfire Rider",
            primaryColor = 0xFFF97316,
            secondaryColor = 0xFFEF4444,
            eyeColor = 0xFFFBBF24,
            accessory = Accessory.BANDANA,
            pattern = Pattern.GLOW,
            description = "Ghost Rider inspired blaze! Trails of glowing ashes, fireballs and lava scales!",
            price = 400
        ),
        Skin(
            id = "ice",
            name = "Glacier Frostbite",
            primaryColor = 0xFF38BDF8,
            secondaryColor = 0xFF0284C7,
            eyeColor = 0xFFE0F2FE,
            accessory = Accessory.CROWN,
            pattern = Pattern.GLOW,
            description = "Frozen ice shell with frosty mist, diamond crystals and snow sparkles!",
            price = 400
        )
    )

    val LEVEL_CONFIGS: List<LevelConfig> = listOf(
        LevelConfig(
            level = 1,
            targetScore = 100,
            speed = 260,
            obstacles = emptyList(),
            theme = LevelTheme("Sunny Grassland 🏡", 0xFFC2F5D3, 0xFFE6FCEE, 0xFFE2E8F0, 0xFF34D399)
        ),
        LevelConfig(
            level = 2,
            targetScore = 120,
            speed = 245,
            obstacles = listOf(
                Position(5, 5), Position(5, 6), Position(14, 5), Position(14, 6),
                Position(5, 13), Position(5, 14), Position(14, 13), Position(14, 14)
            ),
            theme = LevelTheme("Candy Forest 🍬", 0xFFFED7AA, 0xFFFFF7ED, 0xFFFED7AA, 0xFFFB923C)
        ),
        LevelConfig(
            level = 3,
            targetScore = 150,
            speed = 230,
            obstacles = listOf(
                Position(3, 3), Position(3, 4), Position(3, 5), Position(4, 3), Position(5, 3),
                Position(16, 3), Position(16, 4), Position(16, 5), Position(15, 3), Position(14, 3),
                Position(3, 16), Position(3, 15), Position(3, 14), Position(4, 16), Position(5, 16),
                Position(16, 16), Position(16, 15), Position(16, 14), Position(15, 16), Position(14, 16)
            ),
            theme = LevelTheme("Castle Ruins 🏰", 0xFFE9D5FF, 0xFFFAF5FF, 0xFFE9D5FF, 0xFFA855F7)
        ),
        LevelConfig(
            level = 4,
            targetScore = 180,
            speed = 215,
            obstacles = listOf(
                Position(5, 5), Position(6, 5), Position(5, 6), Position(6, 6),
                Position(13, 5), Position(14, 5), Position(13, 6), Position(14, 6),
                Position(5, 13), Position(6, 13), Position(5, 14), Position(6, 14),
                Position(13, 13), Position(14, 13), Position(13, 14), Position(14, 14)
            ),
            theme = LevelTheme("Spicy Volcano 🌋", 0xFFFECDD3, 0xFFFFF1F2, 0xFFFECDD3, 0xFFF43F5E)
        ),
        LevelConfig(
            level = 5,
            targetScore = 200,
            speed = 205,
            obstacles = listOf(
                Position(4, 4), Position(4, 5), Position(4, 6), Position(4, 7), Position(4, 8),
                Position(4, 11), Position(4, 12), Position(4, 13), Position(4, 14), Position(4, 15),
                Position(15, 4), Position(15, 5), Position(15, 6), Position(15, 7), Position(15, 8),
                Position(15, 11), Position(15, 12), Position(15, 13), Position(15, 14), Position(15, 15),
                Position(8, 3), Position(11, 3), Position(8, 16), Position(11, 16)
            ),
            theme = LevelTheme("Cyber Neon Grid 🌌", 0xFF1E1B4B, 0xFF0B0A21, 0xFF1E293B, 0xFF8B5CF6)
        ),
        LevelConfig(
            level = 6,
            targetScore = 220,
            speed = 195,
            obstacles = listOf(
                Position(5, 3), Position(6, 3), Position(7, 3), Position(12, 3), Position(13, 3), Position(14, 3),
                Position(5, 16), Position(6, 16), Position(7, 16), Position(12, 16), Position(13, 16), Position(14, 16)
            ),
            theme = LevelTheme("Golden Desert 🏜️", 0xFFFEF08A, 0xFFFEF9C3, 0xFFFEF08A, 0xFFEAB308)
        ),
        LevelConfig(
            level = 7,
            targetScore = 240,
            speed = 185,
            obstacles = listOf(
                Position(3, 7), Position(3, 8), Position(16, 7), Position(16, 8),
                Position(8, 3), Position(11, 3), Position(8, 16), Position(11, 16)
            ),
            theme = LevelTheme("Frozen Ocean ❄️", 0xFF93C5FD, 0xFFDBEAFE, 0xFF93C5FD, 0xFF38BDF8)
        ),
        LevelConfig(
            level = 8,
            targetScore = 260,
            speed = 175,
            obstacles = listOf(
                Position(2, 2), Position(17, 2), Position(2, 17), Position(17, 17),
                Position(6, 6), Position(13, 6), Position(6, 13), Position(13, 13)
            ),
            theme = LevelTheme("Ancient Jungle 🌴", 0xFF86EFAC, 0xFFDCFCE7, 0xFF86EFAC, 0xFF22C55E)
        ),
        LevelConfig(
            level = 9,
            targetScore = 280,
            speed = 168,
            obstacles = listOf(
                Position(4, 10), Position(3, 10), Position(5, 10), Position(4, 9), Position(4, 11),
                Position(15, 10), Position(14, 10), Position(16, 10), Position(15, 9), Position(15, 11)
            ),
            theme = LevelTheme("Haunted Graveyard 👻", 0xFFCBD5E1, 0xFFF1F5F9, 0xFFCBD5E1, 0xFF64748B)
        ),
        LevelConfig(
            level = 10,
            targetScore = 300,
            speed = 160,
            obstacles = listOf(
                Position(8, 4), Position(9, 4), Position(11, 4),
                Position(8, 15), Position(9, 15), Position(11, 15),
                Position(4, 8), Position(4, 9), Position(4, 10), Position(4, 11),
                Position(15, 8), Position(15, 9), Position(15, 10), Position(15, 11)
            ),
            theme = LevelTheme("Cosmic Blackhole 🌌", 0xFF4C1D95, 0xFF1E1B4B, 0xFF4C1D95, 0xFF7C3AED)
        ),
        LevelConfig(
            level = 11,
            targetScore = 320,
            speed = 155,
            obstacles = listOf(
                Position(2, 3), Position(3, 2), Position(16, 2), Position(17, 3),
                Position(2, 16), Position(3, 17), Position(16, 17), Position(17, 16),
                Position(5, 9), Position(14, 9)
            ),
            theme = LevelTheme("Candy Wonderland 🍭", 0xFFFBCFE8, 0xFFFFF1F2, 0xFFFBCFE8, 0xFFEC4899)
        ),
        LevelConfig(
            level = 12,
            targetScore = 340,
            speed = 150,
            obstacles = listOf(
                Position(6, 6), Position(13, 6), Position(6, 13), Position(13, 13)
            ),
            theme = LevelTheme("Emerald Temple 🛕", 0xFF064E3B, 0xFF022C22, 0xFF064E3B, 0xFF10B981)
        ),
        LevelConfig(
            level = 13,
            targetScore = 360,
            speed = 145,
            obstacles = listOf(
                Position(1, 3), Position(2, 3), Position(3, 3), Position(4, 3), Position(5, 3),
                Position(14, 3), Position(15, 3), Position(16, 3), Position(17, 3), Position(18, 3),
                Position(1, 16), Position(2, 16), Position(3, 16), Position(4, 16), Position(5, 16),
                Position(14, 16), Position(15, 16), Position(16, 16), Position(17, 16), Position(18, 16)
            ),
            theme = LevelTheme("Volcanic River 🌋", 0xFF7C2D12, 0xFF451A03, 0xFF7C2D12, 0xFFEA580C)
        ),
        LevelConfig(
            level = 14,
            targetScore = 380,
            speed = 140,
            obstacles = listOf(
                Position(3, 3), Position(4, 3), Position(3, 4),
                Position(15, 3), Position(16, 3), Position(16, 4),
                Position(3, 15), Position(3, 16), Position(4, 16),
                Position(16, 15), Position(16, 16), Position(15, 16)
            ),
            theme = LevelTheme("Sky Haven ☁️", 0xFF93C5FD, 0xFFEFF6FF, 0xFF93C5FD, 0xFF3B82F6)
        ),
        LevelConfig(
            level = 15,
            targetScore = 400,
            speed = 135,
            obstacles = listOf(
                Position(2, 5), Position(3, 5), Position(4, 5),
                Position(15, 5), Position(16, 5), Position(17, 5),
                Position(2, 14), Position(3, 14), Position(4, 14),
                Position(15, 14), Position(16, 14), Position(17, 14)
            ),
            theme = LevelTheme("Neon Arcade 👾", 0xFF581C87, 0xFF0F0926, 0xFF581C87, 0xFFD946EF)
        ),
        LevelConfig(
            level = 16,
            targetScore = 420,
            speed = 130,
            obstacles = listOf(
                Position(5, 2), Position(14, 2), Position(2, 8), Position(17, 8),
                Position(2, 11), Position(17, 11), Position(5, 17), Position(14, 17)
            ),
            theme = LevelTheme("Golden Sandhills 🏜️", 0xFFFDE68A, 0xFFFEF9C3, 0xFFFDE68A, 0xFFF59E0B)
        ),
        LevelConfig(
            level = 17,
            targetScore = 440,
            speed = 125,
            obstacles = listOf(
                Position(5, 5), Position(5, 14), Position(14, 5), Position(14, 14),
                Position(2, 9), Position(17, 9)
            ),
            theme = LevelTheme("Frostbite Caves 🥶", 0xFF1E3A8A, 0xFF0F172A, 0xFF1E3A8A, 0xFF06B6D4)
        ),
        LevelConfig(
            level = 18,
            targetScore = 460,
            speed = 122,
            obstacles = listOf(
                Position(4, 4), Position(15, 4), Position(4, 15), Position(15, 15),
                Position(9, 3), Position(10, 3), Position(9, 16), Position(10, 16)
            ),
            theme = LevelTheme("Atlantis Depths 🔱", 0xFF115E59, 0xFF064E3B, 0xFF115E59, 0xFF14B8A6)
        ),
        LevelConfig(
            level = 19,
            targetScore = 480,
            speed = 120,
            obstacles = listOf(
                Position(3, 3), Position(16, 3), Position(3, 16), Position(16, 16),
                Position(7, 7), Position(12, 7), Position(7, 12), Position(12, 12)
            ),
            theme = LevelTheme("Autumn Harvest 🍁", 0xFFFDBA74, 0xFFFFF7ED, 0xFFFDBA74, 0xFFF97316)
        ),
        LevelConfig(
            level = 20,
            targetScore = 500,
            speed = 118,
            obstacles = listOf(
                Position(4, 5), Position(5, 5), Position(14, 5), Position(15, 5),
                Position(4, 14), Position(5, 14), Position(14, 14), Position(15, 14)
            ),
            theme = LevelTheme("Toxic Lab 🧪", 0xFF022C22, 0xFF0A0A0A, 0xFF022C22, 0xFF22C55E)
        ),
        LevelConfig(
            level = 21,
            targetScore = 520,
            speed = 115,
            obstacles = listOf(
                Position(3, 6), Position(16, 6), Position(3, 13), Position(16, 13),
                Position(8, 3), Position(11, 3), Position(8, 16), Position(11, 16)
            ),
            theme = LevelTheme("Nebula Oasis 🌌", 0xFF4A044E, 0xFF1E1B4B, 0xFF4A044E, 0xFFE879F9)
        ),
        LevelConfig(
            level = 22,
            targetScore = 540,
            speed = 112,
            obstacles = listOf(
                Position(5, 4), Position(6, 4), Position(13, 4), Position(14, 4),
                Position(5, 15), Position(6, 15), Position(13, 15), Position(14, 15)
            ),
            theme = LevelTheme("Cherry Sakura 🌸", 0xFFFECDD3, 0xFFFFF1F2, 0xFFFECDD3, 0xFFFB7185)
        ),
        LevelConfig(
            level = 23,
            targetScore = 560,
            speed = 110,
            obstacles = listOf(
                Position(4, 4), Position(5, 4), Position(6, 4),
                Position(13, 4), Position(14, 4), Position(15, 4),
                Position(4, 15), Position(5, 15), Position(6, 15),
                Position(13, 15), Position(14, 15), Position(15, 15)
            ),
            theme = LevelTheme("Emerald Dungeon ⛓️", 0xFF18181B, 0xFF09090B, 0xFF27272A, 0xFF059669)
        ),
        LevelConfig(
            level = 24,
            targetScore = 580,
            speed = 108,
            obstacles = listOf(
                Position(3, 5), Position(16, 5), Position(3, 14), Position(16, 14),
                Position(6, 9), Position(13, 9)
            ),
            theme = LevelTheme("Diamond Valley 💎", 0xFF7DD3FC, 0xFFF0F9FF, 0xFF7DD3FC, 0xFF0284C7)
        ),
        LevelConfig(
            level = 25,
            targetScore = 600,
            speed = 105,
            obstacles = listOf(
                Position(3, 3), Position(4, 3), Position(5, 3),
                Position(14, 3), Position(15, 3), Position(16, 3),
                Position(3, 16), Position(4, 16), Position(5, 16),
                Position(14, 16), Position(15, 16), Position(16, 16),
                Position(7, 7), Position(12, 7), Position(7, 12), Position(12, 12)
            ),
            theme = LevelTheme("Champion Arena 👑", 0xFF78350F, 0xFF292524, 0xFF78350F, 0xFFF59E0B)
        )
    )

    val ACHIEVEMENTS: List<Achievement> = listOf(
        Achievement("first_bite", "First Bite 🍎", "Eat your first delicious fruit.", "😋", category = "POWER", rewardCoins = 50, targetGoal = 1, accentColor = 0xFFEF4444),
        Achievement("half_century", "Half Century 🥉", "Score at least 50 XP in one game.", "🥉", category = "SCORE", rewardCoins = 60, targetGoal = 50, accentColor = 0xFFF59E0B),
        Achievement("century", "Centurion 💯", "Reach 100 XP in a single match.", "💯", category = "SCORE", rewardCoins = 100, targetGoal = 100, accentColor = 0xFF10B981),
        Achievement("snake_master", "Snake God 👑", "Reach a massive 300 XP in one game.", "👑", category = "SCORE", rewardCoins = 200, targetGoal = 300, accentColor = 0xFFFBBF24),
        Achievement("score_500", "Viper Lord 🐉", "Reach 500 XP in one thrilling run.", "🐉", category = "SCORE", rewardCoins = 350, targetGoal = 500, accentColor = 0xFF8B5CF6),
        Achievement("score_1000", "Slytherin Emperor 💀", "Reach a legendary score of 1000 XP!", "💀", category = "SCORE", rewardCoins = 500, targetGoal = 1000, accentColor = 0xFFEC4899),
        Achievement("blue_magic", "Blue Sorcerer 🧪", "Eat a Magical Blue Booster Potion.", "🧪", category = "POWER", rewardCoins = 75, targetGoal = 1, accentColor = 0xFF3B82F6),
        Achievement("star_power", "Star Catcher ⭐", "Collect a glowing Golden Star.", "⭐", category = "POWER", rewardCoins = 80, targetGoal = 1, accentColor = 0xFFFACC15),
        Achievement("spicy_run", "Spicy Runner 🌶️", "Eat a Spicy Chili and sprint with fire trails.", "🔥", category = "POWER", rewardCoins = 80, targetGoal = 1, accentColor = 0xFFF97316),
        Achievement("chili_crying", "Spicy Tears 😭", "Cry funny fiery tears for 10 seconds.", "😭", category = "POWER", rewardCoins = 90, targetGoal = 1, accentColor = 0xFFFB923C),
        Achievement("magnet_pull", "Magneto 🧲", "Activate magnetic force to vacuum fruits.", "🧲", category = "POWER", rewardCoins = 85, targetGoal = 1, accentColor = 0xFFF87171),
        Achievement("immortal_ghost", "Ghost Mode 👻", "Activate ghost immortality through obstacles.", "👻", category = "POWER", rewardCoins = 100, targetGoal = 1, accentColor = 0xFFA78BFA),
        Achievement("shrink_master", "Mini Snake 🍄", "Eat a Shrink Shroom to shorten your tail.", "🤏", category = "POWER", rewardCoins = 80, targetGoal = 1, accentColor = 0xFF34D399),
        Achievement("hungry_slitherer", "Voracious Eater 🍔", "Eat 30 foods in a single run.", "🍔", category = "POWER", rewardCoins = 120, targetGoal = 30, accentColor = 0xFFD97706),
        Achievement("double_deal", "Double Jackpot 💎", "Eat fruit while Double Points is active.", "💎", category = "POWER", rewardCoins = 95, targetGoal = 1, accentColor = 0xFFF472B6),
        Achievement("perfect_reflexes", "Speed Demon ⚡", "Control snake at blazing Hyper Speed.", "⚡", category = "POWER", rewardCoins = 110, targetGoal = 1, accentColor = 0xFF38BDF8),
        Achievement("combo_king", "Combo Master 🔥", "Reach an 8x combo multiplier streak!", "💥", category = "SCORE", rewardCoins = 150, targetGoal = 8, accentColor = 0xFFEF4444),
        Achievement("all_skins", "Fashionista 👗", "Play with at least 3 different snake skins.", "🎭", category = "COLLECTION", rewardCoins = 100, targetGoal = 3, accentColor = 0xFF06B6D4),
        Achievement("skin_collector", "Skin Collector 🎭", "Play with at least 6 different snake skins.", "🕶️", category = "COLLECTION", rewardCoins = 250, targetGoal = 6, accentColor = 0xFF6366F1),
        Achievement("level_clear", "Level Explorer 🗺️", "Complete Level 1 in Adventure Mode.", "🏆", category = "ADVENTURE", rewardCoins = 100, targetGoal = 1, accentColor = 0xFF10B981),
        Achievement("level_3_master", "Viper Hunter 🗺️", "Complete Level 3 in Adventure Mode.", "🛡️", category = "ADVENTURE", rewardCoins = 180, targetGoal = 3, accentColor = 0xFFA855F7),
        Achievement("volcano_conqueror", "Volcano Conqueror 🌋", "Survive Level 4 in Adventure Mode.", "🌋", category = "ADVENTURE", rewardCoins = 250, targetGoal = 4, accentColor = 0xFFEA580C),
        Achievement("cyber_god", "Cyber Legend 🌌", "Complete Level 5 and conquer the grid.", "🌌", category = "ADVENTURE", rewardCoins = 400, targetGoal = 5, accentColor = 0xFFD946EF)
    )

    val BOARD_THEMES: List<BoardTheme> = listOf(
        BoardTheme("mint", "Mint Forest 🌿", 0xFFC2F5D3, 0xFFE6FCEE, price = 0, description = "Fresh spring garden checkered grass arena"),
        BoardTheme("crimson", "Crimson Velvet 🍎", 0xFFFCA5A5, 0xFFFEE2E2, price = 0, description = "Sweet velvety ruby stadium floor"),
        BoardTheme("butter", "Sweet Butter 🥞", 0xFFFDE68A, 0xFFFEF3C7, price = 0, description = "Warm honeyed pancake waffle tiles"),
        BoardTheme("lavender", "Royal Lavender 🌌", 0xFFE9D5FF, 0xFFFAF5FF, price = 0, description = "Dreamy mystical purple twilight court"),
        BoardTheme("sky", "Sky Bubblegum 🏖️", 0xFFBFDBFE, 0xFFDBEAFE, price = 0, description = "Sunny coastal sea breeze checkered stadium"),
        BoardTheme("cyber", "Cyber Shadow 👾", 0xFF1E1B4B, 0xFF0B0A21, price = 50, description = "Ultra dark synthwave neon mainframe grid"),
        BoardTheme("chocolate", "Choco Cream 🍫", 0xFFFED7AA, 0xFFFFEDD5, price = 60, description = "Rich dessert cocoa bakery floor"),
        BoardTheme("volcano", "Spicy Volcano 🌋", 0xFFFCA5A5, 0xFF451A03, price = 80, description = "Hot magma lava rock battle arena"),
        BoardTheme("neon_arcade", "Neon Arcade 🔮", 0xFF581C87, 0xFF020617, price = 100, description = "Glowing ultraviolet 80s arcade court"),
        BoardTheme("gold_empire", "Gold Empire 👑", 0xFFFBBF24, 0xFF1E1B4B, price = 120, description = "Gilded royal palace with golden glow")
    )

    val ALL_FOOD_TEMPLATES: List<FoodTemplate> = listOf(
        // 50 Standard Delicious Foods (Fresh Fruit, Sweet Treats, Savory Meals)
        FoodTemplate("APPLE", "🍎", "Red Apple", 0xFFEF4444, 10, 28, FoodCategory.FRESH_FRUIT, "Fresh, juicy, and sweet standard apple."),
        FoodTemplate("CAKE", "🍰", "Feast Cake", 0xFFFF007F, 40, 5, FoodCategory.SWEET_TREAT, "Delicious sugar rush! Grows you 2 extra segments!"),
        FoodTemplate("WATERMELON", "🍉", "Watermelon", 0xFF22C55E, 30, 8, FoodCategory.FRESH_FRUIT, "GIANT WATERMELON SPLASH! High points & red juice particles!"),
        FoodTemplate("BANANA", "🍌", "Sweet Banana", 0xFFFBBF24, 12, 10, FoodCategory.FRESH_FRUIT, "Sweet tropical banana snack."),
        FoodTemplate("COCONUT", "🥥", "Hard Coconut", 0xFF854D0E, 20, 8, FoodCategory.FRESH_FRUIT, "Hard nut crunch with wooden splinter particles."),
        FoodTemplate("PINEAPPLE", "🍍", "Pineapple", 0xFFEAB308, 18, 8, FoodCategory.FRESH_FRUIT, "Tropical sweetness that brings huge satisfaction."),
        FoodTemplate("DRAGON_FRUIT", "🌸", "Dragon Fruit", 0xFFEC4899, 50, 5, FoodCategory.FRESH_FRUIT, "Sweet tropical Pitaya bursting with pink star sprinkles!"),
        FoodTemplate("MEAT", "🍖", "Juicy Meat", 0xFFB45309, 35, 8, FoodCategory.SAVORY_MEAL, "Hearty meat snack packed with gaming energy."),
        FoodTemplate("MANGO", "🥭", "Sweet Mango", 0xFFF59E0B, 22, 9, FoodCategory.FRESH_FRUIT, "Rich sweet golden mango treat."),
        FoodTemplate("PIZZA", "🍕", "Cheesy Pizza", 0xFFEF4444, 45, 6, FoodCategory.SAVORY_MEAL, "Mouth-watering slice loaded with mozzarella."),
        FoodTemplate("BURGER", "🍔", "Classic Burger", 0xFFD97706, 42, 6, FoodCategory.SAVORY_MEAL, "Juicy double stack burger for championship points."),
        FoodTemplate("PEACH", "🍑", "Juicy Peach", 0xFFFB923C, 15, 8, FoodCategory.FRESH_FRUIT, "Sweet fuzzy peach bursting with flavor."),
        FoodTemplate("KIWI", "🥝", "Zesty Kiwi", 0xFF4ADE80, 16, 8, FoodCategory.FRESH_FRUIT, "Zesty green kiwi rich in vitamins."),
        FoodTemplate("PEAR", "🍐", "Sweet Pear", 0xFFA3E635, 14, 8, FoodCategory.FRESH_FRUIT, "Crisp golden-green pear slice."),
        FoodTemplate("ORANGE", "🍊", "Sweet Orange", 0xFFF97316, 12, 10, FoodCategory.FRESH_FRUIT, "Juicy citrus burst."),
        FoodTemplate("BLUEBERRY", "🫐", "Blueberries", 0xFF60A5FA, 18, 9, FoodCategory.FRESH_FRUIT, "Sweet berry cluster."),
        FoodTemplate("CORN", "🌽", "Sweet Corn", 0xFFFBBF24, 10, 10, FoodCategory.SAVORY_MEAL, "Buttery golden corn cob."),
        FoodTemplate("SWEET_POTATO", "🍠", "Sweet Potato", 0xFFC084FC, 16, 8, FoodCategory.SAVORY_MEAL, "Warm roasted sweet potato."),
        FoodTemplate("HONEY", "🍯", "Honey Jar", 0xFFF59E0B, 30, 6, FoodCategory.SWEET_TREAT, "Golden sweet raw nectar jar."),
        FoodTemplate("WAFFLE", "🧇", "Spiced Waffle", 0xFFD97706, 25, 7, FoodCategory.SWEET_TREAT, "Crispy golden waffle grid."),
        FoodTemplate("CROISSANT", "🥐", "Croissant", 0xFFF59E0B, 20, 8, FoodCategory.SWEET_TREAT, "Flaky buttery French pastry."),
        FoodTemplate("CHERRY", "🍒", "Twin Cherries", 0xFFDC2626, 14, 10, FoodCategory.FRESH_FRUIT, "Sweet paired red cherries."),
        FoodTemplate("AVOCADO", "🥑", "Fresh Avocado", 0xFF10B981, 16, 8, FoodCategory.FRESH_FRUIT, "Creamy superfood slice."),
        FoodTemplate("DOUGHNUT", "🍩", "Glazed Donut", 0xFFEC4899, 24, 7, FoodCategory.SWEET_TREAT, "Pink glazed sprinkle donut."),
        FoodTemplate("SUSHI", "🍣", "Salmon Sushi", 0xFFF87171, 32, 7, FoodCategory.SAVORY_MEAL, "Fresh nigiri sushi roll."),
        FoodTemplate("COOKIE", "🍪", "Choco Cookie", 0xFF92400E, 18, 9, FoodCategory.SWEET_TREAT, "Fresh baked chocolate chip cookie."),
        FoodTemplate("ICE_CREAM", "🍨", "Ice Cream Cup", 0xFFF472B6, 26, 7, FoodCategory.SWEET_TREAT, "Triple scoop sundae."),
        FoodTemplate("TACO", "🌮", "Crunchy Taco", 0xFFF59E0B, 28, 8, FoodCategory.SAVORY_MEAL, "Crunchy fiesta taco."),
        FoodTemplate("BROCCOLI", "🥦", "Green Broccoli", 0xFF059669, 8, 10, FoodCategory.SAVORY_MEAL, "Crisp healthy green florets."),
        FoodTemplate("CARROT", "🥕", "Sweet Carrot", 0xFFF97316, 12, 10, FoodCategory.FRESH_FRUIT, "Crunchy sweet garden carrot."),
        FoodTemplate("CHEESE", "🧀", "Swiss Cheese", 0xFFFBBF24, 15, 10, FoodCategory.SAVORY_MEAL, "Aged holey swiss cheese wedge."),
        FoodTemplate("FRENCH_FRIES", "🍟", "Crispy Fries", 0xFFFACC15, 22, 8, FoodCategory.SAVORY_MEAL, "Salty crispy golden fries."),
        FoodTemplate("HOTDOG", "🌭", "Sizzling Hotdog", 0xFFEA580C, 27, 8, FoodCategory.SAVORY_MEAL, "Stadium hotdog with mustard."),
        FoodTemplate("STRAWBERRY", "🍓", "Strawberry", 0xFFEF4444, 13, 11, FoodCategory.FRESH_FRUIT, "Ripe garden strawberry."),
        FoodTemplate("LEMON", "🍋", "Sour Lemon", 0xFFEAB308, 11, 11, FoodCategory.FRESH_FRUIT, "Zesty tangy citrus lemon."),
        FoodTemplate("POPCORN", "🍿", "Hot Popcorn", 0xFFF3F4F6, 21, 9, FoodCategory.SWEET_TREAT, "Movie theater buttery popcorn."),
        FoodTemplate("PANCAKES", "🥞", "Pancakes", 0xFFF59E0B, 30, 6, FoodCategory.SWEET_TREAT, "Fluffy pancake stack with syrup."),
        FoodTemplate("PRETZEL", "🥨", "Hot Pretzel", 0xFFB45309, 16, 8, FoodCategory.SWEET_TREAT, "Twisted salted bavarian pretzel."),
        FoodTemplate("EGG", "🍳", "Fried Egg", 0xFFFBBF24, 14, 9, FoodCategory.SAVORY_MEAL, "Sunny side up breakfast egg."),
        FoodTemplate("NOODLES", "🍜", "Hot Ramen", 0xFFF59E0B, 35, 6, FoodCategory.SAVORY_MEAL, "Steaming bowl of spicy ramen."),
        FoodTemplate("DUMPLING", "🥟", "Dumpling", 0xFFFEF08A, 25, 7, FoodCategory.SAVORY_MEAL, "Steamed juicy dumpling."),
        FoodTemplate("LOLLIPOP", "🍭", "Lollipop", 0xFFF472B6, 15, 9, FoodCategory.SWEET_TREAT, "Swirly carnival sucker."),
        FoodTemplate("CHOCOLATE", "🍫", "Choco Bar", 0xFF78350F, 28, 7, FoodCategory.SWEET_TREAT, "Rich dark chocolate squares."),
        FoodTemplate("PUDDING", "🍮", "Sweet Pudding", 0xFFFBBF24, 22, 8, FoodCategory.SWEET_TREAT, "Creamy caramel custard flan."),
        FoodTemplate("MILK", "🥛", "Milk Glass", 0xFFF3F4F6, 12, 10, FoodCategory.SAVORY_MEAL, "Pure fresh cold whole milk."),
        FoodTemplate("CUPCAKE", "🧁", "Cupcake", 0xFFEC4899, 32, 6, FoodCategory.SWEET_TREAT, "Frosted birthday cupcake with sprinkles."),
        FoodTemplate("GRAPES_RED", "🍇", "Sweet Grapes", 0xFF8B5CF6, 16, 9, FoodCategory.FRESH_FRUIT, "Sweet juicy purple grape bunch."),
        FoodTemplate("GREEN_APPLE", "🍏", "Green Apple", 0xFF4ADE80, 12, 11, FoodCategory.FRESH_FRUIT, "Crisp tart green granny smith apple."),
        FoodTemplate("RED_PEPPER", "🫑", "Bell Pepper", 0xFFEF4444, 14, 10, FoodCategory.FRESH_FRUIT, "Fresh crunchy sweet bell pepper."),
        FoodTemplate("ONION_SOUP", "🍲", "Warm Stew", 0xFFF59E0B, 30, 7, FoodCategory.SAVORY_MEAL, "Steaming bowl of hearty vegetable stew.")
    )

    val ALL_POWER_TEMPLATES: List<FoodTemplate> = listOf(
        FoodTemplate("POWER_SPEED", "⚡", "Hyper Speed", 0xFF38BDF8, 30, 10, FoodCategory.POWER_UP, "FIRE SPEED! Surge forward with 1.6x sprint speed & 2x combo score for 10s!"),
        FoodTemplate("POWER_IMMORTAL", "👻", "Ghost Shield", 0xFFA78BFA, 30, 10, FoodCategory.POWER_UP, "GHOST MODE! Freely pass through boundaries, obstacles & body for 10s!"),
        FoodTemplate("POWER_DOUBLE", "💎", "Double Points", 0xFFF472B6, 30, 10, FoodCategory.POWER_UP, "DOUBLE DEAL! Doubles all score increments, combo rewards & multipliers for 10s!"),
        FoodTemplate("POWER_MAGNET", "🧲", "Magnet Pull", 0xFFF87171, 30, 10, FoodCategory.POWER_UP, "MAGNETIC FORCE! Automatically draws all nearby foods directly into your mouth for 10s!"),
        FoodTemplate("POWER_SHRINK", "🍄", "Shrink Shroom", 0xFF34D399, 30, 10, FoodCategory.POWER_UP, "SHRINK SHROOM! Instantly cuts your tail length by 35% for easy maneuvering!"),
        FoodTemplate("BOOSTER", "🧪", "Magic Elixir", 0xFF3B82F6, 30, 10, FoodCategory.POWER_UP, "BLUE POTION! Speed surge with glowing blue magical sparkles for 10s!"),
        FoodTemplate("GOLDEN_STAR", "⭐", "Golden Star", 0xFFFACC15, 35, 10, FoodCategory.POWER_UP, "STAR BLAST! Mega points explosion with glittering golden star burst!"),
        FoodTemplate("POWER_FREEZE", "❄️", "Frost Chill", 0xFF06B6D4, 30, 10, FoodCategory.POWER_UP, "FROST CHILL! Slows down movement speed into chill motion for effortless precision!"),
        FoodTemplate("POWER_SHIELD", "🛡️", "Safe Shield", 0xFFEAB308, 30, 10, FoodCategory.POWER_UP, "SAFE SHIELD! Protects you from 1 fatal crash or obstacle collision!"),
        FoodTemplate("POWER_DRAGON", "🐉", "Dragon Beast", 0xFFF59E0B, 50, 10, FoodCategory.POWER_UP, "DRAGON BEAST! Transforms into an invincible blazing dragon with fiery aura & double points for 12s!")
    )
}
