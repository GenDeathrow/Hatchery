package com.gendeathrow.hatchery.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

public class BlockLiquidFertilizer extends BlockFluidClassic {

	public BlockLiquidFertilizer(Fluid fluid) 
	{
		super(fluid, Material.WATER);
		this.setUnlocalizedName(fluid.getUnlocalizedName());
		this.setRegistryName(fluid.getName());
		fluid.setBlock(this);
	}


}
