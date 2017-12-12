package com.gendeathrow.hatchery.api.crafting;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class ShredderRecipe {
	
	private ItemStack itemIn;
	public ItemStack itemOut;
	private ItemStack itemExtra;
	private int chance;
	public int shredTime;
	private Random rand = new Random();
    	
	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut)
	{
		this(itemIn, itemOut, (ItemStack)null);
	}
    	
	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra)
	{
		this(itemIn, itemOut, itemExtra, 3, 100);
	}
    	
	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra, int chance, int shredTime)
	{
		this.itemIn = itemIn;
		this.itemOut = itemOut;
		this.itemExtra = itemExtra;
		this.chance = chance;
		this.shredTime = shredTime;
	}
    	
	public boolean isInputItem(ItemStack stack)
	{
		return this.itemIn.isItemEqual(stack); 
	}
    	
	public ItemStack getInputItem()
	{
		return this.itemIn;
	}
	
	public boolean hasOutput()
	{
		return itemOut != null;
	}
    	
	public boolean hasExtraOutput()
	{
		return itemExtra != null;
	}
	
	public ItemStack getOutputItem()
	{
		return itemOut.copy();
	}
    	
	@Nullable
	public ItemStack getExtraItem()
	{
		if(rand.nextInt(chance) == 1)
			return itemExtra.copy();
		else return null;
	}
    	
}
