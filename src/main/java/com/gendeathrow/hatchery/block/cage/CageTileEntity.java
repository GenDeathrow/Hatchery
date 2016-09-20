package com.gendeathrow.hatchery.block.cage;

import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class CageTileEntity extends TileEntity  implements ITickable, IInventory
{
	
	private EntityChicken chickenStored;
	
	
	private void spawnStoredEntity()
	{
		
	}

	@Override
	public void update() 
	{
		
		//chickenStored.onUpdate();
//		if(chickenStored != null)
//		{
//			//chickenStored.get
//
//			float yaw = NestPenBlock.getFacing(worldObj.getBlockState(this.getPos())).getHorizontalAngle();
//			
//			chickenStored.setPositionAndRotation(getPos().getX()+.5, getPos().getY() + .2, getPos().getZ()+.5, yaw, 0);			
//			
//		}else
//		{
//			chickenStored = new EntityChicken(worldObj);
//			chickenStored.setPosition(getPos().getX()+.5, getPos().getY() + .2, getPos().getZ()+.5);
//			chickenStored.setNoAI(true);
//			//chickenStored.setEntityBoundingBox(new AxisAlignedBB(.5, .5, .5, .51, .51, .51));
//			worldObj.spawnEntityInWorld(chickenStored);
//		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{

		super.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{

		return super.writeToNBT(compound);
	}
	
	//////////////////////////////////////////////////////////////////
	// Inventory
	/////////////////////////////////////////////////////////////////
	@Override
	public String getName() 
	{
		return null;
	}


	@Override
	public boolean hasCustomName() 
	{
		return false;
	}


	@Override
	public int getSizeInventory() 
	{
		return 0;
	}


	@Override
	public ItemStack getStackInSlot(int index) 
	{
		return null;
	}


	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		return null;
	}


	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return null;
	}


	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		
	}


	@Override
	public int getInventoryStackLimit() 
	{
		return 0;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return false;
	}


	@Override
	public void openInventory(EntityPlayer player) 
	{
		
	}


	@Override
	public void closeInventory(EntityPlayer player) 
	{
		
	}


	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return false;
	}


	@Override
	public int getField(int id) 
	{
		return 0;
	}


	@Override
	public void setField(int id, int value) { }


	@Override
	public int getFieldCount() 
	{
		return 0;
	}


	@Override
	public void clear() {}



}
