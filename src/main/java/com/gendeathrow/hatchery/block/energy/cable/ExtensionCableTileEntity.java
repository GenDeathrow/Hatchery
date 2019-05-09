package com.gendeathrow.hatchery.block.energy.cable;

import com.gendeathrow.hatchery.block.TilePowered;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class ExtensionCableTileEntity extends TilePowered {
	
	
	public ExtensionCableTileEntity linkedCable = null;
	
	
	public ExtensionCableTileEntity() {
		super(0);
	}
	
	
	
	public void addLikedCable(ExtensionCableTileEntity cable) {
		linkedCable = cable;
	}
	
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
    }

	@Override
	public <T> T getCapability(Capability<T> capability, final EnumFacing from) {

		
		if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(new net.minecraftforge.energy.IEnergyStorage() {

				@Override
				public int receiveEnergy(int maxReceive, boolean simulate) {

					return ExtensionCableTileEntity.this.receiveEnergy(from, maxReceive, simulate);
				}

				@Override
				public int extractEnergy(int maxExtract, boolean simulate) {

					return ExtensionCableTileEntity.this.extractEnergy(from, maxExtract, simulate);
				}

				@Override
				public int getEnergyStored() {

					return ExtensionCableTileEntity.this.getEnergyStored(from);
				}

				@Override
				public int getMaxEnergyStored() {

					return ExtensionCableTileEntity.this.getMaxEnergyStored(from);
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
