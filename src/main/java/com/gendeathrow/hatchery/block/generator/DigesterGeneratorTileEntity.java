package com.gendeathrow.hatchery.block.generator;

import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.inventory.InventoryStorage;
import com.gendeathrow.hatchery.item.upgrades.BaseUpgrade;
import com.gendeathrow.hatchery.item.upgrades.RFEfficiencyUpgrade;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class DigesterGeneratorTileEntity extends TileUpgradable implements IInventory, IEnergyProvider, ITickable
{
	public int time = 0;
	
	protected int baseEnergyStorage = 200000;
	protected int baseTankStorage = 5000;
	
	protected EnergyStorageRF energy = new EnergyStorageRF(200000);
	protected InventoryStorage inventory = new InventoryStorage(this, 2);
  
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
	
	
	

	// Currently this handles the old ModBlocks.pn
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
		if((oldState.getBlock() == ModBlocks.digesterGenerator || oldState.getBlock() == ModBlocks.digesterGeneratorOn) && (newSate.getBlock() == ModBlocks.digesterGenerator || newSate.getBlock() == ModBlocks.digesterGeneratorOn))
		{
			return false;
		}
		else 
		  return oldState != newSate;
    }

	protected boolean canGenerate() {
		return fuelRF > 0 ? true : getTank().getFluidAmount() >= 50;
	}
	
    public boolean isGenerating()
    {
        return this.fuelRF > 0;
    }
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	/* IEnergyProvider */
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		return energy.extractEnergy(maxExtract, simulate);
	}

	/* IEnergyHandler */
	@Override
	public int getEnergyStored(EnumFacing from) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energy.getMaxEnergyStored();
	}
	
	
	int rfEnergyFuel = 15000;
	int fuelRF;
	boolean isActive = true;

	
	@Override
	public void update() 
	{
        boolean flag = this.isGenerating();
        
		if(!this.worldObj.isRemote)
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
			
			if ((energy.getEnergyStored() > 0)) 
			{
				for (EnumFacing facing : EnumFacing.VALUES) 
				{
						TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
						if (tile != null && tile instanceof IEnergyReceiver) 
						{
							int received = ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(), energy.getEnergyStored(), false);
							extractEnergy(facing, received, false);
						}
				}
			}
			
			if(this.inventory.getStackInSlot(0) != null && this.inventory.getStackInSlot(1) == null && this.fertlizerTank.getFluidAmount() < this.fertlizerTank.getCapacity())
			{
				ItemStack stack = this.inventory.getStackInSlot(0);
				
				IFluidHandler handler = FluidUtil.getFluidHandler(stack);
				
				if(handler != null)
				{
					if(FluidUtil.tryFluidTransfer(this.fertlizerTank, handler, this.fertlizerTank.getCapacity(), true) != null)
					{
						this.inventory.setInventorySlotContents(1, stack);
						this.inventory.setInventorySlotContents(0, null);
					}
				}
			}
			
			
			if (flag != isGenerating())
			{
				//flag1 = true;
				DigesterGeneratorBlock.setState(this.isGenerating(), this.worldObj, this.pos);
			}
		}
	}
	

	public FluidTank getTank()
	{
		return fertlizerTank;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		this.inventory.readFromNBT(nbt);
		this.energy.readFromNBT(nbt);
		this.fertlizerTank.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = this.inventory.writeToNBT(nbt);
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
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
            return (T) new InvWrapper(this);
        else if (capability == CapabilityEnergy.ENERGY)
        	return (T) this.energy;
    	
        return super.getCapability(capability, facing);
    }
    
    // INVENTORY
	@Override
	public ItemStack removeStackFromSlot(int index){
		return this.inventory.removeStackFromSlot(index);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public String getName() {
		return null;
	}


	@Override
	public boolean hasCustomName() {
		return false;
	}


	@Override
	public int getSizeInventory() {
		return this.inventory.getSizeInventory();
	}


	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.getStackInSlot(index);
	}


	@Override
	public ItemStack decrStackSize(int index, int count) {
		return this.inventory.decrStackSize(index, count);
	}


	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.inventory.setInventorySlotContents(index, stack);
	}


	@Override
	public int getInventoryStackLimit() {
		return 64;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return true;
	}


	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	public float tankLevel;

	@Override
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


	@Override
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
            	System.out.println("TANK IN:"+ value);
            	this.getTank().setCapacity(value);
                break;
            default:
                break;
        }	
	}


	@Override
	public int getFieldCount() {
		return 3;
	}


	@Override
	public void clear() {
		this.inventory.clear();
	}
	
	
	public int getRFPerTick() 
	{
		return this.rfPerTick;
	}

	protected void updateUpgrades()
	{
		boolean rfupgrade = false;
		boolean rfcapacity = false;
		boolean tankcapacity = false;
		
		for(ItemStack upgrade : this.getUpgrades())
		{
			if(upgrade == null) continue;
		
			
			if(upgrade.getItem() instanceof RFEfficiencyUpgrade)
			{
				rfupgrade = true;
				
				int newTick = ((RFEfficiencyUpgrade)upgrade.getItem()).getUpgradeTier(upgrade, "") * 20 + 20;
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
					
					System.out.println("tank level"+ this.fertlizerTank.getCapacity());
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
