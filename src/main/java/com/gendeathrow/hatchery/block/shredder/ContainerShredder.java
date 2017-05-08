package com.gendeathrow.hatchery.block.shredder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerShredder extends Container 
{

	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)	{
		return true;
	}
}
