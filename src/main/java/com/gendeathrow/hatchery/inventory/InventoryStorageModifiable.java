package com.gendeathrow.hatchery.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class InventoryStorageModifiable implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>
{

	//public ItemStack[] publicInventory;
	
	protected ItemStack[] stacks;
	
	public TileEntity inventoryTile;
	private String ID = "Items";

	public InventoryStorageModifiable(TileEntity tile, int invtSize)
	{
		//this.publicInventory = new ItemStack[invtSize];
		this.inventoryTile = tile;
		stacks = new ItemStack[invtSize];
	}

    public void setSize(int size)
    {
        stacks = new ItemStack[size];
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack)
    {
        validateSlotIndex(slot);
        if (ItemStack.areItemStacksEqual(this.stacks[slot], stack))
            return;
        this.stacks[slot] = stack;
        onContentsChanged(slot);
    }

    @Override
    public int getSlots()
    {
        return stacks.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
    	if(!canInteractWithSlot(slot))
    		return null;
    	
        validateSlotIndex(slot);
        return this.stacks[slot];
    }
    
    public ItemStack getStackInSlotInternal(int slot)
    {
        validateSlotIndex(slot);
        return this.stacks[slot];
    }
    
    public boolean canInteractWithSlot(int slot) {
    	return true;
    }
    
    public boolean isItemValidForslot(int Slot, ItemStack stack){
    	return true;
    }
    
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
    {
        if (stack == null || stack.stackSize == 0)
            return null;
        
        validateSlotIndex(slot);


        if(!isItemValidForslot(slot, stack))
        	return null;
        
        
        ItemStack existing = this.stacks[slot];

        int limit = getStackLimit(slot, stack);

        if (existing != null)
        {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.stackSize;
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.stackSize > limit;

        if (!simulate)
        {
            if (existing == null)
            {
                this.stacks[slot] = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
            }
            else
            {
                existing.stackSize += reachedLimit ? limit : stack.stackSize;
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - limit) : null;
    }

    
    public ItemStack extractItemIternal(int slot, int amount, boolean simulate)
    {
    	
    	return this.extractItem(slot, amount, simulate);
    }
    
    
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
    	return this.extractStack(slot, amount, simulate);
    }
    
    
    private ItemStack extractStack(int slot, int amount, boolean simulate)
    {
        if (amount == 0)
            return null;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks[slot];

        if (existing == null)
            return null;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.stackSize <= toExtract)
        {
            if (!simulate)
            {
                this.stacks[slot] = null;
                onContentsChanged(slot);
            }
            return existing;
        }
        else
        {
            if (!simulate)
            {
                this.stacks[slot] = ItemHandlerHelper.copyStackWithSize(existing, existing.stackSize - toExtract);
                onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    protected int getStackLimit(int slot, ItemStack stack)
    {
        return stack.getMaxStackSize();
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
	
	
	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < stacks.length; i++)
        {
            if (stacks[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                stacks[i].writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", stacks.length);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.length);
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < stacks.length)
            {
                stacks[slot] = ItemStack.loadItemStackFromNBT(itemTags);
            }
        }
        onLoad();
    }

    protected void validateSlotIndex(int slot)
    {
        if (slot < 0 || slot >= stacks.length)
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.length + ")");
    }

    protected void onLoad()
    {

    }

    protected void onContentsChanged(int slot)
    {

    }	
}
