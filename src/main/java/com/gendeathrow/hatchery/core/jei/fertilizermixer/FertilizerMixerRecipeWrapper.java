package com.gendeathrow.hatchery.core.jei.fertilizermixer;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FertilizerMixerRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> outputs;
	private final List<ItemStack>  inputs;
	
	
	public FertilizerMixerRecipeWrapper(ItemStack itemIn) 
	{
		this.outputs = new ArrayList<ItemStack>();
		this.inputs = new ArrayList<ItemStack>();
		
		this.inputs.add(itemIn);
		//this.inputs.add(new ItemStack(ModBlocks.manureBlock));
		this.inputs.add(new ItemStack(Items.WATER_BUCKET));
		this.inputs.add(new ItemStack(ModBlocks.fertilizerMixer));
		
		
		this.outputs.add(ModFluids.getFertilizerBucket());
		//if(recipe.hasExtraOutput())
		//	this.outputs.add(recipe.getExtraItem());
		
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
		
		if(mouseX > 121 && mouseX < 134 && mouseY > 7 && mouseY < 63)
			list.add(ModFluids.liquidfertilizer.getName());
		else if(mouseX > 143 && mouseX < 156 && mouseY > 7 && mouseY < 63)
			list.add("RF Energy");	
		else if(mouseX > 48 && mouseX < 61 && mouseY > 7 && mouseY < 63)
			list.add("Water In");	
		return list.size() > 0 ? list : null;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
	}

	public List<ItemStack> getInput() {
		return inputs;
	}

	public List<ItemStack> getOutput() {
		return outputs;
	}
}
