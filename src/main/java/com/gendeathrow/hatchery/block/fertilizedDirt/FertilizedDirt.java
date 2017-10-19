package com.gendeathrow.hatchery.block.fertilizedDirt;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class FertilizedDirt extends Block
{

	public FertilizedDirt() 
	{
		super(Material.GROUND);
		this.setUnlocalizedName("fertilized_dirt");
		this.setHardness(1);
		this.setHarvestLevel("shovel", 0);
		this.setTickRandomly(true);
	}

	
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        net.minecraftforge.common.EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));

        if (plant.getBlock() instanceof BlockSapling)
        {
            return true;
        }

        return false;
    }
    
    @Override
    public void onPlantGrow(IBlockState state, World world, BlockPos pos, BlockPos source)
    {
    	
    }
    
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
       if(rand.nextInt(99)+1 <= 60)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.up());

            if (iblockstate.getBlock() instanceof IGrowable && iblockstate.getBlock() != Blocks.GRASS)
            {
                IGrowable igrowable = (IGrowable)iblockstate.getBlock();

                if (igrowable.canGrow(worldIn, pos.up(), iblockstate, worldIn.isRemote))
                {
                    if (!worldIn.isRemote)
                    {
                        if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos.up(), iblockstate))
                        {
                            igrowable.grow(worldIn, worldIn.rand, pos.up(), iblockstate);
                            worldIn.playEvent(2005, pos, 0);
                        }

                    }

                }
            }
        }
    }

}
