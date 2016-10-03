package com.gendeathrow.hatchery.core.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.gendeathrow.hatchery.block.feeder.FeederTileEntity;
import com.gendeathrow.hatchery.block.nestblock.NestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;

public class HatcheryTileProvider implements IWailaDataProvider
{

	private static final HatcheryTileProvider INSTANCE = new HatcheryTileProvider();
	

	
    public static void load(IWailaRegistrar registrar) 
    {
        //registrar.registerBodyProvider(INSTANCE, HatcheryTileEntity.class);
        registrar.registerTailProvider(INSTANCE, NestTileEntity.class);
        registrar.registerNBTProvider(INSTANCE, NestTileEntity.class);
        
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
		


		if(tileEntity instanceof NestTileEntity)
		{
			NestTileEntity hte = (NestTileEntity) tileEntity;
			
			if(accessor.getNBTData().getBoolean("hasEgg"))
			{
				float percentage = accessor.getNBTData().getFloat("hatchPercentage");
				currenttip.add(I18n.format("text.hatching", new Object[0]) +": "+ percentage +"%");
				currenttip.add(accessor.getNBTData().getString("eggName"));
			}
			else currenttip.add(I18n.format("text.nothatching", new Object[0]));
		}
		else if(tileEntity instanceof NestPenTileEntity)
		{
			if(accessor.getNBTData().getBoolean("hasChicken"))
			{
				currenttip.add(I18n.format("text.chicken", new Object[0]) +": "+ accessor.getNBTData().getString("entityname"));
				currenttip.add(I18n.format("text.nxdrop", new Object[0]) +": "+ accessor.getNBTData().getLong("nextDrop"));
			}
			else currenttip.add(I18n.format("text.nochicken", new Object[0]));
			
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
				currenttip.add(I18n.format("text.storedseed", new Object[0]) +": "+ accessor.getNBTData().getString("qty"));
			}
		}
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) 
	{
		
		if(te instanceof NestTileEntity)
		{
			NestTileEntity hte = (NestTileEntity) te;
			
			tag.setFloat("hatchPercentage", hte.getPercentage());
			tag.setString("eggName", hte.getStackInSlot(0).getDisplayName());
			tag.setBoolean("hasEgg", hte.getStackInSlot(0) != null);
		}
		else if(te instanceof NestPenTileEntity)
		{
			NestPenTileEntity hte = (NestPenTileEntity) te;
			
			tag.setBoolean("hasChicken", ((NestPenTileEntity) te).storedEntity() != null);
			if(((NestPenTileEntity) te).storedEntity() != null) tag.setString("entityname",  ((NestPenTileEntity) te).storedEntity().getDisplayName().getFormattedText());
			tag.setLong("nextDrop",  ((NestPenTileEntity) te).getTimeToNextDrop());
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
