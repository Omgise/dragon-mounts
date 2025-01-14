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

	private static final String categoryFood = "dragon food";
	private static final String categoryGrowth = "dragon growth";
    private static final String categoryStats = "dragon stats";
	private static final String categoryOther = "other";

	public static String dragonAllFoodTag = "listAllmeatraw";
    public static String dragonFavoriteFoodTag = "listAllfishraw";
    public static float healthFromFoodMultiplier = 1f;
    public static boolean registerMeatToOreDictionary = true;

    public static int eggToHatchlingTime = 72000;
    public static int hatchlingToJuvenileTime = 48000;
    public static int juvenileToAdultTime = 24000;

    public static int baseDamage = 8;
    public static int baseHealth = 60;
    public static float incomingDamageMultiplier = 1;
    public static int flatResistance = 0;

    public static boolean debug = false;
    public static int dragonEntityID = -1;
    public static boolean editSplashes;
    public static boolean eggsInChests = false;

    public static void LoadDragonMountsConfig(Configuration config) {

    	dragonAllFoodTag = config.getString("dragonAllFoodTag", categoryFood, dragonAllFoodTag, "Items with the specified Ore Dictionary tag are considered edible foods.");
        dragonFavoriteFoodTag = config.getString("dragonFavoriteFoodTag", categoryFood, dragonFavoriteFoodTag, "Items with the specified Ore Dictionary tag are considered favorite foods.");
        healthFromFoodMultiplier = config.getFloat("healthFromFoodMultiplier", categoryFood, healthFromFoodMultiplier, 0.1f, 100000f, "Determines how effectively edible foods heal dragons.");
        registerMeatToOreDictionary = config.getBoolean("registerMeatToOreDictionary", categoryFood, registerMeatToOreDictionary, "Registers meats to the ore dictionary.\nUseless if non-default values are assigned to dragonFavoriteFoodTag and dragonAllFoodTag");

        baseDamage = config.getInt("baseDamage", categoryStats, baseDamage, 1, Integer.MAX_VALUE, "The base amount of damage dragons deal.");
        baseHealth = config.getInt("baseHealth", categoryStats, baseHealth, 1, Integer.MAX_VALUE, "The base amount of health dragons have.");
        flatResistance = config.getInt("resistanceFlat", categoryStats, flatResistance, 0, Integer.MAX_VALUE, "Flat amount reduced from incoming damage.");
        incomingDamageMultiplier = config.getFloat("incomingDamageMultiplier", categoryStats, incomingDamageMultiplier, 0.01f, 10f, "Multiplies incoming damage after it has been reduced by flat resistance.\n0.1 would be a 90% reduction, and 2.5 a 150% increase.");

        eggToHatchlingTime = config.getInt("eggToHatchlingTime", categoryGrowth, eggToHatchlingTime, 4800, Integer.MAX_VALUE, "How many ticks eggs take to turn into hatchlings.\nThere are 20 ticks in a second and 24000 in a Minecraft day.");
        hatchlingToJuvenileTime = config.getInt("hatchlingToJuvenileTime", categoryGrowth, hatchlingToJuvenileTime, 4800, Integer.MAX_VALUE, "How many ticks hatchlings take to turn into juveniles.\nThere are 20 ticks in a second and 24000 in a Minecraft day.");
        juvenileToAdultTime = config.getInt("juvenileToAdultTime", categoryGrowth, juvenileToAdultTime, 4800, Integer.MAX_VALUE, "How many ticks juveniles take to turn into adults.\nThere are 20 ticks in a second and 24000 in a Minecraft day.");

        debug = config.getBoolean("debug", categoryOther, debug, "Debug mode. Unless you're a developer or are told to activate it, you don't want to set this to true.");
        editSplashes = config.getBoolean("editSplashes", categoryOther, editSplashes, "Adds to and edits the splash message list.");
        dragonEntityID = config.getInt("dragonEntityID", categoryOther, dragonEntityID, -1, 255, "Overrides the entity ID for dragons to fix problems with manual IDs from other mods.\nSet to -1 for automatic assignment (recommended).\nWarning: wrong values may cause crashes and loss of data!");
        eggsInChests = config.getBoolean("eggsInChests", categoryOther, eggsInChests, "Spawns dragon eggs in generated chests when enabled.");

        if (config.hasChanged()) {
            config.save();
        }
    }

}
