package com.gendeathrow.hatchery.inventory;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.api.items.IUpgradeItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SlotUpgrade extends Slot
{
	public SlotUpgrade(IInventory inventoryIn, int index,int xPosition, int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);
	}

	public boolean isItemValid(@Nullable ItemStack stack)
    {
		if(stack == null) return false;
		
		if(stack.getItem() instanceof IUpgradeItem)
			return true;
		
		return false;
    }


}