package com.gendeathrow.hatchery.common.capability;

import net.minecraft.entity.passive.EntityAnimal;

public interface IAnimalStats 
{

	public abstract void update(EntityAnimal animal);;

	/**
	 * Returns if the entity/chicken has already eatten 
	 * @return
	 */
	int getEattenTime();
	
	int addEattenTime(int add);
	
	int setEattenTime(int i);
	
	boolean canEat();

	
	
	
}
