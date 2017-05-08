package com.gendeathrow.hatchery.item.upgrades;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gendeathrow.hatchery.api.items.IUpgradeItem;

public class BaseUpgrade extends Item implements IUpgradeItem
{
	protected int upgradeTier;
	protected String upgradeType;
	
	
	public BaseUpgrade(int tier, String type)
	{
		super();
		this.setMaxStackSize(1);
		this.upgradeTier = tier;
		this.upgradeType = type;
	}

	@Override
	public int getUpgradeTier(ItemStack stack, String type) 
	{
		return this.upgradeTier;
	}

	@Override
	public void updateTick(ItemStack stack, String type) 
	{
		
	}

}
