package com.gendeathrow.hatchery.block.energy.cable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ExtensionCableBlock extends Block implements ITileEntityProvider{

	public ExtensionCableBlock() {
		super(Material.CIRCUITS);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ExtensionCableTileEntity();
	}

}
