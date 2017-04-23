package com.gendeathrow.hatchery.api.items;

import net.minecraft.item.ItemStack;


public interface IUpgradeItem 
{
	/**
	 * Gets the type of upgrade and Tier of the upgrade.
	 * 
	 * @param stack
	 * @param type
	 * @return
	 */
	public abstract int getUpgradeTier(ItemStack stack, String type);	
	
	
	/**
	 * Update tick for item while inside an upgrade slot. 
	 * 
	 * @param stack
	 * @param type
	 */
	public abstract void updateTick(ItemStack stack, String type);
}
