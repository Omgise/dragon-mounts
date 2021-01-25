/*
 ** 2013 May 30
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.dragon;

import net.minecraftforge.common.config.Configuration;

/**
 *
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class DragonMountsConfig {

	private static final String categoryDebug = "debug";
	private static final String categoryFoods = "dragonFoods";
	private static final String categoryGrowth = "dragonGrowthTimes";
	private static final String categoryOther = "other";
	

    // config properties
    public static boolean eggsInChests = false;
    public static int dragonEntityID = -1;

    public static boolean registerMeatToOreDictionary = true;
    public static String dragonFavoriteFoodTag = "listAllfishraw";
    public static String dragonAllFoodTag = "listAllmeatraw";

    public static int eggToHatchlingTime = 72000;
    public static int hatchlingToJuvenileTime = 48000;
    public static int juvenileToAdultTime = 24000;

    public static boolean debug = false;

    public static void LoadDragonMountsConfig(Configuration config) {
        debug = config.getBoolean("debug", categoryDebug, debug, "Debug mode. Unless you're a developer or are told to activate it, you don't want to set this to true.");

        registerMeatToOreDictionary = config.getBoolean("registerMeatToOreDictionary", categoryFoods, registerMeatToOreDictionary, "Registers meats to the ore dictionary.\nUsesless if non-default values are assigned to dragonFavoriteFoodTag and dragonAllFoodTag");
        dragonFavoriteFoodTag = config.getString("dragonFavoriteFoodTag", categoryFoods, dragonFavoriteFoodTag, "Items with the specified Ore Dictionary tag are considered favorite foods.");
        dragonAllFoodTag = config.getString("dragonAllFoodTag", categoryFoods, dragonAllFoodTag, "Items with the specified Ore Dictionary tag are considered edible foods.");

        eggToHatchlingTime = config.getInt("eggToHatchlingTime", categoryGrowth, eggToHatchlingTime, 4800, Integer.MAX_VALUE, "How long an egg takes to hatch into a hatchling.");
        hatchlingToJuvenileTime = config.getInt("hatchlingToJuvenileTime", categoryGrowth, hatchlingToJuvenileTime, 4800, Integer.MAX_VALUE, "How long a hatchling takes to mature into a juvenile.");
        juvenileToAdultTime = config.getInt("juvenileToAdultTime", categoryGrowth, juvenileToAdultTime, 4800, Integer.MAX_VALUE, "How long a juvenile takes to mature into an adult.");

        eggsInChests = config.getBoolean("eggsInChests", categoryOther, eggsInChests, "Spawns dragon eggs in generated chests when enabled.");
        dragonEntityID = config.getInt("dragonEntityID", categoryOther, dragonEntityID, -1, 255, "Overrides the entity ID for dragons to fix problems with manual IDs from other mods.\nSet to -1 for automatic assignment (recommended).\nWarning: wrong values may cause crashes and loss of data!");

        if (config.hasChanged()) {
            config.save();
        }
    }

}
