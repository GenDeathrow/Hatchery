package com.gendeathrow.hatchery.core;

import net.minecraft.block.Block;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.nestblock.NestBlock;
import com.gendeathrow.hatchery.block.nestpen.NestPenBlock;
import com.gendeathrow.hatchery.item.AnimalNet;
import com.gendeathrow.hatchery.item.ChickenManure;
import com.gendeathrow.hatchery.item.HatcheryEgg;

public class ModItems 
{
	//Blocks
	public static NestBlock nest = new NestBlock();
	
	
	public static Block pen = new NestPenBlock().setRegistryName(Hatchery.MODID, "pen").setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen_chicken = new NestPenBlock().setRegistryName(Hatchery.MODID, "pen_chicken");
	
	//public static ItemBlock nestItem = new ItemBlock(nest);
	
	
	//ITEMS
	public static HatcheryEgg hatcheryEgg = new HatcheryEgg();
	public static AnimalNet animalNet = new AnimalNet();
	public static ChickenManure manure = new ChickenManure();
	
	

}
