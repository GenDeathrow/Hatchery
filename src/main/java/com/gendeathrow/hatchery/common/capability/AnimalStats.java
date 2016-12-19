package com.gendeathrow.hatchery.common.capability;

import java.util.Random;

import com.gendeathrow.hatchery.core.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.nbt.NBTTagCompound;

public class AnimalStats implements IAnimalStats
{
	public static Random rand = new Random();

	private int timeToEat = 200 + rand.nextInt(200);
	
	private int timeToDrink = 1000 + rand.nextInt(1000);
	
	private int timeToPoop = this.rand.nextInt(6000) + 6000;
	
	public AnimalStats(){}
	

	@Override
	public void update(EntityAnimal entity) 
	{
		if(timeToEat >= 0) timeToEat--;
		
		if(--timeToPoop <= 0)
		{
			if(!entity.worldObj.isRemote)
				entity.dropItem(ModItems.manure, 1);
			
			timeToPoop = entity.getRNG().nextInt(6000) + 6000;
		}
	}
	
	public boolean needsToPoop(Entity entity)
	{
		return false;
	}
    public AnimalStats readFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("timeToEat"))
        {
        	this.setEattenTime(nbt.getInteger("timeToEat"));
        }
        else
        {
            this.setEattenTime(2000);
        }
        
        return this;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {

    	nbt.setInteger("timeToEat", timeToEat);
    	
        return nbt;
    }
    
    
    
    /////////////////////////////////////
    // Animal Eatting
    /////////////////////////////////////
    
	@Override
	public int getEattenTime()  
	{
		return timeToEat;
	}

	@Override
	public int addEattenTime(int add) 
	{
		return timeToEat += add;
	}

	@Override
	public int setEattenTime(int i) 
	{
		return timeToEat = i;
	}
	
	@Override
	public boolean canEat() 
	{
		if(timeToEat <= 0)
		{
			timeToEat = 200 + rand.nextInt(200);
			return true;
		}
		
		return false;
	}



    /////////////////////////////////////
    // Animal Drinking
    /////////////////////////////////////
    
	

}
