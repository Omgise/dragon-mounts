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
    
    // config properties
    private boolean eggsInChests = false;
    private int dragonEntityID = -1;
    private boolean registerMeatToOreDictionary = true;
    private String dragonFavoriteFoodTag = "listAllfishraw";
    private String dragonAllFoodTag = "listAllmeatraw";
    
    private boolean debug = false;
    
    public DragonMountsConfig(Configuration config) {
        eggsInChests = config.getBoolean("eggsInChests", "server", eggsInChests, "Spawns dragon eggs in generated chests when enabled.");
        dragonEntityID = config.getInt("dragonEntityID", "server", dragonEntityID, -1, 255, "Overrides the entity ID for dragons to fix problems with manual IDs from other mods.\nSet to -1 for automatic assignment (recommended).\nWarning: wrong values may cause crashes and loss of data!");
        registerMeatToOreDictionary = config.getBoolean("registerMeatToOreDictionary", "server", registerMeatToOreDictionary, "Registers meats to the ore dictionary.\nUsesless if non-default values are assigned to dragonFavoriteFoodTag and dragonAllFoodTag");
        dragonFavoriteFoodTag = config.getString("dragonFavoriteFoodTag", "server", dragonFavoriteFoodTag, "Items with the specified Ore Dictionary tag are considered favorite foods.");
        dragonAllFoodTag = config.getString("dragonAllFoodTag", "server", dragonAllFoodTag, "Items with the specified Ore Dictionary tag are considered edible foods.");
        
        debug = config.getBoolean("debug", "client", debug, "Debug mode. Unless you're a developer or are told to activate it, you don't want to set this to true.");
        
        if (config.hasChanged()) {
            config.save();
        }
    }
    
    public boolean isEggsInChests() {
        return eggsInChests;
    }
    
    public int getDragonEntityID() {
        return dragonEntityID;
    }
    
    public String getDragonFavoriteFoodTag() {
    	return dragonFavoriteFoodTag;
    }
    
    public String getDragonAllFoodTag() {
    	return dragonAllFoodTag;
    }

    public boolean getRegisterMeatToOreDictionary() {
    	return registerMeatToOreDictionary;
    }

    public boolean isDebug() {
        return debug;
    }
}
