package com.gendeathrow.hatchery.core;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.common.capability.CapabilityAnimalStatsHandler;
import com.gendeathrow.hatchery.core.config.ConfigHandler;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.entities.EntityRooster;
import com.gendeathrow.hatchery.entities.ai.ChickenBreeding;
import com.gendeathrow.hatchery.entities.ai.EntityAIMateWithRooster;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler 
{

	@SubscribeEvent
	public void onPlayerInteract(RightClickItem event) 
	{

		if(!(Settings.CAN_THROW_EGG) && event.getItemStack().getItem() instanceof ItemEgg && !event.getItemStack().getItem().getRegistryName().toString().equalsIgnoreCase("chickens:liquid_egg"))
		{
			event.setCanceled(true);
		}
	}
	
	
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equals(Hatchery.MODID))
		{
			ConfigHandler.CONFIG.save();
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

		if (event.getEntity() instanceof EntityLivingBase) 
		{
			EntityLivingBase entity = (EntityLivingBase) event.getEntity();
			
			if(event.getEntity() instanceof EntityChicken)
			{
				EntityChicken chicken = (EntityChicken) event.getEntity();
				World world = chicken.worldObj;
				if (!world.isRemote) 
				{
					chicken.tasks.addTask(2, new EntityAIMateWithRooster((EntityChicken) chicken, 1.0D));
					
					// rooster breeding only or is egg breeding remove vanilla AI
					if(Settings.ROOSTER_BREED_ONLY || Settings.IS_EGG_BREEDING)
					{
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
							chicken.tasks.removeTask(rmv);
						}
						
//						chicken.tasks.removeTask(new EntityAIMate((EntityChicken) chicken, 1.0D));
						
						
					}
					
					// if were not only rooster breeding add in special ai for eggs
					if(!Settings.ROOSTER_BREED_ONLY && Settings.IS_EGG_BREEDING)
					{
						chicken.tasks.addTask(1, new ChickenBreeding(chicken, 1.0F));
					}
				}
			}
		
			if (event.getEntity() instanceof EntityRooster) 
			{
				World world = event.getEntity().worldObj;
				if (!world.isRemote) 
				{
					((EntityRooster) entity).tasks.removeTask(new EntityAIMate((EntityRooster) entity, 1.0D));
					((EntityRooster) entity).tasks.removeTask(new EntityAIPanic((EntityRooster) entity, 1.4D));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void AttachCap(AttachCapabilitiesEvent event)
	{

		if(event.getObject() instanceof EntityChicken)
		{
			if(!((EntityChicken)event.getObject()).hasCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, EnumFacing.DOWN))
			{
				event.addCapability(new ResourceLocation(Hatchery.MODID,"eatting_animal"), new CapabilityAnimalStatsHandler());
			}
		}
		
	}


	@SubscribeEvent
	public void EntityUpdate(LivingUpdateEvent event)
	{
		if(!(event.getEntity() instanceof EntityAnimal)) return;
		
		if(event.getEntity().hasCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, null))
		{
			event.getEntity().getCapability(CapabilityAnimalStatsHandler.ANIMAL_HANDLER_CAPABILITY, null).update((EntityAnimal)event.getEntity());
		}
	}
}
