package com.gendeathrow.hatchery.block.feeder;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.common.capability.CapabilityAnimalStatsHandler;
import com.gendeathrow.hatchery.common.capability.IAnimalStats;
import com.gendeathrow.hatchery.core.theoneprobe.TOPInfoProvider;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FeederBlock extends Block implements ITileEntityProvider, TOPInfoProvider
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 3);
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	//public static final PropertyEnum QTY;
	protected static final AxisAlignedBB Base_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.125D, 0.875D);
	protected static final AxisAlignedBB Container_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D,0.5625D, 0.625);
	protected static final AxisAlignedBB bounding = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.5625D, 0.875D);
	   
	
	public FeederBlock() 
	{
		super(Material.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEVEL,0));
		this.setHardness(2);
		this.setHarvestLevel("axe", 0);
		this.setCreativeTab(Hatchery.hatcheryTabs);
		this.setTickRandomly(true);

	}
	
   public boolean getHasSubtypes()
   {
        return true;
   }

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
    	if(!worldIn.isRemote)
    	{
    		FeederTileEntity te = (FeederTileEntity) worldIn.getTileEntity(pos);
    		
    		if(te.getSeedsInv() > 0)
    		{
    			AxisAlignedBB RANGE_AABB = new AxisAlignedBB(pos.getX() - 4, pos.getY(), pos.getZ() - 4, pos.getX() + 4, pos.getY() + 1, pos.getZ() + 4);
    			
    			List<EntityChicken> entitys = worldIn.getEntitiesWithinAABB(EntityChicken.class, RANGE_AABB);
    			for(EntityChicken entity : entitys)
    			{
    				if(te.getSeedsInv() > 0)
    				{
 						if(entity.hasCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, null))
						{
 		   					
 							IAnimalStats cap = entity.getCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, null);
 							
							if(cap != null && cap.canEat())
							{
								if(entity.isChild())
								{
									te.decrSeedsInv();
    								
    	    						entity.ageUp((int)((float)(-entity.getGrowingAge() / 10) * 0.35F), true);
								}
								else
								{
									te.decrSeedsInv();
									entity.timeUntilNextEgg -= 5;
								}

								this.setFeederLevel(worldIn, pos, state);
							}

						}
    				}
    			}
    		}
    	}
	}
	
    boolean keepInventory;
    
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		if (!keepInventory)
        {
			
        }
		
		ItemStack stack = this.createStackedBlock(state);
		if(stack != null)
		{
    		if(worldIn.getTileEntity(pos)  != null && worldIn.getTileEntity(pos) instanceof FeederTileEntity)
    		{
    			if(!stack.hasTagCompound())
    			{
    				stack.setTagCompound(new NBTTagCompound());
    			}
    		 
    			stack.getTagCompound().setInteger("seedInv", ((FeederTileEntity)worldIn.getTileEntity(pos)).getSeedsInv());
    			 
    		}
    	
    		this.spawnAsEntity(worldIn, pos, stack);
		}
		
		super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
    	return new java.util.ArrayList<ItemStack>();
    }
        
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(!worldIn.isRemote)
    	{
    		
			FeederTileEntity te = (FeederTileEntity) worldIn.getTileEntity(pos);
			
			if(heldItem != null && te != null && te.isItemValidForSlot(0, heldItem))
			{
				
				if (playerIn.isSneaking())
				{
                    	te.setSeeds(1, heldItem, playerIn.capabilities.isCreativeMode);
				}
				else
				{
                    	te.setSeeds(heldItem.stackSize, heldItem, playerIn.capabilities.isCreativeMode);
				}
    			
    			return true;
			}
    	}
    	return true;
    }
	
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, Base_AABB);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, Container_AABB);

    }
    
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return bounding;
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new FeederTileEntity();
	}
	
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(LEVEL, 0);    
    }
    
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
    	int lvl = 0;
    	if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("seedInv"))
    	{
            if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof FeederTileEntity)
            {
            
            	((FeederTileEntity)worldIn.getTileEntity(pos)).setSeeds(stack.getTagCompound().getInteger("seedInv"));
            	
            }
     	}
    	
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(LEVEL, state.getValue(LEVEL)), 2);
        
    	this.setFeederLevel(worldIn, pos, state);
    }
    
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    
    public boolean isFullyOpaque(IBlockState state)
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
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
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(LEVEL, Integer.valueOf((meta) >> 2 ));
    }
    
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
        i |= state.getValue(LEVEL).intValue() << 2;
        return i;
    }
    
    
    public static void setFeederLevel(World worldIn, BlockPos pos, IBlockState state)
    {
   	 	IBlockState iblockstate = worldIn.getBlockState(pos);
   	 	TileEntity tileentity = worldIn.getTileEntity(pos);
   	 	
   		int level = 0;
        if (tileentity != null && tileentity instanceof FeederTileEntity)
        {
        	FeederTileEntity te = (FeederTileEntity) tileentity;
        	float percentage = ((float)te.getSeedsInv()) / te.getMaxSeedInv();
        	level = MathHelper.ceiling_float_int(percentage * 3);
        }
		
        if(state.getValue(LEVEL).intValue() != level)
        {
        	worldIn.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING)).withProperty(LEVEL, Integer.valueOf(MathHelper.clamp_int(level, 0, 3))), 2);
        
        	if (tileentity != null)
        	{
        		tileentity.validate();
        		worldIn.setTileEntity(pos, tileentity);
        	}
        }
    }
    
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, LEVEL});
    }

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) 
	{
		
		TileEntity te = world.getTileEntity(data.getPos());
		if(te instanceof FeederTileEntity)
		{
			FeederTileEntity fte = (FeederTileEntity) te;
			probeInfo.text(TextFormatting.YELLOW + "Stored Seeds: "+ TextFormatting.GREEN + fte.getSeedsInv());
			
		}		
	}
}
