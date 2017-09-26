package com.gendeathrow.hatchery.block;

import com.gendeathrow.hatchery.inventory.InventoryStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileUpgradable extends TileEntity
{
	protected InventoryStorage upgradeStorage;
	
	public TileUpgradable(int upgradeSize)
	{
		this.upgradeStorage = new InventoryStorage(this, upgradeSize).setID("Upgrades");
	}
	
	public ItemStack[] getUpgrades()
	{
		return this.upgradeStorage.getInventory();
	}
	
	public InventoryStorage getUpgradeStorage()
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
