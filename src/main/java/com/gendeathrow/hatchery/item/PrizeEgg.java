package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.item.Item;

public class PrizeEgg extends Item{
	
	public PrizeEgg()
	{
		super();
		this.setCreativeTab(Hatchery.hatcheryTabs);
		this.setNoRepair();
		this.setMaxStackSize(16);
	}

}
