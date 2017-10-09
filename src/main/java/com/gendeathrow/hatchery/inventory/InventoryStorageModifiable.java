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
import net.minecraftforge.items.ItemStackHandler;

public class InventoryStorageModifiable extends ItemStackHandler
{

	//public ItemStack[] publicInventory;
	
	protected ItemStack[] stacks;
	
	public TileEntity inventoryTile;
	private String ID = "Items";

	
	public InventoryStorageModifiable(TileEntity tile, int invtSize, String ID)
	{
		this(tile, invtSize);
		this.ID = ID;
	}
	
	public InventoryStorageModifiable(TileEntity tile, int invtSize)
	{
		//this.publicInventory = new ItemStack[invtSize];
		this.inventoryTile = tile;
		stacks = new ItemStack[invtSize];
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
