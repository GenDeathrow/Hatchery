package com.gendeathrow.hatchery.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityChicken;

public class RegisterEggsUtil 
{

	
	
	public static void register()
	{
		for(Class<? extends Entity> entity : EntityList.CLASS_TO_NAME.keySet())
		{
			
			
			if(entity.isInstance(EntityChicken.class))
			{
				System.out.println("Chicken:"+ entity.getName());
			}
		}
	}
}
