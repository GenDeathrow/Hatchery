package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineBlock;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineTileEntity;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineTopTileEntity;
import com.gendeathrow.hatchery.block.feeder.FeederBlock;
import com.gendeathrow.hatchery.block.feeder.FeederTileEntity;
import com.gendeathrow.hatchery.block.fertilizedDirt.FertilizedDirt;
import com.gendeathrow.hatchery.block.fertilizedDirt.FertilizedFarmland;
import com.gendeathrow.hatchery.block.fertilizermixer.FertilizerMixer;
import com.gendeathrow.hatchery.block.fertilizermixer.FertilizerMixerTileEntity;
import com.gendeathrow.hatchery.block.generator.DigesterGeneratorBlock;
import com.gendeathrow.hatchery.block.generator.DigesterGeneratorTileEntity;
import com.gendeathrow.hatchery.block.manure.ManureBlock;
import com.gendeathrow.hatchery.block.nest.EggNestBlock;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestingPenBlock;
import com.gendeathrow.hatchery.block.nursery.BlockMobNursery;
import com.gendeathrow.hatchery.block.nursery.TileEntityMobNursery;
import com.gendeathrow.hatchery.block.shredder.ShredderBlock;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	public static Block fertilizerMixer;
	public static Block digesterGenerator;
	public static Block digesterGeneratorOn;
	public static Block shredder;
	public static Block chickenMachine;
	
	public static void preInit(FMLPreInitializationEvent event) 
	{
		nest = new EggNestBlock();
		pen = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs);
		pen_chicken = new NestingPenBlock();
		feeder = new FeederBlock();
		fertlizedDirt = new FertilizedDirt().setCreativeTab(Hatchery.hatcheryTabs);
		fertilzedFarmland = new FertilizedFarmland();
		manureBlock = new ManureBlock();
		nuseryBlock = new BlockMobNursery();
		fertilizerMixer = new FertilizerMixer().setCreativeTab(Hatchery.hatcheryTabs);
		digesterGenerator = new DigesterGeneratorBlock(false).setCreativeTab(Hatchery.hatcheryTabs);
		digesterGeneratorOn = new DigesterGeneratorBlock(true).setTickRandomly(true);
		shredder = new ShredderBlock();
		chickenMachine = new EggMachineBlock(Material.GLASS);
		
		
		registerBlock(nest, "nest");
		registerBlock(pen, "pen");
		registerBlock(pen_chicken, "pen_chicken");
		registerBlock(feeder, "feeder");
		registerBlock(fertlizedDirt, "fertilized_dirt");
		registerBlock(fertilzedFarmland, "fertilized_farmland");
		registerBlock(manureBlock, "manure_block");
		registerBlock(nuseryBlock, "nursery_block");
		
		registerBlock(fertilizerMixer, "fertilizer_mixer");
		
		registerBlock(digesterGenerator, "digester_generator");
		registerBlock(digesterGeneratorOn, "digester_generator_on"); 
		  digesterGeneratorOn.setUnlocalizedName(Hatchery.MODID +".digester_generator");
		  
		registerBlock(shredder, "shredder");
		
		registerBlock(chickenMachine, "chicken_machine_block");
		  
		GameRegistry.registerTileEntity(TileEntityMobNursery.class, "hatchery.nursery");
		GameRegistry.registerTileEntity(ShredderTileEntity.class, "hatchery.shredder");
		GameRegistry.registerTileEntity(EggNestTileEntity.class, EggNestTileEntity.class.getName());
		GameRegistry.registerTileEntity(NestPenTileEntity.class, NestPenTileEntity.class.getName());
		GameRegistry.registerTileEntity(FeederTileEntity.class, FeederTileEntity.class.getName());
		GameRegistry.registerTileEntity(FertilizerMixerTileEntity.class, FertilizerMixerTileEntity.class.getName());
		
		GameRegistry.registerTileEntity(DigesterGeneratorTileEntity.class, "digesterGenerator");
		
		GameRegistry.registerTileEntity(EggMachineTileEntity.class, "hatchery.chicken_machine_block");
		GameRegistry.registerTileEntity(EggMachineTopTileEntity.class, "hatchery.chicken_machine_block_top");
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
