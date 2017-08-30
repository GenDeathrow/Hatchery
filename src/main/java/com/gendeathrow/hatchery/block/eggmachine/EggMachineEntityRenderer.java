package com.gendeathrow.hatchery.block.eggmachine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EggMachineEntityRenderer extends TileEntitySpecialRenderer<EggMachineTileEntity>
{
	RenderManager renderManager;
	
	ModelBase chicken = new ModelChicken();
	
	private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation("textures/entity/chicken.png");
	private final Entity entity = EntityList.createEntityByName("chicken", (World)null);
	
	
	public EggMachineEntityRenderer()
	{
		renderManager = Minecraft.getMinecraft().getRenderManager();
	}
	
	public void renderTileEntityAt(EggMachineTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
		try
		{
			boolean flag = te.getWorld() != null;
	        if (flag)
	        {
	        	renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
	        	super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
	        }
		}catch(IllegalArgumentException e) { }
    }
   
    
    public void renderAModelAt(EggMachineTileEntity te, double x, double y, double z, float f, float partialTicks)
    {
    	
    	IBlockState state = te.getWorld().getBlockState(te.getPos());
    	
    	EnumFacing facing = EggMachineBlock.getFacing(state);
    	
    	if(facing == EnumFacing.EAST || facing == EnumFacing.WEST) facing = facing.getOpposite();

        GlStateManager.pushMatrix();
        	float ticks = te.animationTicks + (te.animationTicks - te.prevAnimationTicks) * partialTicks;
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + .5, y + 2.5, z + .5);
        GlStateManager.rotate(180, 1, 0, 1);
        GlStateManager.rotate(ticks, 0, 1, 0);
        GlStateManager.enableLighting();
    	bindTexture(CHICKEN_TEXTURES);
        	chicken.render(entity, 0.0F,0F, 0F, 0.0F, 0.0F, 0.0625F);
    	GlStateManager.disableLighting();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
}
