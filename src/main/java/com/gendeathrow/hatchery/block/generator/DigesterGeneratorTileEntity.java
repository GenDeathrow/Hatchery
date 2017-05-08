package com.gendeathrow.hatchery.block.generator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

import com.gendeathrow.hatchery.api.items.IUpgradeItem;
import com.gendeathrow.hatchery.block.InventoryStorage;
import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.item.upgrades.RFEfficiencyUpgrade;

public class DigesterGeneratorTileEntity extends TileUpgradable implements IInventory, IEnergyProvider, ITickable
{
	public int time = 0;
	
	protected EnergyStorage storage = new EnergyStorage(200000);
	protected InventoryStorage inventory = new InventoryStorage(this, 2);
  
	private int rfPerTick = 20;
	private int baseRfPerTick = 20;
	
	private FluidTank fertlizerTank = new FluidTank(new FluidStack(ModFluids.liquidfertilizer, 0), 5000){
		@Override
	    public boolean canDrain()
	    {
			return false;
		}
	};
	
	public DigesterGeneratorTileEntity() 
	{
		super(2);
	}

	protected boolean canGenerate() {
		return fuelRF > 0 ? true : getTank().getFluidAmount() >= 50;
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	/* IEnergyProvider */
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return storage.extractEnergy(maxExtract, simulate);
	}

	/* IEnergyHandler */
	@Override
	public int getEnergyStored(EnumFacing from) {
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return storage.getMaxEnergyStored();
	}
	
	
	int rfEnergyFuel = 15000;
	int fuelRF;
	boolean isActive = true;

	
	@Override
	public void update() 
	{
		if(!this.worldObj.isRemote)
		{
			updateRFPerTick();
			
			if(isActive && canGenerate() && storage.getEnergyStored() < storage.getMaxEnergyStored())
			{
				if (fuelRF <= 0) 
				{
					fuelRF = rfEnergyFuel;
					getTank().drainInternal(50, true);
				}
				
				storage.modifyEnergyStored(getRFPerTick());
				fuelRF -= getRFPerTick();
			}
			
			if ((storage.getEnergyStored() > 0)) {
				for (EnumFacing facing : EnumFacing.VALUES) 
				{
						TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
						if (tile != null && tile instanceof IEnergyReceiver) 
						{
							int received = ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(), storage.getEnergyStored(), false);
							extractEnergy(facing, received, false);
						}
				}
			}
			
			if(this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(1) == null && this.fertlizerTank.getFluidAmount() < this.fertlizerTank.getCapacity())
			{
				ItemStack stack = this.inventory.getStackInSlot(0);
				
				IFluidHandler handler = FluidUtil.getFluidHandler(stack);
				
				if(handler != null)
				{
					if(FluidUtil.tryFluidTransfer(this.fertlizerTank, handler, this.fertlizerTank.getCapacity(), true) != null)
					{
						this.inventory.setInventorySlotContents(1, stack);
						this.inventory.setInventorySlotContents(0, null);
					}
				}
			}
		}
	}
	

	public FluidTank getTank()
	{
		return fertlizerTank;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		this.inventory.readFromNBT(nbt);
		this.storage.readFromNBT(nbt);
		this.fertlizerTank.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = this.inventory.writeToNBT(nbt);
		nbt = this.storage.writeToNBT(nbt);
		nbt = this.fertlizerTank.writeToNBT(nbt);
		return super.writeToNBT(nbt);
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
    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) this.fertlizerTank;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
        {
            return (T) new InvWrapper(this);
        }
        return super.getCapability(capability, facing);
    }
    
    // INVENTORY
	@Override
	public ItemStack removeStackFromSlot(int index){
		return this.inventory.removeStackFromSlot(index);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}


	@Override
	public boolean hasCustomName() {
		return false;
	}


	@Override
	public int getSizeInventory() {
		return this.inventory.getSizeInventory();
	}


	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.getStackInSlot(index);
	}


	@Override
	public ItemStack decrStackSize(int index, int count) {
		return this.inventory.decrStackSize(index, count);
	}


	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory.setInventorySlotContents(index, stack);
	}


	@Override
	public int getInventoryStackLimit() {
		return 64;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}


	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	public float tankLevel;

	@Override
	public int getField(int id) 
	{
        switch (id)
        {
            case 0:
            	return this.storage.getEnergyStored();
            case 1:
            	return this.getTank().getFluidAmount();
            case 2:
            	return this.rfPerTick;
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
            	this.storage.setEnergyStored(value);
                break;
            case 1:
            	this.tankLevel = value;
            	break;
            case 2:
            	this.rfPerTick = value;
                break;
        }	
	}


	@Override
	public int getFieldCount() {
		return 3;
	}


	@Override
	public void clear() {
		this.inventory.clear();
	}
	
	
	public int getRFPerTick() 
	{
		return this.rfPerTick;
	}

	protected void updateRFPerTick()
	{
		boolean rfupgrade = false;
		
		for(ItemStack upgrade : this.getUpgrades())
		{
			if(upgrade == null) continue;
		
			
			if(upgrade.getItem() instanceof RFEfficiencyUpgrade)
			{
				rfupgrade = true;
				
				int newTick = ((RFEfficiencyUpgrade)upgrade.getItem()).getUpgradeTier(upgrade, "") * 20 + 20;
				if(newTick > this.getRFPerTick())
				{
					this.rfPerTick = newTick;
				}
			}
		}
		
		if(!rfupgrade) 
			this.rfPerTick = this.baseRfPerTick;
	}
}
