package com.gendeathrow.hatchery.block.nest;

import com.gendeathrow.hatchery.core.init.ModBlocks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class EggNestTileEntityRender extends TileEntitySpecialRenderer<EggNestTileEntity>
{
	EntityItem renderitem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, new ItemStack(Items.EGG));
	
		
	public void renderTileEntityAt(EggNestTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
		try
		{
	        boolean flag = te.getWorld() != null;
	        boolean flag1 = !flag || te.getBlockType() == ModBlocks.nest;
			
	        if (flag1)
	        {
	        	if(EggNestBlock.doesHaveEgg(te.getWorld().getBlockState(te.getPos()))) 
	        	{
	        		renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
	        	}
	        }
		}catch(IllegalArgumentException e) { }
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    }
   
    
    public void renderAModelAt(EggNestTileEntity te, double x, double y, double z, float f, float partialTicks)
    {
        GlStateManager.pushMatrix();
    	
    	renderitem.setAngles(0, 0);
    	renderitem.hoverStart = 0f;

        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x, y, z);
        GlStateManager.enableLighting();
        	renderItem(te);
    	GlStateManager.disableLighting();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
    
    private void renderItem(EggNestTileEntity te) 
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
            
            GlStateManager.rotate(90, 0, 1, 0);
            Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
    }
    
	public boolean isGlobalRenderer(EggNestTileEntity te)
	{
		return true;
	}
}
