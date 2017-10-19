package com.gendeathrow.hatchery.client.render.entity;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.client.model.ModelRooster;
import com.gendeathrow.hatchery.entities.EntityRooster;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderRooster extends RenderLiving<EntityRooster> {
	public static final ResourceLocation TEXTURE = new ResourceLocation(Hatchery.MODID, "textures/entity/rooster.png");

	public RenderRooster(RenderManager renderManagerIn) {
		super(renderManagerIn, new ModelRooster(), 0.5F);
	}

	@Override
	protected float handleRotationFloat(EntityRooster livingBase, float partialTicks) 
	{
		float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
		float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
		return (MathHelper.sin(f) + 1.0F) * f1;
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityRooster entity) {
		return TEXTURE;
	}
}
