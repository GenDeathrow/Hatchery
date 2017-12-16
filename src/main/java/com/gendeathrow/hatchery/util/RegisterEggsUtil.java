package com.gendeathrow.hatchery.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.setycz.chickens.registry.ChickensRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "com.setycz.chickens.ChickensRegistry", modid = "chickens")
public class RegisterEggsUtil 
{

	public static HashMap<String, Integer> ENTITYIDTORGB = new HashMap<String, Integer>();
	
	static Class renderClass;
	
	@Optional.Method(modid = "chickens")
	public static int getChickensModColor(String type)
	{
		if(ChickensRegistry.getByRegistryName(type) == null) return 0xdfce9b;
		if(ChickensRegistry.getByRegistryName(type).isDye())
		{
			return EnumDyeColor.byDyeDamage(ChickensRegistry.getByRegistryName(type).getDyeMetadata()).getMapColor().colorValue;
		}
		else return ChickensRegistry.getByRegistryName(type).getBgColor();
	}
	
	public static int getEggColor(NBTTagCompound entitytag, String entityID)
	{
		String post = "";
		if(entitytag.hasKey("Type"))
		{
			post = entitytag.getTag("Type").toString();
		}
		
		if(Loader.isModLoaded("chickens") && entitytag.hasKey("Type"))
		{
			return getChickensModColor(entitytag.getString("Type"));
		}
		else if(ENTITYIDTORGB.containsKey(entityID)) 
		{
			return ENTITYIDTORGB.get(entityID+post);
		}
		else return 0xdfce9b;

	}
	
		
	public static void getRGBfromEntityTexture(Entity entity)
	{
		Render<Entity> renderer = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
		
		renderClass = renderer.getClass();
		Class noparams[] = {};
		Method getResourceTexture;
		
		ResourceLocation textureResourceLocation = null;
		
		try
		{
			getResourceTexture = renderClass.getMethod("getEntityTexture", entity.getClass());
			
			
			textureResourceLocation = (ResourceLocation) getResourceTexture.invoke(renderClass, entity);
			
		}catch(Throwable e)
		{
//			System.out.println("Error");
		}
		
		
		if(textureResourceLocation != null)
		{
			ITextureObject texture = renderer.getRenderManager().renderEngine.getTexture(textureResourceLocation);
		}
		
	}
	
	
	public void GetPixelColor() throws IOException
	{
		File file= new File("your_file.jpg");
		BufferedImage image = ImageIO.read(file);
	  // 	Getting pixel color by position x and y 
		int clr=  image.getRGB(14,14); 
		int  red   = (clr & 0x00ff0000) >> 16;
		int  green = (clr & 0x0000ff00) >> 8;
		int  blue  =  clr & 0x000000ff;
//		System.out.println("Red Color value = "+ red);
//		System.out.println("Green Color value = "+ green);
//		System.out.println("Blue Color value = "+ blue);
	}
	
	
	public static void register()
	{
//		for(Class<? extends Entity> entity : EntityList.CLASS_TO_NAME.keySet())
//		{
//			
//			if(EntityChicken.class.isAssignableFrom(entity))
//			{
//				System.out.println(">>>>>>>>>>>>>Chicken:"+ entity.getName());
//			}
//			else System.out.println(entity.getName() +"<<<<<<<<<<<<<<<NOT");
//		}
	}

}
