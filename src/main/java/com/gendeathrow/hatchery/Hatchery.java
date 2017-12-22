package com.gendeathrow.hatchery;

import java.io.IOException;

import com.gendeathrow.hatchery.common.capability.CapabilityAnimalStatsHandler;
import com.gendeathrow.hatchery.core.config.ConfigHandler;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;
import com.gendeathrow.hatchery.core.theoneprobe.TheOneProbeSupport;
import com.gendeathrow.hatchery.network.HatcheryPacket;
import com.gendeathrow.hatchery.network.HatcheryWindowPacket;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Hatchery.MODID, name=Hatchery.NAME, version = Hatchery.VERSION, dependencies=Hatchery.dependencies, guiFactory = "com.gendeathrow.hatchery.client.config.ConfigGuiFactory")
public class Hatchery 
{
 
		public static final String MODID = "hatchery";
	    public static final String VERSION = "@VERSION@";
	    public static final String NAME = "Hatchery";
	    private static final String PROXYLOC = "com.gendeathrow.hatchery.core.proxies";
	    public static final String CHANNELNAME = "genhatchery";
	    public static final String dependencies =  "after:chickens@[5,)";
		   
	    @Instance(MODID)
		public static Hatchery INSTANCE;
	    
		@SidedProxy(clientSide = PROXYLOC + ".ClientProxy", serverSide = PROXYLOC + ".CommonProxy")
		public static CommonProxy PROXY;
		public static SimpleNetworkWrapper network;
		public static FMLEventChannel channel;
	    public static org.apache.logging.log4j.Logger logger;
		
	    public static CreativeTabs hatcheryTabs = new CreativeTabs(MODID) {
	        @Override public ItemStack getTabIconItem() {
	            return new ItemStack(ModItems.hatcheryEgg);
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
	    	
			PROXY.registerRenderers();
	    	PROXY.preInit(event);
	    }
	    
	    @EventHandler
	    public void init(FMLInitializationEvent event) throws IOException
	    {
	    	
	    	PROXY.init(event);
	    	ConfigHandler.loadConfig();
	    	
	    	// waila integration
	        FMLInterModComms.sendMessage("Waila", "register", "com.gendeathrow.hatchery.core.waila.HatcheryTileProvider.load");
	        
	        if (Loader.isModLoaded("theoneprobe")){
	        	TheOneProbeSupport.register();
	        }
	    	
	    	PROXY.registerEventHandlers();
	     }
	    
	    @EventHandler
	    public void postInit(FMLPostInitializationEvent event)
	    {
	    	PROXY.postInit(event);
	    }
	    
		@EventHandler
		public void serverStart(FMLServerStartingEvent event)
		{

		}
	}
