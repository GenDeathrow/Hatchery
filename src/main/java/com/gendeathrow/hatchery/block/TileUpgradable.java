package com.gendeathrow.hatchery.block;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileUpgradable extends TileEntity
{

	protected ItemStack[] upgrades = new ItemStack[1];
	
	public TileUpgradable(int upgradeSize)
	{
		this.upgrades = new ItemStack[upgradeSize];
	}
	
	public ItemStack[] getUpgrades()
	{
		return this.upgrades;
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
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) 
	{

		return super.writeToNBT(nbt);
	}
	
	
}
