package com.gendeathrow.hatchery.block.eggmachine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class EggMachineTopTileEntity extends TileEntity {

	
	public EggMachineTileEntity getBaseTE(){
		EggMachineTileEntity te = (EggMachineTileEntity)this.world.getTileEntity(this.pos.offset(EnumFacing.DOWN));
		
		return te;
	}
	
	public boolean hasBaseTE(){
		TileEntity te = this.world.getTileEntity(this.pos.offset(EnumFacing.DOWN));
		if(te != null && te instanceof EggMachineTileEntity)
			return true;
		
		return false;
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
