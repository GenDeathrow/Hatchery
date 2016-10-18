package com.gendeathrow.hatchery.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import com.gendeathrow.hatchery.Hatchery;

public class Sprayer extends Item implements IFluidHandler, ICapabilityProvider
{

	
	public Sprayer()
	{
		this.setCreativeTab(Hatchery.hatcheryTabs);
		this.setMaxStackSize(1);
	}

	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return null;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties() 
	{
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) 
	{
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		return null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
        {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        return null;
	}

	
	
	
}
