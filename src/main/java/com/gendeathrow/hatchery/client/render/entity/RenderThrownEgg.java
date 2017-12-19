package com.gendeathrow.hatchery.client.render.entity;

import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.item.HatcheryEggThrown;
import com.setycz.chickens.ChickensMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderThrownEgg extends RenderSnowball<HatcheryEggThrown>{

	
	public RenderThrownEgg(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn) {
		super(renderManagerIn, itemIn, itemRendererIn);
	}

	@Override
    public ItemStack getStackToRender(HatcheryEggThrown entityIn)
    {
		//System.out.println("<><>"+entityIn.getItemStacktoRender().getItem().getRegistryName());
        return entityIn.getItemStacktoRender();
    }
	
	public static class HatcheryEggThrownFactory implements IRenderFactory<HatcheryEggThrown>{

		@Override
		public Render<? super HatcheryEggThrown> createRenderFor(RenderManager manager) {
			return new RenderThrownEgg(manager, ModItems.hatcheryEgg, Minecraft.getMinecraft().getRenderItem());
		}
		
	}
}
