package com.gendeathrow.hatchery.core.proxies;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.nestblock.HatcheryTileEntity;
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
		
    	GameRegistry.register(ModItems.nest);
    		ItemBlock modItem = new ItemBlock(ModItems.nest);
    	GameRegistry.register(modItem.setRegistryName(ModItems.nest.getRegistryName()));
    	
    	GameRegistry.register(ModItems.pen);
    		ItemBlock penItem = new ItemBlock(ModItems.pen);
    	GameRegistry.register(penItem.setRegistryName(ModItems.pen.getRegistryName()));
    	
    	GameRegistry.register(ModItems.hatcheryEgg);
    	
    	GameRegistry.registerTileEntity(HatcheryTileEntity.class, Hatchery.MODID);
    	GameRegistry.addRecipe(new ItemStack(ModItems.nest), "xxx", "AxA", "AAA",'A', Blocks.HAY_BLOCK);

    	
	}
	
	public void postInit(FMLPostInitializationEvent event)
	{
	
	}
}
