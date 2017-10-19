package com.gendeathrow.hatchery.block.nest;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.theoneprobe.TOPInfoProvider;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class EggNestBlock extends Block implements ITileEntityProvider, TOPInfoProvider
{
    public static final PropertyBool hasEgg = PropertyBool.create("hasegg");
    
    protected static final AxisAlignedBB noEgg_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.15, 0.8125D);
    protected static final AxisAlignedBB withEgg_AABB = new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.25, 0.8125D);
    protected static final AxisAlignedBB[] BOUNDING_BOXES = new AxisAlignedBB[] {noEgg_AABB, withEgg_AABB};    

	protected String name;
	
	public EggNestBlock() 
	{
		super(Material.LEAVES);
		this.name = "nest";
		this.setUnlocalizedName("nest"); 
		//this.setRegistryName(Hatchery.MODID,"nest");
		this.setCreativeTab(Hatchery.hatcheryTabs);	
		this.setHardness(.2f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(hasEgg, false));
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		 if(worldIn.getTileEntity(pos)  != null && worldIn.getTileEntity(pos) instanceof EggNestTileEntity)
		 {
			 ItemStack stack = ((EggNestTileEntity)worldIn.getTileEntity(pos)).eggSlot[0];
			 if(stack != null)
			 {
				 this.spawnAsEntity(worldIn, pos, stack);
			 }
		 }
		 super.breakBlock(worldIn, pos, state);
	 }
	 
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(ModBlocks.nest);
    }
	 
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn)
	{ }

	
	@Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }
    
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDING_BOXES[state.getValue(hasEgg) ? 1 : 0];
    }
    
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List list) 
	{
	    list.add(new ItemStack(itemIn, 1, 0)); //Meta 0
	}
	
	@Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
    	return true;
    }
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
			
    	if(doesHaveEgg(state))
    	{
    			if(!worldIn.isRemote)
    			{
    				EggNestTileEntity te = ((EggNestTileEntity)worldIn.getTileEntity(pos));
    				ItemStack egg = te.removeStackFromSlot(0);
    				worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.getX(), pos.getY() + .5d, pos.getZ(), egg));
    			}
    			
     			this.removeEgg(worldIn, state, pos);
    		return true;
    	}
    	else if(heldItem != null && heldItem.getItem() instanceof ItemEgg)
    	{
    		this.addEgg(worldIn, state, pos);
    		
			if(!worldIn.isRemote)
			{
		        if (!playerIn.capabilities.isCreativeMode) 
		        {
		            --heldItem.stackSize;
		        }
    			
		        ItemStack itemstack = heldItem.copy();
		        itemstack.stackSize = 1;
				((EggNestTileEntity)worldIn.getTileEntity(pos)).setInventorySlotContents(0,itemstack);
    		}
    		
    		return true;
    	}

		return false;
    }
	
//	public boolean placeEgg(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem)
//	{
//    	
////    	if(heldItem != null && heldItem.getItem() instanceof ItemEgg)
////    	{
////    		System.out.println("noegg");
////    		
////    		this.addEgg(worldIn, state, pos);
////    		
////    		return true;
////    	}
//    	
//        return false;
//	}
    
	@Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
    	return false;
    }
    
    public static boolean doesHaveEgg(IBlockState state)
    {
    	if(state.getBlock() != ModBlocks.nest) return false;
    	
    	return state.getValue(hasEgg);
    }
    
    public static void addEgg(World worldIn, IBlockState state, BlockPos pos)
    {
    	worldIn.setBlockState(pos,state.withProperty(hasEgg, true));
    }
    
    public static void removeEgg(World worldIn, IBlockState state, BlockPos pos)
    {
    	worldIn.setBlockState(pos, state.withProperty(hasEgg, false));
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new EggNestTileEntity();
	}
	
	
	public static EggNestBlock create() 
	{
		EggNestBlock res = new EggNestBlock();
		return res;
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
	
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {hasEgg});
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
    	if(meta == 1)
    	{
    		return this.getDefaultState().withProperty(hasEgg, true);
    	}
    	else return this.getDefaultState().withProperty(hasEgg, false);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
    	if (((boolean)state.getValue(hasEgg)))
    	{
    		return 1;
    	}
        return 0;
    }

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) 
	{
		
		
		TileEntity te = world.getTileEntity(data.getPos());
		if(te instanceof EggNestTileEntity)
		{
			EggNestTileEntity hte = (EggNestTileEntity) te;
			
			if(hte.getStackInSlot(0) != null)
			{
				float percentage = hte.getPercentage();
				probeInfo.text(TextFormatting.YELLOW + "Hatching: "+ TextFormatting.GREEN + percentage +"%");
				probeInfo.text(TextFormatting.YELLOW + hte.eggSlot[0].getDisplayName());
			}
			else 
				probeInfo.text(TextFormatting.RED + "Not Hatching");
		}
	}

}
