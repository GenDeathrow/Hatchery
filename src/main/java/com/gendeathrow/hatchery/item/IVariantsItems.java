package com.gendeathrow.hatchery.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IVariantsItems {

	public abstract void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items);
}
