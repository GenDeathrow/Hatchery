package com.gendeathrow.hatchery.storage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryStroageModifiable extends ItemStackHandler
{
	public String ID = "Items"; 
	
    public InventoryStroageModifiable()
    {
        this(1);
    }
    public InventoryStroageModifiable(String ID, int size)
    {
    	this(size);
    	this.ID = ID;
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
    		return stack;
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
		
	public void readFromNBT(NBTTagCompound nbt) {

		NBTTagCompound tags = nbt.getCompoundTag(this.ID);
		this.deserializeNBT(tags);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagCompound tags = serializeNBT();
			nbt.setTag(this.ID, tags);
		return nbt;
	}
}
