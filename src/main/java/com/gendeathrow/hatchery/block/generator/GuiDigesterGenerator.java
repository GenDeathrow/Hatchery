package com.gendeathrow.hatchery.block.generator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModFluids;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiDigesterGenerator extends GuiContainer
{
	private static final ResourceLocation GUI_GENERATOR_INVENTORY = new ResourceLocation(Hatchery.MODID, "textures/gui/digester_generator.png");
	DigesterGeneratorTileEntity GENERATOR;
	
	public GuiDigesterGenerator(InventoryPlayer inventory,DigesterGeneratorTileEntity tileEntity) 
	{
		super(new ContainerDigesterGenerator(inventory, tileEntity));
		xSize = 176;
		ySize = 166;
		this.GENERATOR = tileEntity;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) 
	{
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
	
		previousFertAmt = fertilizerTankAmt;
		previousRFLevel = rfEnergyLevels;
		
		fertilizerTankAmt = (int) (((float)GENERATOR.tankLevel / GENERATOR.getTank().getCapacity()) * 58);
		rfEnergyLevels = (int) (((float)GENERATOR.energy.getEnergyStored() / GENERATOR.energy.getMaxEnergyStored()) * 58);
		
		
		int rfOut = previousRFLevel - rfEnergyLevels > 0 ? previousRFLevel - rfEnergyLevels: 0;
		int fertIn = previousFertAmt - fertilizerTankAmt > 0 ? previousFertAmt - fertilizerTankAmt : 0;  
		
		String generatingRFString = new TextComponentTranslation("container.generator.generating", GENERATOR.getRFPerTick()).getFormattedText();
		String rfOutString = new TextComponentTranslation("container.generator.rfout", rfOut).getFormattedText();
		String fertInString = new TextComponentTranslation("container.generator.fertin", fertIn).getFormattedText();

		GlStateManager.pushMatrix();
			GlStateManager.translate(95, 15, 0);
				GlStateManager.scale(0.75, 0.75, 0);
					fontRendererObj.drawString(generatingRFString, 0 , 0, 4210752);
		GlStateManager.popMatrix();

		fontRendererObj.drawString(new TextComponentTranslation("container.upgrades").getFormattedText(), xSize - 80, 40, 4210752);

		
		GlStateManager.pushMatrix();
		GlStateManager.translate(-this.guiLeft, -this.guiTop, 0);
		
		hover.clear();
		
		if(x > xOffSet+16 && x < xOffSet+28 && y > yOffSet+13 && y < yOffSet+70)
		{
			hover.add(formatter.format((int)GENERATOR.energy.getEnergyStored()) +"/ "+ formatter.format(this.GENERATOR.energy.getMaxEnergyStored())+" RF");
		}
		else if(x > xOffSet+53 && x < xOffSet+66 && y > yOffSet+13 && y < yOffSet+70)
		{
			hover.add(ModFluids.liquidfertilizer.getName()+":");
			hover.add(formatter.format(((int)GENERATOR.tankLevel)) +"/"+ formatter.format(this.GENERATOR.getTank().getCapacity())+" mb");
		}

		if(hover.size() > 0)
			this.drawHoveringText(hover, x, y);
		
		GlStateManager.translate(this.guiLeft, this.guiTop, 0);
		GlStateManager.popMatrix();
	}  

	int previousFertAmt = 0;
	int previousRFLevel = 0;
	int fertilizerTankAmt = 0;
	int rfEnergyLevels = 0;
	
	List<String> hover =  new ArrayList<String>();
	DecimalFormat formatter = new DecimalFormat("#,###");
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		mc.getTextureManager().bindTexture(GUI_GENERATOR_INVENTORY);

		drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, xSize, ySize);


		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawTexturedModalRect(guiLeft + 53, guiTop + 13 + 58 - fertilizerTankAmt, 208, 58 - fertilizerTankAmt, 13, fertilizerTankAmt);
		drawTexturedModalRect(guiLeft + 15, guiTop + 13 + 58 - rfEnergyLevels, 220, 58 - rfEnergyLevels, 13, rfEnergyLevels);
				

	}
	

}
