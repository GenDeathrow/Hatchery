package com.gendeathrow.hatchery.core.jei.shredder;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity.ShredderRecipe;
import com.gendeathrow.hatchery.core.init.ModBlocks;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ShredderRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> outputs;
	private final List<ItemStack>  inputs;
	
	
	public ShredderRecipeWrapper(ShredderRecipe recipe) 
	{
		this.outputs = new ArrayList<ItemStack>();
		this.inputs = new ArrayList<ItemStack>();
		
		this.inputs.add(recipe.getInputItem());
		this.inputs.add(new ItemStack(ModBlocks.shredder));
		
		
		this.outputs.add(recipe.getOutputItem());
		if(recipe.hasExtraOutput())
			this.outputs.add(recipe.getExtraItem());
		
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
        ingredients.setOutputs(ItemStack.class, outputs);
	}

	public List<ItemStack> getInput() {
		return inputs;
	}

	public List<ItemStack> getOutput() {
		return outputs;
	}
}
