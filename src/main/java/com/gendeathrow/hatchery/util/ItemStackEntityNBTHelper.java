package com.gendeathrow.hatchery.util;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemStackEntityNBTHelper 
{

	public static ItemStack addEntitytoItemStack(ItemStack stackIn, EntityLiving entityIn)
	{
		return addEntitytoItemStack(stackIn, entityIn, false);
	}
	
	public static ItemStack addEntitytoItemStack(ItemStack stackIn, EntityLiving entityIn, boolean saveAll)
	{
		NBTTagCompound nbt = getStackNBT(stackIn);
		NBTTagCompound eTag = new NBTTagCompound();
			entityIn.writeEntityToNBT(eTag);
			
			if(saveAll)
				entityIn.writeToNBT(eTag);
			
			eTag.setString("id", EntityList.getEntityString(entityIn));
			nbt.setTag("storedEntity", eTag);
		stackIn.setTagCompound(nbt);
		
		return stackIn;
	}
	
	public static NBTTagCompound getEntityTagFromStack(ItemStack stackIn)
	{
		if(stackIn.hasTagCompound() && stackIn.getTagCompound().hasKey("storedEntity"))
		{
			return (NBTTagCompound) stackIn.getTagCompound().getTag("storedEntity");
		}
		
		return null;
	}
	
	public static NBTTagCompound getStackNBT(ItemStack stackIn)
	{
		NBTTagCompound compound = stackIn.getTagCompound();
		
		if(compound == null) 
		{
			compound = new NBTTagCompound();
		}
		
		return compound;
	}
}
