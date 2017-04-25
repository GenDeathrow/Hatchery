package com.gendeathrow.hatchery.item;

import net.minecraft.item.ItemStack;

public class RFEfficiencyUpgrade  extends BaseUpgrade
{
	protected final static String type = "rfupgrade";

	public RFEfficiencyUpgrade(int tier) 
	{
		super(tier, type);
	}

	@Override
	public int getUpgradeTier(ItemStack stack, String type) 
	{
		return this.upgradeTier;
	}
}
