package com.gendeathrow.hatchery.storage;

import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryStroageModifiable extends ItemStackHandler
{
	
    public InventoryStroageModifiable()
    {
        this(1);
    }

    public InventoryStroageModifiable(int size)
    {
        super(size);
    }
    
	@Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
    	if(canInsertSlot(slot, stack))
    		return super.insertItem(slot, stack, simulate);
    	else
    		return null;
    }
	
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
    	if(canExtractSlot(slot))
    		return super.extractItem(slot, amount, simulate);
    	else
    		return null;	
    }
    
    
    public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate)
    {
    	return super.insertItem(slot, stack, simulate);
    }
    
    public ItemStack extractItemInternal(int slot, int amount, boolean simulate)
    {
    	return super.extractItem(slot, amount, simulate);
    }
    
	public boolean canExtractSlot(int slot)
	{
		return true;
	}
	
	public boolean canInsertSlot(int slot, ItemStack stack)
	{
		return true;
	}
}
