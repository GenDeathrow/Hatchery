package com.gendeathrow.hatchery.fluid;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class LiquidFertilizer extends Fluid
{

	public LiquidFertilizer() 
	{
		//super("liquid_fertilizer", new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"));

		super("liquid_fertilizer", new ResourceLocation(Hatchery.MODID,"blocks/fertilizer_still"), new ResourceLocation(Hatchery.MODID,"blocks/fertilizer_flow"));
		setUnlocalizedName("liquid_fertilizer");
	}



//	@Override
//	public int getColor()
//	{
//		return 0x5b3104;
//	}
}
