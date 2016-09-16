package com.gendeathrow.hatchery.block.nestblock;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.network.HatcheryPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.RenderItemFrame;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class HatcheryTileEntityRender extends TileEntitySpecialRenderer<HatcheryTileEntity>
{
	EntityItem renderitem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, new ItemStack(Items.EGG));
	
		
	public void renderTileEntityAt(HatcheryTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
    	if(HatcheryBlock.doesHaveEgg(te.getWorld().getBlockState(te.getPos()))) 
    	{
    		renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
    	}
        super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
    }
   
    
    public void renderAModelAt(HatcheryTileEntity te, double x, double y, double z, float f, float partialTicks)
    {
    	RenderManager manager = Minecraft.getMinecraft().getRenderManager();
    	
//    	if(renderitem == null) 
//    	{
//    		renderitem =  new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, new ItemStack(Items.EGG));
//    		System.out.println("defaultegg");
//    	}
//		
//    	if(te.getStackInSlot(0) != null &&  renderitem.getEntityItem().getItem() != te.getStackInSlot(0).getItem())
//    	{
//    		System.out.println("itemChanged");
//    		 renderitem = new EntityItem(Minecraft.getMinecraft().theWorld, 0D, 0D, 0D, te.getStackInSlot(0));
//    	}
//    	else
//    	{
//        	if(Minecraft.getSystemTime() % 300 == 0)
//        	{
//        		if(te.getStackInSlot(0) == null && HatcheryBlock.doesHaveEgg(te.getWorld().getBlockState(te.getPos())))
//        		{
//        			Hatchery.network.sendToServer(HatcheryPacket.requestItemstackTE(te.getPos()));
//        			
//        			System.out.println("Requesting Packet");
//        		}
//        	}
//        	
//
//    	}

    	
    	renderitem.setAngles(0, 0);
    	renderitem.hoverStart = 0f;
    	
    	
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x+.5, y-.18, z+.5);
        //GlStateManager.rotate(-20, 0, 1, 0);
        GlStateManager.enableLighting();
    		manager.doRenderEntity(renderitem, 0, 0, 0, 0, partialTicks, false);
    	GlStateManager.disableLighting();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
	public boolean isGlobalRenderer(HatcheryTileEntity te)
	{
		return true;
	}
}
