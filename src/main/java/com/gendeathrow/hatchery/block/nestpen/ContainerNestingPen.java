package com.gendeathrow.hatchery.block.nestpen;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerNestingPen extends Container 
{
    private final IInventory nestingPenInventory;
	
	public ContainerNestingPen(InventoryPlayer playerInventory, IInventory nestingPenIn, EntityPlayer thePlayer) 
	{
        this.nestingPenInventory = nestingPenIn;
        nestingPenIn.openInventory(thePlayer);
        int i = 51;

        for (int j = 0; j < nestingPenIn.getSizeInventory(); ++j)
        {
            this.addSlotToContainer(new Slot(nestingPenIn, j, 44 + j * 18, 20));
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
        }
	}

	
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.nestingPenInventory.getSizeInventory())
            {
                if (!this.mergeItemStack(itemstack1, this.nestingPenInventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.nestingPenInventory.getSizeInventory(), false))
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
        }

        return itemstack;
    }
    
    
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)	
	{
		return true;
	}
	
	
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
        this.nestingPenInventory.closeInventory(playerIn);
    }
}
