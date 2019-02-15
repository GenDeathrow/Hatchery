package com.gendeathrow.hatchery.block.generator;

import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;

public class DigesterGeneratorTileEntity extends TileUpgradable implements ITickable
{
	public int time = 0;
	
	protected int baseEnergyStorage = 200000;
	protected int baseTankStorage = 5000;
	
	protected EnergyStorageRF energy = new EnergyStorageRF(200000) 
	{ 	@Override
		public boolean canReceive() 
		{return false;}
	};
	
	protected InventoryStroageModifiable inputInventory =  new InventoryStroageModifiable("inputItems", 2) {
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			if(slot == 0 && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
			{
				if(FluidUtil.getFluidContained(stack) != null && FluidUtil.getFluidContained(stack).getFluid() == ModFluids.liquidfertilizer){
					return true;
				}
			}

			return false; 
		}
	};
	protected InventoryStroageModifiable outputInventory = new InventoryStroageModifiable("outputItems", 2);
  
	private int rfPerTick = 20;
	private int baseRfPerTick = 20;
	
	boolean isGenerating;
	
	private FluidTank fertlizerTank = new FluidTank(new FluidStack(ModFluids.liquidfertilizer, 0), 5000){
		@Override
	    public boolean canDrain()
	    {
			return false;
		}
	};
	
	public DigesterGeneratorTileEntity() 
	{
		super(2);
	}
	
	
	public FluidTank getFertilizerTank()
	{
		return this.fertlizerTank;
	}

	// Currently this handles the old ModBlocks.pn
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
		if(oldState.getBlock() == ModBlocks.digesterGenerator && newSate.getBlock() == ModBlocks.digesterGenerator)
		{
			return false;
		}
		else 
		  return oldState != newSate;
    }

	protected boolean canGenerate() {
		if(world.isBlockPowered(getPos()))
			return false;
		
		return fuelRF > 0 ? true : getTank().getFluidAmount() >= 50;
	}
	
    public boolean isGenerating()
    {
		if(world.isBlockPowered(getPos()))
			return false;
		
        return this.fuelRF > 0;
    }
	
	int rfEnergyFuel = 15000;
	int fuelRF;
	boolean isActive = true;

	
	@Override
	public void update() 
	{
        boolean flag = this.isGenerating();
        
		if(!this.world.isRemote)
		{
			updateUpgrades();
			
			if(isActive && canGenerate() && energy.getEnergyStored() < energy.getMaxEnergyStored())
			{
				if (fuelRF <= 0) 
				{
					fuelRF = rfEnergyFuel;
					getTank().drainInternal(50, true);
				}
				
				energy.modifyEnergyStored(getRFPerTick());
				fuelRF -= getRFPerTick();
			}
			
			for (EnumFacing facing : EnumFacing.VALUES) 
			{
					if(energy.getEnergyStored() <= 0) break;
					
					TileEntity tile = world.getTileEntity(pos.offset(facing));
		
					if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite())) 
					{
						IEnergyStorage tileEnergy = tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite());
						
						if(tileEnergy.canReceive()) {
							int received = tileEnergy.receiveEnergy(energy.getEnergyStored(), false);
							energy.extractEnergy(received, false);
						}
					}
			}
			
			if(!this.inputInventory.getStackInSlot(0).isEmpty() && this.outputInventory.getStackInSlot(0).isEmpty() && this.fertlizerTank.getFluidAmount() < this.fertlizerTank.getCapacity())
			{
				ItemStack stack = this.inputInventory.getStackInSlot(0);
				
				IFluidHandlerItem handler = FluidUtil.getFluidHandler(stack);
				
				if(handler != null)
				{
					if(FluidUtil.tryFluidTransfer(this.fertlizerTank, handler, this.fertlizerTank.getCapacity(), true) != null)
					{
						this.outputInventory.setStackInSlot(0, handler.getContainer());
						this.inputInventory.setStackInSlot(0, ItemStack.EMPTY);
					}
				}
			}
			
			if (flag != isGenerating() || updateState)
			{
				DigesterGeneratorBlock.setState(this.isGenerating(), this.world, this.pos);
				updateState = false;
				//System.out.println("test 2");
				this.markDirty();
			}
		}
	}
	public boolean updateState = false; 

	public FluidTank getTank()
	{
		return fertlizerTank;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		this.inputInventory.readFromNBT(nbt);
		this.outputInventory.readFromNBT(nbt);
		this.energy.readFromNBT(nbt);
		this.fertlizerTank.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = this.inputInventory.writeToNBT(nbt);
		nbt = this.outputInventory.writeToNBT(nbt);
		nbt = this.energy.writeToNBT(nbt);
		nbt = this.fertlizerTank.writeToNBT(nbt);
		return super.writeToNBT(nbt);
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
            return (T) this.fertlizerTank;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) 
            return (T) this.outputInventory;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
            return (T) this.inputInventory;
        else if (capability == CapabilityEnergy.ENERGY)
        	return (T) this.energy;
    	
        return super.getCapability(capability, facing);
    }
 
	public float tankLevel;
//
//	@Override
	public int getField(int id) 
	{
        switch (id)
        {
            case 0:
            	return this.energy.getEnergyStored();
            case 1:
            	return this.getTank().getFluidAmount();
            case 2:
            	return this.rfPerTick;
            case 3:
            	return this.energy.getMaxEnergyStored();
            case 4:
            	return this.getTank().getCapacity();
            default:
                return 0;
        }
	}


//	@Override
	public void setField(int id, int value) 
	{
        switch (id)
        {
            case 0:
            	this.energy.setEnergyStored(value);
                break;
            case 1:
            	this.tankLevel = value;
            	break;
            case 2:
            	this.rfPerTick = value;
                break;
            case 3:
            	this.energy.setCapacity(value);
                break;
            case 4:
            	this.getTank().setCapacity(value);
                break;
            default:
                break;
        }	
	}


//	@Override
//	public int getFieldCount() {
//		return 3;
//	}
//
//
//	@Override
//	public void clear() {
//		this.inventory.clear();
//	}
	
	
	public int getRFPerTick() 
	{
		return this.rfPerTick;
	}

	@Override
	public boolean canUseUpgrade(ItemStack item)
	{
		return item.getItem() == ModItems.rfUpgradeTier  || item.getItem() == ModItems.tankUpgradeTier1 || item.getItem() == ModItems.rfCapacityUpgradeTier1;
	}
	
	
	protected void updateUpgrades()
	{
		boolean rfupgrade = false;
		boolean rfcapacity = false;
		boolean tankcapacity = false;
		
		for(ItemStack upgrade : this.getAllUpgrades())
		{
			if(upgrade.isEmpty()) continue;
			
			if(upgrade.getItem() == ModItems.rfUpgradeTier )
			{
				rfupgrade = true;
				
				int tier = upgrade.getMetadata()+1;
				int newTick = tier * 20 + 20;
				if(newTick > this.getRFPerTick())
				{
					this.rfPerTick = newTick;
				}
			}
			
			if(upgrade.getItem() == ModItems.tankUpgradeTier1 && tankcapacity == false)
			{
				tankcapacity = true;
				
				int tier = upgrade.getMetadata()+1;
				int newTank = this.baseTankStorage;
				
				if(tier == 1) {
					newTank += 2000;
				}
				else if(tier == 2) {
					newTank += 4000; 
				}
				else if(tier == 3) {
					newTank += 6000; 
				}

				if(newTank != this.fertlizerTank.getCapacity())
				{
					this.fertlizerTank.setCapacity(newTank);
				}
				
			}
			
			if(upgrade.getItem() == ModItems.rfCapacityUpgradeTier1 && rfcapacity == false)
			{
				int tier = upgrade.getMetadata()+1;
				
				int newEnergy = this.baseEnergyStorage;
				
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

		
		if(!tankcapacity && this.fertlizerTank.getCapacity() != this.baseTankStorage)
		{
			this.fertlizerTank.setCapacity(this.baseTankStorage);
		}
		
		if(!rfcapacity && this.energy.getMaxEnergyStored() != this.baseEnergyStorage)
		{
			this.energy.setCapacity(this.baseEnergyStorage);
		}
		
		if(!rfupgrade) 
			this.rfPerTick = this.baseRfPerTick;
	}
}
