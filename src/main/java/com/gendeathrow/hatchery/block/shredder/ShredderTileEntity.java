package com.gendeathrow.hatchery.block.shredder;

import cofh.api.energy.EnergyStorage;

import com.gendeathrow.hatchery.block.InventoryStorage;
import com.gendeathrow.hatchery.block.TileUpgradable;

public class ShredderTileEntity extends TileUpgradable
{
	protected EnergyStorage energy= new EnergyStorage(10000);
	protected InventoryStorage storage = new InventoryStorage(this, 5);
	
	public ShredderTileEntity() 
	{
		super(2);
	}

}
