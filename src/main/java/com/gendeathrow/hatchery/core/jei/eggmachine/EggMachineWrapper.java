package com.gendeathrow.hatchery.core.jei.eggmachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gendeathrow.hatchery.core.init.ModItems;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class EggMachineWrapper extends BlankRecipeWrapper 
{
	private final List<ItemStack> outputs;
	private final List<ItemStack>  inputs;
	private final List<ItemStack>  allEggs;
	
	
	public EggMachineWrapper() 
	{
		
		this.outputs = new ArrayList<ItemStack>();
		this.inputs = new ArrayList<ItemStack>();
		this.allEggs = new ArrayList<ItemStack>();
		
		Set<ItemStack> eggOre = new HashSet<ItemStack>();

		Set<ItemStack> eggOreExtra = new HashSet<ItemStack>();
		
		for(Item item : Item.REGISTRY) {
			if(item instanceof ItemEgg) {
				eggOre.add(new ItemStack(item));
			}
		}
		
		for(ItemStack egg : eggOre)	{
				NonNullList<ItemStack> extraEggs = NonNullList.create();
				egg.getItem().getSubItems(CreativeTabs.SEARCH, extraEggs);
				eggOreExtra.addAll(extraEggs);
		}

		eggOre = eggOreExtra;

		for(ItemStack egg : eggOre)	{egg.setCount(24);}
		
		this.allEggs.addAll(eggOre);
		
		//this.inputs.add(new ItemStack(ModItems.hatcheryEgg, 24));
		this.inputs.add(new ItemStack(ModItems.plastic, 2));
		this.inputs.add(new ItemStack(ModItems.chickenmachine));
		
		this.outputs.add(new ItemStack(ModItems.prizeEgg));
	}

	@Override
	public void drawInfo(Minecraft arg0, int arg1, int arg2, int arg3, int arg4) 
	{
		
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, outputs);
        ingredients.setInputs(ItemStack.class, allEggs);
	}

	public List<ItemStack> getAllEggs(){
		return allEggs;
	}
	
	public List<ItemStack> getInput() {
		return inputs;
	}

	public List<ItemStack> getOutput() {
		return outputs;
	}
}
