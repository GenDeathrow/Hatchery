package com.gendeathrow.hatchery.core;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.common.capability.CapabilityAnimalStatsHandler;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.entity.ai.ChickenBreeding;

public class EventHandler 
{

	@SubscribeEvent
	public void onPlayerInteract(RightClickItem event) 
	{

		if(!(Settings.canThrowEgg) && event.getItemStack().getItem() instanceof ItemEgg)
		{
			event.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onHoeEvent(UseHoeEvent event)
	{
		IBlockState blockstate = event.getWorld().getBlockState(event.getPos());
		Block block = blockstate.getBlock();
		
         if (event.getWorld().isAirBlock(event.getPos().up()))
         {
     		if(block == ModBlocks.fertlizedDirt)
    		{
     			event.getWorld().playSound(((EntityPlayer) null), event.getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

     	        if (!event.getWorld().isRemote)
     	        {
     	        	event.getWorld().setBlockState(event.getPos(), ModBlocks.fertilzedFarmland.getDefaultState(), 11);
     	        	event.getCurrent().damageItem(1, event.getEntityLiving());
     	        }
     	        
     	        event.setResult(Result.ALLOW);
    		}
         }
		
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
			
			// Not yet
			//chicken.tasks.addTask(priority, new AutoBreeding(chicken, 1.0f));
			
		}
	}
	
	@SubscribeEvent
	public void AttachCap(AttachCapabilitiesEvent event)
	{
		if(event.getObject() instanceof EntityChicken)
		{
			event.addCapability(new ResourceLocation(Hatchery.MODID,"eatting_animal"), new CapabilityAnimalStatsHandler());
		}
	}


	@SubscribeEvent
	public void EntityUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntity() instanceof EntityAnimal)) return;
		
		if(event.getEntity().hasCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, null))
		{
			event.getEntity().getCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, null).update();
		}
	}
	
//	@SubscribeEvent
//	public void craftingEvent(ItemCraftedEvent event)
//	{
//		System.out.println("test");
//	}
}
