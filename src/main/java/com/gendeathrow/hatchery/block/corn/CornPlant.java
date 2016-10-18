package com.gendeathrow.hatchery.block.corn;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.gendeathrow.hatchery.core.init.ModItems;

public class CornPlant extends BlockCrops
{
    private static final AxisAlignedBB[] CORN_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.4375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5625D, 1.0D)};
    
    
    public CornPlant()
    {
        super();

    }
    
//    protected Item getSeed()
//    {
//        return ModItems.cornSeeds;
//    }
//
//    protected Item getCrop()
//    {
//        return ModItems.corn;
//    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return CORN_AABB[((Integer)state.getValue(this.getAgeProperty())).intValue()];
    }

    
//    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
//    {
//        if (worldIn.getBlockState(pos.down()).getBlock() == ModItems.CornPlant || this.checkForDrop(worldIn, pos, state))
//        {
//            if (worldIn.isAirBlock(pos.up()))
//            {
//                int i;
//
//                for (i = 1; worldIn.getBlockState(pos.down(i)).getBlock() == this; ++i)
//                {
//                    ;
//                }
//
//                if (i < 4)
//                {
//                    int j = ((Integer)state.getValue(AGE)).intValue();
//
//                    if (j == 15)
//                    {
//                        worldIn.setBlockState(pos.up(), this.getDefaultState());
//                        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(0)), 4);
//                    }
//                    else
//                    {
//                        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(j + 1)), 4);
//                    }
//                }
//            }
//        }
//    }
//    
//    
//    protected final boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
//    {
//        if (this.canBlockStay(worldIn, pos))
//        {
//            return true;
//        }
//        else
//        {
//            this.dropBlockAsItem(worldIn, pos, state, 0);
//            worldIn.setBlockToAir(pos);
//            return false;
//        }
//    }
//    
//    public boolean canBlockStay(World worldIn, BlockPos pos)
//    {
//        return this.canPlaceBlockAt(worldIn, pos);
//    }
//    
//    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
//    {
//        IBlockState state = worldIn.getBlockState(pos.down());
//        Block block = state.getBlock();
//        if (block.canSustainPlant(state, worldIn, pos.down(), EnumFacing.UP, this)) return true;
//
//        if (block == this)
//        {
//            return true;
//        }else
//        {
//            BlockPos blockpos = pos.down();
//
//            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
//            {
//                IBlockState iblockstate = worldIn.getBlockState(blockpos.offset(enumfacing));
//
//                if (iblockstate.getMaterial() == Material.WATER || iblockstate.getBlock() == Blocks.FROSTED_ICE)
//                {
//                    return true;
//                }
//            }
//
//            return false;
//        }
//    }
//    
    
    
    
    
    
    
    
    public enum HEIGHTENUM implements IStringSerializable
    {
    		BOTTOM(0, "bottom"),
    		MIDDLE_BOTTOM(1, "middle_bottom"),
    		MIDDLE_TOP(2, "middle_top"),
    		TOP(3, "top");

    		String name;
    		int id;
    		
    		HEIGHTENUM(int id, String name)
    		{
    			this.name = name;
    			this.id = id;
    		}
    		
			@Override
			public String getName() 
			{
				return this.name();
			}
    }
//    @Override
//    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
//    {
//        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
//        if (this.isMaxAge(state) && RANDOM.nextInt(50) == 0)
//            ret.add(new ItemStack(Items.POISONOUS_POTATO));
//        return ret;
//    }
}

