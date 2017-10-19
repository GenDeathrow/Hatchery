package com.gendeathrow.hatchery;

import java.io.IOException;

import com.gendeathrow.hatchery.common.capability.CapabilityAnimalStatsHandler;
import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.core.config.ConfigHandler;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;
import com.gendeathrow.hatchery.core.theoneprobe.TheOneProbeSupport;
import com.gendeathrow.hatchery.entities.EntityRooster;
import com.gendeathrow.hatchery.network.HatcheryPacket;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;



@Mod(modid = Hatchery.MODID, name=Hatchery.NAME, version = Hatchery.VERSION, dependencies=Hatchery.dependencies, guiFactory = "com.gendeathrow.hatchery.client.config.ConfigGuiFactory")
public class Hatchery 
{

		public static final String MODID = "hatchery";
	    public static final String VERSION = "0.3.22";
	    public static final String NAME = "Hatchery";
	    private static final String PROXYLOC = "com.gendeathrow.hatchery.core.proxies";
	    public static final String CHANNELNAME = "genhatchery";
	    public static final String dependencies =  "after:chickens@[4.1,)";
		   
	    @Instance(MODID)
		public static Hatchery INSTANCE;
	    
		@SidedProxy(clientSide = PROXYLOC + ".ClientProxy", serverSide = PROXYLOC + ".CommonProxy")
		public static CommonProxy PROXY;
		public static SimpleNetworkWrapper network;
		public static FMLEventChannel channel;
	    public static org.apache.logging.log4j.Logger logger;
		
	    static int startEntityId = 1;
		
	    public static CreativeTabs hatcheryTabs = new CreativeTabs(MODID)
	    {
	        @Override public Item getTabIconItem() 
	        {
	            return ModItems.hatcheryEgg;
	        }
	 
	    };
	    
	    static{
	    	FluidRegistry.enableUniversalBucket();
	    }
	    
	    
	    @EventHandler
	    public void preInit(FMLPreInitializationEvent event)
	    {
	    	logger = event.getModLog();
			ConfigHandler.INSTANCE.loadConfig(event);
			
	    	CapabilityAnimalStatsHandler.register();

	    	Hatchery.network = NetworkRegistry.INSTANCE.newSimpleChannel(Hatchery.CHANNELNAME);
	    	network.registerMessage(HatcheryPacket.ServerHandler.class, HatcheryPacket.class, 0, Side.SERVER);
	    	network.registerMessage(HatcheryPacket.ClientHandler.class, HatcheryPacket.class, 1, Side.CLIENT);
	    	
	    	network.registerMessage(HatcheryWindowPacket.ClientHandler.class, HatcheryWindowPacket.class, 2, Side.CLIENT);
	    	NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, PROXY);
	    	
			EntityRegistry.registerModEntity(EntityRooster.class, "Rooster", 1, this, 120, 1, true);
			registerEntityEgg(EntityRooster.class, 0x592C00, 0xC10000);
			PROXY.registerRenderers();
			
			for (Biome allBiomes : ForgeRegistries.BIOMES.getValues())
				if(!BiomeDictionary.isBiomeOfType(allBiomes, Type.WASTELAND)  && !BiomeDictionary.isBiomeOfType(allBiomes, Type.COLD) && !BiomeDictionary.isBiomeOfType(allBiomes, Type.NETHER)  && !BiomeDictionary.isBiomeOfType(allBiomes, Type.END)  && !BiomeDictionary.isBiomeOfType(allBiomes, Type.WATER) && !BiomeDictionary.isBiomeOfType(allBiomes, Type.SWAMP))
					EntityRegistry.addSpawn(EntityRooster.class, Settings.ROOSTER_SPAWN_PROBABILITY, Settings.ROOSTER_MIN_SPAWN_SIZE, Settings.ROOSTER_MAX_SPAWN_SIZE, EnumCreatureType.CREATURE, allBiomes);
			
	    	PROXY.preInit(event);
	    	
	    }
	    
		public static int getUniqueEntityId() 
		{
			do
				startEntityId++;
			while(EntityList.getClassFromID(startEntityId)!= null);
			return startEntityId;
		}
		
		public static void registerEntityEgg(Class <? extends Entity> entity, int baseColor, int spotColor) 
		{
			int id = getUniqueEntityId();
			EntityList.addMapping(entity, "Rooster", id);
			EntityList.ENTITY_EGGS.put("Rooster", new EntityList.EntityEggInfo("Rooster", baseColor, spotColor));
		}
		
	    @EventHandler
	    public void init(FMLInitializationEvent event) throws IOException
	    {
	    	PROXY.init(event);
	    	
	    	ConfigHandler.loadConfig();
	    	
	    	// waila integration
	        FMLInterModComms.sendMessage("Waila", "register", "com.gendeathrow.hatchery.core.waila.HatcheryTileProvider.load");
	        
	        if (Loader.isModLoaded("theoneprobe")) 
	        {
	        	TheOneProbeSupport.register();
	        }
	    	
	    	PROXY.registerEventHandlers();
	    	PROXY.initRenderers();
	     }
	    
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent event)
	    {
	    	PROXY.postInit(event);
	    	
	    	//RegisterEggsUtil.register();
	    }
	    
		@EventHandler
		public void serverStart(FMLServerStartingEvent event)
		{

		}
	}
