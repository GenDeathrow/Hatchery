package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.item.AnimalNet;
import com.gendeathrow.hatchery.item.ChickenManure;
import com.gendeathrow.hatchery.item.HatcheryEgg;
import com.gendeathrow.hatchery.item.RFEfficiencyUpgrade;
import com.gendeathrow.hatchery.item.Sprayer;

public class ModItems 
{
	
	public static final List<Item> ITEMS = new ArrayList<>();
	
	//ITEMS
	public static HatcheryEgg hatcheryEgg = new HatcheryEgg();
	public static AnimalNet animalNet = new AnimalNet();
	public static ChickenManure manure = new ChickenManure();
	public static Sprayer sprayer = new Sprayer();
	
	//Upgrades  
	// rf tier 1 +10rf/t
	public static RFEfficiencyUpgrade rfUpgradeTier1 = (RFEfficiencyUpgrade) new RFEfficiencyUpgrade(1).setMaxStackSize(16);
	// rf tier 2 +30rf/t
	public static RFEfficiencyUpgrade rfUpgradeTier2 = new RFEfficiencyUpgrade(2);
	// rf tier 3
	public static RFEfficiencyUpgrade rfUpgradeTier3 = new RFEfficiencyUpgrade(3);
	
	// 
	
	
	//public static FluidPump pump = new FluidPump();
	
	//public static CornSeed cornSeeds= new CornSeed(pen, pen);
//	  public static Corn corn = new Corn(1);

	public static void RegisterItems()
	{
		registerItem(ModItems.hatcheryEgg, "hatcheryegg");
		registerItem(ModItems.animalNet, "animalnet");
		registerItem(ModItems.manure, "chickenmanure");
		registerItem(ModItems.sprayer, "sprayer");
		
		registerItem(ModItems.rfUpgradeTier1, "upgrade_rf_tier1");
		
		//registerItem(ModItems.pump, "fluidpump");
		
	}
	
	
	private static void registerItem(Item item, String name)
	{
		item.setUnlocalizedName(Hatchery.MODID +"."+ name);
		GameRegistry.register(item.setRegistryName(name));
		ITEMS.add(item);
	}
	

}
