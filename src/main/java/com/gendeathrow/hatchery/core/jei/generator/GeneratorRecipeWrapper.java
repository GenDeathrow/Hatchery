package com.gendeathrow.hatchery.core.jei.generator;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class GeneratorRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack>  inputs;

	public GeneratorRecipeWrapper() 
	{
		this.inputs = new ArrayList<ItemStack>();
		
		this.inputs.add(ModFluids.getFertilizerBucket());
		this.inputs.add(new ItemStack(ModFluids.blockLiquidFertilizer));
		this.inputs.add(new ItemStack(ModBlocks.digesterGenerator));
	}
	
	@Override
	public void drawAnimations(Minecraft arg0, int arg1, int arg2) 
	{
		
	}

	@Override
	public void drawInfo(Minecraft arg0, int arg1, int arg2, int arg3, int arg4) 
	{
		
	}
	
	
	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
	
		List<String> list = new ArrayList<String>();
		
		if(mouseX > 42 && mouseX < 55 && mouseY > 6 && mouseY < 62)
			list.add(ModFluids.liquidfertilizer.getName());
		else if(mouseX > 5 && mouseX < 18 && mouseY > 6 && mouseY < 62)
			list.add("RF Energy");		
		return list.size() > 0 ? list : null;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
	}

	public List<ItemStack> getInput() {
		return inputs;
	}

}
