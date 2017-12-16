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
import net.minecraftforge.fluids.FluidStack;

public class GeneratorRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack>  inputs;
	private final List<FluidStack>  fluidInputs;
	
	public GeneratorRecipeWrapper() 
	{
		this.inputs = new ArrayList<ItemStack>();
		this.fluidInputs = new ArrayList<FluidStack>();
		
		this.inputs.add(ModFluids.getFertilizerBucket());

		this.inputs.add(new ItemStack(ModBlocks.digesterGenerator));
		
		this.fluidInputs.add(new FluidStack(ModFluids.liquidfertilizer, 0));		
	}
	
	@Override
	public void drawInfo(Minecraft arg0, int arg1, int arg2, int arg3, int arg4) 
	{
		
	}
	
	
	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
	
		List<String> list = new ArrayList<String>();
		

		if(mouseX > 5 && mouseX < 18 && mouseY > 6 && mouseY < 62)
			list.add("RF Energy");		
		return list.size() > 0 ? list : null;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setInputs(FluidStack.class, fluidInputs);
	}

	public List<ItemStack> getInput() {
		return inputs;
	}

}
