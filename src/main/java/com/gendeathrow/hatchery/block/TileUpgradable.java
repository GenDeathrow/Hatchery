package com.gendeathrow.hatchery.block;

import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

public class TileUpgradable extends TileEntity
{
	protected InventoryStroageModifiable upgradeStorage;
	
	public TileUpgradable(int upgradeSize)
	{
		this.upgradeStorage = new InventoryStroageModifiable("Upgrades", upgradeSize);
	}
	
	public NonNullList<ItemStack> getAllUpgrades()
	{
        NonNullList<ItemStack> stacks = NonNullList.withSize(upgradeStorage.getSlots()+1, ItemStack.EMPTY);
        
		for(int i = 0; i < upgradeStorage.getSlots(); i++)
			stacks.set(i, upgradeStorage.getStackInSlot(i));
		
		return stacks;
	}
	
	public InventoryStroageModifiable getUpgradeStorage()
	{
		return this.upgradeStorage;
	}
	
	public boolean hasUpgrade(String type, int level)
	{
		return false;
	}
	
	protected boolean hasDuplicateUpgrade(String type, int level, int slot) 
	{
		return false;
	}
	
	protected boolean addUpgrade(int slot)
	{
		return false;
	}
	
	public boolean canUseUpgrade(ItemStack item)
	{
		return true;
	}
	
	
	protected void onUpgradeChange() {}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		this.upgradeStorage.readFromNBT(nbt);
		
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
	{
		this.upgradeStorage.writeToNBT(nbt);
		
		return super.writeToNBT(nbt);
	}
	
	
}
