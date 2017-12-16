package com.gendeathrow.hatchery.api.crafting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gendeathrow.hatchery.core.init.ModItems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class EggMachineRecipe {
	
	
	private final List<ItemStack> outputs;
	private final List<ItemStack> inputs;
	
	private final List<ItemStack> allEggs;

	public EggMachineRecipe() 
	{
		this.outputs = new ArrayList<ItemStack>();
		this.inputs = new ArrayList<ItemStack>();
		this.allEggs = new ArrayList<ItemStack>();
		
		Set<ItemStack> eggOre = new HashSet<ItemStack>(OreDictionary.getOres("egg"));
		Set<ItemStack> eggOreExtra = new HashSet<ItemStack>();
		
		for(ItemStack egg : eggOre)	{
				NonNullList<ItemStack> extraEggs = NonNullList.create();
				egg.getItem().getSubItems(null, extraEggs);
				eggOreExtra.addAll(extraEggs);
		}

		eggOre = eggOreExtra;

		for(ItemStack egg : eggOre)	{egg.setCount(24);}
		
		
		this.allEggs.addAll(eggOre);
		
		this.inputs.add(new ItemStack(ModItems.hatcheryEgg, 24));
		this.inputs.add(new ItemStack(ModItems.plastic, 2));
		this.inputs.add(new ItemStack(ModItems.chickenmachine));
		this.inputs.addAll(eggOre);
		
		
		this.outputs.add(new ItemStack(ModItems.prizeEgg));
	}
    	
	public List getInputItem()
	{
		return this.inputs;
	}
	
	public boolean hasOutput()
	{
		return outputs != null && outputs.size() > 0;
	}
	
	public List<ItemStack> getAllEggs(){
		return this.allEggs;
	}
	
	public List<ItemStack> getOutputItems()
	{
		return outputs;
	}
    	
}
