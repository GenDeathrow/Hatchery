package com.gendeathrow.hatchery.block.feeder;

import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class FeederTileEntity extends TileEntity implements ITickable
{

	private int seedInventory = 0;
	private int maxSeedInventory = 200;
	
	protected InventoryStroageModifiable inventory = new InventoryStroageModifiable("inputItems", 1) {
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{ return isItemValidForSlot(slot, stack); }
		@Override
		public boolean canExtractSlot(int slot)	{ return false; }
		
	};
	
	
	int ticksExisted = 0;
	
	@Override
	public void update() {
		System.out.println("update");

		if(ticksExisted++%5 == 0 && !world.isRemote) {
			convertSeeds();
			ticksExisted = 0;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		this.seedInventory = compound.getInteger("seeds");
		
		super.readFromNBT(compound);
	}
	
	public void convertSeeds() {
		if(this.hasSeeds() && getSeedsSlot().getCount() >= 2 && this.seedInventory + 2 <= this.maxSeedInventory) {
			this.seedInventory += 2;
			this.getSeedsSlot().shrink(2);
			FeederBlock.setFeederLevel(this.world, pos, world.getBlockState(pos));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger("seeds", this.seedInventory);
		return super.writeToNBT(compound);
	}
	
	public ItemStack getSeedsSlot()
	{
		return this.inventory.getStackInSlot(0);
	}
	
	
	public boolean hasSeeds() {
		return !getSeedsSlot().isEmpty();
	}
	
	public int getSeedsInv()
	{
		return this.seedInventory;
	}
	
	public void decrSeedsInv()
	{
		this.seedInventory--;
	}
	
	public int getMaxSeedInv()
	{
		return this.maxSeedInventory;
	}

	////////////////////////////////////////////////////
	// Inventory
	////////////////////////////////////////////////////
	public void dropContents()
	{
		
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
        	ItemStack stack = this.inventory.getStackInSlot(i);
        	
        	if(!stack.isEmpty())
        	{
        		this.world.spawnEntity(new EntityItem(world, this.pos.getX(), this.pos.getY()+1, this.pos.getZ(), stack));
        	}
        }
	}
	
	public void setSeeds(int qty)
	{
		this.seedInventory = qty;
	}

	boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return this.seedInventory < this.maxSeedInventory ? (stack.getItem() instanceof ItemSeeds || stack.getItem() == ModItems.chickenFeed) : false;
	}
	
	public void setSeeds(int qty, ItemStack stack, boolean creative)
	{
		if(isItemValidForSlot(0, stack))
		{
			if (!stack.isEmpty() && seedInventory < this.maxSeedInventory)
			{
				this.seedInventory += qty;
				if(this.seedInventory > this.maxSeedInventory)
				{
					qty = qty - (this.seedInventory - this.maxSeedInventory);
					this.seedInventory = this.maxSeedInventory;
				}
				
				if(!creative)
				{
					stack.shrink(qty);
				}

				FeederBlock.setFeederLevel(this.world, pos, world.getBlockState(pos));
			}
		}
        
		this.markDirty();	
	}
	

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
        {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
        
    }
}
