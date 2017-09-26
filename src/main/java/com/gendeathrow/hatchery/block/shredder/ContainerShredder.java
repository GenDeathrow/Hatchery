package com.gendeathrow.hatchery.block.shredder;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.inventory.SlotUpgrade;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;

public class ContainerShredder extends Container 
{

	private final int HOTBAR_SLOT_COUNT = 9;
	private final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private final int VANILLA_FIRST_SLOT_INDEX = 0;
	
	private final IItemHandler inventory;
	
	private final IInventory upgrades;
	
	private final ShredderTileEntity shredder;
	
	public ContainerShredder(InventoryPlayer playerInventory,	ShredderTileEntity tile, EntityPlayer player) 
	{
		inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

		upgrades = tile.getUpgradeStorage();
		
		shredder = tile;
		
		int i;  

		addSlotToContainer(new SlotItemHandler(inventory, 0, 65, 16){
//		    @Override
//		    public boolean canTakeStack(EntityPlayer playerIn){
//		    	return true;
//		    }
		});
		
		addSlotToContainer(new SlotItemHandler(inventory, 1, 55, 54){
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
		});
		
		addSlotToContainer(new SlotItemHandler(inventory, 2, 76, 54){
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
		});
		     
		addSlotToContainer(new SlotUpgrade(upgrades, 0, 121, 55) {
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = shredder.canUseUpgrade(stack);
				return value;
		    }
		});
		addSlotToContainer(new SlotUpgrade(upgrades, 1, 141, 54) {
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = shredder.canUseUpgrade(stack);
				return value;
		    }
		});

	     for (i = 0; i < 3; ++i)
	            for (int j = 0; j < 9; ++j)
	                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

        for (i = 0; i < 9; ++i)
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));	          
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

            if (slotIndex < (this.inventory.getSlots() + this.upgrades.getSizeInventory()))
            {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSlots(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSlots(), false))
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
        
		return  itemstack;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		if (this.listeners.contains(listener)) {
			throw new IllegalArgumentException("Listener already listening");
		} else {
			this.listeners.add(listener);
			listener.updateCraftingInventory(this, this.getInventory());
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
				listener.sendProgressBarUpdate(this, 1, this.shredder.getField(1));
				listener.sendProgressBarUpdate(this, 2, this.shredder.getField(2));
		}

    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)	{
		return true;
	}
}
