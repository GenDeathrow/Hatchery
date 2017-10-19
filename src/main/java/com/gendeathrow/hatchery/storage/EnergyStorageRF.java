package com.gendeathrow.hatchery.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Reference implementation of {@link IEnergyStorage}. 
 * 
 * @author King Lemming
 */
public class EnergyStorageRF implements IEnergyStorage {

	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public EnergyStorageRF(int capacity) {

		this(capacity, capacity, capacity);
	}

	public EnergyStorageRF(int capacity, int maxTransfer) {

		this(capacity, maxTransfer, maxTransfer);
	}

	public EnergyStorageRF(int capacity, int maxReceive, int maxExtract) {

		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public EnergyStorageRF readFromNBT(NBTTagCompound nbt) {

		this.energy = nbt.getInteger("Energy");

		if (energy > capacity) {
			energy = capacity;
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (energy < 0) {
			energy = 0;
		}
		nbt.setInteger("Energy", energy);
		return nbt;
	}

	public EnergyStorageRF setCapacity(int capacity) {

		this.capacity = capacity;

		if (energy > capacity) {
			energy = capacity;
		}
		return this;
	}

	public EnergyStorageRF setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}

	public EnergyStorageRF setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
		return this;
	}

	public EnergyStorageRF setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
		return this;
	}

	public int getMaxReceive() {

		return maxReceive;
	}

	public int getMaxExtract() {

		return maxExtract;
	}

	/**
	 * This function is included to allow for server to client sync. Do not call this externally to the containing Tile Entity, as not all IEnergyHandlers are guaranteed to have it.
	 *
	 * @param energy
	 */
	public void setEnergyStored(int energy) {

		this.energy = energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	/**
	 * This function is included to allow the containing tile to directly and efficiently modify the energy contained in the EnergyStorage. Do not rely on this externally, as not all IEnergyHandlers are guaranteed to have it.
	 *
	 * @param energy
	 */
	public void modifyEnergyStored(int energy) {

		this.energy += energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	/* IEnergyStorage */
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {

		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {

		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {

		return energy;
	}

	@Override
	public int getMaxEnergyStored() {

		return capacity;
	}

	@Override
	public boolean canExtract() 
	{
		return true;
	}

	@Override
	public boolean canReceive() 
	{
		return true;
	}

}