package com.gendeathrow.hatchery.core;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.entity.ai.ChickenBreeding;

public class EventHandler 
{

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) 
	{

		
	}

	@SubscribeEvent
	public void onSpawnCheck(EntityJoinWorldEvent event)
	{

		if(!Settings.eggBreeding) return;
		if(event.getEntity() instanceof EntityChicken)
		{
			EntityChicken chicken = (EntityChicken) event.getEntity();
			
			EntityAIBase rmv = null;
			int priority = 1;
			
			for(EntityAITaskEntry task : chicken.tasks.taskEntries)
			{
				if(task.action instanceof EntityAIMate)
				{
					rmv = task.action;
					priority = task.priority;
					break;
				}
			}
			
			if(rmv != null)
			{
//				Hatchery.logger.info("Removing Old Breeding...");
				chicken.tasks.removeTask(rmv);
//				Hatchery.logger.info("Adding new Breeding...");
				chicken.tasks.addTask(priority, new ChickenBreeding(chicken, 1.0F));
//				Hatchery.logger.info("... Dont");
			}
		}
	}
	
}
