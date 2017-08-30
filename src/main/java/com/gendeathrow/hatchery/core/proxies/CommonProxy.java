package com.gendeathrow.hatchery.core.proxies;

import com.gendeathrow.hatchery.block.eggmachine.ContainerEggMachine;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineTileEntity;
import com.gendeathrow.hatchery.block.eggmachine.GuiEggMachine;
import com.gendeathrow.hatchery.block.fertilizermixer.ContainerFertlizerMixer;
import com.gendeathrow.hatchery.block.fertilizermixer.FertilizerMixerTileEntity;
import com.gendeathrow.hatchery.block.fertilizermixer.GuiFertilizerMixerInventory;
import com.gendeathrow.hatchery.block.generator.ContainerDigesterGenerator;
import com.gendeathrow.hatchery.block.generator.DigesterGeneratorTileEntity;
import com.gendeathrow.hatchery.block.generator.GuiDigesterGenerator;
import com.gendeathrow.hatchery.block.nestpen.ContainerNestingPen;
import com.gendeathrow.hatchery.block.nestpen.GuiNestingPen;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.block.shredder.ContainerShredder;
import com.gendeathrow.hatchery.block.shredder.GuiShredder;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;
import com.gendeathrow.hatchery.core.EventHandler;
import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.core.config.ConfigHandler;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.init.ModRecipes;
import com.gendeathrow.hatchery.entities.ContainerRoosterInventory;
import com.gendeathrow.hatchery.entities.EntityRooster;
import com.gendeathrow.hatchery.entities.GuiRoosterInventory;
import com.gendeathrow.hatchery.item.HatcheryEggThrown;

import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler 
{
	public static final int GUI_ID_ROOSTER = 1;
	public static final int GUI_ID_FERTLIZERMIXER = 2;
	public static final int GUI_ID_DIGESTER_GEN = 3;
	public static final int GUI_ID_NESTINGPEN = 4;
	public static final int GUI_ID_SHREDDER = 5;
	public static int GUI_ID_EGGSTRACTOR = 6;
	
	
	public boolean isClient()
	{
		return false;
	}
	
	public boolean isOpenToLAN()
	{
		return false;
	}
	
	public void registerTickHandlers() {}
	
	
	public void initRenderers() {}
	
	public void registerRenderers() {}
	
	public void registerEventHandlers()
	{
		EventHandler eventhandler = new EventHandler();
		MinecraftForge.EVENT_BUS.register(eventhandler);
		MinecraftForge.EVENT_BUS.register(ConfigHandler.INSTANCE);
	}
	
	public void preInit(FMLPreInitializationEvent event)
	{
		ModBlocks.preInit(event);
		ModItems.RegisterItems();
		ModFluids.registerFluids();	
		
		if(Settings.CAN_THROW_EGG)
			BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.hatcheryEgg, new DispenseHatcheryEgg());
	}
	
	public void init(FMLInitializationEvent event) { }
	
	public void postInit(FMLPostInitializationEvent event)
	{
		ModRecipes.RegisterOreDic();
		ModRecipes.RegisterRecipes();
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) 
	{
		 if (ID == GUI_ID_ROOSTER) 
		 {
				Entity entity = world.getEntityByID(x);
				if (entity != null && entity instanceof EntityRooster)
					return new ContainerRoosterInventory(player.inventory, (EntityRooster) entity);
		 }
		 else if (ID > GUI_ID_ROOSTER) 
		 {
			 
				BlockPos blockpos = new BlockPos(x,y,z);
				IBlockState block = world.getBlockState(blockpos);
				
				if (block != null && block.getBlock().hasTileEntity(block))
				{  
					TileEntity tile = world.getTileEntity(blockpos); 
					if(tile instanceof FertilizerMixerTileEntity)
						return new ContainerFertlizerMixer(player.inventory, (FertilizerMixerTileEntity) tile);
					else if(tile instanceof DigesterGeneratorTileEntity)
						return new ContainerDigesterGenerator(player.inventory, (DigesterGeneratorTileEntity) tile);
					else if(tile instanceof NestPenTileEntity)
						return new ContainerNestingPen(player.inventory, (NestPenTileEntity) tile, player);
					else if(tile instanceof ShredderTileEntity)
						return new ContainerShredder(player.inventory, (ShredderTileEntity) tile, player);
					else if(tile instanceof EggMachineTileEntity)
						return new ContainerEggMachine(player.inventory, (EggMachineTileEntity) tile);
				}
		 }
				
		return null;
	}


	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,	int x, int y, int z) 
	{
		if (ID == GUI_ID_ROOSTER) 
		{
			Entity entity = world.getEntityByID(x);
			if (entity != null && entity instanceof EntityRooster)
				return new GuiRoosterInventory(player.inventory, entity);
		}
		else if (ID > GUI_ID_ROOSTER) 
		{
			BlockPos blockpos = new BlockPos(x,y,z);
			IBlockState block = world.getBlockState(blockpos);
			
			if(block == null) return null;
		
			if (block.getBlock().hasTileEntity(block))
			{
				TileEntity tile = world.getTileEntity(blockpos);
				
				if(tile instanceof FertilizerMixerTileEntity)
					return new GuiFertilizerMixerInventory(player.inventory, (FertilizerMixerTileEntity) tile);
				else if(tile instanceof DigesterGeneratorTileEntity)
					return new GuiDigesterGenerator(player.inventory, (DigesterGeneratorTileEntity) tile);
				else if(tile instanceof NestPenTileEntity)
					return new GuiNestingPen(player.inventory, (NestPenTileEntity) tile);
				else if(tile instanceof ShredderTileEntity)
					return new GuiShredder(player.inventory, (ShredderTileEntity) tile);
				else if(tile instanceof EggMachineTileEntity)
					return new GuiEggMachine(player.inventory, (EggMachineTileEntity) tile);
			}
				    
		}
		return null;
	}
	
	
    class DispenseHatcheryEgg extends BehaviorProjectileDispense 
    {
        @Override
        protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) 
        {
        	HatcheryEggThrown entityColoredEgg = new HatcheryEggThrown(worldIn, stackIn, position.getX(), position.getY(), position.getZ());
            return entityColoredEgg;
        }
    }

}
