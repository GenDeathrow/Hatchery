package com.gendeathrow.hatchery.inventory;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.api.items.IUpgradeItem;
import com.gendeathrow.hatchery.block.TileUpgradable;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUpgrade extends Slot
{
	private final TileUpgradable tile;
	public SlotUpgrade(TileUpgradable tileIn, IInventory inventoryIn, int index,int xPosition, int yPosition) 
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.tile = tileIn;
	}

	public boolean isItemValid(@Nullable ItemStack stack)
    {
		if(stack == null) return false;
		
		if(stack.getItem() instanceof IUpgradeItem)
		{
			if(tile.canUseUpgrade(stack))
				return true;
		}
			
		
		return false;
    }


}