package com.gendeathrow.hatchery.entities;

import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerRoosterInventory extends Container {

	private final InventoryStroageModifiable inventory;

	public ContainerRoosterInventory(InventoryPlayer playerInventory, EntityRooster entityInventory) {
		inventory = entityInventory.inventory;
		int i;

		addSlotToContainer(new SlotItemHandler(inventory, 0, 25, 36));

	     for (i = 0; i < 3; ++i)
	            for (int j = 0; j < 9; ++j)
	                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 7 + j * 18, 83 + i * 18));

	        for (i = 0; i < 9; ++i)
	            addSlotToContainer(new Slot(playerInventory, i, 7 + i * 18, 141));
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		ItemStack stack = null;
		Slot slot = (Slot) inventorySlots.get(slotIndex);
		if (slot != null && slot.getHasStack()) {
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if (slotIndex > 0) {
				if (EntityRooster.TEMPTATION_ITEMS.contains(stack.getItem()))
					if (!mergeItemStack(stack1, 0, 1, false))
						return null;
			}
			else if (!mergeItemStack(stack1, 1, inventorySlots.size(), false))
				return null;
			if (stack1.getCount() == 0)
				slot.putStack(null);
			else
				slot.onSlotChanged();
			if (stack1.getCount() != stack.getCount())
				slot.onTake(player, stack1);
			else
				return null;
		}
		return stack;
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

	@Override
    public void onCraftMatrixChanged(IInventory inv) {
        detectAndSendChanges();
    }

	@Override
    public void detectAndSendChanges() {
		super.detectAndSendChanges();
    }

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
	}
}