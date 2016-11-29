package com.gendeathrow.hatchery.block.fertilizermixer;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FertilizerMixer extends Block implements ITileEntityProvider
{
	
	public FertilizerMixer() 
	{
		super(Material.IRON);
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return null;
	}

}
