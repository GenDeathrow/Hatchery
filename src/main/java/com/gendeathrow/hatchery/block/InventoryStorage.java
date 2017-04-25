package com.gendeathrow.hatchery.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;

public class InventoryStorage  implements IInventory
{

	public ItemStack[] inventory;
	
	public TileEntity tile;
	
	
	private String ID = "Items";

	public InventoryStorage(TileEntity tile, int invtSize)
	{
		this.inventory = new ItemStack[invtSize];
		this.tile = tile;
	}
	
	public InventoryStorage setID(String id)
	{
		this.ID = id;
		return this;
	}
	
	public ItemStack[] getInventory()
	{
		return inventory;
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
	public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, slot, count);

        if (itemstack != null)
        {
            tile.markDirty();
        }			
		return itemstack;
	}

	//@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if (inventory[slot] != null) {
			ItemStack itemstack = inventory[slot];
			inventory[slot] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}

	public void readFromNBT(NBTTagCompound nbt) {

		NBTTagList tags = nbt.getTagList(this.ID, 10);
		inventory = new ItemStack[getSizeInventory()];

		for (int i = 0; i < tags.tagCount(); i++) {
			NBTTagCompound data = tags.getCompoundTagAt(i);
			int j = data.getByte("Slot") & 255;

			if (j >= 0 && j < inventory.length)
				inventory[j] = ItemStack.loadItemStackFromNBT(data);
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		NBTTagList tags = new NBTTagList();

		for (int i = 0; i < inventory.length; i++)
			if (inventory[i] != null) {
				NBTTagCompound data = new NBTTagCompound();
				data.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(data);
				tags.appendTag(data);
			}

		nbt.setTag(this.ID, tags);
		return nbt;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) 
	{
	
	}

	@Override
	public void closeInventory(EntityPlayer playerIn) 
	{
	
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
		for (int i = 0; i < inventory.length; i++)
			inventory[i] = null;
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

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public void markDirty() {
		tile.markDirty();
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}
}
