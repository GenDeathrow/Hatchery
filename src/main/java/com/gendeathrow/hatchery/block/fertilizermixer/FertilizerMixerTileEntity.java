package com.gendeathrow.hatchery.block.fertilizermixer;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.item.upgrades.BaseUpgrade;
import com.gendeathrow.hatchery.item.upgrades.RFEfficiencyUpgrade;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import cofh.api.energy.IEnergyReceiver;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerFluidMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

public class FertilizerMixerTileEntity extends TileUpgradable implements ITickable, IEnergyReceiver
{
	int baseTankSize = 12000;
	int baseRFStorage = 20000;
	int baseRFTick = 20;
	double rfEffencyMultpyler = 1;
	double upgradeSpeedMulipyler = 1;
	
	private FluidTank waterTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 0), 12000){
		@Override
	    public boolean canDrain()
	    {
			return false;
		}
	};
	
	
	private FluidTank fertilizerTank = new FluidTank(new FluidStack(ModFluids.liquidfertilizer, 0), 12000){
		@Override
		public boolean canFill()
		{
			return false;
		}
	};
	
	  
	private FluidHandlerFluidMap fluidMap = new FluidHandlerFluidMap().addHandler(ModFluids.liquidfertilizer, fertilizerTank).addHandler(FluidRegistry.WATER, waterTank);
	
	
	protected InventoryStroageModifiable inputInventory = new InventoryStroageModifiable("inputItems", 3) {
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			if(slot == 0 && (stack.getItem() == ModItems.manure || stack.getItem() == Item.getItemFromBlock(ModBlocks.manureBlock)))
				return true;
			else if(slot == 1 && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
			{
				if(FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == FluidRegistry.WATER){
					return true;
				}
			}
			else if(slot == 2 && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
			{
				if(FluidUtil.getFluidContained(stack) == null || FluidUtil.getFluidContained(stack).getFluid() == ModFluids.liquidfertilizer){
					return true;
				}
			}

			return false; 

		}
	};
	
	protected InventoryStroageModifiable outputInventory = new InventoryStroageModifiable("outputItems", 2) {
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			return false;
		}
	};
	
	//protected InventoryStorage inventory = new InventoryStorage(this, 5);
	
	protected EnergyStorageRF energy = new EnergyStorageRF(20000).setMaxReceive(100);
	
	public FertilizerMixerTileEntity() 
	{
		super(2);
	}

	
	public FluidTank getWaterTank()
	{
		return waterTank;
	}
	
	public FluidTank getFertilizerTank()
	{
		return fertilizerTank;
	}
	
//	public ItemStack[] getItemInventory()
//	{
//		return inventory.getInventory();
//	}

    public boolean isMixing()
    {
        return this.fertlizerMixTime > 0;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean isMixing(IInventory inventory)
    {
        return inventory.getField(2) > 0;
    }
    
    public boolean canMix()
    {
   	  	return waterTank.getFluidAmount() > 0 && this.fertilizerTank.getFluidAmount() < this.fertilizerTank.getCapacity();
    }
    
    public static int getItemMixTime(ItemStack stack)
    {
    	if(stack == null)
    	{
    		return 0;
    	}
    	else
    	{
    		Item item = stack.getItem();
    		
    		if(item == ModItems.manure)
    			return 100;
    		else if(item == Item.getItemFromBlock(ModBlocks.manureBlock))
    			return 1120;
    	}
    	return 0;
    }

	private int fertlizerMixTime;
    private int currentItemMixTime;
    private int mixTime;
    private int totalMixTime;

    
    private ItemStack getManureInSlot()
    {
    	return this.inputInventory.getStackInSlot(0);
    }
    
    private ItemStack getWaterInSlot()
    {
    	return this.inputInventory.getStackInSlot(1);
    }

    private ItemStack getBucketOutWaterSlot()
    {
    	return this.outputInventory.getStackInSlot(0);
    }
    
    private ItemStack getBucketInFertilizerrSlot()
    {
    	return this.inputInventory.getStackInSlot(2);
    }
  
    private ItemStack getBucketOutFertilizerSlot()
    {
    	return this.outputInventory.getStackInSlot(1);
    }

	@Override
	public void update() 
	{

		boolean flag = this.isMixing();
		boolean flag1 = false;
			
		if(this.isMixing())
		{
			this.fertlizerMixTime--;
		}
		
		if(this.worldObj != null && !this.worldObj.isRemote)
		{
			updateUpgrades();
			
			
			if (this.isMixing() || this.getManureInSlot() != null)
			{
                if (!this.isMixing() && this.canMix())
                {
                	this.fertlizerMixTime = getItemMixTime(this.getManureInSlot());
                	this.currentItemMixTime = this.fertlizerMixTime;
                			
                	if(this.isMixing())
                	{
                		flag1 = true;
                		 if(this.getManureInSlot() != null)
                		 {
                			 --this.getManureInSlot().stackSize;
                    		 
                             if (this.getManureInSlot().stackSize <= 0)
                             {
                                 this.inputInventory.setStackInSlot(0,null);
                             }
                		 }

                	}
                }
                
    			int rfNeeded = (int)((10 * this.upgradeSpeedMulipyler) * this.rfEffencyMultpyler);
    			
    			if(this.isMixing() && this.canMix() && fertlizerMixTime >= 5 && this.energy.getEnergyStored() >= rfNeeded)
    			{
    					this.fertlizerMixTime -= 5 * this.upgradeSpeedMulipyler;
    					this.energy.extractEnergy(rfNeeded, false);
    				
    					this.fertilizerTank.fillInternal(new FluidStack(ModFluids.liquidfertilizer,(int)(5 * this.upgradeSpeedMulipyler)), true);
    					this.waterTank.drainInternal((int)(5 * this.upgradeSpeedMulipyler), true);
   					
    					flag1 = true;
    			}
    			else if(this.isMixing() && this.canMix() && fertlizerMixTime >= 1)
    			{
					this.fertlizerMixTime -= 1;
    				
					this.fertilizerTank.fillInternal(new FluidStack(ModFluids.liquidfertilizer, 1), true);
					this.waterTank.drainInternal(1, true);
					
					flag1 = true;
    			}
    			else
    			{
    				this.mixTime = 0;
    			}
			}
			else if(!this.isMixing() && this.mixTime > 0)
			{
				 this.mixTime = MathHelper.clamp_int(this.mixTime - 2, 0, this.totalMixTime);
			}
			

			if(this.getWaterInSlot() != null && this.waterTank.getFluidAmount() < this.waterTank.getCapacity())
			{
				ItemStack stack = this.getWaterInSlot();
				ItemStack newStack = this.getWaterInSlot().copy();

				newStack.stackSize = 1;
				
				IFluidHandler handler = FluidUtil.getFluidHandler(newStack);

				if(handler != null)
				{
					
		            if (this.getBucketOutWaterSlot() == null)
		            {
		            	
		            	if(FluidUtil.tryFluidTransfer(this.waterTank, handler, this.waterTank.getCapacity(), true) != null)
		            	{
		            		if(newStack.stackSize > 0)
		            			this.outputInventory.setStackInSlot(0, newStack);
						
		            		this.inputInventory.extractItem(1, 1, false);
		            	}
		            }
				}
			}
			
			if(this.getBucketInFertilizerrSlot() != null && this.getBucketOutFertilizerSlot() == null && this.fertilizerTank.getFluidAmount() > 0)
			{
				ItemStack oldStack = this.getBucketInFertilizerrSlot();
				ItemStack newStack = this.getBucketInFertilizerrSlot().copy();
				
				if(newStack.stackSize > 1)
					newStack.stackSize = 1;
				
				IFluidHandler handler = FluidUtil.getFluidHandler(newStack);
				
				if(handler != null)
				{
					
					if(FluidUtil.tryFluidTransfer(handler, this.fertilizerTank, this.fertilizerTank.getCapacity(), true) != null)
					{
						this.outputInventory.setStackInSlot(1, newStack);
						
						if(oldStack.stackSize > 1)
							this.inputInventory.extractItem(2, 1, false);
						else
							this.inputInventory.setStackInSlot(2, null);
					}
				}
			}
			

		}

		if(flag != this.isMixing())
		{
			flag1 = true;

			//  BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
		}
		
		
        if (flag1)
        {
            this.markDirty();
        }
	}
	
	
	
	@Override
	public boolean canUseUpgrade(ItemStack item)
	{
		return item.getItem() instanceof RFEfficiencyUpgrade || item.getItem() == ModItems.speedUpgradeTier  || item.getItem() == ModItems.tankUpgradeTier1 || item.getItem() == ModItems.rfCapacityUpgradeTier1;
	}
	
	protected void updateUpgrades()
	{
		boolean rfupgrade = false;
		boolean rfcapacity = false;
		boolean tankcapacity = false;
		boolean speedupgrade = false;
		
		for(ItemStack upgrade : this.getUpgrades())
		{
			if(upgrade == null) continue;
		
			
			if(upgrade.getItem() instanceof RFEfficiencyUpgrade)
			{
				rfupgrade = true;
				this.rfEffencyMultpyler = 1 - ((BaseUpgrade)upgrade.getItem()).getUpgradeTier(upgrade, "") * 0.10;
			}
			if(upgrade.getItem() == ModItems.speedUpgradeTier && speedupgrade == false)
			{
				speedupgrade = true;
				this.upgradeSpeedMulipyler = 1 + ((BaseUpgrade)upgrade.getItem()).getUpgradeTier(upgrade, "") * 1 ;
			}
			
			if(upgrade.getItem() == ModItems.tankUpgradeTier1 && tankcapacity == false)
			{
				tankcapacity = true;
				
				int tier = upgrade.getMetadata()+1;
				int newTank = this.baseTankSize;
				
				if(tier == 1) {
					newTank += 2000;
				}
				else if(tier == 2) {
					newTank += 4000; 
				}
				else if(tier == 3) {
					newTank += 6000; 
				}

				if(newTank != this.fertilizerTank.getCapacity())
				{
					this.fertilizerTank.setCapacity(newTank);
				}
				
				if(newTank != this.waterTank.getCapacity())
				{
					this.waterTank.setCapacity(newTank);
				}
				
			}
			
			if(upgrade.getItem() == ModItems.rfCapacityUpgradeTier1 && rfcapacity == false)
			{
				int tier = upgrade.getMetadata()+1;
				
				int newEnergy = this.baseRFStorage;
				
				rfcapacity = true;

				if(tier == 1) {
					newEnergy += (int)(newEnergy * 0.5); 
				}
				else if(tier == 2) {
					newEnergy += (int)(newEnergy * 0.75); 
				}
				else if(tier == 3) {
					newEnergy += (int)(newEnergy * 1); 
				}

				if(newEnergy != this.energy.getMaxEnergyStored())
				{
					this.energy.setCapacity(newEnergy);
				}
			}
		}

		
		if(!tankcapacity && this.fertilizerTank.getCapacity() != this.baseTankSize)
		{
			this.fertilizerTank.setCapacity(this.baseTankSize);
		}
		
		if(!tankcapacity && this.waterTank.getCapacity() != this.baseTankSize)
		{
			this.waterTank.setCapacity(this.baseTankSize);
		}
		
		if(!rfcapacity && this.energy.getMaxEnergyStored() != this.baseRFStorage)
		{
			this.energy.setCapacity(this.baseRFStorage);
		}
		
		if(!rfupgrade && this.rfEffencyMultpyler != 1)
		{
			this.rfEffencyMultpyler = 1;
		}
		
		
		if(!speedupgrade && this.upgradeSpeedMulipyler != 1)
		{
			this.upgradeSpeedMulipyler = 1;
		}

	}

	public int getMixTime(@Nullable ItemStack stack)
	{
		return 200;
	}
	
//	/// INVENTORY
//	@Override
//	public String getName() { return null; }
//
//	@Override
//	public boolean hasCustomName() { return false; }
//
//	@Override
//	public int getSizeInventory() 
//	{
//		return 5;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int slot) 
//	{
//		return this.inventory.getStackInSlot(slot);
//	}
//
//	@Override
//	public ItemStack decrStackSize(int slot, int count) 
//	{
//		return this.inventory.decrStackSize(slot, count);
//	}
//
//	@Override
//	public ItemStack removeStackFromSlot(int slot) 
//	{
//		return this.inventory.removeStackFromSlot(slot);
//	}
//
//	@Override
//	public void setInventorySlotContents(int slot, ItemStack stack) 
//	{
//		this.inventory.setInventorySlotContents(slot, stack);
//	}
//
//	@Override
//	public int getInventoryStackLimit() 
//	{
//		return 64;
//	}
//
//	@Override
//	public boolean isUseableByPlayer(EntityPlayer player) { return true; }
//
//	@Override
//	public void openInventory(EntityPlayer player) { this.inventory.openInventory(player); }
//
//	@Override
//	public void closeInventory(EntityPlayer player) { this.inventory.closeInventory(player); }
//
//	@Override
//	public boolean isItemValidForSlot(int index, ItemStack stack) 
//	{
//		if(index == 0 && (stack.getItem() == ModItems.manure || stack.getItem() == Item.getItemFromBlock(ModBlocks.manureBlock)))
//				return true;
//		else if(index == 1 && stack.getItem() == Items.WATER_BUCKET)
//			return true;
//		else if(index == 3 && stack.getItem() == ModFluids.getFertilizerBucket().getItem())
//			return true;
//
//		return false; 
//	}

//	@Override
	public int getField(int id) 
	{ 
        switch (id)
        {
            case 0:
            	return this.waterTank.getFluidAmount();
            case 1:
            	return this.fertilizerTank.getFluidAmount();	
            case 2:
            	return this.fertlizerMixTime;
            case 3:
            	return this.energy.getEnergyStored();
            case 4:
            	return this.waterTank.getCapacity();
            case 5:
            	return this.fertilizerTank.getCapacity();
            case 6: 
            	return this.energy.getMaxEnergyStored();
            default:
                return 0;
        }

	}
	
	public int waterLevel = 0;
	public int fertilizerLevel = 0;
	public int storedEnergyLevel = 0;
//	@Override
	public void setField(int id, int value) 
	{ 
		
        switch (id)
        {
            case 0:
            	waterLevel = value;
                break;
            case 1:
            	fertilizerLevel = value;
                break;
            case 2:
                this.fertlizerMixTime  = value;
                break;
            case 3:
            	this.energy.setEnergyStored(value);
            	break;
            case 4:
            	this.waterTank.setCapacity(value);
            	break;
            case 5:
            	this.fertilizerTank.setCapacity(value);
                break;
            case 6:
            	this.energy.setCapacity(value);
            	break;

        }
	}

//	@Override
//	public int getFieldCount() { return 4; }
//
//	@Override
//	public void clear() { 
//		this.inventory.clear();
//	}
//	
	

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
       	this.waterTank.readFromNBT(tag.getCompoundTag("waterTank"));
        this.fertilizerTank.readFromNBT(tag.getCompoundTag("fertilizerTank"));
        
        this.energy.readFromNBT(tag);
        
        this.inputInventory.readFromNBT(tag);
        this.outputInventory.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
    	tag = super.writeToNBT(tag);
    	
    	NBTTagCompound waterTTag = new NBTTagCompound();
        NBTTagCompound fertTTag = new NBTTagCompound();
    		this.waterTank.writeToNBT(waterTTag);
    		this.fertilizerTank.writeToNBT(fertTTag);
        tag.setTag("waterTank", waterTTag);
        tag.setTag("fertilizerTank", fertTTag);
        
        this.inputInventory.writeToNBT(tag);
        this.outputInventory.writeToNBT(tag);
        
        this.energy.writeToNBT(tag);
        return tag;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
    		return (T) this.fluidMap;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) 
            return (T) this.outputInventory;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
            return (T) this.inputInventory;
		else if (capability == CapabilityEnergy.ENERGY) 
			return (T) this.energy;
        return super.getCapability(capability, facing);
    }

    // ENERGY
	@Override
	public int getEnergyStored(EnumFacing from) {
		return this.energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return this.energy.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return this.energy.receiveEnergy(maxReceive, simulate);
	}


    
}
