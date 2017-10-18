package com.gendeathrow.hatchery.core.jei.eggmachine;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class LuckyEggHandler implements IRecipeHandler<LuckyEggWrapper> 
{


	@Override
	public String getRecipeCategoryUid() 
	{
		return  LuckyEggCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(LuckyEggWrapper arg0) 
	{
		return LuckyEggCategory.UID;
	}

	@Override
	public Class<LuckyEggWrapper> getRecipeClass() 
	{
		return LuckyEggWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(LuckyEggWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(LuckyEggWrapper recipe) 
	{
		return recipe.getInput() != null && recipe.getOutput().size() > 0;
	}

}