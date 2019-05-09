package com.gendeathrow.hatchery.block.energy.battery;

import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModBlocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseBatteryBlock extends BlockHorizontal implements ITileEntityProvider{

	
	public BaseBatteryBlock() {
		super(Material.IRON);
		this.setHardness(2);
		this.setHarvestLevel("pickaxe", 0);
		this.setTranslationKey("battery_bank");
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new BaseBatteryTileEntity();
	}
	
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.digesterGenerator);
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
    }
    
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    		TileEntity te = worldIn.getTileEntity(pos);
    		if(te instanceof BaseBatteryTileEntity) {
    			((BaseBatteryTileEntity) te).upgradeStorage.dropInventory(worldIn, pos);
    			
    			
       			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
           		if(te instanceof BaseBatteryTileEntity) {
           			NBTTagCompound cmp = new NBTTagCompound();
           			cmp.setLong("power", ((BaseBatteryTileEntity)te).getEnergyStorage().getEnergyStored());
           			stack.setTagCompound(cmp);
           		}    	
    		}
    		super.breakBlock(worldIn, pos, state);
    }
	
	
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
        
        if(!worldIn.isRemote)
          	 worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 2);
        
        if(stack.hasTagCompound())
        {
        	if(stack.getTagCompound().hasKey("power"))
        	{
        		TileEntity te = worldIn.getTileEntity(pos);
        		if(te instanceof BaseBatteryTileEntity) {
        			((BaseBatteryTileEntity) te).setEnergyStored(stack.getTagCompound().getInteger("power"));
        		}
        	}
        }
    }
    
    @Override
 	public boolean isOpaqueCube(IBlockState state)
 	{
 		return false;
 	}
 	  
 	@Override
 	public boolean isFullBlock(IBlockState state)
 	{
 		return false;
 	}
 	
     @Override
     public boolean isFullCube(IBlockState state)
     {
         return false;
     }
 	
     public boolean isFullyOpaque(IBlockState state)
     {
     	return false;
     }
 	
     @SideOnly(Side.CLIENT)
     public BlockRenderLayer getBlockLayer()
     {
         return BlockRenderLayer.CUTOUT_MIPPED;
     }
     
     @Override
     public EnumBlockRenderType getRenderType(IBlockState state)
     {
         return EnumBlockRenderType.MODEL;
     }
     
     @Override
     protected BlockStateContainer createBlockState()
     {
     	return new BlockStateContainer(this, new IProperty[] {FACING});
     }
     
     @Override
     public IBlockState withRotation(IBlockState state, Rotation rot)
     {
     	return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
     }

    @Override
    public IBlockState getStateFromMeta(int meta)
	{
 		 return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
 	}
    
    @Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
	}
 	    
}
