package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.feeder.FeederBlock;
import com.gendeathrow.hatchery.block.feeder.FeederTileEntity;
import com.gendeathrow.hatchery.block.fertilizedDirt.FertilizedDirt;
import com.gendeathrow.hatchery.block.fertilizedDirt.FertilizedFarmland;
import com.gendeathrow.hatchery.block.manure.ManureBlock;
import com.gendeathrow.hatchery.block.nestblock.NestBlock;
import com.gendeathrow.hatchery.block.nestblock.NestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenBlock;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.block.nursery.BlockMobNursery;
import com.gendeathrow.hatchery.block.nursery.TileEntityMobNursery;

public class ModBlocks 
{
	
	public static final List<Block> BLOCKS = new ArrayList<>();

	public static Block nest;
	public static Block pen;
	public static Block pen_chicken;
	public static Block feeder;
	public static Block fertlizedDirt;
	public static Block fertilzedFarmland;
	public static Block manureBlock;
	public static Block nuseryBlock;
	
	
	public static void preInit(FMLPreInitializationEvent event) 
	{
		nest = new NestBlock();
		pen = new NestPenBlock().setCreativeTab(Hatchery.hatcheryTabs);
		pen_chicken = new NestPenBlock();
		feeder = new FeederBlock();
		fertlizedDirt = new FertilizedDirt().setCreativeTab(Hatchery.hatcheryTabs);
		fertilzedFarmland = new FertilizedFarmland();
		manureBlock = new ManureBlock();
		nuseryBlock = new BlockMobNursery();
		
		registerBlock(nest, "nest");
		registerBlock(pen, "pen");
		registerBlock(pen_chicken, "pen_chicken");
		registerBlock(feeder, "feeder");
		registerBlock(fertlizedDirt, "fertilized_dirt");
		registerBlock(fertilzedFarmland, "fertilized_farmland");
		registerBlock(manureBlock, "manure_block");
		registerBlock(nuseryBlock, "nursery_block");
		  
		GameRegistry.registerTileEntity(TileEntityMobNursery.class, "hatchery.nursery");
		GameRegistry.registerTileEntity(NestTileEntity.class, NestTileEntity.class.getName());
		GameRegistry.registerTileEntity(NestPenTileEntity.class, NestPenTileEntity.class.getName());
		GameRegistry.registerTileEntity(FeederTileEntity.class, FeederTileEntity.class.getName());
		
	}
	
	private static void registerItem(Item item, String name)
	{
		GameRegistry.register(item.setRegistryName(new ResourceLocation(Hatchery.MODID, name)));
	}

	private static void registerBlock(Block block, String name)
	{
		registerBlock(block, new ItemBlock(block), name);
	}

	private static void registerBlock(Block block, ItemBlock item, String name)
	{
		block.setUnlocalizedName(Hatchery.MODID +"."+ name);
		GameRegistry.register(block.setRegistryName(new ResourceLocation(Hatchery.MODID, name)));
		registerItem(item, name);
		BLOCKS.add(block);
	}
	
}
