package com.gendeathrow.hatchery.common.capability;

public interface IAnimalStats 
{
	
	
	
	
	public abstract void update();

	/**
	 * Returns if the entity/chicken has already eatten 
	 * @return
	 */
	int getEattenTime();
	
	int addEattenTime(int add);
	
	int setEattenTime(int i);
	
	boolean canEat();

	
	
	
}
