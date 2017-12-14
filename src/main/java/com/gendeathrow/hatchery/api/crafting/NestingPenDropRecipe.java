package com.gendeathrow.hatchery.api.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModBlocks;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class NestingPenDropRecipe {

	private final ItemStack chicken;
	private final List<ItemStack> output;
	private final List<ItemStack> inputs;
	
	private final Entity chickenEnityIn;
	
	
	public NestingPenDropRecipe(ItemStack chickenIn, List<ItemStack> dropped) 
	{
		chicken = chickenIn;
		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(chickenIn);
		this.inputs.add(new ItemStack(ModBlocks.pen));
		chickenEnityIn = null;
		output = dropped;
	}
    	
	public List getInputItem()
	{
		return this.inputs;
	}
	
	public boolean hasOutput()
	{
		return output != null;
	}
    	
	public List<ItemStack> getOutputItems()
	{
		return output;
	}
    	

}
