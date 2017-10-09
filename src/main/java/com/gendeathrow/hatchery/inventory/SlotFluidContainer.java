package com.gendeathrow.hatchery.inventory;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotFluidContainer extends SlotItemHandler
{
	Fluid fluid;
	public SlotFluidContainer(IItemHandler inventory, int index,int xPosition, int yPosition, Fluid fluid) 
	{
		super(inventory, index, xPosition, yPosition);
		this.fluid = fluid;
	}

	public boolean isItemValid(@Nullable ItemStack stack)
    {
		if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
		{
			IFluidHandler fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
			
			if(fluidHandler == null) return false;
			
			if(FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == this.fluid)
				return true;
		}
		
		return false;
    }


}