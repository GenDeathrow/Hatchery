package com.gendeathrow.hatchery.core.proxies;


import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineEntityRenderer;
import com.gendeathrow.hatchery.block.eggmachine.EggMachineTileEntity;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntity;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntityRender;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.block.nestpen.NestingPenTileEntityRenderer;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntityRenderer;
import com.gendeathrow.hatchery.client.render.entity.RenderRooster;
import com.gendeathrow.hatchery.client.render.entity.RenderThrownEgg.HatcheryEggThrownFactory;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.entities.EntityRooster;
import com.gendeathrow.hatchery.item.HatcheryEggThrown;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy
{

	@Override
	public boolean isClient()
	{
		return true;
	}
	
	@Override
	public boolean isOpenToLAN()
	{
		if (Minecraft.getMinecraft().isIntegratedServerRunning())
		{
			return Minecraft.getMinecraft().getIntegratedServer().getPublic();
		} else
		{
			return false;
		}
	}
	
	@Override
	public void registerTickHandlers()
	{
		super.registerTickHandlers();
	}
	
	@Override
	public void registerEventHandlers()
	{
		super.registerEventHandlers();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void registerRenderers() 
	{

		RenderingRegistry.registerEntityRenderingHandler(EntityRooster.class, new IRenderFactory() {
			@Override
			public Render createRenderFor(RenderManager manager) {
				return new RenderRooster(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(HatcheryEggThrown.class, new HatcheryEggThrownFactory());
	}
	
	
	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		registerFluidModel(ModFluids.blockLiquidFertilizer,  "fertilizer");
    	initRenderers();
	}
	
	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
		
		ModItems.registerItemColorHandler(null);
	}
	
	
	@Override
	public void initRenderers()
	{
		
		//Register Blocks

		ClientRegistry.bindTileEntitySpecialRenderer(EggNestTileEntity.class, new EggNestTileEntityRender());
		ClientRegistry.bindTileEntitySpecialRenderer(NestPenTileEntity.class, new NestingPenTileEntityRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(ShredderTileEntity.class, new ShredderTileEntityRenderer());		
		ClientRegistry.bindTileEntitySpecialRenderer(EggMachineTileEntity.class, new EggMachineEntityRenderer());
	}


	@SideOnly(Side.CLIENT)
	public static void registerBlockModel(Block block)
	{
		registerBlockModel(block, 0, block.getRegistryName().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerBlockModel(Block block, int meta, String name)
	{
		Item item = Item.getItemFromBlock(block);
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		
		if(!name.equals(item.getRegistryName()))
		{
		    ModelBakery.registerItemVariants(item, model);
		}
		
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, model);
	}
	
	

	private void registerFluidModel(Block fluidBlock, String name) 
	{
		Item item = Item.getItemFromBlock(fluidBlock);

		ModelBakery.registerItemVariants(item);

		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Hatchery.MODID +":fluid", name);
		ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition(){

			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {

				return modelResourceLocation;
			}

		});

		ModelLoader.setCustomStateMapper(fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) 
			{
				return modelResourceLocation;
			}
		});
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item)
	{
		registerItemModel(item, 0, item.getRegistryName().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, int meta, String name)
	{
		ModelResourceLocation model = new ModelResourceLocation(name, "inventory");
		
		if(!name.equals(item.getRegistryName()))
		{
		    ModelBakery.registerItemVariants(item, model);
		}
		
	    Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, model);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}

}
