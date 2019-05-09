package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineBlock;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineTileEntity;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineTopTileEntity;
import com.gendeathrow.hatchery.block.energy.battery.BaseBatteryBlock;
import com.gendeathrow.hatchery.block.energy.battery.BaseBatteryTileEntity;
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
import com.gendeathrow.hatchery.item.IVariantsItems;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSaddle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;


@EventBusSubscriber
public class ModBlocks 
{
	
	public static final List<Block> BLOCKS = new ArrayList<>();

	public static Block nest = new EggNestBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "nest")).setTranslationKey(Hatchery.MODID + ".nest" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen")).setTranslationKey(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
	/*
	public static Block pen_acacia = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen_acacia")).setTranslationKey(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen_birch = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen_birch")).setTranslationKey(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen_jungle = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen_jungle")).setTranslationKey(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen_spruce = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen_spruce")).setTranslationKey(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block pen_big_oak = new NestingPenBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "pen_big_oak")).setTranslationKey(Hatchery.MODID + ".pen" ).setCreativeTab(Hatchery.hatcheryTabs);
		*/
	public static Block feeder = new FeederBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "feeder")).setTranslationKey(Hatchery.MODID + ".feeder" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block fertlizedDirt = new FertilizedDirt().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "fertilized_dirt")).setTranslationKey(Hatchery.MODID + ".fertilized_dirt" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block fertilzedFarmland = new FertilizedFarmland().setRegistryName(new ResourceLocation(Hatchery.MODID, "fertilized_farmland")).setTranslationKey(Hatchery.MODID + ".fertilized_farmland" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block manureBlock = new ManureBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "manure_block")).setTranslationKey(Hatchery.MODID + ".manure_block" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block nuseryBlock = new BlockMobNursery().setRegistryName(new ResourceLocation(Hatchery.MODID, "nursery_block")).setTranslationKey(Hatchery.MODID + ".nursery_block" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block fertilizerMixer = new FertilizerMixer().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "fertilizer_mixer")).setTranslationKey(Hatchery.MODID + ".fertilizer_mixer" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block digesterGenerator = new DigesterGeneratorBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "digester_generator")).setTranslationKey(Hatchery.MODID + ".digester_generator" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block batterybank = new BaseBatteryBlock().setCreativeTab(Hatchery.hatcheryTabs).setRegistryName(new ResourceLocation(Hatchery.MODID, "battery_bank")).setTranslationKey(Hatchery.MODID + ".battery_bank" );
	
	//public static Block digesterGeneratorOn = new DigesterGeneratorBlock(true).setTickRandomly(true).setRegistryName(new ResourceLocation(Hatchery.MODID, "digester_generator_on")).setUnlocalizedName(Hatchery.MODID + ".digester_generator_on" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block shredder = new ShredderBlock().setRegistryName(new ResourceLocation(Hatchery.MODID, "shredder")).setTranslationKey(Hatchery.MODID + ".shredder" ).setCreativeTab(Hatchery.hatcheryTabs);
	public static Block chickenMachine = new EggMachineBlock(Material.GLASS).setRegistryName(new ResourceLocation(Hatchery.MODID, "chicken_machine_block")).setTranslationKey(Hatchery.MODID + ".chicken_machine_block" ).setCreativeTab(Hatchery.hatcheryTabs);
	
	public static IForgeRegistry<Block> blockRegistry;
	
	 
	@SubscribeEvent
	public static void blockRegistry(RegistryEvent.Register<Block> event) {
		blockRegistry = event.getRegistry();

		registerAllBlock(nest,
				pen,
				/*
				pen_acacia,
				pen_birch,
				pen_jungle,
				pen_spruce,
				pen_big_oak,
				*/
				feeder, 
				fertlizedDirt, 
				fertilzedFarmland, 
				manureBlock,
				nuseryBlock,
				fertilizerMixer,
				digesterGenerator,
				shredder,
				chickenMachine,
				batterybank);
		
	}
	
	private static void registerAllBlock(Block...blocks) {
		for(Block block : blocks) {
			BLOCKS.add(block);
			blockRegistry.register(block);
		}
	}
	//{ 
//	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		//ModBlocks.pen.getSubBlocks(Hatchery.hatcheryTabs, items);
	//}
//}
    public static void registerItems(RegistryEvent.Register<Item> event) {
    	
    	ModItems.registerAllItems( 
    			new ItemBlock(ModBlocks.nest).setRegistryName(ModBlocks.nest.getRegistryName()),
    			new ItemBlock(ModBlocks.pen).setRegistryName(ModBlocks.pen.getRegistryName()),
    			/*
    			new ItemBlock(ModBlocks.pen_acacia).setRegistryName(ModBlocks.pen.getRegistryName()+"_acacia"),
    			new ItemBlock(ModBlocks.pen_birch).setRegistryName(ModBlocks.pen.getRegistryName()+" _birch"),
    			new ItemBlock(ModBlocks.pen_jungle).setRegistryName(ModBlocks.pen.getRegistryName()+" jungle"),
    			new ItemBlock(ModBlocks.pen_spruce).setRegistryName(ModBlocks.pen.getRegistryName()+" _spruce"),
    			new ItemBlock(ModBlocks.pen_big_oak).setRegistryName(ModBlocks.pen.getRegistryName()+"_big_oak"),
    			*/
	   			new ItemBlock(ModBlocks.feeder).setRegistryName(ModBlocks.feeder.getRegistryName()),
    			new ItemBlock(ModBlocks.fertlizedDirt).setRegistryName(ModBlocks.fertlizedDirt.getRegistryName()),
    			new ItemBlock(ModBlocks.fertilzedFarmland).setRegistryName(ModBlocks.fertilzedFarmland.getRegistryName()),
    			new ItemBlock(ModBlocks.manureBlock).setRegistryName(ModBlocks.manureBlock.getRegistryName()),
    			new ItemBlock(ModBlocks.nuseryBlock).setRegistryName(ModBlocks.nuseryBlock.getRegistryName()),
    			new ItemBlock(ModBlocks.fertilizerMixer).setRegistryName(ModBlocks.fertilizerMixer.getRegistryName()),
    			new ItemBlock(ModBlocks.digesterGenerator).setRegistryName(ModBlocks.digesterGenerator.getRegistryName()),
    			new ItemBlock(ModBlocks.shredder).setRegistryName(ModBlocks.shredder.getRegistryName()),
    			new ItemBlock(ModBlocks.batterybank).setRegistryName(ModBlocks.batterybank.getRegistryName()));

    }
	
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	
		registerModel(nest);
		registerModel(pen);
		registerModel(pen);
		/*
		registerModel(pen_acacia);
		registerModel(pen_birch);
		registerModel(pen_jungle);
		registerModel(pen_spruce);
		registerModel(pen_big_oak);
		*/
		registerModel(feeder);
		registerModel(fertlizedDirt);
		registerModel(fertilzedFarmland);
		registerModel(manureBlock);
		registerModel(nuseryBlock);
		registerModel(fertilizerMixer);
		registerModel(digesterGenerator);
		registerModel(shredder);
		registerModel(chickenMachine);
		registerModel(batterybank);
		
	}

	private static void registerModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
	
	private static void registerModelWithVarriants(Block block, String variantName, String... constances) {
		int i=0;
		for(String contance : constances) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i++, new ModelResourceLocation(block.getRegistryName(), variantName+"="+ contance));
		}
	}


	
	public static void preInit(FMLPreInitializationEvent event) 
	{
		GameRegistry.registerTileEntity(EggNestTileEntity.class, "hatchery.eggnest");
		GameRegistry.registerTileEntity(TileEntityMobNursery.class, "hatchery.nursery");
		GameRegistry.registerTileEntity(ShredderTileEntity.class, "hatchery.shredder");
		GameRegistry.registerTileEntity(NestPenTileEntity.class, "hatchery.nestingpen");
		GameRegistry.registerTileEntity(FeederTileEntity.class, "hatchery.feeder");
		GameRegistry.registerTileEntity(FertilizerMixerTileEntity.class, "hatchery.mixer");
		GameRegistry.registerTileEntity(DigesterGeneratorTileEntity.class, "hatchery.digesterGenerator");
		GameRegistry.registerTileEntity(EggMachineTileEntity.class, "hatchery.chicken_machine_block");
		GameRegistry.registerTileEntity(EggMachineTopTileEntity.class, "hatchery.chicken_machine_block_top");
		GameRegistry.registerTileEntity(BaseBatteryTileEntity.class, "hatchery.battery_bank");
	}
}
