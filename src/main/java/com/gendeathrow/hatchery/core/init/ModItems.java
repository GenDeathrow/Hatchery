package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.item.AnimalNet;
import com.gendeathrow.hatchery.item.ChickenManure;
import com.gendeathrow.hatchery.item.FluidPump;
import com.gendeathrow.hatchery.item.HatcheryEgg;

public class ModItems 
{
	
	public static final List<Item> ITEMS = new ArrayList<>();
	
	//ITEMS
	public static HatcheryEgg hatcheryEgg = new HatcheryEgg();
	public static AnimalNet animalNet = new AnimalNet();
	public static ChickenManure manure = new ChickenManure();
	
	//public static FluidPump pump = new FluidPump();
	
	//public static CornSeed cornSeeds= new CornSeed(pen, pen);
//	  public static Corn corn = new Corn(1);

	public static void RegisterItems()
	{
		registerItem(ModItems.hatcheryEgg, "hatcheryegg");
		registerItem(ModItems.animalNet, "animalnet");
		registerItem(ModItems.manure, "chickenmanure");
		//registerItem(ModItems.pump, "fluidpump");
		
	}
	
	
	private static void registerItem(Item item, String name)
	{
		item.setUnlocalizedName(Hatchery.MODID +"."+ name);
		GameRegistry.register(item.setRegistryName(name));
		ITEMS.add(item);
	}
	

}
