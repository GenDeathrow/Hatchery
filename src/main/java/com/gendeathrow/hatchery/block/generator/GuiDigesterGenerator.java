package com.gendeathrow.hatchery.block.generator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModFluids;

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
		
		fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.generator.generating", 60).getFormattedText(), GENERATOR.storage.getEnergyStored()), xOffSet + 100 , yOffSet + 15, 4210752);
		//fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.fertilizermixerinventory").getFormattedText()), xSize - 170, ySize - 93, 4210752);
		


	}  

	List<String> hover =  new ArrayList<String>();
	DecimalFormat formatter = new DecimalFormat("#,###");
	
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

		drawTexturedModalRect(xOffSet + 53, yOffSet + 13 + 58 - fertilizerTankAmt, 208, 58 - fertilizerTankAmt, 13, fertilizerTankAmt);
				drawTexturedModalRect(xOffSet + 16, yOffSet + 13 + 58 - rfEnergyLevels, 220, 58 - rfEnergyLevels, 13, rfEnergyLevels);
				

		
		hover.clear();
				
		if(x > xOffSet+16 && x < xOffSet+28 && y > yOffSet+13 && y < yOffSet+59)
		{
			hover.add(formatter.format((int)GENERATOR.storage.getEnergyStored()) +"rf / "+ formatter.format(this.GENERATOR.storage.getMaxEnergyStored())+"rf");
		}
		else if(x > xOffSet+53 && x < xOffSet+66 && y > yOffSet+13 && y < yOffSet+59)
		{
			hover.add("Liquid Fertlizer:");
			hover.add(formatter.format(((int)GENERATOR.tankLevel)) +"mb/"+ formatter.format(this.GENERATOR.getTank().getCapacity())+"mb");
		}

		if(hover.size() > 0)
			this.drawHoveringText(hover, x, y);
	}
	

}
