package com.gendeathrow.hatchery.block.nestpen;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.hatchery.core.init.ModBlocks;

public class NestingPenBlock extends Block implements ITileEntityProvider
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	//public static final PropertyBool hasChicken = PropertyBool.create("false");
	
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.00D, 0.0D, 0.00, 1.0D, 1.2D, 1.0D);
    
    protected static final AxisAlignedBB NORTH_STAIRS_AABB = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.6875D,0.125D, 0.1875D);
    protected static final AxisAlignedBB NORTH_BASE_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.25D, 0.9375D,0.125D, 1.0D);
    protected static final AxisAlignedBB NORTH_SOUTH_WALL_AABB = new AxisAlignedBB(0.125D, 0.1875D, 0.9375D, 0.9375D, 1.6D, 1.0D);
    protected static final AxisAlignedBB NORTH_WALL_AABB = new AxisAlignedBB(0.0625D, 0.1875D, 0.25D, 0.125D, 1.6D, 1.0D);
    
    protected static final AxisAlignedBB NORTH_WEST_WALL_AABB = new AxisAlignedBB(0.0625D, 0.1875D, 0.25D, 0.125D, 1.6D, 1.0D);
    protected static final AxisAlignedBB NORTH_EAST_WALL_AABB = new AxisAlignedBB(0.81250D, 0.1875D, 0.25D, 0.9375D, 1.6D, 1.0D);
    protected static final AxisAlignedBB[] NORTH_FACING_AABB = new AxisAlignedBB[] {NORTH_STAIRS_AABB, NORTH_BASE_AABB, NORTH_SOUTH_WALL_AABB, NORTH_WEST_WALL_AABB, NORTH_EAST_WALL_AABB};
    
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {AABB};    

	protected String name;
    
	private static boolean keepInventory = false;
    
	public NestingPenBlock() 
	{
		super(Material.WOOD);
		this.name = "pen";
		this.setUnlocalizedName("pen");
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setHardness(2);
		this.setHarvestLevel("axe", 0);
	}

	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
    	if(!worldIn.isRemote)
    	{
    		NestPenTileEntity te = (NestPenTileEntity)worldIn.getTileEntity(pos);
    		
    		if(te == null) return false;
    		    		
    		return te.grabItems(playerIn);
    	}
    	return true;
    }
	
	
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!keepInventory)
        {
    		NestPenTileEntity te = (NestPenTileEntity)worldIn.getTileEntity(pos);
    		
    		te.dropContents();
    		
    		if(te.storedEntity() != null)
    		{
    			te.storedEntity().setPosition(te.getPos().getX() + .5, te.getPos().getY(), te.getPos().getZ() + .5);
    			worldIn.spawnEntityInWorld(te.storedEntity());
    		}
    		
        }
        super.breakBlock(worldIn, pos, state);
    }
    
    
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.pen);
    }
    
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
    }
    
    public static void setState(boolean hasChicken, World worldIn, BlockPos pos)
    {
    	 IBlockState iblockstate = worldIn.getBlockState(pos);
         TileEntity tileentity = worldIn.getTileEntity(pos);
         keepInventory = true;

         if (hasChicken)
         {
             worldIn.setBlockState(pos, ModBlocks.pen_chicken.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)));
         }
         else
         {
             worldIn.setBlockState(pos, ModBlocks.pen.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)));
         }
			
         
         keepInventory = false;

         if (tileentity != null)
         {
             tileentity.validate();
             worldIn.setTileEntity(pos, tileentity);
             if(!worldIn.isRemote)
            	 worldIn.notifyBlockUpdate(pos, worldIn.getBlockState(pos), worldIn.getBlockState(pos), 2); 
         }
    }
    
    public static boolean hasChicken(IBlockState state)
    {
		return state.getBlock() == ModBlocks.pen_chicken;
    }
    
    public static EntityChicken getNearByMate(World world, IBlockState state, BlockPos pos)
    {
        TileEntity tileentity = world.getTileEntity(pos);

        if (!(tileentity instanceof NestPenTileEntity))
        {
            return null;
        }
        else
        {
        	NestPenTileEntity pen = (NestPenTileEntity) tileentity;
        	
        	for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        	{
        		if(enumfacing == EnumFacing.UP || enumfacing == EnumFacing.DOWN) continue;
        	
            		BlockPos blockpos = pos.offset(enumfacing);
            		Block block = world.getBlockState(blockpos).getBlock();
            		
                    if (block instanceof NestingPenBlock)
                    {
                    	TileEntity tileentity1 = world.getTileEntity(blockpos);

                        if (tileentity1 instanceof NestPenTileEntity)
                        {
                        	NestPenTileEntity penMate =  (NestPenTileEntity) tileentity1;
                        	EntityAnimal targetmate = (EntityAnimal) penMate.storedEntity();

                        	if(targetmate != null && pen.storedEntity() != null && pen.storedEntity().getClass() == targetmate.getClass())
                        	{
                        		return (EntityChicken) ((NestPenTileEntity) tileentity1).storedEntity();
                        	}
                        }
                    }
        	}
        }
    	
    	return null;
    }
    
    
    
    private boolean isFeederNear()
    {
      	return false;
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
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }
    
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new NestPenTileEntity();
	}
	
	public static EnumFacing getFacing(IBlockState blockStateContainer)
	{
		return (EnumFacing)blockStateContainer.getValue(FACING);
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
	{
		addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
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

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }


    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
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
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }
    
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	return false;
    }
	
    

}
