package com.gendeathrow.hatchery.inventory;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotModifiable extends SlotItemHandler{

	public SlotModifiable(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
		this.indexSlot = index;
	}




	public final int indexSlot;


    @Override
    public boolean canTakeStack(EntityPlayer playerIn)
    {
    	if(this.getItemHandler() instanceof InventoryStorageModifiable)
    		return ((InventoryStorageModifiable)this.getItemHandler()).extractItemIternal(indexSlot, 1, true) != null;
    				
    	return this.getItemHandler().extractItem(indexSlot, 1, true) != null;
    }
    
    @Override
    public ItemStack decrStackSize(int amount)
    {
    	if(this.getItemHandler() instanceof InventoryStorageModifiable)
    		return ((InventoryStorageModifiable)this.getItemHandler()).extractItemIternal(indexSlot, amount, false);
    	
        return this.getItemHandler().extractItem(indexSlot, amount, false);
    }
    
    
    @Nullable
    public ItemStack getStack()
    {
        return this.inventory.getStackInSlot(this.indexSlot);
    }
}
