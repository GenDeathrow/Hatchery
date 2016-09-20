package com.gendeathrow.hatchery.block.cage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.passive.EntityChicken;

import com.gendeathrow.hatchery.block.nestblock.NestBlock;
import com.gendeathrow.hatchery.block.nestblock.NestTileEntity;

public class CageTileEntityRenderer extends TileEntitySpecialRenderer<CageTileEntity>
{
	EntityChicken chicken;
	public void renderTileEntityAt(CageTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
    		renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    }
   
    
    public void renderAModelAt(CageTileEntity te, double x, double y, double z, float f, float partialTicks)
    {
        GlStateManager.pushMatrix();
        
        
        if(chicken == null) 
       	{
        	chicken = new EntityChicken(te.getWorld());
       	}
        
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
    	
//    	renderitem.setAngles(0, 0);
//    	renderitem.hoverStart = 0f;

        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + .5, y + .2, z + .5);
        //GlStateManager.rotate(-20, 0, 1, 0);
        GlStateManager.enableLighting();
        	manager.doRenderEntity(chicken, 0, 0, 0, 0, partialTicks, true);;
    	GlStateManager.disableLighting();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
