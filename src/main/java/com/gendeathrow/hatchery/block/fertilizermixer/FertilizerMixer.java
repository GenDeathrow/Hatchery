package com.gendeathrow.hatchery.block.fertilizermixer;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FertilizerMixer extends BlockContainer implements ITileEntityProvider
{
	
	public FertilizerMixer() 
	{
		super(Material.IRON);
		this.setHardness(2);
		this.setHarvestLevel("pickaxe", 0);
		this.setUnlocalizedName("fertilizer_mixer");
	}


	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
        	if(heldItem != null && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side))
        	{
        		FertilizerMixerTileEntity tileentity = (FertilizerMixerTileEntity) worldIn.getTileEntity(pos);
        		
				IFluidHandler handler = FluidUtil.getFluidHandler(heldItem);
				
				boolean hasFluid = FluidUtil.getFluidContained(heldItem) != null && FluidUtil.getFluidContained(heldItem).getFluid() == FluidRegistry.WATER;
				boolean notnull = tileentity != null && handler != null;
				
				if( notnull && hasFluid && tileentity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)){
					FluidUtil.tryFluidTransfer(tileentity.getWaterTank(), handler, tileentity.getWaterTank().getCapacity(), true);
				}
				else if(notnull) {
					FluidUtil.tryFillContainerAndStow(heldItem, tileentity.getFertilizerTank(), new InvWrapper(playerIn.inventory), tileentity.getFertilizerTank().getCapacity(), playerIn);
					
					//.tryFillContainer(heldItem,  tileentity.getFertilizerTank(), tileentity.getFertilizerTank().getCapacity(), playerIn, true);
				}
	        	else
	        		playerIn.openGui(Hatchery.INSTANCE, CommonProxy.GUI_ID_FERTLIZERMIXER, worldIn, pos.getX(), pos.getY(), pos.getZ());

        	}
        	else
        		playerIn.openGui(Hatchery.INSTANCE, CommonProxy.GUI_ID_FERTLIZERMIXER, worldIn, pos.getX(), pos.getY(), pos.getZ());
        	
        	
        	
        }
        
		return true;
    }
	
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new FertilizerMixerTileEntity();
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
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }
	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

}
