package com.gendeathrow.hatchery.core.jei.eggmachine;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModItems;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class LuckyEggWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> outputs;
	private final List<ItemStack>  inputs;
	
	//private final int weight;
	//private final double percentage;
		
	public LuckyEggWrapper(List<ItemStack> item) 
	{
		this.outputs = new ArrayList<ItemStack>();
		
		this.inputs = new ArrayList<ItemStack>();
		
		
		this.inputs.add(new ItemStack(ModItems.prizeEgg));
		
		this.outputs.addAll(item);
		
		//percentage = (double)weightIn / (double)WeightedRandom.getTotalWeight(ConfigLootHandler.drops);
				
		//weight = weightIn;
	}
	
	@Override
	public void drawAnimations(Minecraft arg0, int arg1, int arg2) 
	{
		
	}

	@Override
	public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) 
	{
		
	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> tooltip = new ArrayList<String>();
		
		//tooltip.add("Weight: "+ this.weight);
		//tooltip.add("Drop %: "+ this.percentage);
		return null;
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
