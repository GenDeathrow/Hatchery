package com.gendeathrow.hatchery.block.generator;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;

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
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		//fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.fertilizermixer").getFormattedText()), xSize / 2 - fontRendererObj.getStringWidth(I18n.format(new TextComponentTranslation("container.fertilizermixerinventory").getFormattedText())) / 2, 6, 4210752);
		//fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.fertilizermixerinventory").getFormattedText()), xSize - 170, ySize - 93, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_GENERATOR_INVENTORY);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
		
		int fertilizerTankAmt = (int) (((float)GENERATOR.tankLevel / GENERATOR.getTank().getCapacity()) * 58);
		int rfEnergyLevels = (int) (((float)GENERATOR.storage.getEnergyStored() / GENERATOR.storage.getMaxEnergyStored()) * 58);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		drawTexturedModalRect(xOffSet + 145, yOffSet + 13 + 58 - fertilizerTankAmt, 208, 58 - fertilizerTankAmt, 13, fertilizerTankAmt);
		
		drawTexturedModalRect(xOffSet + 100, yOffSet + 13 + 58 - rfEnergyLevels, 208, 58 - rfEnergyLevels, 13, rfEnergyLevels);
	}
	

}
