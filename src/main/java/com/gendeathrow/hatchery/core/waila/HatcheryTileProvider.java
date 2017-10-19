package com.gendeathrow.hatchery.core.waila;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.gendeathrow.hatchery.block.feeder.FeederTileEntity;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HatcheryTileProvider implements IWailaDataProvider, IWailaPlugin
{

	private static final HatcheryTileProvider INSTANCE = new HatcheryTileProvider();
	


	@Override
	public void register(IWailaRegistrar registrar) 
	{
		load(registrar);
	}

	
    public static void load(IWailaRegistrar registrar) 
    {
        registrar.registerTailProvider(INSTANCE, EggNestTileEntity.class);
        registrar.registerNBTProvider(INSTANCE, EggNestTileEntity.class);
        
        registrar.registerTailProvider(INSTANCE, NestPenTileEntity.class);
        registrar.registerNBTProvider(INSTANCE, NestPenTileEntity.class);
        
        registrar.registerTailProvider(INSTANCE, FeederTileEntity.class);
        registrar.registerNBTProvider(INSTANCE, FeederTileEntity.class);
    }
    
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor,	IWailaConfigHandler config) 
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		return null;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{

		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		TileEntity tileEntity = accessor.getTileEntity();
		


		if(tileEntity instanceof EggNestTileEntity)
		{
			EggNestTileEntity hte = (EggNestTileEntity) tileEntity;
			
			if(accessor.getNBTData().getBoolean("hasEgg"))
			{
				float percentage = accessor.getNBTData().getFloat("hatchPercentage");
				currenttip.add(I18n.format("text.hatchery.hatching", new Object[0]) +": "+ percentage +"%");
				currenttip.add(accessor.getNBTData().getString("eggName"));
			}
			else currenttip.add(I18n.format("text.hatchery.nothatching", new Object[0]));
		}
		else if(tileEntity instanceof NestPenTileEntity)
		{

			if(accessor.getNBTData().getBoolean("hasChicken"))
			{
				currenttip.add(I18n.format("text.hatchery.chicken", new Object[0]) +": "+ accessor.getNBTData().getString("entityname"));

	    		if(accessor.getNBTData().hasKey("Growth"))
	    		{
	    			currenttip.add("Growth: "+ accessor.getNBTData().getInteger("Growth"));
	    		}
	    		if(accessor.getNBTData().hasKey("Gain"))
	    		{
	    			currenttip.add("Gain: "+ accessor.getNBTData().getInteger("Gain"));
	    		}
	    		if(accessor.getNBTData().hasKey("Strength"))
	    		{
	    			currenttip.add("Strength: "+ accessor.getNBTData().getInteger("Strength"));
	    		}

	    		
	    		
				long uptime = accessor.getNBTData().getLong("nextDrop")/20;
				long minutes = TimeUnit.SECONDS.toMinutes(uptime);
				  uptime -= TimeUnit.MINUTES.toSeconds(minutes);
				long seconds = TimeUnit.SECONDS.toSeconds(uptime);
				String output = minutes > 0 ? minutes+":"+ (seconds < 10 ? "0"+seconds : seconds)  +" mins" : (seconds < 10 ? "0"+seconds : seconds) + " secs";

				currenttip.add(I18n.format("text.hatchery.nxdrop", new Object[0]) +": "+ output);
			}
			else currenttip.add(I18n.format("text.hatchery.nochicken", new Object[0]));
			
			if(accessor.getNBTData().hasKey("inventory"))
			{
				NBTTagList inv = (NBTTagList) accessor.getNBTData().getTag("inventory");
				if(inv != null) 
				{
					for(int i=0; i < inv.tagCount(); i++)
					{
						NBTTagCompound item = inv.getCompoundTagAt(i);
						currenttip.add("Slot "+ item.getByte("Slot") +": "+ item.getString("id") +" x"+ item.getInteger("cnt") );
					}
				}
			}
		}	
		else if(tileEntity instanceof FeederTileEntity)
		{
			if(accessor.getNBTData().hasKey("qty"))
			{
				accessor.getNBTData().getString("qty");
				currenttip.add(I18n.format("text.hatchery.storedseed", new Object[0]) +": "+ accessor.getNBTData().getString("qty"));
			}
		}
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) 
	{
		
		if(te instanceof EggNestTileEntity)
		{
			EggNestTileEntity hte = (EggNestTileEntity) te;
			
			float hatchPercentage = 0;
			String eggName = "";
			float hasEgg = 0;
			
			if(hte.getStackInSlot(0) != null)
			{
				tag.setFloat("hatchPercentage", hte.getPercentage());
				tag.setString("eggName", hte.getStackInSlot(0).getDisplayName());
			}
			
			tag.setBoolean("hasEgg", hte.getStackInSlot(0) != null);

		}
		else if(te instanceof NestPenTileEntity)
		{

			NestPenTileEntity hte = (NestPenTileEntity) te;
			
			tag.setBoolean("hasChicken", hte.storedEntity() != null);
			
			if(hte.storedEntity() != null) 
			{
				tag.setString("entityname",  hte.storedEntity().getDisplayName().getFormattedText());

	    		NBTTagCompound entityNBT = hte.storedEntity().getEntityData();

	    		if(entityNBT.hasKey("Gain"))
	    		{
	    			tag.setInteger("Gain", entityNBT.getInteger("Gain"));
	    		}
	    		if(entityNBT.hasKey("Strength"))
	    		{
	    			tag.setInteger("Strength", entityNBT.getInteger("Strength"));
	    		}
	    		if(entityNBT.hasKey("Growth"))
	    		{
	    			tag.setInteger("Growth", entityNBT.getInteger("Growth"));
	    		}
	    		
			}
			
			tag.setLong("nextDrop",  hte.getTimeToNextDrop());
			NBTTagList list = hte.getInventoryContents(hte);
			if(list != null) tag.setTag("inventory", list);
		}
		else if(te instanceof FeederTileEntity)
		{

			FeederTileEntity hte = (FeederTileEntity) te;
			
			tag.setString("qty", hte.getSeedsInv() +"/"+ hte.getMaxSeedInv());
		}
		
		
		return tag;
	}



}
