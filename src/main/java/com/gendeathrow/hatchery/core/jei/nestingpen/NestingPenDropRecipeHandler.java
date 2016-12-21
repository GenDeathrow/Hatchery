package com.gendeathrow.hatchery.core.jei.nestingpen;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.util.Ingredients;
import net.minecraft.item.ItemStack;

public class NestingPenDropRecipeHandler implements IRecipeHandler<NestingPenDropRecipeWrapper> 
{


	@Override
	public String getRecipeCategoryUid() 
	{
		return  NestingPenCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(NestingPenDropRecipeWrapper arg0) 
	{
		return NestingPenCategory.UID;
	}

	@Override
	public Class<NestingPenDropRecipeWrapper> getRecipeClass() 
	{
		return NestingPenDropRecipeWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(NestingPenDropRecipeWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(NestingPenDropRecipeWrapper recipe) 
	{
		IIngredients ingredients = new Ingredients();
		recipe.getIngredients(ingredients);
		return ingredients.getInputs(ItemStack.class).size() > 0 && ingredients.getOutputs(ItemStack.class).size() > 0;

	}

}