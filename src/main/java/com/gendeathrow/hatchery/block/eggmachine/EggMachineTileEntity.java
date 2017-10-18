package com.gendeathrow.hatchery.block.eggmachine;

import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.inventory.InventoryStorage;
import com.gendeathrow.hatchery.item.upgrades.RFEfficiencyUpgrade;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

public class EggMachineTileEntity extends TileUpgradable implements ITickable, IEnergyReceiver
{

	public int EggInSlot = 0;
	public int PlasticInSlot = 1;
	public int PrizeEggSlot = 0;
	
	public EggMachineTileEntity() {
		super(2);
	}
	
	
	protected InventoryStroageModifiable inputInventory = new InventoryStroageModifiable("inputItems", 2)
	{
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			if(slot == EggInSlot && stack.getItem() instanceof ItemEgg) {
				return true;
			}
			else if(slot == PlasticInSlot && stack.getItem() == ModItems.plastic) {
				return true;
			}
			return false;
		}
	};
	
	
	protected InventoryStroageModifiable outputInventory = new InventoryStroageModifiable("outputItems", 1)
	{
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			return false;
		}
	};
	
	
	protected InventoryStorage inventorys = new InventoryStorage(this, 3)
	{
		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			if(index == EggInSlot && stack.getItem() instanceof ItemEgg) {
				return true;
			}
			else if(index == PlasticInSlot && stack.getItem() == ModItems.plastic) {
				return true;
			}
			return false;
		}
	};
	
	protected EnergyStorageRF energy = new EnergyStorageRF(20000){
		@Override
		public boolean canExtract() {
			return false;
		}
	}.setMaxReceive(100);
	
	protected int internalEggStorage = 0;
	protected int internalPlasticStorage = 0;
	
	private int eggToPrizeSize = 24;
	
	public int eggTime = 0;
	public int maxEggTime = 200;

	
	float zeroedFacing;
	float currentFacing;
	float prevAnimationTicks;
	float animationTicks;
	boolean firstRun = true;
	
	public void updateClient()
	{
		if(firstRun)
		{
			firstRun = false;
			EnumFacing facing = EggMachineBlock.getFacing(this.worldObj.getBlockState(this.pos));
			this.zeroedFacing = facing.getHorizontalAngle();
			animationTicks = this.zeroedFacing;
		}
		
		prevAnimationTicks = animationTicks;
		if (animationTicks < 360)
			animationTicks += 5;
		if (animationTicks >= 360) 
		{
			animationTicks -= 360;
			prevAnimationTicks -= 360;
		}
		
	}
	
	@Override
	public void update() {
		
		if(this.worldObj.isRemote)
			this.updateClient();
		
		ItemStack eggIn = this.inputInventory.getStackInSlot(this.EggInSlot);
		ItemStack plasticIn = this.inputInventory.getStackInSlot(this.PlasticInSlot);
		
		if(eggIn != null && eggIn.getItem() instanceof ItemEgg) {
			this.internalEggStorage += eggIn.stackSize;
			this.inputInventory.setStackInSlot(this.EggInSlot, null);
		}
		
		if(plasticIn != null && plasticIn.getItem() == ModItems.plastic) {
			this.internalPlasticStorage += plasticIn.stackSize;
			this.inputInventory.setStackInSlot(this.PlasticInSlot, null);
		}

		if(this.eggTime <= 0 && this.canMakePrizeEgg())	{
			this.eggTime = maxEggTime;
			this.internalEggStorage -= eggToPrizeSize;
			this.internalPlasticStorage-= 2;
			
			this.markDirty();
		}
		
		boolean hasTimeLeft = this.eggTime > 0;
		ItemStack prizeSlot = this.outputInventory.getStackInSlot(this.PrizeEggSlot);
		boolean hasRoomForEgg = prizeSlot == null ? true : prizeSlot.stackSize < prizeSlot.getMaxStackSize();
		
        if (!this.worldObj.isRemote){
    		if(hasTimeLeft && this.energy.getEnergyStored() >= 40 && hasRoomForEgg){
    			--eggTime;
    			this.energy.extractEnergy(40, false);
    			
    			if(this.eggTime <= 0) {
    				this.createPrizeEgg();
    				this.markDirty();
    			}
    			
    		}
        }
	}
	
	
	private boolean canMakePrizeEgg(){
		if(eggToPrizeSize <= internalEggStorage && internalPlasticStorage >= 2) {
			return true;
		}
		else
			return false;
	}
	
	
	
	private void createPrizeEgg()
	{
		ItemStack itemstack = new ItemStack(ModItems.prizeEgg);
		
		ItemStack eggStack = this.outputInventory.getStackInSlot(this.PrizeEggSlot);

		if(eggStack == null)
			this.outputInventory.setStackInSlot(this.PrizeEggSlot, itemstack);
		else if(eggStack.getItem() == ModItems.prizeEgg && eggStack.stackSize < eggStack.getMaxStackSize())
		{
			eggStack.stackSize++;
		}
	}

	@Override
	public boolean canUseUpgrade(ItemStack item)
	{
		return item.getItem() instanceof RFEfficiencyUpgrade || item.getItem() == ModItems.speedUpgradeTier || item.getItem() == ModItems.rfCapacityUpgradeTier1;
	}
	
	
    public void readFromNBT(NBTTagCompound compound)
    {
    	super.readFromNBT(compound);
    	this.inputInventory.readFromNBT(compound);
    	this.outputInventory.readFromNBT(compound);
    	this.energy.readFromNBT(compound);
    	
    	this.internalEggStorage = compound.getInteger("EggStorage");
    	this.internalPlasticStorage = compound.getInteger("PlasticStorage");
    	
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	super.writeToNBT(compound);
    	this.inputInventory.writeToNBT(compound);
    	this.outputInventory.writeToNBT(compound);
    	this.energy.writeToNBT(compound);
    	compound.setInteger("EggStorage", this.internalEggStorage);
    	compound.setInteger("PlasticStorage", this.internalPlasticStorage);
		return compound;
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN)
        	return (T) this.outputInventory;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        	return (T) this.inputInventory;
            //return (T) new InvWrapper(this);
		else if (capability == CapabilityEnergy.ENERGY) 
			return (T) this.energy;
        return super.getCapability(capability, facing);
    }


//	@Override
//	public String getName() {
//		return this.inventory.getName();
//	}
//
//	@Override
//	public boolean hasCustomName() {
//		return this.inventory.hasCustomName();
//	}
//
//
//	@Override
//	public int getSizeInventory() {
//		return this.inventory.getSizeInventory();
//	}
//
//
//	@Override
//	public ItemStack getStackInSlot(int index) {
//		return this.inventory.getStackInSlot(index);
//	}
//
//
//	@Override
//	public ItemStack decrStackSize(int index, int count) {
//		return this.inventory.decrStackSize(index, count);
//	}
//
//
//	@Override
//	public ItemStack removeStackFromSlot(int index) {
//		return this.inventory.removeStackFromSlot(index);
//	}
//
//
//	@Override
//	public void setInventorySlotContents(int index, ItemStack stack) {
//		this.inventory.setInventorySlotContents(index, stack);
//	}
//
//
//	@Override
//	public int getInventoryStackLimit() {
//		return this.inventory.getInventoryStackLimit();
//	}
//
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer player) {
//		return this.inventory.isUseableByPlayer(player);
//	}
//
//
//	@Override
//	public void openInventory(EntityPlayer player) {
//		this.inventory.openInventory(player);
//	}
//
//
//	@Override
//	public void closeInventory(EntityPlayer player) {
//		this.inventory.closeInventory(player);
//	}
//
//
//	@Override
//	public boolean isItemValidForSlot(int index, ItemStack stack) {
//		return this.inventory.isItemValidForSlot(index, stack);
//	}
//
//
//	@Override
	public int getField(int id) {
		
		switch(id) {
			case 0:
				return this.energy.getEnergyStored();
			case 1:
				return this.internalEggStorage;
			case 2:
				return this.internalPlasticStorage;
			case 3:
				return this.eggTime;
		}
		return 0;
	}


//	@Override
	public void setField(int id, int value) {
		
		switch(id) {
			case 0:
				this.energy.setEnergyStored(value);
				break;
			case 1:
				this.internalEggStorage = value;
				break;
			case 2:
				this.internalPlasticStorage = value;
				break;
			case 3:
				this.eggTime = value;
				break;
		}
	}


//	@Override
	public int getFieldCount() {
		return 4;
	}


//	@Override
//	public void clear() {
//		this.inventory.clear();
//	}
//

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

}
