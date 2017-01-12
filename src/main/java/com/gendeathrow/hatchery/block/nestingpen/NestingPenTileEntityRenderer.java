package com.gendeathrow.hatchery.block.nestingpen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.EnumFacing;

import com.gendeathrow.hatchery.block.nest.EggNestBlock;
import com.gendeathrow.hatchery.block.nest.EggNestTileEntity;
import com.gendeathrow.hatchery.core.init.ModBlocks;

public class NestingPenTileEntityRenderer extends TileEntitySpecialRenderer<NestingPenTileEntity>
{
	EntityChicken chicken;
	
	public void renderTileEntityAt(NestingPenTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
    {
		//	this is a temp fix to catch an error 
		try
		{
			boolean flag = te.getWorld() != null;
			boolean flag1 = !flag || (te.getBlockType() == ModBlocks.pen || te.getBlockType() == ModBlocks.pen_chicken);

	        if (flag1)
	        {
	        	renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
	        	super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
	        }
		}catch(IllegalArgumentException e) { }
    }
   
    
    public void renderAModelAt(NestingPenTileEntity te, double x, double y, double z, float f, float partialTicks)
    {
    	if(te.storedEntity() == null) return;
    	
    	IBlockState state = te.getWorld().getBlockState(te.getPos());
    	
    	EnumFacing facing = NestingPenBlock.getFacing(state);
    	
    	if(facing == EnumFacing.EAST || facing == EnumFacing.WEST) facing = facing.getOpposite();

        GlStateManager.pushMatrix();
        
        RenderManager manager = Minecraft.getMinecraft().getRenderManager();
    	
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + .5, y + .1, z + .5);
        GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1, 0);
        GlStateManager.enableLighting();
        	manager.doRenderEntity(te.storedEntity(), 0, 0, 0, 0, partialTicks, true);
    	GlStateManager.disableLighting();
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
    
    
    private float getCorrectYawAngle(NestingPenTileEntity te)
    {
    	
    	return 0;
    }
}
