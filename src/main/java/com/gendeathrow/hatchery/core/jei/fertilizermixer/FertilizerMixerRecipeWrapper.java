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
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FertilizerMixerRecipeWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> outputs;
	private final List<ItemStack>  inputs;
	
	private final List<ItemStack> solidItemsInputs;
	
	private final List<FluidStack> inputsFluid;
	private final List<FluidStack> outputsFluid;
	
	
	public FertilizerMixerRecipeWrapper(ItemStack itemIn) 
	{
		this.outputs = new ArrayList<ItemStack>();
		this.inputs = new ArrayList<ItemStack>();
		
		this.outputsFluid = new ArrayList<FluidStack>();
		this.inputsFluid = new ArrayList<FluidStack>();
		this.solidItemsInputs = new ArrayList<ItemStack>();
		
		this.inputs.add(itemIn);
		this.inputs.add(new ItemStack(Items.WATER_BUCKET));
		this.inputs.add(new ItemStack(ModBlocks.fertilizerMixer));
		this.inputs.add(new ItemStack(ModBlocks.manureBlock));
		
		this.outputsFluid.add(new FluidStack(ModFluids.liquidfertilizer, 0));
		this.inputsFluid.add(new FluidStack(FluidRegistry.WATER, 0));
		
		this.outputs.add(ModFluids.getFertilizerBucket());
		
		this.solidItemsInputs.add(itemIn);
		this.solidItemsInputs.add(new ItemStack(ModBlocks.manureBlock));
	}
	
	@Override
	public void drawInfo(Minecraft arg0, int arg1, int arg2, int arg3, int arg4) 
	{
		
	}
	
	
	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		List<String> list = new ArrayList<String>();
		
		if(mouseX > 143 && mouseX < 156 && mouseY > 7 && mouseY < 63)
			list.add("RF Energy");	

		return list.size() > 0 ? list : null;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
        
        ingredients.setInputs(FluidStack.class, inputsFluid);
        ingredients.setOutputs(FluidStack.class, outputsFluid);
	}

	public List<ItemStack> getInput() {
		return inputs;
	}

	public List<ItemStack> getOutput() {
		return outputs;
	}
	
	public List<FluidStack> getFluidInput() {
		return inputsFluid;
	}

	public List<FluidStack> getFluidOutput() {
		return outputsFluid;
	}
	
	public List<ItemStack> getSolidInputs() {
		return this.solidItemsInputs;
	}
}
