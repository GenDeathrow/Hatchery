package com.gendeathrow.hatchery.core.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

import com.gendeathrow.hatchery.core.Settings;

public class ConfigHandler 
{
	public static File dir = new File("config/hatchery");
	public static File configFile = new File(dir, "hatchery.cfg");
	public static Configuration config;
	
	
	public static void loadConfig()
	{
		config = new Configuration(configFile);
		
		config.load();  
		
			Settings.eggBreeding = config.getBoolean("Breeding gives Eggs", "Special AI", true, "Replaces Default Mating, Gives an egg that must be hatched to get baby");
			
			Settings.canThrowEgg = config.getBoolean("Can Throw Eggs", "mechanics", true, "Sets if players can throw eggs to hatch chickens.");
			
			Settings.eggNestDropRate = config.getInt("Hatchery Egg Drop Rate", "Drop Rates", 40, 0, 100, "Configure the drop rate  %  of Eggs in the Nesting Pen. ");
			
			Settings.renderChickenFlaps = config.getBoolean("Render Chicken Flaps", Configuration.CATEGORY_CLIENT, true, "If you feel the chickens may be dropping your FPS when in chicken pens, Cause of all that darn flapping try this.");
			
		config.save();
	}

}
