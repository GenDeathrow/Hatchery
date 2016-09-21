package com.gendeathrow.hatchery.core.proxies;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.gendeathrow.hatchery.core.EventHandler;
import com.gendeathrow.hatchery.core.ModItems;

public class CommonProxy
{
	public boolean isClient()
	{
		return false;
	}
	
	public boolean isOpenToLAN()
	{
		return false;
	}
	
	public void registerTickHandlers() 
	{
	
	}
	
	
	public void initRenderers()
	{
		
	}
	
	public void registerEventHandlers()
	{
		EventHandler eventhandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventhandler);
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		
	}
	
	public void init(FMLInitializationEvent event)
	{
		ModItems.RegisterBlocks();
		ModItems.RegisterTileEntitys();
		ModItems.RegisterItems();
		ModItems.RegisterRecipes();
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
	
	}
}
