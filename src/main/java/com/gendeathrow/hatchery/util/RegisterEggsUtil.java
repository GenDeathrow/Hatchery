package com.gendeathrow.hatchery.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.setycz.chickens.ChickensRegistry;

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
	
	
	// Currently Hardcoding mod addons.
	static
	{
		// Add all Chickens Mod Chickens
		ENTITYIDTORGB.put("chickens.ChickensChicken4", 0xffff00);
		ENTITYIDTORGB.put("chickens.ChickensChicken11", 0x000066);
		ENTITYIDTORGB.put("chickens.ChickensChicken13", 0x006600);
		ENTITYIDTORGB.put("chickens.ChickensChicken14", 0x660000);
		ENTITYIDTORGB.put("chickens.ChickensChicken15", 0x666666);
		ENTITYIDTORGB.put("chickens.ChickensChicken101", 0x6b6b47);
		ENTITYIDTORGB.put("chickens.ChickensChicken104", 0x4d0000);
		ENTITYIDTORGB.put("chickens.ChickensChicken108", 0x98846d);
		ENTITYIDTORGB.put("chickens.ChickensChicken105", 0xece5b1);
		ENTITYIDTORGB.put("chickens.ChickensChicken0", 0xf2f2f2);
		ENTITYIDTORGB.put("chickens.ChickensChicken303", 0x331a00);
		ENTITYIDTORGB.put("chickens.ChickensChicken202", 0xffff66);
		ENTITYIDTORGB.put("chickens.ChickensChicken100", 0x999999);
		ENTITYIDTORGB.put("chickens.ChickensChicken201", 0xe60000);
		ENTITYIDTORGB.put("chickens.ChickensChicken106", 0xffffff);
		ENTITYIDTORGB.put("chickens.ChickensChicken203", 0xffffcc);
		ENTITYIDTORGB.put("chickens.ChickensChicken204", 0x262626);
		ENTITYIDTORGB.put("chickens.ChickensChicken12", 0x663300);
		ENTITYIDTORGB.put("chickens.ChickensChicken300", 0xcccc00);
		ENTITYIDTORGB.put("chickens.ChickensChicken102", 0x33bbff);
		ENTITYIDTORGB.put("chickens.ChickensChicken206", 0x000099);
		ENTITYIDTORGB.put("chickens.ChickensChicken103", 0xcc3300);
		ENTITYIDTORGB.put("chickens.ChickensChicken200", 0xcccccc);
		ENTITYIDTORGB.put("chickens.ChickensChicken107", 0xA7A06C);
		ENTITYIDTORGB.put("chickens.ChickensChicken207", 0x800000);
		ENTITYIDTORGB.put("chickens.ChickensChicken301", 0x99ccff);
		ENTITYIDTORGB.put("chickens.ChickensChicken302", 0xffff66);
		ENTITYIDTORGB.put("chickens.ChickensChicken205", 0x009933);
		ENTITYIDTORGB.put("chickens.ChickensChicken401", 0x001a00);
		ENTITYIDTORGB.put("chickens.ChickensChicken402", 0xffffcc);
		ENTITYIDTORGB.put("chickens.ChickensChicken400", 0x00cc00);
		ENTITYIDTORGB.put("chickens.ChickensChicken403", 0x1a0500);
		
		//MoreChickens #Tinkers
		ENTITYIDTORGB.put("chickens.ChickensChicken80", 0xdc3e00);
		ENTITYIDTORGB.put("chickens.ChickensChicken81", 0x0c5abe);
		ENTITYIDTORGB.put("chickens.ChickensChicken82", 0x652e87);
		ENTITYIDTORGB.put("chickens.ChickensChicken83", 0xe6b8b8);
		ENTITYIDTORGB.put("chickens.ChickensChicken84", 0xc17ced);
		ENTITYIDTORGB.put("chickens.ChickensChicken85", 0xc50616);
		ENTITYIDTORGB.put("chickens.ChickensChicken86", 0xc9aad9);
		ENTITYIDTORGB.put("chickens.ChickensChicken87", 0xecb55f);
		ENTITYIDTORGB.put("chickens.ChickensChicken88", 0x67b4c4);

		//MoreChickens #DraconicEvolution
		ENTITYIDTORGB.put("chickens.ChickensChicken90", 0x301549);
		ENTITYIDTORGB.put("chickens.ChickensChicken91", 0xcc440c);	
		
		//MoreChickens #Botania
		ENTITYIDTORGB.put("chickens.ChickensChicken120", 0x3ff123);
		ENTITYIDTORGB.put("chickens.ChickensChicken121", 0x69d7ff);
		ENTITYIDTORGB.put("chickens.ChickensChicken122", 0xf655f3);
		
		//MoreChickens
		ENTITYIDTORGB.put("chickens.ChickensChicken500", 0x3dff1e);
		ENTITYIDTORGB.put("chickens.ChickensChicken501", 0x43806e);
		ENTITYIDTORGB.put("chickens.ChickensChicken502", 0x4e6961);	
	}
	
	
	@Optional.Method(modid = "chickens")
	public static int getChickensModColor(int type)
	{
		if(ChickensRegistry.getByType(type) == null) return 0xdfce9b;
		if(ChickensRegistry.getByType(type).isDye())
		{
			return EnumDyeColor.byDyeDamage(ChickensRegistry.getByType(type).getDyeMetadata()).getMapColor().colorValue;
		}
		else return ChickensRegistry.getByType(type).getBgColor();
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
			return getChickensModColor(entitytag.getInteger("Type"));
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
