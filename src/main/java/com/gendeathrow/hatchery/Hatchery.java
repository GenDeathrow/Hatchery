package com.gendeathrow.hatchery;

import java.io.IOException;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.FluidRegistry;
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
import net.minecraftforge.fml.relauncher.Side;

import com.gendeathrow.hatchery.common.capability.CapabilityAnimalStatsHandler;
import com.gendeathrow.hatchery.core.config.ConfigHandler;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;
import com.gendeathrow.hatchery.network.HatcheryPacket;



@Mod(modid = Hatchery.MODID, name=Hatchery.NAME, version = Hatchery.VERSION, dependencies=Hatchery.dependencies)
public class Hatchery {

		public static final String MODID = "hatchery";
	    public static final String VERSION = "0.2.0";
	    public static final String NAME = "Hatchery";
	    public static final String PROXY = "com.gendeathrow.hatchery.core.proxies";
	    public static final String CHANNELNAME = "genhatchery";
	    public static final String dependencies =  "after:chickens@[4.1,)";
		   
	    @Instance(MODID)
		public static Hatchery instance;
	    
		@SidedProxy(clientSide = PROXY + ".ClientProxy", serverSide = PROXY + ".CommonProxy")
		public static CommonProxy proxy;

		public static SimpleNetworkWrapper network;
		 
		public static FMLEventChannel channel;
		
	    public static org.apache.logging.log4j.Logger logger;
	    
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
	    	Hatchery.network = NetworkRegistry.INSTANCE.newSimpleChannel(Hatchery.CHANNELNAME);
	    	
	    	CapabilityAnimalStatsHandler.register();

	    	network.registerMessage(HatcheryPacket.ServerHandler.class, HatcheryPacket.class, 0, Side.SERVER);
	    	network.registerMessage(HatcheryPacket.ClientHandler.class, HatcheryPacket.class, 1, Side.CLIENT);
	    	
	    	proxy.preInit(event);
	    }
		
	    @EventHandler
	    public void init(FMLInitializationEvent event) throws IOException
	    {
	    	proxy.init(event);
	    	
	    	ConfigHandler.loadConfig();
	    	
	    	// waila integration
	        FMLInterModComms.sendMessage("Waila", "register", "com.gendeathrow.hatchery.core.waila.HatcheryTileProvider.load");
	    	
	    	proxy.registerEventHandlers();
	    	proxy.initRenderers();
	     }
	    
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent event)
	    {
	    	proxy.postInit(event);
	    	
	    	//RegisterEggsUtil.register();
	    }
	    
		@EventHandler
		public void serverStart(FMLServerStartingEvent event)
		{

		}
	}
