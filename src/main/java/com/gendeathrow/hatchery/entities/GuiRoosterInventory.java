package com.gendeathrow.hatchery.entities;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiRoosterInventory extends GuiContainer {

	private static final ResourceLocation GUI_ROOSTER_INVENTORY = new ResourceLocation(Hatchery.MODID, "textures/gui/roosterGui.png");
	EntityRooster ROOSTER;
	public GuiRoosterInventory(InventoryPlayer inventory, Entity entityInventory) {
		super(new ContainerRoosterInventory(inventory, (IInventory) entityInventory));
		xSize = 174;
		ySize = 164;
		ROOSTER = (EntityRooster) entityInventory;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.roosterInventory").getFormattedText()), xSize / 2 - fontRendererObj.getStringWidth(I18n.format(new TextComponentTranslation("container.roosterInventory").getFormattedText())) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format(new TextComponentTranslation("container.inventory").getFormattedText()), xSize - 170, ySize - 93, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTickTime, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(GUI_ROOSTER_INVENTORY);
		int xOffSet = (width - xSize) / 2;
		int yOffSet = (height - ySize) / 2;
		drawTexturedModalRect(xOffSet, yOffSet, 0, 0, xSize, ySize);
		
		int seeds = ROOSTER.getScaledSeeds(58);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (seeds >= 2) {
			drawTexturedModalRect(xOffSet + 75, yOffSet + 72 - seeds, 195, 58 - seeds, 13, seeds);
		}
	}
}