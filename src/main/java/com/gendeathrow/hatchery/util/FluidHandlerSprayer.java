package com.gendeathrow.hatchery.util;

import com.gendeathrow.hatchery.core.init.ModFluids;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class FluidHandlerSprayer extends FluidHandlerItemStack
{

	public FluidHandlerSprayer(ItemStack container, int capacity) 
	{
		super(container, capacity);
	}

	public boolean canFillFluidType(FluidStack fluid)
	{
		if(fluid.getFluid() == ModFluids.liquidfertilizer) 	return true;
		
		return false;
	}
}
