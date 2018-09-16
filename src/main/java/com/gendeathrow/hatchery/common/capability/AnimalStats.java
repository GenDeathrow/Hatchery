package com.gendeathrow.hatchery.common.capability;

import java.util.Random;

import com.gendeathrow.hatchery.core.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class AnimalStats implements IAnimalStats
{
	public static Random rand = new Random();

	private int timeToEat = 200 + rand.nextInt(200);
	
	private int timeToDrink = 1000 + rand.nextInt(1000);
	
	private int timeToPoop = this.rand.nextInt(2000) + 5000;
	
	public AnimalStats(){}
	

	@Override
	public void update(EntityAnimal entity) 
	{
		if(timeToEat >= 0) timeToEat--;
		
		if(canPoop())  {
			if(!entity.world.isRemote) {
				entity.dropItem(ModItems.manure, rand.nextInt(2)+1);
			}
			timeToPoop = entity.getRNG().nextInt(2000) + 6000;
		}
	}
	
    public AnimalStats readFromNBT(NBTTagCompound nbt)
    {
        if (nbt.hasKey("timeToEat"))
        	this.setEattenTime(nbt.getInteger("timeToEat"));
        else
            this.setEattenTime(400);
        if (nbt.hasKey("timeToPoop")) 
        	this.timeToPoop = nbt.getInteger("timeToPoop");
        
        return this;
    }

	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
    	nbt.setInteger("timeToEat", timeToEat);
    	nbt.setInteger("timeToPoop", timeToPoop);
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
		return timeToEat <= 0;
	}
	
	@Override
	public void Eat() {
		timeToEat = rand.nextInt(200) + 200;
	}
	
	@Override
	public boolean canPoop() {
		return (--timeToPoop <= 0);
	}
	 
	@Override
	public int getToPoopTime() {
		return this.timeToPoop;
	}


	@Override
	public int setPoopTime(int value) {
		return this.timeToPoop = value;
	}

    /////////////////////////////////////
    // Animal Drinking
    /////////////////////////////////////
    
	

}
