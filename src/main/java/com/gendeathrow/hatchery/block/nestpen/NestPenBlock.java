package com.gendeathrow.hatchery.block.nestpen;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.gendeathrow.hatchery.Hatchery;

public class NestPenBlock extends Block implements ITileEntityProvider
{
    public static final PropertyBool hasEgg = PropertyBool.create("haschicken");
    
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.06D, 0.0D, 0.06, 0.94D, 0.15, 0.94D);
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {AABB};    

	protected String name;
	
	public NestPenBlock() 
	{
		super(Material.WOOD);
		this.name = "pen";
		this.setUnlocalizedName("pen");
		this.setRegistryName(Hatchery.MODID,"pen");
		this.setCreativeTab(CreativeTabs.MISC);	
		this.setHardness(5);
		
		
	}

	
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	  
	@Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
    public boolean isFullyOpaque(IBlockState state)
    {
    	return false;
    }
	
    
    @Override
    public BlockRenderLayer getBlockLayer()
    {
		return BlockRenderLayer.TRANSLUCENT;
    	
    }
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return null;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
	{
		//state = state.getActualState(worldIn, pos);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
//
//		if (((Boolean)state.getValue(hasEgg)).booleanValue())
//		{
//			addCollisionBoxToList(pos, entityBox, collidingBoxes, withEgg_AABB);
//		}
	}
	
	
	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOXES[0];
    }
    
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		return false;
    }
}
