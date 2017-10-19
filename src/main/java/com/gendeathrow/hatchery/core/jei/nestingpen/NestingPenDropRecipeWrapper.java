package com.gendeathrow.hatchery.core.jei.nestingpen;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.core.init.ModBlocks;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class NestingPenDropRecipeWrapper extends BlankRecipeWrapper 
{
	private final ItemStack chicken;
	private final List<ItemStack> output;
	private final List inputs;
	
	
	public NestingPenDropRecipeWrapper(ItemStack chickenIn, List<ItemStack> dropped) 
	{
		chicken = chickenIn;

		this.inputs = new ArrayList<List<ItemStack>>();
		this.inputs.add(chickenIn);
		this.inputs.add(new ItemStack(ModBlocks.pen));
		
		output = dropped;
	}
	
	@Override
	public void drawAnimations(Minecraft arg0, int arg1, int arg2) 
	{
		
	}

	@Override
	public void drawInfo(Minecraft arg0, int arg1, int arg2, int arg3, int arg4) 
	{
		
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, output);
	}

	public List getInput() {
		return inputs;
	}

	public List<ItemStack> getOutput() {
		return output;
	}
}
