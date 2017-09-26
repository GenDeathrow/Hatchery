package com.gendeathrow.hatchery.block.eggmachine;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.inventory.SlotUpgrade;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerEggMachine extends Container 
{
	private final IInventory inventory;
	private final IInventory upgrades;

	
	public ContainerEggMachine(InventoryPlayer playerInventory, EggMachineTileEntity eggstractorInventory) 
	{
		inventory = eggstractorInventory;
		upgrades = eggstractorInventory.getUpgradeStorage();

		int i;

		addSlotToContainer(new Slot(eggstractorInventory, eggstractorInventory.EggInSlot, 38, 18)
		{
		    public boolean isItemValid(@Nullable ItemStack stack)
		    {
		        return stack.getItem() instanceof ItemEgg;
		    }
			 
		});
		

		addSlotToContainer(new Slot(eggstractorInventory,  eggstractorInventory.PlasticInSlot, 63, 18)
		{
		    public boolean isItemValid(@Nullable ItemStack stack)
		    {
		        return stack.getItem() == ModItems.plastic;
		    }
			
		});
		
		addSlotToContainer(new Slot(eggstractorInventory, eggstractorInventory.PrizeEggSlot, 51, 53)
		{
		    public boolean isItemValid(@Nullable ItemStack stack)
		    {
		        return stack.getItem() == ModItems.prizeEgg;
		    }
		});
		
		
		addSlotToContainer(new SlotUpgrade(upgrades, 0, 100, 53));
		addSlotToContainer(new SlotUpgrade(upgrades, 1, 124, 53));

	     for (i = 0; i < 3; ++i)
	            for (int j = 0; j < 9; ++j)
	                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

	        for (i = 0; i < 9; ++i)
	            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));

	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return true;
	}
	

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
	{
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < (this.inventory.getSizeInventory() + this.upgrades.getSizeInventory()))
            {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(),  this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false))
            {
                return null;
            }
            
            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(player, itemstack1);
        }
        
		return itemstack;
	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		this.inventory.setField(id, value);
	}

	@Override
    public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for (IContainerListener listener : this.listeners) 
		 {
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 0, this.inventory.getField(0));
					listener.sendProgressBarUpdate(this, 1, this.inventory.getField(1));
					listener.sendProgressBarUpdate(this, 2, this.inventory.getField(2));
					listener.sendProgressBarUpdate(this, 3, this.inventory.getField(3));
					//HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 3, this.inventory.getField(3));
		 }

    }

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		inventory.closeInventory(player);
	}
	
	
}
