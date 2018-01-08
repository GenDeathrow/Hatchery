package com.gendeathrow.hatchery.block.shredder;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.block.BasicHatcheryContainer;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerShredder extends BasicHatcheryContainer 
{
	
	private final IItemHandler inputInventory;
	private final IItemHandler outputInventory;
	
	private final IItemHandler upgrades;
	
	private final ShredderTileEntity shredder;
	
	public ContainerShredder(InventoryPlayer playerInventory,	ShredderTileEntity tile, EntityPlayer player) 
	{
		inputInventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
		
		outputInventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

		upgrades = tile.getUpgradeStorage();

		addInventories(inputInventory, upgrades, outputInventory);  
		
		shredder = tile;
		
		int i;  

		addSlotToContainer(new SlotItemHandler(inputInventory, 0, 65, 16));
		
		
		addSlotToContainer(new SlotItemHandler(upgrades, 0, 121, 54) {
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = shredder.canUseUpgrade(stack);
				return value;
		    }
		});
		addSlotToContainer(new SlotItemHandler(upgrades, 1, 141, 54) {
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = shredder.canUseUpgrade(stack);
				return value;
		    }
		});
		
		
		addSlotToContainer(new SlotItemHandler(outputInventory, 0, 55, 54){
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
		});
		

		
		addSlotToContainer(new SlotItemHandler(outputInventory, 1, 76, 54){
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
		});
		     
		bindPlayerInventory(playerInventory);          
	}
	

	@Override
	public void addListener(IContainerListener listener) {
		if (this.listeners.contains(listener)) {
			throw new IllegalArgumentException("Listener already listening");
		} else {
			this.listeners.add(listener);
			listener.sendAllContents(this, this.getInventory());
			this.detectAndSendChanges();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		this.shredder.setField(id, value);
	}

	@Override
    public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for (IContainerListener listener : this.listeners) 
		{
				HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 3, this.shredder.getField(3));
				HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 0, this.shredder.getField(0));
				listener.sendWindowProperty(this, 1, this.shredder.getField(1));
				listener.sendWindowProperty(this, 2, this.shredder.getField(2));
		}

    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)	{
		return true;
	}
}
