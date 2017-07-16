package com.gendeathrow.hatchery.block.nursery;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiNursery  extends GuiContainer
{
	private static final ResourceLocation NURSERY_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");

	
	public GuiNursery(Container inventorySlotsIn) 
	{
		super(inventorySlotsIn);
	}

	
	// Buttons to allow adults thur and allow child thur
	public void initGui()
	{
		this.buttonList.add(new GuiButton(0, 0, 0, null));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks,int mouseX, int mouseY) 
	{
		
	}
}
