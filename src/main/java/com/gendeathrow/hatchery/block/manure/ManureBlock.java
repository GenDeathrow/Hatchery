package com.gendeathrow.hatchery.block.manure;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ManureBlock extends Block
{

	public ManureBlock() 
	{
		super(Material.CLAY);
		this.setDefaultState(this.blockState.getBaseState());
		this.setHardness(1);
		this.setHarvestLevel("shovel", 0);
		this.setCreativeTab(Hatchery.hatcheryTabs);
	}
	
	

}
