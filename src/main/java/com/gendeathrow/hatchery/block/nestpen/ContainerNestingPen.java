package com.gendeathrow.hatchery.block.nestpen;

import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerNestingPen extends Container 
{
	private final InventoryStroageModifiable nestingPenInventory;
	
	public ContainerNestingPen(InventoryPlayer playerInventory, NestPenTileEntity tile, EntityPlayer thePlayer) 
	{
        this.nestingPenInventory = tile.inventory;

        for (int j = 0; j < nestingPenInventory.getSlots(); ++j)
            this.addSlotToContainer(new SlotItemHandler(nestingPenInventory, j, 44 + j * 18, 20));

        for (int l = 0; l < 3; ++l)
            for (int k = 0; k < 9; ++k)
                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + 51));

        for (int i1 = 0; i1 < 9; ++i1)
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 109));
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
	{
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()){
        	
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < this.nestingPenInventory.getSlots())
                if (!this.mergeItemStack(itemstack1, this.nestingPenInventory.getSlots(),  this.inventorySlots.size(), true))
                    return null;
            else if (!this.mergeItemStack(itemstack1, 0, this.nestingPenInventory.getSlots(), false))
                return null;
            
            if (itemstack1.getCount() == 0)
                slot.putStack((ItemStack)null);
            else
                slot.onSlotChanged();

            if (itemstack1.getCount() == itemstack.getCount())
                return null;

            slot.onTake(player, itemstack1);
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
    }
}
