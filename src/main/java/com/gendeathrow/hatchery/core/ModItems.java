package com.gendeathrow.hatchery.core;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.corn.CornPlant;
import com.gendeathrow.hatchery.block.fertilizedDirt.FertilizedDirt;
import com.gendeathrow.hatchery.block.fertilizedDirt.FertilizedFarmland;
import com.gendeathrow.hatchery.block.nestblock.NestBlock;
import com.gendeathrow.hatchery.block.nestblock.NestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenBlock;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.item.AnimalNet;
import com.gendeathrow.hatchery.item.ChickenManure;
import com.gendeathrow.hatchery.item.Corn;
import com.gendeathrow.hatchery.item.CornSeed;
import com.gendeathrow.hatchery.item.HatcheryEgg;

public class ModItems 
{
	//Blocks
	public static NestBlock nest = new NestBlock();
	
	public static Block pen = new NestPenBlock().setRegistryName(Hatchery.MODID, "pen").setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen_chicken = new NestPenBlock().setRegistryName(Hatchery.MODID, "pen_chicken");
	
	public static CornPlant CornPlant = new CornPlant();
	
	public static Block fertlizedDirt = new FertilizedDirt().setRegistryName(Hatchery.MODID, "fertilized_dirt").setCreativeTab(Hatchery.hatcheryTabs); 
	public static Block fertilzedFarmland = new FertilizedFarmland().setRegistryName(Hatchery.MODID, "fertilized_farmland");
	
	//public static ItemBlock nestItem = new ItemBlock(nest);
	
	
	//ITEMS
	public static HatcheryEgg hatcheryEgg = new HatcheryEgg();
	public static AnimalNet animalNet = new AnimalNet();
	public static ChickenManure manure = new ChickenManure();
	public static CornSeed cornSeeds= new CornSeed(pen, pen);
	public static Corn corn = new Corn(1);
	
	

	
	
	public static void RegisterBlocks()
	{
		//NestBlock
    	GameRegistry.register(ModItems.nest);
		ItemBlock modItem = new ItemBlock(ModItems.nest);
		GameRegistry.register(modItem.setRegistryName(ModItems.nest.getRegistryName()));
	
		//Nesting Pen
		GameRegistry.register(ModItems.pen);
		ItemBlock penItem = new ItemBlock(ModItems.pen);
		GameRegistry.register(penItem.setRegistryName(ModItems.pen.getRegistryName()));

		//Nesting Pen with Chicken
		GameRegistry.register(ModItems.pen_chicken);
			ItemBlock penItem2 = new ItemBlock(ModItems.pen_chicken);
			GameRegistry.register(penItem2.setRegistryName(ModItems.pen_chicken.getRegistryName()));
		
		//Dirt fertilized
		GameRegistry.register(ModItems.fertlizedDirt);
			ItemBlock fertlizedDirt = new ItemBlock(ModItems.fertlizedDirt);
			GameRegistry.register(fertlizedDirt.setRegistryName(ModItems.fertlizedDirt.getRegistryName()));
			
		//farmland fertilized
		GameRegistry.register(ModItems.fertilzedFarmland);
			ItemBlock fertlizedfarm = new ItemBlock(ModItems.fertilzedFarmland);
			GameRegistry.register(fertlizedfarm.setRegistryName(ModItems.fertilzedFarmland.getRegistryName()));
	}
	
	public static void RegisterTileEntitys()
	{
		GameRegistry.registerTileEntity(NestTileEntity.class, NestTileEntity.class.getName());
		GameRegistry.registerTileEntity(NestPenTileEntity.class, NestPenTileEntity.class.getName());
	}
	
	public static void RegisterItems()
	{
		GameRegistry.register(ModItems.hatcheryEgg);
		GameRegistry.register(ModItems.animalNet);
		GameRegistry.register(ModItems.manure);		
	}
	
	
	public static void RegisterModels()
	{
		
	}
	
	public static void RegisterRecipes()
	{

		GameRegistry.addRecipe(new ItemStack(ModItems.nest), "xxx", "AxA", "AAA",'A', Blocks.HAY_BLOCK);
		GameRegistry.addRecipe(new ItemStack(ModItems.animalNet), "xSS", "SAA", "xxA",'S', Items.STICK, 'A', Items.STRING);
	 	GameRegistry.addRecipe(new ItemStack(ModItems.pen), "WxW", "WNW", "WWW",'W', Blocks.PLANKS, 'N', ModItems.nest);
	 	GameRegistry.addRecipe(new ItemStack(ModItems.fertlizedDirt), "PPP", "PDP", "PPP",'P', ModItems.manure, 'D', Blocks.DIRT);
	}
}
