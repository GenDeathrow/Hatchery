package com.gendeathrow.hatchery.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RendererWrapper extends Render
{

	protected RendererWrapper(RenderManager renderManager) 
	{
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) 
	{
		return null;
	}

}
