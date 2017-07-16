package com.gendeathrow.hatchery.block.eggstractor;

import com.gendeathrow.hatchery.inventory.InventoryStorage;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class EggstractorTileEntity extends TileEntity implements ITickable
{

	
	protected InventoryStorage inventory = new InventoryStorage(this, 3);
	
	protected EnergyStorageRF energy = new EnergyStorageRF(20000).setMaxReceive(100);
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
