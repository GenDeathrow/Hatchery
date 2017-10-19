package com.gendeathrow.hatchery.client.config;

import java.util.ArrayList;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.config.ConfigHandler;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiHatcheryConfig extends GuiConfig
{

	public GuiHatcheryConfig(GuiScreen parent)
	{
		super(parent, getCategories(ConfigHandler.CONFIG), Hatchery.MODID, false, false, Hatchery.NAME);
	}
	
	@SuppressWarnings({"rawtypes"})
	public static ArrayList<IConfigElement> getCategories(Configuration config)
	{
		ArrayList<IConfigElement> cats = new ArrayList<IConfigElement>();
		
		for(String s : config.getCategoryNames())
		{
			cats.add(new ConfigElement(config.getCategory(s)));
		}

		
		return cats;
	}
	
    @Override
    public void initGui()
    {
        super.initGui();
    }

    
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        super.actionPerformed(button);
    }
}
