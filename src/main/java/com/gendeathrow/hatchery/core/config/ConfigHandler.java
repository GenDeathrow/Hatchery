package com.gendeathrow.hatchery.core.config;

import java.io.File;

import com.gendeathrow.hatchery.core.Settings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler 
{
	
	
	public static File dir = new File("config/hatchery");
	public static File configFile = new File(dir, "hatchery.cfg");
	public static Configuration CONFIG;
	
	public static final ConfigHandler INSTANCE = new ConfigHandler();
	//public Configuration CONFIG;


	public final String[] usedCategories = { "Rooster Spawn Settings" };

	public void loadConfig(FMLPreInitializationEvent event) 
	{
		CONFIG = new Configuration(configFile);
		CONFIG.load();
		syncConfigs();
		
		// Loots
		
		ConfigLootHandler.load();
	}

	private void syncConfigs() 
	{
		Settings.IS_EGG_BREEDING = CONFIG.getBoolean("Breeding gives Eggs", "Special AI", true, "Replaces Default Mating, Gives an egg that must be hatched to get baby");
		Settings.CAN_THROW_EGG = CONFIG.getBoolean("Can Throw Eggs", "mechanics", true, "Sets if players can throw eggs to hatch chickens.");
		Settings.EGG_NESTINGPEN_DROP_RATE = CONFIG.getInt("Hatchery Egg Drop Rate", "Drop Rates", 40, 0, 100, "Configure the drop rate  %  of Eggs in the Nesting Pen. Only affects single pens. If two pens placed next to each other 100% chance to drop");
		Settings.SHOULD_RENDER_CHICKEN_FLAPS = CONFIG.getBoolean("Render Chicken Flaps", Configuration.CATEGORY_CLIENT, true, "If you feel the chickens may be dropping your FPS when in chicken pens, Cause of all that darn flapping try this.");

		Settings.ROOSTER_BREED_ONLY = CONFIG.get("Rooster Spawn Settings", "Can only breed chickens with roosters", false, "Only set for Vanilla Chickens, Will likly break some modded chickens.\n Not compatable with Chickens mod. \n WIP may not work completly as inteded").getBoolean();

		Settings.ROOSTER_MIN_SPAWN_SIZE = CONFIG.get("Rooster Spawn Settings", "Rooster Spawn Group Minimum Size", 1).getInt(1);
		Settings.ROOSTER_MAX_SPAWN_SIZE = CONFIG.get("Rooster Spawn Settings", "Rooster Spawn Group Maximum Size", 2).getInt(2);
		Settings.ROOSTER_SPAWN_PROBABILITY = CONFIG.get("Rooster Spawn Settings", "Rooster Spawn Chance Probability", 10).getInt(10);

		if (CONFIG.hasChanged())
			CONFIG.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (event.getModID().equals("hatchery"))
			syncConfigs();
	}

	
	
	public static void loadConfig()
	{
			
	}

	public static void load() {
		// TODO Auto-generated method stub
		
	}

}
