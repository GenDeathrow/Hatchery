package com.gendeathrow.hatchery.core;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

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
     		if(block == ModItems.fertlizedDirt)
    		{
     			event.getWorld().playSound(((EntityPlayer) null), event.getPos(), SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

     	        if (!event.getWorld().isRemote)
     	        {
     	        	event.getWorld().setBlockState(event.getPos(), ModItems.fertilzedFarmland.getDefaultState(), 11);
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
		}
	}
	
}
