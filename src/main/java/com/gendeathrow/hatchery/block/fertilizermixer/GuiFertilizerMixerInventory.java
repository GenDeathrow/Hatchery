package com.gendeathrow.hatchery.block.fertilizermixer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;

@SideOnly(Side.CLIENT)
public class GuiFertilizerMixerInventory extends GuiContainer
{
	private static final ResourceLocation GUI_MIXER_INVENTORY = new ResourceLocation(Hatchery.MODID, "textures/gui/fertilizer_mixer_gui.png");
	
	FertilizerMixerTileEntity MIXER;
	
	public GuiFertilizerMixerInventory(InventoryPlayer inventory, FertilizerMixerTileEntity entityInventory) 
	{
		super(new ContainerFertlizerMixer(inventory, entityInventory));
		xSize = 176;
		ySize = 166;
		MIXER = (FertilizerMixerTileEntity) entityInventory;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.fertilizermixer").getFormattedText()), xSize / 2 - fontRendererObj.getStringWidth(I18n.format(new TextComponentTranslation("container.fertilizermixerinventory").getFormattedText())) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.fertilizermixerinventory").getFormattedText()), xSize - 170, ySize - 93, 4210752);
		
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;

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
		
		int fertilizerTankAmt = (int) (((float)MIXER.fertilizerLevel / MIXER.getFertilizerTank().getCapacity()) * 58);
		int waterTankAmt = (int) (((float)MIXER.waterLevel / MIXER.getWaterTank().getCapacity()) * 58);
		

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			drawTexturedModalRect(xOffSet + 53, yOffSet + 13 + 58 - waterTankAmt, 221, 58 - waterTankAmt, 13, waterTankAmt);
			drawTexturedModalRect(xOffSet + 145, yOffSet + 13 + 58 - fertilizerTankAmt, 208, 58 - fertilizerTankAmt, 13, fertilizerTankAmt);
			
	
			hover.clear();
			
			if(x > xOffSet+53 && x < xOffSet+66 && y > yOffSet+13 && y < yOffSet+59)
			{
				hover.add("Water:");
				hover.add(formatter.format(MIXER.waterLevel) +"mb / "+ formatter.format(this.MIXER.getWaterTank().getCapacity())+"mb");
			}
			else if(x > xOffSet+145 && x < xOffSet+158 && y > yOffSet+13 && y < yOffSet+59)
			{

				hover.add("Liquid Fertlizer:");
				hover.add(formatter.format(((int)MIXER.fertilizerLevel)) +"mb / "+ formatter.format(this.MIXER.getFertilizerTank().getCapacity())+"mb");
			}
	
			if(hover.size() > 0)
			{
				this.drawHoveringText(hover, x, y);
			}
	}
}
