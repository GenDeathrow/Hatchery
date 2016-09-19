package com.gendeathrow.hatchery.block.nestblock;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class NestTileEntityRender extends TileEntitySpecialRenderer<NestTileEntity>
{
	EntityItem renderitem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, new ItemStack(Items.EGG));
	
		
	public void renderTileEntityAt(NestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
    	if(NestBlock.doesHaveEgg(te.getWorld().getBlockState(te.getPos()))) 
    	{
    		renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
    	}
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    }
   
    
    public void renderAModelAt(NestTileEntity te, double x, double y, double z, float f, float partialTicks)
    {
        GlStateManager.pushMatrix();
    	
    	renderitem.setAngles(0, 0);
    	renderitem.hoverStart = 0f;

        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x, y, z);
        //GlStateManager.rotate(-20, 0, 1, 0);
        GlStateManager.enableLighting();
        	renderItem(te);
    	GlStateManager.disableLighting();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
    
    private void renderItem(NestTileEntity te) 
    {
        ItemStack stack = te.getStackInSlot(0);
 
        if (stack == null)
        {
        	stack = new ItemStack(Items.EGG); 
        }
            
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            // Translate to the center of the block and .9 points higher
            GlStateManager.translate(.5, .25, .5);
            GlStateManager.scale(.4f, .4f, .4f);

            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
    }
    
	public boolean isGlobalRenderer(NestTileEntity te)
	{
		return true;
	}
}
