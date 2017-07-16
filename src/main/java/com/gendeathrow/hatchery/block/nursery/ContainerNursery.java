package com.gendeathrow.hatchery.block.nursery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerNursery  extends Container {

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return true;
	}

}
