package com.gendeathrow.hatchery.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;

public class SidedInterface 
{
	protected ArrayList<InterfaceType> allinterfaceTypes = new ArrayList<InterfaceType>();
	protected ArrayList<InterfaceOption> allinterfaceOptions = new ArrayList<InterfaceOption>();
	
	
	public HashMap <EnumFacing, InterfaceOption> powerInterface = new HashMap<EnumFacing,InterfaceOption>(){
		private static final long serialVersionUID = 1L;
		{
			for(EnumFacing facing : EnumFacing.VALUES) {
				put(facing, InterfaceOption.None);
			}
		}
	};
	
	public HashMap <EnumFacing, InterfaceOption> itemInterface = new HashMap<EnumFacing,InterfaceOption>(){
		private static final long serialVersionUID = 1L;
		{
			for(EnumFacing facing : EnumFacing.VALUES) {
				put(facing, InterfaceOption.None);
			}
		}
	};
	
	public HashMap <EnumFacing, InterfaceOption> fluidInterface = new HashMap<EnumFacing,InterfaceOption>(){
		private static final long serialVersionUID = 1L;
		{
			for(EnumFacing facing : EnumFacing.VALUES) {
				put(facing, InterfaceOption.None);
			}
		}
	};

	public SidedInterface(InterfaceOption... iotypes) {
		allinterfaceOptions.addAll(Arrays.asList(iotypes));
		
		for(InterfaceOption option : iotypes) {
			InterfaceType type = getTypeFromOption(option);

			if(!allinterfaceTypes.contains(type)) 
				allinterfaceTypes.add(type);
		}
			
	}


	public boolean hasInterface(InterfaceType interfacetype) {
		if(allinterfaceTypes.contains(interfacetype))
			return true;
		
		return false;
	}
	
	/**
	 * Checks to see if a particular interface exist. 
	 * @param type
	 */
	public boolean hasInterfaceType(InterfaceType type){
		return allinterfaceTypes.contains(type);
	}
	
	
	public HashMap<EnumFacing, InterfaceOption> getListforType(InterfaceType type){
		switch(type) 
		{
			case Power:
				return this.powerInterface;
			case Fluid:
				return this.fluidInterface; 
			case Item:
				return this.itemInterface;
			default:
				return null;
		}
	}
	
	public HashMap<EnumFacing, InterfaceOption>  getListFromOption(InterfaceOption option){
		switch(option) 
		{
			case PowerInput:
			case PowerOutput:
				return this.powerInterface;
			case FluidInput:
			case FluidOutput:
				return this.fluidInterface; 
			case ItemInput:
			case ItemOutput:
				return this.itemInterface;
			default:
				return null;
		}
	}
	
	public InterfaceType getTypeFromOption(InterfaceOption option){
		switch(option) 
		{
			case PowerInput:
			case PowerOutput:
				return InterfaceType.Power;
			case FluidInput:
			case FluidOutput:
				return InterfaceType.Fluid; 
			case ItemInput:
			case ItemOutput:
				return InterfaceType.Item;
			default:
				return null;
		}
	}
	
	/**
	 * Gets a type of interface for the particular facing. 
	 * 
	 * @param facing
	 */
	public InterfaceOption getInterfaceOnFacing(EnumFacing facing, InterfaceType type) {
		return getListforType(type).get(facing);
	}

	/**
	 * Set the interface for that facing of a block. 
	 * 
	 * @param facing
	 * @param option
	 */
	public boolean setInterfaceOnFacing(InterfaceOption option, EnumFacing... facings) {
		if(!allinterfaceOptions.contains(option)) {
			System.out.println("failed");
			return false;
		}
		
		for(EnumFacing face : facings)
			getListFromOption(option).put(face, option);
		
		return true;
	}
	
	public boolean tryToggleInterface(EnumFacing facing, InterfaceType type) {
		if(!allinterfaceTypes.contains(type)) 
			return false;
		
		if(type == InterfaceType.Power) {
			if(getInterfaceOnFacing(facing, type) == InterfaceOption.PowerInput)
				setInterfaceOnFacing(InterfaceOption.PowerOutput, facing);
			else
				setInterfaceOnFacing(InterfaceOption.PowerInput, facing);
		}
		else if(type == InterfaceType.Fluid) {
			if(getInterfaceOnFacing(facing, type) == InterfaceOption.FluidInput)
				setInterfaceOnFacing(InterfaceOption.FluidOutput, facing);
			else
				setInterfaceOnFacing(InterfaceOption.FluidInput, facing);
		}
		else if(type == InterfaceType.Item) {
			if(getInterfaceOnFacing(facing, type) == InterfaceOption.ItemInput)
				setInterfaceOnFacing(InterfaceOption.ItemOutput, facing);
			else
				setInterfaceOnFacing(InterfaceOption.ItemInput, facing);
		}
		
		return true;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		for(InterfaceType type : InterfaceType.values()) {
			if(nbt.hasKey(type.name()))
				readSideInterfaceList(nbt.getTagList(type.name(), 9), type);
		}
	}
	
	private void readSideInterfaceList(NBTTagList listInterfaces, InterfaceType type) {
		Iterator<NBTBase> SI = listInterfaces.iterator();
		while(SI.hasNext())
		{
			NBTBase next = SI.next();
			EnumFacing facing = EnumFacing.byIndex(((NBTTagCompound)next).getInteger("facing"));
			InterfaceOption sideOption = InterfaceOption.valueOf(((NBTTagCompound)next).getString("interfaceOption"));
			
			if(facing != null && sideOption != null)
				getListforType(type).put(facing, sideOption);
		}
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		for(InterfaceType type : InterfaceType.values()) {
			if(hasInterfaceType(type))
				nbt.setTag(type.name(), writeListType(type));
		}
		return nbt;
	}
	
	private NBTTagList writeListType(InterfaceType type) {
		NBTTagList sideOptions = new NBTTagList();
		for(Entry<EnumFacing, InterfaceOption> side : getListforType(type).entrySet()) {
			NBTTagCompound data = new NBTTagCompound();
			data.setInteger("facing", side.getKey().getIndex());
			data.setString("interfaceOption", side.getValue().name());
			sideOptions.appendTag(data);	
		}
		return sideOptions;
	}
	
	public static enum InterfaceType{
		Power,
		Fluid,
		Item;
	}
	
	public static enum InterfaceOption{
		None,
		PowerInput,
		PowerOutput,
		ItemInput,
		ItemOutput,
		FluidInput,
		FluidOutput;
	}
}
