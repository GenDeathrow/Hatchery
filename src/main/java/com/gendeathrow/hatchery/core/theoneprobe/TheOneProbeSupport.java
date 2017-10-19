package com.gendeathrow.hatchery.core.theoneprobe;

import com.gendeathrow.hatchery.Hatchery;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TheOneProbeSupport
{


    private static boolean registered;
    
    public static void register() {
        if (registered) {
            return;
        }
        registered = true;
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", "com.gendeathrow.hatchery.core.theoneprobe.TheOneProbeSupport$GetTheOneProbe");
      }

    
    public static class GetTheOneProbe implements com.google.common.base.Function<ITheOneProbe, Void> 
    {
        public static ITheOneProbe probe;
    	
    	@Override
    	public Void apply(ITheOneProbe theOneProbe)
    	{
    		probe = theOneProbe;
    		Hatchery.logger.info("Enabled support for The One Probe");
        
    		probe.registerProvider(new IProbeInfoProvider() {
    			@Override
    			public String getID() 
    			{
    				return "hatchery:default";
    			}

    			@Override
    			public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo,	EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
    			{ 
    				if (blockState.getBlock() instanceof TOPInfoProvider) 
    				{
    					TOPInfoProvider provider = (TOPInfoProvider) blockState.getBlock();
    					provider.addProbeInfo(mode, probeInfo, player, world, blockState, data);
    				}
    			}
    		});

    		return null;
    	}
    }
}