package com.gendeathrow.hatchery.block.nursery;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

public class TileEntityMobNursery extends TileEntity implements ITickable {

	public TileEntityMobNursery() 
	{
		super();
	}

	@Override
	public void update() 
	{
		if (worldObj.isRemote)
			return;
		
		boolean isDayCycle = worldObj.getGameRules().getBoolean("doDaylightCycle");

		if ((isDayCycle ? worldObj.getWorldTime() % 10 == 0 : worldObj.rand.nextInt(9) == 0) && worldObj.getBlockState(pos).getBlock() != null) 
		{
			checkForAdult();
			checkForChild();
		}
	}

	@SuppressWarnings("unchecked")
	protected Entity checkForAdult() 
	{
		IBlockState state = getWorld().getBlockState(pos);
		EnumFacing facing = state.getValue(BlockMobNursery.FACING);
		float xPos = 1F, zPos = 1F;
		float xNeg = 1F,  zNeg = 1F;

		if(facing == EnumFacing.WEST) {
			xNeg = 4F;
			xPos = - 1F;
		}

		if(facing == EnumFacing.EAST) {
			xNeg = - 1F;
			xPos = 4F;
		}

		if(facing == EnumFacing.NORTH) {
			zNeg = 4F;
			zPos = - 1F;
		}

		if(facing == EnumFacing.SOUTH) {
			zNeg = - 1F;
			zPos = 4F;
		}

		List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(pos.getX() - xNeg, pos.getY(), pos.getZ() - zNeg, pos.getX() + 1D  + xPos, pos.getY() + 1D, pos.getZ() + 1D  + zPos));
		for (int i = 0; i < list.size(); i++) 
		{
			Entity entity = list.get(i);
			if (entity != null) {
				if (entity instanceof EntityLivingBase && !((EntityLivingBase) entity).isChild())
					entity.setPosition(pos.getX() + 0.5D + MathHelper.sin(facing.getHorizontalAngle() * 3.141593F / 180.0F) * 1.5D, pos.getY(), pos.getZ() + 0.5D +  -MathHelper.cos(facing.getHorizontalAngle() * 3.141593F / 180.0F) * 1.5D);
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Entity checkForChild() 
	{
		
		IBlockState state = getWorld().getBlockState(pos);
		EnumFacing facing = state.getValue(BlockMobNursery.FACING);
		float xPos = 1F, zPos = 1F;
		float xNeg = 1F, zNeg = 1F;

		if(facing == EnumFacing.EAST) {
			xNeg = 4F;
			xPos = 1F;
		}

		if(facing == EnumFacing.WEST) {
			xNeg = 1F;
			xPos = 4F;
		}

		if(facing == EnumFacing.SOUTH) {
			zNeg = 4F;
			zPos = 1F;
		}

		if(facing == EnumFacing.NORTH) {
			zNeg = 1F;
			zPos = 4F;
		}

		List<EntityAnimal> list = worldObj.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(pos.getX() - xNeg, pos.getY(), pos.getZ() - zNeg, pos.getX() + 1D  + xPos, pos.getY() + 1D, pos.getZ() + 1D  + zPos));
		for (int i = 0; i < list.size(); i++) 
		{
			Entity entity = list.get(i);
			if (entity != null) 
			{
				if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).isChild())
					entity.setPosition(pos.getX() + 0.5D + MathHelper.sin(facing.getOpposite().getHorizontalAngle() * 3.141593F / 180.0F) * 1.5D, pos.getY(), pos.getZ() + 0.5D + -MathHelper.cos(facing.getOpposite().getHorizontalAngle() * 3.141593F / 180.0F) * 1.5D);
			}
		}
		return null;
	}
}

