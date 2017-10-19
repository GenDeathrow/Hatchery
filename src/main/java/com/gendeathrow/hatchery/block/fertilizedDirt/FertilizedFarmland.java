package com.gendeathrow.hatchery.block.fertilizedDirt;

import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FertilizedFarmland extends Block
{
    public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
    protected static final AxisAlignedBB FARMLAND_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

    public FertilizedFarmland()
    {
        super(Material.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(0)));
        this.setHardness(1);
        this.setHarvestLevel("shovel", 0);
        this.setUnlocalizedName("fertilized_farmland");
        this.setTickRandomly(true);
        this.setSoundType(SoundType.GROUND);
        this.setLightOpacity(255);
        
    }

    @Override
    public boolean getUseNeighborBrightness(IBlockState state)
    {
        return true;
    }
    
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return FARMLAND_AABB;
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }
    
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    public boolean isVisuallyOpaque()
    {
        return false;
    }
    
    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int i = ((Integer)state.getValue(MOISTURE)).intValue();
        
        if (!this.hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up()))
        {
            if (i > 0)
            {
                worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(i - 1)), 2);
            }
            else if (!this.hasCrops(worldIn, pos))
            {
                worldIn.setBlockState(pos, ModBlocks.fertlizedDirt.getDefaultState());
            }
        }
        else if (i < 7)
        {
            worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(7)), 2);
        }
        
        if(this.hasCrops(worldIn, pos) && this.hasWater(worldIn, pos) && rand.nextInt(2) == 0)
        {
            IBlockState iblockstate = worldIn.getBlockState(pos.up());

            if (iblockstate.getBlock() instanceof IGrowable)
            {
                IGrowable igrowable = (IGrowable)iblockstate.getBlock();

                if (igrowable.canGrow(worldIn, pos.up(), iblockstate, worldIn.isRemote))
                {
                	Block o = Blocks.FARMLAND;
                    if (!worldIn.isRemote)
                    {
                        if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos.up(), iblockstate))
                        {
                            igrowable.grow(worldIn, worldIn.rand, pos.up(), iblockstate);
                            worldIn.playEvent(2005, pos.up(), 0);
                        }
                        else 
                        {
                        	worldIn.scheduleBlockUpdate(pos.up(), iblockstate.getBlock(), 20, 1);
                        	iblockstate.getBlock().updateTick(worldIn, pos.up(), iblockstate, rand);
//                        	iblockstate.getBlock().updateTick(worldIn, pos.up(), iblockstate, rand);
//                        	iblockstate.getBlock().updateTick(worldIn, pos.up(), iblockstate, rand);
//                        	iblockstate.getBlock().updateTick(worldIn, pos.up(), iblockstate, rand);
                        	worldIn.playEvent(2005, pos.up(), 0);
                        }

                    }

                }
            }
        }
    }

    /**
     * Block's chance to react to a living entity falling on it.
     */
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
        if (!worldIn.isRemote && worldIn.rand.nextFloat() < fallDistance - 0.5F && entityIn instanceof EntityLivingBase && (entityIn instanceof EntityPlayer || worldIn.getGameRules().getBoolean("mobGriefing")) && entityIn.width * entityIn.width * entityIn.height > 0.512F)
        {
            worldIn.setBlockState(pos, ModBlocks.fertlizedDirt.getDefaultState());
        }

        super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }

    private boolean hasCrops(World worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return block instanceof net.minecraftforge.common.IPlantable && canSustainPlant(worldIn.getBlockState(pos), worldIn, pos, net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)block);
    }

    private boolean hasWater(World worldIn, BlockPos pos)
    {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
        {
            if (worldIn.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        super.neighborChanged(state, worldIn, pos, blockIn);

        if (worldIn.getBlockState(pos.up()).getMaterial().isSolid())
        {
            worldIn.setBlockState(pos, ModBlocks.fertlizedDirt.getDefaultState());
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        switch (side)
        {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                Block block = iblockstate.getBlock();
                return !iblockstate.isOpaqueCube() && block != ModBlocks.fertilzedFarmland;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModBlocks.fertlizedDirt.getItemDropped(ModBlocks.fertlizedDirt.getDefaultState(), rand, fortune);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ModBlocks.fertlizedDirt);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(MOISTURE, Integer.valueOf(meta & 7));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(MOISTURE)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {MOISTURE});
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
    	//return true;
    	return (side != EnumFacing.DOWN && side != EnumFacing.UP);
    }
    
    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));
        net.minecraftforge.common.EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));

        //System.out.println(plantable.getClass().getSimpleName());
        if (plant.getBlock() instanceof BlockBush)
        {
            return true;
        }

        return false;
    }
    
    @Override
    public boolean isFertile(World world, BlockPos pos)
    {
        return true;
    }
    

    
}
