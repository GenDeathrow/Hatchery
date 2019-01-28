package com.gendeathrow.hatchery.block.generator;

import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DigesterGeneratorBlock extends BlockHorizontal implements ITileEntityProvider
{
    public static final PropertyBool ISGENERATING = PropertyBool.create("isgenerating");
    
    
	public DigesterGeneratorBlock() 
	{
		super(Material.IRON);
		this.setHardness(2);
		this.setHarvestLevel("pickaxe", 0);
		this.setTranslationKey("digester_generator");
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(ISGENERATING, false));
	}

	@Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return true;
    }
	
	/** Is the Generator turned on? */
	public static boolean isGenerating(IBlockState blockStateContainer)
	{
		return blockStateContainer.getValue(ISGENERATING);
	}
	
	/** Update Generator */
	public static void setGenerating(World worldIn, BlockPos pos, IBlockState state, boolean isActiveIn)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, getFacing(state)).withProperty(ISGENERATING, isActiveIn), 3);
	}
	
	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
        	TileEntity te = worldIn.getTileEntity(pos);
        	if(te instanceof DigesterGeneratorTileEntity) {
        		((DigesterGeneratorTileEntity) te).updateState = true;
        		System.out.println("test");
        	}
		}
	}
	   
	
    @Nullable
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.digesterGenerator);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this.getItemDropped(state, world.rand, 0), 1, this.damageDropped(state));
    }
    
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    		TileEntity te = worldIn.getTileEntity(pos);
    		if(te instanceof DigesterGeneratorTileEntity) {
    			((DigesterGeneratorTileEntity) te).inputInventory.dropInventory(worldIn, pos);
    			((DigesterGeneratorTileEntity) te).outputInventory.dropInventory(worldIn, pos);
    			((DigesterGeneratorTileEntity) te).upgradeStorage.dropInventory(worldIn, pos);
    		}
    		super.breakBlock(worldIn, pos, state);
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new DigesterGeneratorTileEntity();
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
        	ItemStack heldItem = playerIn.getHeldItem(hand);
        	
        	if(!heldItem.isEmpty() && heldItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
        	{
        		DigesterGeneratorTileEntity tileentity = (DigesterGeneratorTileEntity) worldIn.getTileEntity(pos);
        		
				IFluidHandler handler = FluidUtil.getFluidHandler(heldItem);
				
				boolean hasFluid =FluidUtil.getFluidContained(heldItem) != null && FluidUtil.getFluidContained(heldItem).getFluid() == ModFluids.liquidfertilizer;
	
				if(tileentity != null && handler != null  && hasFluid && tileentity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
				{
					FluidUtil.tryFluidTransfer(tileentity.getFertilizerTank(), handler, tileentity.getFertilizerTank().getCapacity(), true);
				}
	        	else
	        		playerIn.openGui(Hatchery.INSTANCE, CommonProxy.GUI_ID_DIGESTER_GEN, worldIn, pos.getX(), pos.getY(), pos.getZ());

        	}
        	else
        		playerIn.openGui(Hatchery.INSTANCE, CommonProxy.GUI_ID_DIGESTER_GEN, worldIn, pos.getX(), pos.getY(), pos.getZ());
        	
        }
        
		return true;
    }
	
    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
        if(!worldIn.isRemote)
          	 worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 2); 
    }
    
    
    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
            {
                enumfacing = EnumFacing.SOUTH;
            }
            else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
            {
                enumfacing = EnumFacing.NORTH;
            }
            else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
            {
                enumfacing = EnumFacing.EAST;
            }
            else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
            {
                enumfacing = EnumFacing.WEST;
            }

            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (DigesterGeneratorBlock.isGenerating(stateIn))
        {
            EnumFacing enumfacing = (EnumFacing)stateIn.getValue(FACING);
            double d0 = (double)pos.getX() + 0.5D;
            double d1 = (double)pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double)pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;

            if (rand.nextDouble() < 0.1D)
            {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }

            switch (enumfacing)
            {
                case WEST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case EAST:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + 0.52D, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case NORTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
                    break;
                case SOUTH:
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + 0.52D, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    
    
    public static void setState(boolean active, World worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        setGenerating(worldIn, pos, iblockstate, active);
   
        if (tileentity != null)
        {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
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
    
	public static EnumFacing getFacing(IBlockState blockStateContainer)
	{
		return (EnumFacing)blockStateContainer.getValue(FACING);
	}
	
    @Override
	public IBlockState getStateFromMeta(int meta)
	{
		 return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta)).withProperty(ISGENERATING, (meta >> 2) == 1 ? true : false);
	}

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
    @Override
	public int getMetaFromState(IBlockState state)
	{
		int i = 0;
		i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
		i |= (state.getValue(ISGENERATING) ? 1 : 0 ) << 2;
	    	
		return i  ;
	}


	    /**
	     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	     * blockstate.
	     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
    	return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
    	return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
    	return new BlockStateContainer(this, new IProperty[] {FACING, ISGENERATING});
    }
	    
    
}
