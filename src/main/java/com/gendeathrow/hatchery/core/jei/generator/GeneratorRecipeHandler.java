package com.gendeathrow.hatchery.core.jei.generator;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class GeneratorRecipeHandler implements IRecipeHandler<GeneratorRecipeWrapper> 
{


	@Override
	public String getRecipeCategoryUid() 
	{
		return  GeneratorCategory.UID;
	}

	@Override
	public String getRecipeCategoryUid(GeneratorRecipeWrapper arg0) 
	{
		return GeneratorCategory.UID;
	}

	@Override
	public Class<GeneratorRecipeWrapper> getRecipeClass() 
	{
		return GeneratorRecipeWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(GeneratorRecipeWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(GeneratorRecipeWrapper recipe) 
	{
		return recipe.getInput().size() > 0;
	}

}