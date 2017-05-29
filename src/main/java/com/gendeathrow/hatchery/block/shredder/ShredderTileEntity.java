package com.gendeathrow.hatchery.block.shredder;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;

import com.gendeathrow.hatchery.block.InventoryStorage;
import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModItems;

public class ShredderTileEntity extends TileUpgradable implements ITickable, IInventory, IEnergyReceiver
{
	protected EnergyStorage energy= new EnergyStorage(100000);
	protected net.minecraftforge.energy.EnergyStorage energy2 = new net.minecraftforge.energy.EnergyStorage(100000);
	
	   
	public int animationTicks;
	public int prevAnimationTicks;

	protected InventoryStorage inventory = new InventoryStorage(this, 3);
	protected InventoryStorage upgrades = new InventoryStorage(this, 2);
    private int transferCooldown = -1;
	int slotIn = 0;
	
	public ShredderTileEntity() 
	{
		super(2);
	}

	@Override
	public void update() 
	{
		if (worldObj.isRemote) 
		{
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 5;
			if (animationTicks >= 360) 
			{
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}
		
		
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.transferCooldown;
            
            
            if (!this.isOnTransferCooldown())
            {
            	  this.captureDroppedItems();
                  this.setTransferCooldown(8);
            }
        }
	}

    public boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }
    
    public void setTransferCooldown(int cooldown)
    {
    	this.transferCooldown = cooldown;
    }
    
	public boolean captureDroppedItems()
	{
        EnumFacing enumfacing = EnumFacing.DOWN;

        if (isInventoryEmpty(this, enumfacing))
        {
            return false;
        }

        for (EntityItem entityitem : TileEntityHopper.getCaptureItems(this.worldObj, this.pos.getX(), this.pos.getY(), this.pos.getZ()))
        {
        	if(isShreddableItem(entityitem.getEntityItem()))
        	{
        		
        		return true;
        	}
        }
		
		return false;
	}
	
	protected boolean isShreddableItem(ItemStack stack)
	{
		return stack.getItem() == Items.FEATHER;
	}
	
	/// INVENTORY
    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint)
            {
                if (isidedinventory.getStackInSlot(i) != null)
                {
                    return false;
                }
            }
        }
        else
        {
            int j = inventoryIn.getSizeInventory();

            for (int k = 0; k < j; ++k)
            {
                if (inventoryIn.getStackInSlot(k) != null)
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    
		@Override
		public String getName() { return null; }

		@Override
		public boolean hasCustomName() { return false; }

		@Override
		public int getSizeInventory() 
		{
			return this.inventory.getSizeInventory();
		}

		@Override
		public ItemStack getStackInSlot(int slot) 
		{
			return this.inventory.getStackInSlot(slot);
		}

		@Override
		public ItemStack decrStackSize(int slot, int count) 
		{
			return this.inventory.decrStackSize(slot, count);
		}

		@Override
		public ItemStack removeStackFromSlot(int slot) 
		{
			return this.inventory.removeStackFromSlot(slot);
		}

		@Override
		public void setInventorySlotContents(int slot, ItemStack stack) 
		{
			this.inventory.setInventorySlotContents(slot, stack);
		}

		@Override
		public int getInventoryStackLimit() 
		{
			return 64;
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player) { return true; }

		@Override
		public void openInventory(EntityPlayer player) { this.inventory.openInventory(player); }

		@Override
		public void closeInventory(EntityPlayer player) { this.inventory.closeInventory(player); }

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) 
		{
			if(index == this.slotIn && this.isShreddableItem(stack))
					return true;
			else if(index > this.slotIn && (stack.getItem() == ModItems.featherFiber || stack.getItem() == ModItems.featherMeal))
				return false;
			return false; 
		}

		@Override
		public int getField(int id) 
		{ 
	        switch (id)
	        {
	            case 0:
	            	return this.energy.getEnergyStored();
	            default:
	                return 0;
	        }

		}
		
		@Override
		public void setField(int id, int value) 
		{ 
			
	        switch (id)
	        {
	            case 0:
	            	this.energy.setEnergyStored(value);
	                break;

	        }
		}

		@Override
		public int getFieldCount() { return 1; }

		@Override
		public void clear() { 
			this.inventory.clear();
		}
		
		

	    @Override
	    public void readFromNBT(NBTTagCompound tag)
	    {
	        super.readFromNBT(tag);
	        this.energy.readFromNBT(tag);
	        this.inventory.readFromNBT(tag);
	    }

	    @Override
	    public NBTTagCompound writeToNBT(NBTTagCompound tag)
	    {
	    	tag = super.writeToNBT(tag);
	    	
	        this.inventory.writeToNBT(tag);
	        this.energy.writeToNBT(tag);
	        return tag;
	    }


	    // ENERGY
	    
		@Override
		public int getEnergyStored(EnumFacing from) {
			return this.energy.getEnergyStored();
		}

		@Override
		public int getMaxEnergyStored(EnumFacing from) {
			return this.energy.getMaxEnergyStored();
		}

		@Override
		public boolean canConnectEnergy(EnumFacing from) {
			return true;
		}

		@Override
		public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
			return this.energy.receiveEnergy(maxReceive, simulate);
		}

		
	    @Override
	    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	    {
	        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	    }

	    @SuppressWarnings("unchecked")
	    @Override
	    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	    {
	    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
	        {
	            return (T) new InvWrapper(this);
	        }
	        return super.getCapability(capability, facing);
	    }

	    

}
