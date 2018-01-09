package com.gendeathrow.hatchery.block.eggmachine;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.block.BasicHatcheryContainer;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerEggMachine extends BasicHatcheryContainer 
{
	private final IItemHandler inputInventory;
	private final IItemHandler outputInventory;
	//private final IInventory inventory;
	private final InventoryStroageModifiable upgrades;
	private final EggMachineTileEntity te;

	 
	public ContainerEggMachine(InventoryPlayer playerInventory, EggMachineTileEntity eggstractorInventory) 
	{
		inputInventory = eggstractorInventory.inputInventory;
		outputInventory = eggstractorInventory.outputInventory;
		upgrades = eggstractorInventory.getUpgradeStorage();
		te = eggstractorInventory;
		
		int i;

		addInventories(inputInventory, upgrades, outputInventory);  
		
		addSlotToContainer(new SlotItemHandler(eggstractorInventory.inputInventory, eggstractorInventory.EggInSlot, 38, 18));
		addSlotToContainer(new SlotItemHandler(eggstractorInventory.inputInventory,  eggstractorInventory.PlasticInSlot, 63, 18));
		
		addSlotToContainer(new SlotItemHandler(upgrades, 0, 100, 53){
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = te.canUseUpgrade(stack);
				return value;
		    }
		});
		addSlotToContainer(new SlotItemHandler(upgrades, 1, 124, 53){
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = te.canUseUpgrade(stack);
				return value;
		    }
		});
		
		addSlotToContainer(new SlotItemHandler(eggstractorInventory.outputInventory, eggstractorInventory.PrizeEggSlot, 51, 53){
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
		});
		
		bindPlayerInventory(playerInventory);   
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return true;
	}
	

//	@Override
//	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
//	{
//        ItemStack itemstack = ItemStack.EMPTY;
//        Slot slot = (Slot)this.inventorySlots.get(slotIndex);
//
//        if (slot != null && slot.getHasStack())
//        {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            if (slotIndex < (this.inputInventory.getSlots() + this.upgrades.getSlots()))
//            {
//                if (!this.mergeItemStack(itemstack1, this.inputInventory.getSlots(), this.inventorySlots.size(), true))
//                {
//                    return ItemStack.EMPTY;
//                }
//            }
//            else if (!this.mergeItemStack(itemstack1, 0, this.inputInventory.getSlots(), false))
//            {
//                return ItemStack.EMPTY;
//            }
//            
//            if (itemstack1.getCount() == 0)
//            {
//                slot.putStack(ItemStack.EMPTY);
//            }
//            else
//            {
//                slot.onSlotChanged();
//            }
//        }
//        
//		return  itemstack;
//	}
	
	
//	@Override
//	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
//	{
//        ItemStack itemstack = null;
//        Slot slot = (Slot)this.inventorySlots.get(slotIndex);
//
//        if (slot != null && slot.getHasStack())
//        {
//            ItemStack itemstack1 = slot.getStack();
//            itemstack = itemstack1.copy();
//
//            if (slotIndex < (this.inventory.getSizeInventory() + this.upgrades.getSizeInventory()))
//            {
//                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(),  this.inventorySlots.size(), true))
//                {
//                    return null;
//                }
//            }
//            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false))
//            {
//                return null;
//            }
//            
//            if (itemstack1.stackSize == 0)
//            {
//                slot.putStack((ItemStack)null);
//            }
//            else
//            {
//                slot.onSlotChanged();
//            }
//
//            if (itemstack1.stackSize == itemstack.stackSize)
//            {
//                return null;
//            }
//
//            slot.onPickupFromSlot(player, itemstack1);
//        }
//        
//		return itemstack;
//	}

	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		this.te.setField(id, value);
	}

	@Override
    public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for (IContainerListener listener : this.listeners) 
		 {
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 0, this.te.getField(0));
					listener.sendWindowProperty(this, 1, this.te.getField(1));
					listener.sendWindowProperty(this, 2, this.te.getField(2));
					listener.sendWindowProperty(this, 3, this.te.getField(3));
					//HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 3, this.inventory.getField(3));
		 }

    }

	
	
}
