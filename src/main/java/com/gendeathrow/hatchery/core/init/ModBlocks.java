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
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@EventBusSubscriber
public class ModBlocks 
{
	
	public static final List<Block> BLOCKS = new ArrayList<>();

	public static Block nest = new EggNestBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "nest")).setUnlocalizedName(Hatchery.MODID + ".nest" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen")).setUnlocalizedName(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block feeder = new FeederBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "feeder")).setUnlocalizedName(Hatchery.MODID + ".feeder" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block fertlizedDirt = new FertilizedDirt().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "fertilized_dirt")).setUnlocalizedName(Hatchery.MODID + ".fertilized_dirt" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block fertilzedFarmland = new FertilizedFarmland().setRegistryName(new ResourceLocation(Hatchery.MODID, "fertilized_farmland")).setUnlocalizedName(Hatchery.MODID + ".fertilized_farmland" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block manureBlock = new ManureBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "manure_block")).setUnlocalizedName(Hatchery.MODID + ".manure_block" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block nuseryBlock = new BlockMobNursery().setRegistryName(new ResourceLocation(Hatchery.MODID, "nursery_block")).setUnlocalizedName(Hatchery.MODID + ".nursery_block" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block fertilizerMixer = new FertilizerMixer().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "fertilizer_mixer")).setUnlocalizedName(Hatchery.MODID + ".fertilizer_mixer" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block digesterGenerator = new DigesterGeneratorBlock(false).setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "digester_generator")).setUnlocalizedName(Hatchery.MODID + ".digester_generator" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block digesterGeneratorOn = new DigesterGeneratorBlock(true).setTickRandomly(true).setRegistryName(new ResourceLocation(Hatchery.MODID, "digester_generator_on")).setUnlocalizedName(Hatchery.MODID + ".digester_generator_on" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block shredder = new ShredderBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "shredder")).setUnlocalizedName(Hatchery.MODID + ".shredder" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block chickenMachine = new EggMachineBlock(Material.GLASS).setRegistryName(new ResourceLocation(Hatchery.MODID, "chicken_machine_block")).setUnlocalizedName(Hatchery.MODID + ".chicken_machine_block" ).setCreativeTab(Hatchery.hatcheryTabs);

	public static IForgeRegistry<Block> blockRegistry;
	
	
	@SubscribeEvent
	public static void blockRegistry(RegistryEvent.Register<Block> event) {
		blockRegistry = event.getRegistry();

		registerAllBlock(nest,
				pen, 
				feeder, 
				fertlizedDirt, 
				fertilzedFarmland, 
				manureBlock,
				nuseryBlock,
				fertilizerMixer,
				digesterGenerator,
				digesterGeneratorOn,
				shredder,
				chickenMachine);
		
	}
	
	private static void registerAllBlock(Block...blocks) {
		for(Block block : blocks) {
			BLOCKS.add(block);
			blockRegistry.register(block);
		}
	}
	
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	
    	ModItems.registerAllItems( 
    			new ItemBlock(ModBlocks.nest).setRegistryName(ModBlocks.nest.getRegistryName()),
    			new ItemBlock(ModBlocks.pen).setRegistryName(ModBlocks.pen.getRegistryName()),
    			new ItemBlock(ModBlocks.feeder).setRegistryName(ModBlocks.feeder.getRegistryName()),
    			new ItemBlock(ModBlocks.fertlizedDirt).setRegistryName(ModBlocks.fertlizedDirt.getRegistryName()),
    			new ItemBlock(ModBlocks.fertilzedFarmland).setRegistryName(ModBlocks.fertilzedFarmland.getRegistryName()),
    			new ItemBlock(ModBlocks.manureBlock).setRegistryName(ModBlocks.manureBlock.getRegistryName()),
    			new ItemBlock(ModBlocks.nuseryBlock).setRegistryName(ModBlocks.nuseryBlock.getRegistryName()),
    			new ItemBlock(ModBlocks.fertilizerMixer).setRegistryName(ModBlocks.fertilizerMixer.getRegistryName()),
    			new ItemBlock(ModBlocks.digesterGenerator).setRegistryName(ModBlocks.digesterGenerator.getRegistryName()),
    			new ItemBlock(ModBlocks.shredder).setRegistryName(ModBlocks.shredder.getRegistryName())	);

    }
	
	public static void registerRenderer() {
		
		registerModel(nest);
		registerModel(pen);
		registerModel(feeder);
		registerModel(fertlizedDirt);
		registerModel(fertilzedFarmland);
		registerModel(manureBlock);
		registerModel(nuseryBlock);
		registerModel(fertilizerMixer);
		registerModel(digesterGenerator);
		registerModel(shredder);
		registerModel(chickenMachine);
		
	}

	private static void registerModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	


	
	public static void preInit(FMLPreInitializationEvent event) 
	{
		GameRegistry.registerTileEntity(TileEntityMobNursery.class, "hatchery.nursery");
		GameRegistry.registerTileEntity(ShredderTileEntity.class, "hatchery.shredder");
		GameRegistry.registerTileEntity(EggNestTileEntity.class, "hatchery.eggnest");
		GameRegistry.registerTileEntity(NestPenTileEntity.class, "hatchery.nestingpen");
		GameRegistry.registerTileEntity(FeederTileEntity.class, "hatchery.feeder");
		GameRegistry.registerTileEntity(FertilizerMixerTileEntity.class, "hatchery.mixer");
		GameRegistry.registerTileEntity(DigesterGeneratorTileEntity.class, "hatchery.digesterGenerator");
		GameRegistry.registerTileEntity(EggMachineTileEntity.class, "hatchery.chicken_machine_block");
		GameRegistry.registerTileEntity(EggMachineTopTileEntity.class, "hatchery.chicken_machine_block_top");
	}
}
