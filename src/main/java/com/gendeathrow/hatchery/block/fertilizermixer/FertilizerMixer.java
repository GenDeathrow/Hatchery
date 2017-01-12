package com.gendeathrow.hatchery.block.fertilizermixer;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class FertilizerMixer extends BlockContainer implements ITileEntityProvider
{
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	public FertilizerMixer() 
	{
		super(Material.IRON);
		
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new FertilizerMixerTileEntity();
	}
	
    public static EnumFacing getFacing(int meta)
    {
        return EnumFacing.getHorizontal(meta);
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, getFacing(meta));
    }
    
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        	i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
        return i;
    }

}
