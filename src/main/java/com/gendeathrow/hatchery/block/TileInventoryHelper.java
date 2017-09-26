package com.gendeathrow.hatchery.block;

import com.gendeathrow.hatchery.inventory.InventoryStorageModifiable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;

public class TileInventoryHelper extends TileUpgradable implements ISidedInventory{

	protected InventoryStorageModifiable inventorySided;
	
	protected ItemStack[] inventory;
	private String ID = "Items";
	
	public TileInventoryHelper(int invetorySize, int upgradeSize) {
		super(upgradeSize);
		
		inventory = new ItemStack[invetorySize];
	}


	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}


	@Override
    public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(inventory, index, count);
		if (itemstack != null)
			this.markDirty();
		return itemstack;
	}

	@Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory[index] = stack;
        if (stack.stackSize > this.getInventoryStackLimit())
            stack.stackSize = this.getInventoryStackLimit();
        this.markDirty();
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : inventory) {
			if (itemstack != null) {
				return false;
			}
		}

		return true;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		NBTTagCompound tags = nbt.getCompoundTag(this.ID);
		this.deserializeNBT(tags);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		NBTTagCompound tags = serializeNBT();
			nbt.setTag(this.ID, tags);
			
		return nbt;
	}

	
    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < inventory.length; i++)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                inventory[i].writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", inventory.length);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : inventory.length);
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < inventory.length)
            {
            	inventory[slot] = ItemStack.loadItemStackFromNBT(itemTags);
            }
        }
        onLoad();
    }

    public void setSize(int size)
    {
    	inventory = new ItemStack[size];
    }

	@Override
	public void openInventory(EntityPlayer playerIn) {
	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
	
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	public boolean canInsertItem() {
		return true;
	}


	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}


	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}


	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[]{0};
	}


	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return true;
	}


	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}



}
