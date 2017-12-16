package com.gendeathrow.hatchery.api.crafting;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.core.init.ModBlocks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class NestingPenDropRecipe {

	private final ItemStack chicken;
	private final List<ItemStack> output;
	private final List<ItemStack> inputs;
	
	private final EntityLivingBase chickenEnityIn;
	
	
	public NestingPenDropRecipe(EntityLivingBase entityChicken, ItemStack chickenIn, List<ItemStack> dropped) 
	{
		chicken = chickenIn;
		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(chickenIn);
		this.inputs.add(new ItemStack(ModBlocks.pen));
		chickenEnityIn = entityChicken;
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
	
	public EntityLivingBase getEntity() {
		return chickenEnityIn;
	}
    	
	public List<ItemStack> getOutputItems()
	{
		return output;
	}
    	

}
