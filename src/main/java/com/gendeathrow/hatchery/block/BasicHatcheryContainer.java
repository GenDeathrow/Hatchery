package com.gendeathrow.hatchery.block;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import scala.actors.threadpool.Arrays;

public class BasicHatcheryContainer extends Container{

	List<IItemHandler> inventories;
	
	@SuppressWarnings("unchecked")
	public void addInventories(IItemHandler... invs){
		inventories = Arrays.asList(invs);
	}
	
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	
	private int getAllSlots() {
		int slotCount = 0;
		for(IItemHandler inv : inventories)
			slotCount += inv.getSlots();
		
		return slotCount;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
	{
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < getAllSlots())
            {
                if (!this.mergeItemStack(itemstack1, getAllSlots(), this.inventorySlots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventorySlots.size(), false))
            {
                return ItemStack.EMPTY;
            }
            
            if (itemstack1.getCount() == 0)
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        
		return  itemstack;
	}

}
