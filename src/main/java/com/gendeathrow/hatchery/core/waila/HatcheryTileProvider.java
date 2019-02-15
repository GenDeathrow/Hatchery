package com.gendeathrow.hatchery.core.waila;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.gendeathrow.hatchery.block.feeder.FeederTileEntity;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.modaddons.ChickensHelper;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

@WailaPlugin
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
			else currenttip.add(TextFormatting.RED + I18n.format("text.hatchery.nothatching", new Object[0]));
		}
		else if(tileEntity instanceof NestPenTileEntity)
		{
			if(accessor.getNBTData().getBoolean("hasChicken"))
			{
				currenttip.add(TextFormatting.LIGHT_PURPLE + I18n.format("text.hatchery.chicken", new Object[0])+ TextFormatting.RESET +": " + TextFormatting.ITALIC +""+ TextFormatting.GREEN + accessor.getNBTData().getString("entityname"));

				if(accessor.getNBTData().hasKey("stats")) {

					NBTTagList statList = accessor.getNBTData().getTagList("stats",10);
					
			        for (int i = 0; i < statList.tagCount(); ++i)
			        {
			            	NBTTagCompound statTag = statList.getCompoundTagAt(i);
			            	String unformatted = statTag.getString("text");
			            	int value = statTag.getInteger("value");
							currenttip.add(I18n.format(unformatted, new Object[] {value}));
			        }
				}
	    		
				long uptime = accessor.getNBTData().getLong("nextDrop")/20;
				long minutes = TimeUnit.SECONDS.toMinutes(uptime);
				  uptime -= TimeUnit.MINUTES.toSeconds(minutes);
				long seconds = TimeUnit.SECONDS.toSeconds(uptime);
				String output = minutes > 0 ? minutes+":"+ (seconds < 10 ? "0"+seconds : seconds)  +" mins" : (seconds < 10 ? "0"+seconds : seconds) + " secs";

				currenttip.add(TextFormatting.GREEN + I18n.format("text.hatchery.nxdrop", new Object[0]) +": "+ output);
			}
			else currenttip.add(TextFormatting.RED + I18n.format("text.hatchery.nochicken", new Object[0]));
			
			if(accessor.getNBTData().hasKey("inventory"))
			{
				NBTTagList inv = (NBTTagList) accessor.getNBTData().getTag("inventory");
				
				currenttip.add("");
				currenttip.add(TextFormatting.GREEN +""+ TextFormatting.UNDERLINE +  I18n.format("text.hatchery.inventory")+ TextFormatting.RESET);
				
				if(inv != null) 
				{
					for(int i=0; i < inv.tagCount(); i++)
					{
						NBTTagCompound item = inv.getCompoundTagAt(i);
						currenttip.add(TextFormatting.GOLD+ "  Slot "+ (item.getByte("Slot")+1)+ TextFormatting.RESET +": "+ item.getString("id") +" x"+ item.getInteger("cnt") );
					}
				}
			}else {
				currenttip.add(TextFormatting.YELLOW + I18n.format("text.hatchery.sneakshow"));
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
			
			if(!hte.getEgg().isEmpty())
			{
				tag.setFloat("hatchPercentage", hte.getPercentage());
				tag.setString("eggName", hte.getEgg().getDisplayName());
			}
			
			tag.setBoolean("hasEgg", !hte.getEgg().isEmpty());

		}
		else if(te instanceof NestPenTileEntity)
		{

			NestPenTileEntity hte = (NestPenTileEntity) te;
			
			tag.setBoolean("hasChicken", hte.storedEntity() != null);
			
			if(hte.storedEntity() != null) 
			{
				tag.setString("entityname",  hte.storedEntity().getDisplayName().getFormattedText());

				if(ChickensHelper.isLoaded()) {
					HashMap<String, Integer> stats = ChickensHelper.getChickenStats(hte.storedEntity());
					
					if(stats != null) {
					  NBTTagList statsTag = new NBTTagList();
					  
					
					  NBTTagCompound growth = new NBTTagCompound();
						growth.setString("text", "entity.ChickensChicken.growth");
						growth.setInteger("value", stats.get("entity.ChickensChicken.growth"));
					
					  NBTTagCompound gain = new NBTTagCompound();
						gain.setString("text", "entity.ChickensChicken.gain");
						gain.setInteger("value", stats.get("entity.ChickensChicken.gain"));
						
				      NBTTagCompound str = new NBTTagCompound();
						str.setString("text", "entity.ChickensChicken.strength");
						str.setInteger("value", stats.get("entity.ChickensChicken.strength"));

						statsTag.appendTag(growth);
						statsTag.appendTag(gain);
						statsTag.appendTag(str);
						
//					  for(Entry<String, Integer> stat : stats.entrySet()) {
//						  NBTTagCompound statNbt = new NBTTagCompound();
//						  statNbt.setString("text", stat.getKey());
//						  statNbt.setInteger("value", stat.getValue());
//						  statsTag.appendTag(statNbt);
//					  }
//					  
					  
					  
				        if (!statsTag.isEmpty())
						  tag.setTag("stats", statsTag);
					}
				}
			}
			
			tag.setLong("nextDrop",  hte.getTimeToNextDrop());

			if(player.isSneaking()) {	
				NBTTagList list = NestPenTileEntity.getInventoryContents(hte);
				if(list != null) tag.setTag("inventory", list);
			}
		}
		else if(te instanceof FeederTileEntity)
		{

			FeederTileEntity hte = (FeederTileEntity) te;
			
			tag.setString("qty", hte.getSeedsInv() +"/"+ hte.getMaxSeedInv());
		}
		
		
		return tag;
	}



}
