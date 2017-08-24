package com.gendeathrow.hatchery.block.eggstractor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModItems;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class GuiEggstractor extends GuiContainer
{
	private static final ResourceLocation GUI_MIXER_INVENTORY = new ResourceLocation(Hatchery.MODID, "textures/gui/eggstractor_gui.png");
	
	EggstractorTileEntity eggstractor;
	InventoryPlayer playerInventory;
	
	public GuiEggstractor(InventoryPlayer inventory, EggstractorTileEntity entityInventory) 
	{
		super(new ContainerEggstractor(inventory, entityInventory));
		xSize = 176;
		ySize = 166;
		eggstractor = (EggstractorTileEntity) entityInventory;
		playerInventory = inventory;
	}

	private ItemStack egg = new ItemStack(Items.EGG);
	private ItemStack plastic = new ItemStack(ModItems.plastic);
		
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.eggstractor").getFormattedText()), 5 , 3, 4210752);
		fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 6, 73, 4210752);
		
		fontRendererObj.drawString(new TextComponentTranslation("container.upgrades").getFormattedText(), 120 - (this.fontRendererObj.getStringWidth(new TextComponentTranslation("container.upgrades").getUnformattedText())/2), 40, 4210752);
		
		fontRendererObj.drawString(new TextComponentTranslation("container.eggstractor.eggs", eggstractor.internalEggStorage).getFormattedText(), 89, 14, 4210752);
		fontRendererObj.drawString(new TextComponentTranslation("container.eggstractor.plastic", eggstractor.internalPlasticStorage).getFormattedText(), 89, 30, 4210752);

		
			this.itemRender.zLevel = 200;
				this.itemRender.renderItemAndEffectIntoGUI(mc.thePlayer,egg, 37, 18);
				this.itemRender.renderItemAndEffectIntoGUI(mc.thePlayer,plastic, 63, 18);
			this.itemRender.zLevel = 0;
	}
	
	DecimalFormat formatter = new DecimalFormat("#,###");
	List<String> hover =  new ArrayList<String>();

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_MIXER_INVENTORY);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
		
	
		int energyAmt = (int) (((float)eggstractor.energy.getEnergyStored() / eggstractor.energy.getMaxEnergyStored()) * 58);
		

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			drawTexturedModalRect(xOffSet + 16, yOffSet + 13 + 58 - energyAmt, 221, 58 - energyAmt, 12, energyAmt);
	
			hover.clear();
			
			if(x > xOffSet+16 && x < xOffSet+39 && y > yOffSet+13 && y < yOffSet+70)
			{

				hover.add("RF:");
				hover.add(formatter.format(((int)eggstractor.energy.getEnergyStored())) +" / "+ formatter.format(this.eggstractor.energy.getMaxEnergyStored())+"rf");
			}
			
			
	
			if(hover.size() > 0)
			{
				this.drawHoveringText(hover, x, y);
			}
	}
	
	
	 protected void renderItemModelIntoGUI(ItemStack stack, int x, int y, IBakedModel bakedmodel)
	    {
	        GlStateManager.pushMatrix();
	        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
	        GlStateManager.enableRescaleNormal();
	        GlStateManager.enableAlpha();
	        GlStateManager.alphaFunc(516, 0.1F);
	        GlStateManager.enableBlend();
	        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	        GlStateManager.glBlendEquation(GL14.GL_FUNC_SUBTRACT);
	        GlStateManager.enableDepth();
	    	GL11.glDepthMask(true);
//	        glBlendFunc(GL_ONE, GL_ONE);
//	        
//	        glBlendEquation(GL_FUNC_SUBTRACT);
//	        glBlendEquation(GL_FUNC_REVERSE_SUBTRACT);
	        
	        
	        GlStateManager.color(1F, 1F, 1F, 0.5F);
	        this.setupGuiTransform(x, y, bakedmodel.isGui3d());
	        bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
	        this.itemRender.renderItem(stack, bakedmodel);
	        GlStateManager.disableDepth();
	        GlStateManager.glBlendEquation(GL14.GL_FUNC_ADD);
	        GlStateManager.disableAlpha();
	        GlStateManager.disableRescaleNormal();
	        GlStateManager.disableLighting();
	        GlStateManager.popMatrix();
	        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	        mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	    }
	
	 
	    private void setupGuiTransform(int xPosition, int yPosition, boolean isGui3d)
	    {
	        GlStateManager.translate((float)xPosition, (float)yPosition, 100.0F + this.zLevel);
	        GlStateManager.translate(8.0F, 8.0F, 0.0F);
	        GlStateManager.scale(1.0F, -1.0F, 1.0F);
	        GlStateManager.scale(16.0F, 16.0F, 16.0F);

	        if (isGui3d)
	        {
	            GlStateManager.enableLighting();
	        }
	        else
	        {
	            GlStateManager.disableLighting();
	        }
	    }
	    
	public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase p_184391_1_, final ItemStack stack, int x, int y)
    {
        if (stack != null && stack.getItem() != null)
        {
            this.zLevel += 50.0F;

            try
            {
                this.renderItemModelIntoGUI(stack, x, y, this.itemRender.getItemModelWithOverrides(stack, (World)null, p_184391_1_));
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering item");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being rendered");
                crashreportcategory.setDetail("Item Type", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf((Object)stack.getItem());
                    }
                });
                crashreportcategory.setDetail("Item Aux", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(stack.getMetadata());
                    }
                });
                crashreportcategory.setDetail("Item NBT", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf((Object)stack.getTagCompound());
                    }
                });
                crashreportcategory.setDetail("Item Foil", new ICrashReportDetail<String>()
                {
                    public String call() throws Exception
                    {
                        return String.valueOf(stack.hasEffect());
                    }
                });
                throw new ReportedException(crashreport);
            }

            this.zLevel -= 50.0F;
        }
    }
}
