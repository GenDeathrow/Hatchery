package com.gendeathrow.hatchery.storage;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	
	public ItemStack insertItemFirstAvaliableSlot(ItemStack stack, boolean simulate) {
		
		for(int i = 0; i < this.getSlots(); i++) {
			
			if(insertItem(i, stack, true) != stack) 
				return insertItem(i, stack, false);
		}
		
		return stack;
	}
	
	public ItemStack getAndRemoveSlot(int slot)	{
		ItemStack extract = this.getStackInSlot(slot).copy();
		
		if(!extract.isEmpty())
			this.setStackInSlot(slot, ItemStack.EMPTY);
		
		return extract;
	}
	
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
    	if(canExtractSlot(slot))
    		return super.extractItem(slot, amount, simulate);
    	else
    		return ItemStack.EMPTY;	
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
	
	public void dropInventory(World world, BlockPos pos) {
        for (int i = 0; i < this.getSlots(); ++i)
        {
        	ItemStack stack = getAndRemoveSlot(i);
        	
        	if(!stack.isEmpty()){
        		world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY()+1, pos.getZ(), stack));
        	}
        }
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
