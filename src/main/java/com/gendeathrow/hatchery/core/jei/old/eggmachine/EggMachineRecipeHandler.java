package com.gendeathrow.hatchery.core.jei.old.eggmachine;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class EggMachineRecipeHandler implements IRecipeHandler<oldEggMachineWrapper> 
{


	@Override
	public String getRecipeCategoryUid(oldEggMachineWrapper arg0) 
	{
		return oldEggMachineCategory.UID;
	}

	@Override
	public Class<oldEggMachineWrapper> getRecipeClass() 
	{
		return oldEggMachineWrapper.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(oldEggMachineWrapper recipe) 
	{
		return recipe;
	}

	@Override
	public boolean isRecipeValid(oldEggMachineWrapper recipe) 
	{
		return recipe.getInput().size() > 0 && recipe.getOutput().size() > 0;
	}

}