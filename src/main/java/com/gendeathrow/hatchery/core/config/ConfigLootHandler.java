package com.gendeathrow.hatchery.core.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import com.gendeathrow.hatchery.util.JsonConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandom;

public class ConfigLootHandler {

	
	public static final File LuckyFile = new File(ConfigHandler.dir, "lucky_egg.json");
	public static JsonConfig config = new JsonConfig(LuckyFile);
	public static List<ItemDrop> drops = new ArrayList<ItemDrop>();
	
	public static ArrayList<ItemDrop> defaultList = new ArrayList<ItemDrop>();
	
	
	public static void load() {

		boolean shouldCreate = false;
		
		if(!LuckyFile.exists())
			shouldCreate = true;
		
		config.Load();
		
		if(shouldCreate)
			createDefault(config);
		
		
			JsonObject array = config.getFullJson();
			

			if(array != null)
				drops = ItemDrop.getArrayItemDrops(array);
			
		if(config.hasChanged())
			config.Save();
	}
	  
	
	private static void createDefault(JsonConfig config2){
		
		JsonObject newobj = new JsonObject();
		
		for(ItemDrop item : defaultList)
		{
			newobj.add(item.itemID +":"+ item.metaID, item.toJsonObj());
		}
		
		config.setFullJson(newobj);
	}
	

	public static class ItemDrop extends WeightedRandom.Item
	{
		private String itemID;
		private int metaID;
		private int weight;
		private int minQty, maxQty;
		private NBTTagCompound tag;
		
		private static final Random rand = new Random();
		
		
		public ItemDrop(ItemStack stack, int weight, int minqty, int maxqty)
		{
			this(stack.getItem().getRegistryName().toString(), stack.getMetadata(), weight, minqty, maxqty, (stack.hasTagCompound() ? stack.getTagCompound() : null));
		}
		
		
		public ItemDrop(String itemID, int metaID, int weight, int minQty, int maxQty, NBTTagCompound tag)
		{
			super(weight);
			this.itemID = itemID;
			this.metaID = metaID;
			this.weight = weight;
			this.minQty = minQty;
			this.maxQty = maxQty;
			this.tag = tag;
		}
		
		public static ArrayList<ItemDrop> getArrayItemDrops(JsonObject itemList)
		{
			ArrayList<ItemDrop> drops = new ArrayList<ItemDrop>();
			for(Entry<String, JsonElement> item : itemList.entrySet())
			{
				try
				{
					drops.add(getItemDrop((JsonObject) item.getValue()));
				}
				catch(NumberFormatException e)
				{
					e.printStackTrace();
				}
			}
			return drops;
		}
		
		public ItemStack getItemStack()
		{
			
			Item item = Item.getByNameOrId(this.itemID);
			if(item == null) return null;
			
			
			ItemStack stack = new ItemStack(item, rand.nextInt(this.maxQty - this.minQty + 1) + this.minQty, this.metaID);
			
			if(this.tag != null)
				stack.setTagCompound(this.tag);
			
			return stack;
		}
		
		public Item getItem()
		{
			return Item.getByNameOrId(this.itemID);
		}
		
		public int getMeta()
		{
			return this.metaID;
		}
		
		
		public static ItemDrop getItemDrop(JsonObject dataIn) throws NumberFormatException
		{
			
			String[] split = dataIn.get("item").getAsString().split(":");
			
			String itemID = split[0]+":"+split[1];
			int metaID = 0;
			int minqty = 1;
			int maxqty = 1;
			int weight = 10;
			NBTTagCompound tag = null;
			
			if(split.length == 3){
				metaID = Integer.parseInt(split[2]);
			}
			
			
			if(dataIn.has("weight"))
				weight = dataIn.get("weight").getAsInt();
			
			if(dataIn.has("nbt")) {
				try {
					tag = JsonToNBT.getTagFromJson(dataIn.get("nbt").getAsString());
				} catch (NBTException e) {
					e.printStackTrace();
				}
			}
			
			if(dataIn.has("qty"))
			{
				minqty = maxqty = dataIn.get("qty").getAsInt();
			}
			else 
			{
				if(dataIn.has("minQty"))
					minqty = dataIn.get("minQty").getAsInt();

				if(dataIn.has("maxQty"))
					maxqty = dataIn.get("maxQty").getAsInt();
			}

			
			return new ItemDrop(itemID, metaID, weight, minqty, maxqty, tag);
		}
		
		public JsonObject toJsonObj()
		{
			JsonObject obj = new JsonObject();
			
			if(this.itemID != null)
			{
				String stringitem = this.itemID + (this.metaID != 0 ? ":"+ this.metaID : "");
				
				obj.addProperty("item", stringitem);
			}
			
			if(minQty == maxQty){
				obj.addProperty("qty", this.minQty);
			}
			else {
				obj.addProperty("minQty", this.minQty);
				obj.addProperty("maxQty", this.maxQty);
			}
			
			obj.addProperty("weight", this.weight);
			
			if(this.tag != null){
				obj.addProperty("nbt", this.tag.toString());
			}
			return obj; 
		}

		
	}
	
	
	static {
		defaultList.add(new ItemDrop(new ItemStack(Items.APPLE), 10,1,2));
		defaultList.add(new ItemDrop(new ItemStack(Items.BONE), 13,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.BOOK), 14,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.BREAD), 10,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.CAKE), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.CARROT), 12,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.COAL), 15,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.FISHING_ROD), 10,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.FLINT), 12,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.FLOWER_POT), 10,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.GUNPOWDER), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.STRING), 12,1,2));
		defaultList.add(new ItemDrop(new ItemStack(Items.STICK), 12,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.LEAD), 6,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.BOWL), 10,1,1));
		
		for(EnumDyeColor color : EnumDyeColor.values())
			defaultList.add(new ItemDrop(new ItemStack(Items.DYE,1,color.getMetadata()), 10,1,2));
		
		defaultList.add(new ItemDrop(new ItemStack(Items.FIRE_CHARGE), 10,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.SUGAR), 10,1,2));
		defaultList.add(new ItemDrop(new ItemStack(Items.ARROW), 10,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.CLAY_BALL), 10,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.GLASS_BOTTLE), 10,1,3));
		defaultList.add(new ItemDrop(new ItemStack(Items.FIREWORKS), 10,1,2));
		defaultList.add(new ItemDrop(new ItemStack(Items.RABBIT_FOOT), 4,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.PAPER), 15,1,2));
		defaultList.add(new ItemDrop(new ItemStack(Items.SKULL), 15,1,1));

		
		defaultList.add(new ItemDrop(new ItemStack(Items.RECORD_11), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.RECORD_13), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.RECORD_CAT), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.RECORD_STRAD), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.RECORD_WARD), 8,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.RECORD_FAR), 8,1,1));
		
		
		defaultList.add(new ItemDrop(new ItemStack(Items.NAME_TAG), 5,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.DIAMOND), 1,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.EMERALD), 1,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.ENDER_PEARL), 2,1,1));
		defaultList.add(new ItemDrop(new ItemStack(Items.GOLDEN_APPLE), 2,1,1));

	}
}
