package com.gendeathrow.hatchery.core.jei.shredder;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class ShredderRecipeHandler implements IRecipeHandler<ShredderRecipeWrapper> 
{


	@Override
	public String getRecipeCategoryUid() 
	{
		return  ShredderCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(ShredderRecipeWrapper arg0) 
	{
		return ShredderCategory.UID;
	}

	@Override
	public Class<ShredderRecipeWrapper> getRecipeClass() 
	{
		return ShredderRecipeWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ShredderRecipeWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(ShredderRecipeWrapper recipe) 
	{
		return recipe.getInput() != null && recipe.getOutput().size() > 0;
	}

}