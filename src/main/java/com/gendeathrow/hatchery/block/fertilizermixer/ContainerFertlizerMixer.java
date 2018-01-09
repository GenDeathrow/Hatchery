package com.gendeathrow.hatchery.block.fertilizermixer;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.block.BasicHatcheryContainer;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.inventory.SlotFluidContainer;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
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

public class ContainerFertlizerMixer extends BasicHatcheryContainer 
{
	
	private final IItemHandler inputInventory;
	private final IItemHandler outputInventory;
	
	private final InventoryStroageModifiable upgrades;
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
		
		addInventories(inputInventory, upgrades, outputInventory);  
		
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
		
		addSlotToContainer(new SlotItemHandler(inputInventory, 2, 104, 16)		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				if(stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.DOWN))
				{
					return true;
				}
				return false;
		    }
			
		});
		
		
		addSlotToContainer(new SlotItemHandler(upgrades, 0, 8, 52){
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = tileEntity.canUseUpgrade(stack);
				return value;
		    }
		});
		addSlotToContainer(new SlotItemHandler(upgrades, 1, 31, 52){
			public boolean isItemValid(@Nullable ItemStack stack)
		    {
				boolean value = super.isItemValid(stack);
				if(value)
					value = tileEntity.canUseUpgrade(stack);
				return value;
		    }
		});
		
		
		addSlotToContainer(new SlotItemHandler(outputInventory, 0, 72, 52)
		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
			
		});
		
		addSlotToContainer(new SlotFluidContainer(outputInventory, 1, 104, 52, ModFluids.liquidfertilizer));

		this.bindPlayerInventory(playerInventory);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) 
	{
		return true;
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
					
					listener.sendWindowProperty(this, 4, this.tileEntity.getField(4));
					listener.sendWindowProperty(this, 5, this.tileEntity.getField(5));
					
					listener.sendWindowProperty(this, 0, this.tileEntity.getField(0));
					listener.sendWindowProperty(this, 1, this.tileEntity.getField(1));
					
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 6, this.tileEntity.getField(6));
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 3, this.tileEntity.getField(3));
		 }
		
		this.waterTank = this.tileEntity.getField(0);
		this.fertilizerTank = this.tileEntity.getField(1);
    }

	
}
