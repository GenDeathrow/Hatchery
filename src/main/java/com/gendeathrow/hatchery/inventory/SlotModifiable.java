package com.gendeathrow.hatchery.inventory;

import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

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
   		return true;
    }
    
    @Override
    public ItemStack decrStackSize(int amount)
    {  
    	if(this.getItemHandler() instanceof InventoryStorageModifiable)
    		return ((InventoryStroageModifiable)this.getItemHandler()).extractItem(indexSlot, amount, false);
    	
        return this.getItemHandler().extractItem(indexSlot, amount, false);
    }
    
}
