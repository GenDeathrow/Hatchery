package com.gendeathrow.hatchery.block.eggmachine;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class EggMachineTopTileEntity extends TileEntity implements IEnergyReceiver{

	
	public EggMachineTileEntity getBaseTE(){
		EggMachineTileEntity te = (EggMachineTileEntity)this.worldObj.getTileEntity(this.pos.offset(EnumFacing.DOWN));
		
		return te;
	}
	
	public boolean hasBaseTE(){
		TileEntity te = this.worldObj.getTileEntity(this.pos.offset(EnumFacing.DOWN));
		if(te != null && te instanceof EggMachineTileEntity)
			return true;
		
		return false;
	}
	
	@Override
	public int getEnergyStored(EnumFacing from) {
		if(hasBaseTE())
			return this.getBaseTE().getEnergyStored(from);
		
		return 0;
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		if(hasBaseTE())
			return this.getBaseTE().getMaxEnergyStored(from);
		return 0;
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		if(hasBaseTE())
			return this.getBaseTE().canConnectEnergy(from);
		return false;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		if(hasBaseTE())
			return this.getBaseTE().receiveEnergy(from, maxReceive, simulate);
		
		return 0;
	}
	
	
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
		if(hasBaseTE())
			return this.getBaseTE().hasCapability(capability, facing);
					
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
		if(hasBaseTE())
			return this.getBaseTE().getCapability(capability, facing);
					
        return null;
    }

}
