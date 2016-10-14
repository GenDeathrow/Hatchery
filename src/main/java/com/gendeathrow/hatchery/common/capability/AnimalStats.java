package com.gendeathrow.hatchery.common.capability;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class AnimalStats implements IAnimalStats
{
	Random rand = new Random();
	
	int timeToEat = 200 + rand.nextInt(200);
	long timeSinceLast = Minecraft.getSystemTime();
	
	@Override
	public void update() 
	{
		if(timeToEat >= 0) timeToEat--;
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
