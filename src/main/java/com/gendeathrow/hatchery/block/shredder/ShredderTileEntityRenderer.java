package com.gendeathrow.hatchery.block.shredder;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.nestpen.NestingPenBlock;
import com.gendeathrow.hatchery.client.model.ModelShredderBlade;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ShredderTileEntityRenderer extends TileEntitySpecialRenderer<ShredderTileEntity>
{
		RenderManager renderManager;
		ModelShredderBlade blade1 = new ModelShredderBlade();
		ModelShredderBlade blade2 = new ModelShredderBlade();
		public static final ResourceLocation TEXTURE = new ResourceLocation(Hatchery.MODID, "textures/entity/shredderblade.png");
		
		public ShredderTileEntityRenderer()
		{
			renderManager = Minecraft.getMinecraft().getRenderManager();
			
		}
		
		public void renderTileEntityAt(ShredderTileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
	    {
			try
			{
		        	renderAModelAt(te, x, y, z, partialTicks, destroyStage);   
		        	super.renderTileEntityAt(te, x, y, z, partialTicks, destroyStage);
			}catch(IllegalArgumentException e) { }
	    }
	   
	    
	    public void renderAModelAt(ShredderTileEntity te, double x, double y, double z, float f, float partialTicks)
	    {

	    	IBlockState state = te.getWorld().getBlockState(te.getPos());
	    	
	    	EnumFacing facing = NestingPenBlock.getFacing(state);
	    	
	    	if(facing == EnumFacing.EAST || facing == EnumFacing.WEST) facing = facing.getOpposite();

	        GlStateManager.pushMatrix();
	        
	        GlStateManager.color(1f, 1f, 1f, 1f);
	        
	        GlStateManager.translate(x + .5, y + .45, z + .5);
	        
	        GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1, 0);

	        GlStateManager.enableLighting();
				bindTexture(TEXTURE);
		        float ticks = te.animationTicks + (te.animationTicks - te.prevAnimationTicks) * partialTicks;
		        
		        this.blade1.render(null, 0, ticks * 3.0F, 0, 0, 0, 0.0625F);
		        
		       // this.blade2.render(null, 0, 0, 0, 0, 0, 0.0625F);
	    	GlStateManager.disableLighting();
	    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	        GlStateManager.popMatrix();
	    }
	    

	}