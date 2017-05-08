package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.item.AnimalNet;
import com.gendeathrow.hatchery.item.ChickenManure;
import com.gendeathrow.hatchery.item.HatcheryEgg;
import com.gendeathrow.hatchery.item.Sprayer;
import com.gendeathrow.hatchery.item.upgrades.RFEfficiencyUpgrade;

public class ModItems 
{
	
	public static final List<Item> ITEMS = new ArrayList<>();
	
	//ITEMS
	public static HatcheryEgg hatcheryEgg = new HatcheryEgg();
	public static AnimalNet animalNet = new AnimalNet();
	public static ChickenManure manure = new ChickenManure();
	public static Sprayer sprayer = new Sprayer();
	
	public static Item featherMeal = new Item().setUnlocalizedName("feather_meal");
	public static Item plastic = new Item().setUnlocalizedName("plastic");
	public static Item featherFiber = new Item().setUnlocalizedName("feather_fiber");
	
	
	//Upgrades  
	
	// RF power converter (Transformer Upgrade)
	// rf tier 1 +10rf/t
	public static RFEfficiencyUpgrade rfUpgradeTier1 = (RFEfficiencyUpgrade) new RFEfficiencyUpgrade(1);
	// rf tier 2 +30rf/t
	public static RFEfficiencyUpgrade rfUpgradeTier2 = new RFEfficiencyUpgrade(2);
	// rf tier 3
	public static RFEfficiencyUpgrade rfUpgradeTier3 = new RFEfficiencyUpgrade(3);
	
	// speed upgrade (faster / increases power comsumption)
	
	// insulation (decrease sound)
	
	// RF Efficiency upgrade (decrease power usage)  1-0.75, 2-0.65, 3-0.5
	
	// Capacity upgrades (increase RF, Tank Capacitys) RF 100,000 x tier, fluid (1-x2, 2-x3, 2-x4)
	
	// Feeder upgrade (auto feeds chickens much like a feeder) requires seeds to be added.  
	
	
	//public static FluidPump pump = new FluidPump();
	

	public static void RegisterItems()
	{
		registerItem(ModItems.hatcheryEgg, "hatcheryegg");
		registerItem(ModItems.animalNet, "animalnet");
		registerItem(ModItems.manure, "chickenmanure");
		registerItem(ModItems.sprayer, "sprayer");
		
		registerItem(ModItems.featherMeal, "feather_meal");
		registerItem(ModItems.plastic, "plastic");
		registerItem(ModItems.featherFiber, "feather_fiber");
		
		
		registerItem(ModItems.rfUpgradeTier1, "rf_upgrade_1");
		registerItem(ModItems.rfUpgradeTier2, "rf_upgrade_2");
		registerItem(ModItems.rfUpgradeTier3, "rf_upgrade_3");
		
		//registerItem(ModItems.pump, "fluidpump");
		
	}
	
	
	public static void RegisterModels()
	{
		
	}
	
	private static void registerItem(Item item, String name)
	{
		item.setUnlocalizedName(Hatchery.MODID +"."+ name);
		GameRegistry.register(item.setRegistryName(name));
		ITEMS.add(item);
	}
	

}
