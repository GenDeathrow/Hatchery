package com.gendeathrow.hatchery.core.init;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.fluid.BlockLiquidFertilizer;
import com.gendeathrow.hatchery.fluid.LiquidFertilizer;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class ModFluids 
{

	public static Fluid liquidfertilizer = new LiquidFertilizer();
	public static Block blockLiquidFertilizer;
		
	
	public static void registerFluids()
	{
		FluidRegistry.registerFluid(liquidfertilizer);
			blockLiquidFertilizer = new BlockLiquidFertilizer(liquidfertilizer);
			blockLiquidFertilizer.setCreativeTab(Hatchery.hatcheryTabs);
			liquidfertilizer.setBlock(blockLiquidFertilizer);
		GameRegistry.register(blockLiquidFertilizer);
		
		ItemBlock itemLiquidFertilizer = new ItemBlock(blockLiquidFertilizer);
		GameRegistry.register(itemLiquidFertilizer.setRegistryName(blockLiquidFertilizer.getRegistryName()));

		FluidRegistry.addBucketForFluid(liquidfertilizer);
		
	//	FluidRegistry.
	}
	
	
	public static ItemStack getFertilizerBucket()
	{
		return UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, liquidfertilizer);
	}

	
}
