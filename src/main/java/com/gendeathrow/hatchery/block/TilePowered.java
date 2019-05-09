package com.gendeathrow.hatchery.block;

import com.gendeathrow.hatchery.storage.EnergyStorageRF;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TilePowered extends TileUpgradable{

	
	protected EnergyStorageRF energyStorage = new EnergyStorageRF(0);
	
	
	public TilePowered(int upgradeSize) {
		super(upgradeSize);
	}

	
	public final void setEnergyStored(int quantity) {
		energyStorage.setEnergyStored(quantity);
	}
	
	public IEnergyStorage getEnergyStorage() {
		return energyStorage;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		energyStorage.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		energyStorage.writeToNBT(nbt);
		return nbt;
	}
	
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {

		return energyStorage.receiveEnergy(maxReceive, simulate);
	}
	
	public int extractEnergy(EnumFacing from, int maxReceive, boolean simulate) {

		return energyStorage.extractEnergy(maxReceive, simulate);
	}

	public int getEnergyStored(EnumFacing from) {

		return energyStorage.getEnergyStored();
	}

	public int getMaxEnergyStored(EnumFacing from) {

		return energyStorage.getMaxEnergyStored();
	}

	public boolean canConnectEnergy(EnumFacing from) {

		return energyStorage.getMaxEnergyStored() > 0;
	}

	/**
	 * Code from COFH Core teams TilePowerd. With modifications. 
	 */
	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing from) {

		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(new net.minecraftforge.energy.IEnergyStorage() {

				@Override
				public int receiveEnergy(int maxReceive, boolean simulate) {

					return TilePowered.this.receiveEnergy(from, maxReceive, simulate);
				}

				@Override
				public int extractEnergy(int maxExtract, boolean simulate) {

					return 0;
				}

				@Override
				public int getEnergyStored() {

					return TilePowered.this.getEnergyStored(from);
				}

				@Override
				public int getMaxEnergyStored() {

					return TilePowered.this.getMaxEnergyStored(from);
				}

				@Override
				public boolean canExtract() {

					return false;
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
