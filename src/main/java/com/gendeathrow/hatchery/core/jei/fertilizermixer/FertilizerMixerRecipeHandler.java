package com.gendeathrow.hatchery.core.jei.fertilizermixer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class FertilizerMixerRecipeHandler implements IRecipeHandler<FertilizerMixerRecipeWrapper> 
{


	@Override
	public String getRecipeCategoryUid() 
	{
		return  FertilizerMixerCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(FertilizerMixerRecipeWrapper arg0) 
	{
		return FertilizerMixerCategory.UID;
	}

	@Override
	public Class<FertilizerMixerRecipeWrapper> getRecipeClass() 
	{
		return FertilizerMixerRecipeWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(FertilizerMixerRecipeWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(FertilizerMixerRecipeWrapper recipe) 
	{
		return recipe.getInput().size() >= 0 && recipe.getOutput().size() > 0;
	}

}