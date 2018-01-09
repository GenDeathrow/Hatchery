package com.gendeathrow.hatchery.core.init;

import com.gendeathrow.hatchery.fluid.BlockLiquidFertilizer;
import com.gendeathrow.hatchery.fluid.LiquidFertilizer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@EventBusSubscriber
public class ModFluids 
{

	public static Fluid liquidfertilizer = new LiquidFertilizer();
	
	static {
		FluidRegistry.addBucketForFluid(liquidfertilizer);
	}
	
	public static Block blockLiquidFertilizer = new BlockLiquidFertilizer(liquidfertilizer);;
		
	public static ItemStack getFertilizerBucket()
	{
		return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, liquidfertilizer);
	}

	@SubscribeEvent
	public static void blockRegistry(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(blockLiquidFertilizer);
	}
	
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
    	//ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(blockLiquidFertilizer), 0, new ModelResourceLocation(blockLiquidFertilizer.getRegistryName(), null));
    }
	
}
