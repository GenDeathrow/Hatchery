package com.gendeathrow.hatchery.core.jei.shredder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.api.crafting.ShredderRecipe;
import com.gendeathrow.hatchery.core.init.ModBlocks;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class ShredderRecipeWrapper extends BlankRecipeWrapper{

	private final List<ItemStack> outputs;
	private final List<ItemStack>  inputs;
	
	private final int chance;
	private boolean hasExtraItem = false;
	
	
	public ShredderRecipeWrapper(ShredderRecipe recipe) 
	{
		this.outputs = new ArrayList<ItemStack>();
		this.inputs = new ArrayList<ItemStack>();
		
		this.inputs.add(recipe.getInputItem());
		this.inputs.add(new ItemStack(ModBlocks.shredder));
		
		this.outputs.add(recipe.getOutputItem());
		this.outputs.add(recipe.getExtraItem());
		
		chance = recipe.getChance();
		
		hasExtraItem = !recipe.getExtraItem().isEmpty();
		
	}

	boolean isOver = false;
	@Override
	public void drawInfo(Minecraft minecraft, int x, int y, int mx, int my) 
	{
		if(this.hasExtraItem)
			minecraft.fontRenderer.drawString(this.chance+"%", x-25, y-25, Color.black.getRGB());
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
