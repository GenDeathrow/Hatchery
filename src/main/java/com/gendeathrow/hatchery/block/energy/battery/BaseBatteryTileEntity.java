package com.gendeathrow.hatchery.block.energy.battery;

import com.gendeathrow.hatchery.block.TilePowered;
import com.gendeathrow.hatchery.storage.ISidedInterface;
import com.gendeathrow.hatchery.storage.SidedInterface;
import com.gendeathrow.hatchery.storage.SidedInterface.InterfaceOption;
import com.gendeathrow.hatchery.storage.SidedInterface.InterfaceType;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BaseBatteryTileEntity extends TilePowered implements ITickable, ISidedInterface{

	boolean isCreative = false;
	protected SidedInterface sideInterface = new SidedInterface(InterfaceOption.PowerInput, InterfaceOption.PowerOutput);
	
	public BaseBatteryTileEntity() {
		super(1);
		this.energyStorage.setCapacity(400000).setMaxTransfer(4096);
		sideInterface.setInterfaceOnFacing(InterfaceOption.PowerInput, EnumFacing.VALUES);
	}
	
	
	@Override
	public SidedInterface getInterface() {
		return sideInterface;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.energyStorage.readFromNBT(nbt);
		sideInterface.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = this.energyStorage.writeToNBT(nbt);
		sideInterface.writeToNBT(nbt);
		return super.writeToNBT(nbt);
	}
	
	@Override
	public void update() 
	{
		transferEnergy();
	}
	
	protected void transferEnergy() {
		for (EnumFacing facing : EnumFacing.values()) 
		{
			if (this.getEnergyStorage().getEnergyStored() > 0 && this.sideInterface.getInterfaceOnFacing(facing, InterfaceType.Power) == InterfaceOption.PowerOutput) {
				TileEntity reciever = this.world.getTileEntity(this.pos.offset(facing));
				if(reciever != null && reciever.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()))
				{
					IEnergyStorage enegeryReveiever = reciever.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
					if (isCreative) {
						enegeryReveiever.receiveEnergy(this.energyStorage.getMaxExtract(), false);
					} else {
						int extraction = this.extractEnergy(facing, this.energyStorage.getMaxExtract(), true);
						int leftover = enegeryReveiever.receiveEnergy(extraction, false);
						this.extractEnergy(facing, leftover, false);
					}
				}
			}
		}
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

    @Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
    	if(this.sideInterface.getInterfaceOnFacing(from, InterfaceType.Power) == InterfaceOption.PowerInput) {
    		return energyStorage.receiveEnergy(maxReceive, simulate);
    	}
    	return 0;
	}
    
    @Override
	public int extractEnergy(EnumFacing from, int maxReceive, boolean simulate) {
    	if(this.sideInterface.getInterfaceOnFacing(from, InterfaceType.Power) == InterfaceOption.PowerOutput)
    		return energyStorage.extractEnergy(maxReceive, simulate);
    	return 0;
	}
    
	@Override
	public int getEnergyStored(EnumFacing from) {
		return energyStorage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energyStorage.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return energyStorage.getMaxEnergyStored() > 0;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing from) {

		
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(new net.minecraftforge.energy.IEnergyStorage() {

				@Override
				public int receiveEnergy(int maxReceive, boolean simulate) {

					return BaseBatteryTileEntity.this.receiveEnergy(from, maxReceive, simulate);
				}

				@Override
				public int extractEnergy(int maxExtract, boolean simulate) {

					return BaseBatteryTileEntity.this.extractEnergy(from, maxExtract, simulate);
				}

				@Override
				public int getEnergyStored() {

					return BaseBatteryTileEntity.this.getEnergyStored(from);
				}

				@Override
				public int getMaxEnergyStored() {

					return BaseBatteryTileEntity.this.getMaxEnergyStored(from);
				}

				@Override
				public boolean canExtract() {

					return true;
				}

				@Override
				public boolean canReceive() {

					return true;
				}
			});
		}
		return super.getCapability(capability, from);
	}


}
