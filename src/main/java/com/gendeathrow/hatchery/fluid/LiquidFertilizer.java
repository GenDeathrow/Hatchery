package com.gendeathrow.hatchery.fluid;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class LiquidFertilizer extends Fluid
{

	public LiquidFertilizer() 
	{
		super("liquid_fertilizer", new ResourceLocation(Hatchery.MODID,"blocks/fertilizer_still"), new ResourceLocation(Hatchery.MODID,"blocks/fertilizer_flow"));
		setUnlocalizedName("liquid_fertilizer");
	}

}
