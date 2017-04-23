package com.gendeathrow.hatchery.block.fertilizermixer;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;

public class ContainerFertlizerMixer extends Container 
{
	
	private final int HOTBAR_SLOT_COUNT = 9;
	private final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private final int VANILLA_FIRST_SLOT_INDEX = 0;
	private final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private final int TE_INVENTORY_SLOT_COUNT = 9;
	
	private final IInventory inventory;
	private final ItemStack[] manure;
	private int waterTank;
	private int fertilizerTank;
	
	public ContainerFertlizerMixer(InventoryPlayer playerInventory, FertilizerMixerTileEntity fertilizerInventory) 
	{
		inventory = fertilizerInventory;
		manure = fertilizerInventory.getItemInventory();
		
		waterTank = fertilizerInventory.getWaterTank().getFluidAmount();
		fertilizerTank = fertilizerInventory.getFertilizerTank().getFluidAmount();

		int i;

		addSlotToContainer(new Slot(fertilizerInventory, 0, 17, 34)
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
		
		addSlotToContainer(new SlotFluidContainer(fertilizerInventory, 1, 72, 16, FluidRegistry.WATER));
		addSlotToContainer(new Slot(fertilizerInventory, 2, 72, 52));
		
		
		addSlotToContainer(new Slot(fertilizerInventory, 3, 122, 16));
		addSlotToContainer(new SlotFluidContainer(fertilizerInventory, 4, 122, 52, ModFluids.liquidfertilizer));

	     for (i = 0; i < 3; ++i)
	            for (int j = 0; j < 9; ++j)
	                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 7 + j * 18, 83 + i * 18));

	        for (i = 0; i < 9; ++i)
	            addSlotToContainer(new Slot(playerInventory, i, 7 + i * 18, 141));
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

            if (slotIndex <= 5)
            {
                if (!this.mergeItemStack(itemstack1, slotIndex, this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, slotIndex, false))
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

	@Override
    public void onCraftMatrixChanged(IInventory inv) {
        detectAndSendChanges();
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
					listener.sendProgressBarUpdate(this, 0, this.inventory.getField(0));
					listener.sendProgressBarUpdate(this, 1, this.inventory.getField(1));
		 }
		
		this.waterTank = this.inventory.getField(0);
		this.fertilizerTank = this.inventory.getField(1);
    }

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		inventory.closeInventory(player);
	}
	
	
	public class SlotFluidContainer extends Slot
	{
		Fluid fluid;
		public SlotFluidContainer(IInventory inventoryIn, int index,int xPosition, int yPosition, Fluid fluid) 
		{
			super(inventoryIn, index, xPosition, yPosition);
			this.fluid = fluid;
		}

		public boolean isItemValid(@Nullable ItemStack stack)
        {
			if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
			{
				IFluidHandler fluidHandler = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
				
				if(fluidHandler != null)
					return true;
			}
			
			return false;
        }
	
	
	}
}
