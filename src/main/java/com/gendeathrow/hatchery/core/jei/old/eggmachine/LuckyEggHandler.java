package com.gendeathrow.hatchery.core.jei.old.eggmachine;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class LuckyEggHandler implements IRecipeHandler<oldLuckyEggWrapper> 
{


	@Override
	public String getRecipeCategoryUid(oldLuckyEggWrapper arg0) 
	{
		return oldLuckyEggCategory.UID;
	}

	@Override
	public Class<oldLuckyEggWrapper> getRecipeClass() 
	{
		return oldLuckyEggWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(oldLuckyEggWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(oldLuckyEggWrapper recipe) 
	{
		return recipe.getInput() != null && recipe.getOutput().size() > 0;
	}

}