package com.gendeathrow.hatchery.core.init;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.fluid.BlockLiquidFertilizer;
import com.gendeathrow.hatchery.fluid.LiquidFertilizer;


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
