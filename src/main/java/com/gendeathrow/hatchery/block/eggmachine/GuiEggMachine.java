package com.gendeathrow.hatchery.block.eggmachine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModItems;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiEggMachine extends GuiContainer
{
	private static final ResourceLocation GUI_MIXER_INVENTORY = new ResourceLocation(Hatchery.MODID, "textures/gui/eggstractor_gui.png");
	
	EggMachineTileEntity eggstractor;
	InventoryPlayer playerInventory;
	
	public GuiEggMachine(InventoryPlayer inventory, EggMachineTileEntity entityInventory) 
	{
		super(new ContainerEggMachine(inventory, entityInventory));
		xSize = 176;
		ySize = 166;
		eggstractor = (EggMachineTileEntity) entityInventory;
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
		fontRendererObj.drawString(new TextComponentTranslation("container.eggstractor.plastic", eggstractor.internalPlasticStorage).getFormattedText(), 89, 26, 4210752);

		
			this.itemRender.zLevel = 200;
				this.itemRender.renderItemAndEffectIntoGUI(mc.thePlayer,egg, 37, 18);
				this.itemRender.renderItemAndEffectIntoGUI(mc.thePlayer,plastic, 63, 18);
			this.itemRender.zLevel = 0;
			
			
			hover.clear();
			
			if(x > this.guiLeft+16 && x < this.guiLeft+39 && y > this.guiTop+13 && y < this.guiTop+70)
			{

				hover.add("RF:");
				hover.add(formatter.format(((int)eggstractor.energy.getEnergyStored())) +" / "+ formatter.format(this.eggstractor.energy.getMaxEnergyStored())+"rf");
			}
			
			
	
			if(hover.size() > 0)
			{
				this.drawHoveringText(hover, x - this.guiLeft, y-this.guiTop);
			}
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
		int k = getTimeLeftScaled(12);
			drawTexturedModalRect(guiLeft + 54, guiTop + 36, 197, 74, 9, 12-k);

	}
	
	
    private int getTimeLeftScaled(int pixels)
    {
    	int i = this.eggstractor.eggTime;
    	int j = this.eggstractor.maxEggTime;
    	
        return i != 0 && j != 0 ? Math.min((int)(((double)i / j) * pixels), pixels): 0;
    }
	
}
