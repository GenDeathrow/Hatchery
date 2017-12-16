package com.gendeathrow.hatchery.core.init;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.client.IItemColorHandler;
import com.gendeathrow.hatchery.item.AnimalNet;
import com.gendeathrow.hatchery.item.ChickenManure;
import com.gendeathrow.hatchery.item.HatcheryEgg;
import com.gendeathrow.hatchery.item.ItemChickenMachine;
import com.gendeathrow.hatchery.item.PrizeEgg;
import com.gendeathrow.hatchery.item.Sprayer;
import com.gendeathrow.hatchery.item.upgrades.BaseUpgrade;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ModItems 
{
	
	public static final List<Item> ITEMS = new ArrayList<>();
	
	//ITEMS
	public static Item hatcheryEgg = setUpItem(new HatcheryEgg(), "hatcheryegg");
			
	public static Item animalNet = setUpItem(new AnimalNet() , "animalnet");
	public static Item manure = setUpItem(new ChickenManure() , "chickenmanure"); 
	public static Item sprayer = setUpItem(new Sprayer(), "sprayer");
	
	public static Item featherMeal = setUpItem(new Item(), "feather_meal");
	public static Item plastic = setUpItem( new Item() , "plastic");
	public static Item featherFiber = setUpItem( new Item(), "feather_fiber");
	public static Item chickenFeed = setUpItem( new Item(), "chicken_feed");
	
	public static Item circuitBoard = setUpItem( new Item(), "circuit_board");
	
	public static Item fiberPad = setUpItem(new Item(),"fiber_pad");
	
	public static Item mealPulp = setUpItem(new Item(), "feather_pulp");
	
	public static Item prizeEgg = setUpItem( new PrizeEgg(), "prize_egg");
	
	public static Item chickenmachine = setUpItem(new ItemChickenMachine(), "chicken_machine");
	
	//Upgrades  
	
	public static Item rfUpgradeTier = setUpItem(new BaseUpgrade(3, "rf_upgrade"), "rf_upgrade");
	public static Item speedUpgradeTier = setUpItem(new BaseUpgrade(3, "speed_upgrade"), "speed_upgrade");
	public static Item tankUpgradeTier1 = setUpItem(new BaseUpgrade(3, "tank_upgrade"), "tank_upgrade");
	public static Item rfCapacityUpgradeTier1 = setUpItem(new BaseUpgrade(3, "rf_capacity_upgrade"), "rf_capacity_upgrade");
	
	// insulation (decrease sound)
	
	
	// Feeder upgrade (auto feeds chickens much like a feeder) requires seeds to be added.  
	
	
	//public static FluidPump pump = new FluidPump();
	public static IForgeRegistry<Item> itemRegistry;
	
	
	public static Item setUpItem(Item item, String name) {
		return item.setRegistryName(new ResourceLocation(Hatchery.MODID, name)).setUnlocalizedName(Hatchery.MODID+"."+ name).setCreativeTab(Hatchery.hatcheryTabs);
	}
	
	@SubscribeEvent
	public static void itemRegistry(RegistryEvent.Register<Item> event) {
		itemRegistry = event.getRegistry();

		registerAllItems(
				ModItems.hatcheryEgg,
				ModItems.animalNet, 
				ModItems.manure,
				ModItems.sprayer,
				ModItems.featherMeal,
				ModItems.plastic,
				ModItems.featherFiber,
				ModItems.chickenFeed,
				ModItems.circuitBoard,
				ModItems.fiberPad,
				ModItems.mealPulp,
				ModItems.prizeEgg,
				ModItems.chickenmachine,
				ModItems.rfUpgradeTier,
				ModItems.speedUpgradeTier,
				ModItems.tankUpgradeTier1,
				ModItems.rfCapacityUpgradeTier1);
		
		ModBlocks.registerItems(event);
	}
	
	public static void registerAllItems(Item... items){
		for(Item item : items) {
			itemRegistry.register(item);
			ModItems.ITEMS.add(item);
		}
	}
	

	public static void registerRenderer() {
		//Register Items
		try {
			for (Field field : ModItems.class.getDeclaredFields()) {
				Object obj = field.get(null);
				if (obj instanceof Item) {
					Item item = (Item) obj;
					
					NonNullList<ItemStack> list = NonNullList.<ItemStack>create();
			
					item.getSubItems(item, Hatchery.hatcheryTabs, list);
			
					if(list.size() > 1)
						for(ItemStack metaitem : list)
							ModelLoader.setCustomModelResourceLocation(item, metaitem.getMetadata(), new ModelResourceLocation(item.getRegistryName()+"_"+metaitem.getMetadata(), "inventory"));
					else
						ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
					
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}


	}

	@SideOnly(Side.CLIENT)
    public static void registerItemColorHandler(Item item)
    {
            FMLClientHandler.instance().getClient().getItemColors().registerItemColorHandler(new IItemColorHandler(), ModItems.hatcheryEgg);
    }


}
