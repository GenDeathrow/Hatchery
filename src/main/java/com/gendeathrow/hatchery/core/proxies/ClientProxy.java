package com.gendeathrow.hatchery.core.proxies;


import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.gendeathrow.hatchery.block.nestblock.HatcheryTileEntity;
import com.gendeathrow.hatchery.block.nestblock.HatcheryTileEntityRender;
import com.gendeathrow.hatchery.core.ModItems;

public class ClientProxy extends CommonProxy
{
	
	
	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public boolean isOpenToLAN()
	{
		if (Minecraft.getMinecraft().isIntegratedServerRunning())
		{
			return Minecraft.getMinecraft().getIntegratedServer().getPublic();
		} else
		{
			return false;
		}
	}
	
	@Override
	public void registerTickHandlers()
	{
		super.registerTickHandlers();
	}
	
	@Override
	public void registerEventHandlers()
	{
		super.registerEventHandlers();
		
		
//			GuiEventHandler guieventhandler = new GuiEventHandler();
//    		MinecraftForge.EVENT_BUS.register(guieventhandler);
//   
//    		FMLCommonHandler.instance().bus().register(new KeyBinds());
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}
	
	@Override
	public void initRenderers()
	{
		
		registerBlockModel(ModItems.nest, 0, ModItems.nest.getRegistryName().toString());
		// registerBlockModel(ModItems.nest, 1, ModItems.nest.getRegistryName().toString());
		
		registerBlockModel(ModItems.pen, 0, ModItems.pen.getRegistryName().toString());
		
		registerItemModel(ModItems.hatcheryEgg);
		ClientRegistry.bindTileEntitySpecialRenderer(HatcheryTileEntity.class, new HatcheryTileEntityRender());
		
		//registerItemModel(ModItems.nestItem);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerBlockModel(Block block)
	{
		registerBlockModel(block, 0, block.getRegistryName().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerBlockModel(Block block, int meta, String name)
	{
		Item item = Item.getItemFromBlock(block);
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		
		if(!name.equals(item.getRegistryName()))
		{
		    ModelBakery.registerItemVariants(item, model);
		}
		
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, model);
	}
	
//	@SideOnly(Side.CLIENT)
//	public static void registerItemModel(Item item)
//	{
//		 Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
//	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item)
	{
		registerItemModel(item, 0, item.getRegistryName().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, int meta, String name)
	{
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		
		if(!name.equals(item.getRegistryName()))
		{
		    ModelBakery.registerItemVariants(item, model);
		}
		
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, model);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}
	
	
	public void VoxelMenu()
	{
//		try
//		{
//
//			Class<? extends GuiMainMenu> ingameGuiClass = (Class<? extends GuiMainMenu>) Class.forName("com.thevoxelbox.voxelmenu.ingame.GuiIngameMenu");
//			Method mRegisterCustomScreen = ingameGuiClass.getDeclaredMethod("registerCustomScreen", String.class, Class.class, String.class);
//			
//			mRegisterCustomScreen.invoke(null, "", EM_Gui_Menu.class, StatCollector.translateToLocal("options.enviromine.menu.title"));
//		
//			boolean voxelMenuExists = true;
//		} catch (ClassNotFoundException ex) { // This means VoxelMenu does not
//			// 	exist
//			boolean voxelMenuExists = false;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
