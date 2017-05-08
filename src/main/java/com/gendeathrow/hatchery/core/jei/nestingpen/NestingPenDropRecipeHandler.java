package com.gendeathrow.hatchery.core.jei.nestingpen;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

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
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}

}