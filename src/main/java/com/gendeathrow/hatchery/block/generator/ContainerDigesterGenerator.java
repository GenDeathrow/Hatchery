package com.gendeathrow.hatchery.block.generator;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.block.BasicHatcheryContainer;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.inventory.SlotFluidContainer;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDigesterGenerator extends BasicHatcheryContainer 
{

	private final DigesterGeneratorTileEntity tile;
	private final InventoryStroageModifiable inputInventory;
	private final InventoryStroageModifiable outputInventory;
	
	private final InventoryStroageModifiable upgrades;

	public ContainerDigesterGenerator(InventoryPlayer playerInventory, DigesterGeneratorTileEntity tileEntity) 
	{
		inputInventory = tileEntity.inputInventory;
		outputInventory = tileEntity.outputInventory;
		tile = tileEntity;

		upgrades = tileEntity.getUpgradeStorage();
		
		int i;  

		addSlotToContainer(new SlotFluidContainer(inputInventory, 0, 72, 16, ModFluids.liquidfertilizer));
        
		addSlotToContainer(new SlotItemHandler(upgrades, 0, 107, 59));
		addSlotToContainer(new SlotItemHandler(upgrades, 1, 134, 59));
		
		addInventories(inputInventory, upgrades);  
	
		addSlotToContainer(new SlotItemHandler(outputInventory, 0, 72, 52)
		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack){
				return false;
		    }
		});


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
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		this.tile.setField(id, value);
	}

	@Override
    public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for (IContainerListener listener : this.listeners) 
		 {
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					listener.sendWindowProperty(this, 4, this.tile.getField(4));
					listener.sendWindowProperty(this, 1, this.tile.getField(1));
					
					listener.sendWindowProperty(this, 2, this.tile.getField(2));
					
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 3, this.tile.getField(3));
					HatcheryWindowPacket.sendProgressBarUpdate(listener, this, 0, this.tile.getField(0));
		 }

    }

}
