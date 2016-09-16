package com.gendeathrow.hatchery.core.waila;

import java.util.List;

import com.gendeathrow.hatchery.block.nestblock.HatcheryTileEntity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class HatcheryTileProvider implements IWailaDataProvider
{

	private static final HatcheryTileProvider INSTANCE = new HatcheryTileProvider();
	

	
    public static void load(IWailaRegistrar registrar) 
    {
        //registrar.registerBodyProvider(INSTANCE, HatcheryTileEntity.class);
        registrar.registerTailProvider(INSTANCE, HatcheryTileEntity.class);
        registrar.registerNBTProvider(INSTANCE, HatcheryTileEntity.class);
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
		


		if(tileEntity instanceof HatcheryTileEntity)
		{
			HatcheryTileEntity hte = (HatcheryTileEntity) tileEntity;
			
			
			
		
			if(accessor.getNBTData().getBoolean("hasEgg"))
			{
				float percentage = accessor.getNBTData().getFloat("hatchPercentage");
				currenttip.add("Hatching: "+ percentage +"%");
				currenttip.add(accessor.getNBTData().getString("eggName"));
			}
			else currenttip.add("Not Hatching");
		}
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) 
	{
		
		if(te instanceof HatcheryTileEntity)
		{
			HatcheryTileEntity hte = (HatcheryTileEntity) te;
			
			tag.setFloat("hatchPercentage", hte.getPercentage());
			tag.setString("eggName", hte.getStackInSlot(0).getDisplayName());
			tag.setBoolean("hasEgg", hte.getStackInSlot(0) != null);
		}
		return tag;
	}



}
