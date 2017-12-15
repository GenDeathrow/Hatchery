package com.gendeathrow.hatchery.api.crafting;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

public class ShredderRecipe {
	
	private ItemStack itemIn = ItemStack.EMPTY;
	public ItemStack itemOut = ItemStack.EMPTY;
	private ItemStack itemExtra = ItemStack.EMPTY;
	private int chance;
	public int shredTime;
	private Random rand = new Random();
    	
	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut)
	{
		this(itemIn, itemOut, ItemStack.EMPTY);
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
    	
	public ItemStack getOutputItem()
	{
		return itemOut.copy();
	}
    	
	public ItemStack getExtraItemByChance()
	{
		if(rand.nextInt(chance) == 1)
			return itemExtra.copy();
		else return ItemStack.EMPTY;
	}
	
	public int getChance() {
		return (int) (((double)1 / (this.chance+1))*100);
	}
	
	public ItemStack getExtraItem()
	{
		return itemExtra;
	}
    	
	@Override
	public String toString()
	{
		return "Shredder Recipe>>" + 
			   "  ItemIn:"+ (!this.itemIn.isEmpty() ? this.itemIn.getDisplayName() : "null") +
			   "  ItemOut:"+ (!this.itemOut.isEmpty() ? this.itemOut.getDisplayName() : "null") +
			   "  ItemExtra:"+ (!this.itemExtra.isEmpty() ? this.itemExtra.getDisplayName() : "null");
		
	}
}
