package com.gendeathrow.hatchery.common.capability;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CapabilityAnimalStatsHandler implements ICapabilityProvider
{

	@CapabilityInject(IAnimalStats.class)
	public static final Capability<IAnimalStats> ANIMAL_HANDLER_CAPABILITY = null;
	
	private IAnimalStats instance = ANIMAL_HANDLER_CAPABILITY.getDefaultInstance();
	
	
    @CapabilityInject(IAnimalStats.class)
    private static void capRegistered(Capability<IAnimalStats> cap)
    {
        Hatchery.logger.info("IAnimalStats was registered wheeeeee!");
    }
    
	public static void register()
	{
		CapabilityManager.INSTANCE.register(IAnimalStats.class, new Capability.IStorage<IAnimalStats>()
				{
					@Override
					public NBTBase writeNBT(Capability<IAnimalStats> capability,	IAnimalStats instance, EnumFacing side) 
					{
						NBTTagCompound nbt = new NBTTagCompound();
						
							AnimalStats eatable = (AnimalStats) instance;
						
							eatable.writeToNBT(nbt);
							
						return nbt;
					}

					@Override
					public void readNBT(Capability<IAnimalStats> capability,	IAnimalStats instance, EnumFacing side, NBTBase nbt) 
					{
		                NBTTagCompound tags = (NBTTagCompound) nbt;
						AnimalStats eatable = (AnimalStats) instance;
						eatable.readFromNBT(tags);		
					}
				}, AnimalStats.class
				);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
	{
		return capability != null && capability == ANIMAL_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) 
	{
		if(capability != null && capability == ANIMAL_HANDLER_CAPABILITY)
			return ANIMAL_HANDLER_CAPABILITY.cast(this.instance);
		return null;
	}
}
