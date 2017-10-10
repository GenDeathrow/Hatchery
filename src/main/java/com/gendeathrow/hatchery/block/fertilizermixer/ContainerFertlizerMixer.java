package com.gendeathrow.hatchery.block.fertilizermixer;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.inventory.SlotFluidContainer;
import com.gendeathrow.hatchery.inventory.SlotUpgrade;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerFertlizerMixer extends Container 
{
	
	private final IItemHandler inputInventory;
	private final IItemHandler outputInventory;
	
	private final IInventory upgrades;
	private final ItemStack[] manure;
	private final FertilizerMixerTileEntity tileEntity;

	private int waterTank;
	private int fertilizerTank;
	
	public ContainerFertlizerMixer(InventoryPlayer playerInventory, FertilizerMixerTileEntity fertilizerInventory) 
	{
		inputInventory = fertilizerInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
		
		outputInventory = fertilizerInventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
		
		tileEntity = fertilizerInventory;
		manure = null;
		upgrades = fertilizerInventory.getUpgradeStorage();
		
		waterTank = fertilizerInventory.getWaterTank().getFluidAmount();
		fertilizerTank = fertilizerInventory.getFertilizerTank().getFluidAmount();

		int i;

		addSlotToContainer(new SlotItemHandler(inputInventory, 0, 17, 16)
		{
			 public boolean isItemValid(@Nullable ItemStack stack)
             {
				 if(stack.getItem() == ModItems.manure || stack.getItem() == Item.getItemFromBlock(ModBlocks.manureBlock))
				 {
					 return true;
				 }
				 else return false;
             }
			 
		});
		
		addSlotToContainer(new SlotFluidContainer(inputInventory, 1, 72, 16, FluidRegistry.WATER));
		
		addSlotToContainer(new SlotItemHandler(outputInventory, 0, 72, 52)
		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
			
		});
		
		
		addSlotToContainer(new SlotItemHandler(inputInventory, 2, 104, 16)		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
				{
					return true;
				}
				return false;
		    }
			
		});
		addSlotToContainer(new SlotFluidContainer(outputInventory, 1, 104, 52, ModFluids.liquidfertilizer));
		
		addSlotToContainer(new SlotUpgrade(tileEntity, upgrades, 0, 8, 52));
		addSlotToContainer(new SlotUpgrade(tileEntity, upgrades, 1, 31, 52));

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

            if (slotIndex < (this.inputInventory.getSlots() + this.upgrades.getSizeInventory()))
            {
                if (!this.mergeItemStack(itemstack1, this.inputInventory.getSlots(),  this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inputInventory.getSlots(), false))
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
		this.tileEntity.setField(id, value);
	}

	@Override
    public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for (IContainerListener listener : this.listeners) 
		 {
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					
					listener.sendProgressBarUpdate(this, 4, this.tileEntity.getField(4));
					listener.sendProgressBarUpdate(this, 5, this.tileEntity.getField(5));
					
					listener.sendProgressBarUpdate(this, 0, this.tileEntity.getField(0));
					listener.sendProgressBarUpdate(this, 1, this.tileEntity.getField(1));
					
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 6, this.tileEntity.getField(6));
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 3, this.tileEntity.getField(3));
		 }
		
		this.waterTank = this.tileEntity.getField(0);
		this.fertilizerTank = this.tileEntity.getField(1);
    }

	
}
