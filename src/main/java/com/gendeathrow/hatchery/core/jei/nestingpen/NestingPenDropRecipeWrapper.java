package com.gendeathrow.hatchery.core.jei.nestingpen;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.api.crafting.NestingPenDropRecipe;
import com.gendeathrow.hatchery.core.init.ModBlocks;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;

public class NestingPenDropRecipeWrapper extends BlankRecipeWrapper{

	private final List<ItemStack> output;
	private final List<ItemStack> inputs;
	
	public final EntityLivingBase chickenEnityIn;
	
	
	public NestingPenDropRecipeWrapper(ItemStack chickenIn, List<ItemStack> dropped) 
	{
		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(chickenIn);
		this.inputs.add(new ItemStack(ModBlocks.pen));
		
		chickenEnityIn = null;
		
		output = dropped;
	}

	public NestingPenDropRecipeWrapper(NestingPenDropRecipe recipe) {
	
		this.inputs = recipe.getInputItem();
		this.output = recipe.getOutputItems();
		EntityChicken chicken = new EntityChicken(null);
		chickenEnityIn = chicken;
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
