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

	private static final String categoryServer = "server";
	private static final String categoryClient = "client";

    // config properties
    public static boolean eggsInChests = false;
    public static int dragonEntityID = -1;
    public static boolean registerMeatToOreDictionary = true;
    public static String dragonFavoriteFoodTag = "listAllfishraw";
    public static String dragonAllFoodTag = "listAllmeatraw";

    public static boolean debug = false;

    public static void LoadDragonMountsConfig(Configuration config) {
        eggsInChests = config.getBoolean("eggsInChests", categoryServer, eggsInChests, "Spawns dragon eggs in generated chests when enabled.");
        dragonEntityID = config.getInt("dragonEntityID", categoryServer, dragonEntityID, -1, 255, "Overrides the entity ID for dragons to fix problems with manual IDs from other mods.\nSet to -1 for automatic assignment (recommended).\nWarning: wrong values may cause crashes and loss of data!");
        registerMeatToOreDictionary = config.getBoolean("registerMeatToOreDictionary", categoryServer, registerMeatToOreDictionary, "Registers meats to the ore dictionary.\nUsesless if non-default values are assigned to dragonFavoriteFoodTag and dragonAllFoodTag");
        dragonFavoriteFoodTag = config.getString("dragonFavoriteFoodTag", categoryServer, dragonFavoriteFoodTag, "Items with the specified Ore Dictionary tag are considered favorite foods.");
        dragonAllFoodTag = config.getString("dragonAllFoodTag", categoryServer, dragonAllFoodTag, "Items with the specified Ore Dictionary tag are considered edible foods.");

        debug = config.getBoolean("debug", categoryClient, debug, "Debug mode. Unless you're a developer or are told to activate it, you don't want to set this to true.");
        
        if (config.hasChanged()) {
            config.save();
        }
    }

}
