package com.gendeathrow.hatchery.item.upgrades;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpeedUpgrade extends BaseUpgrade
{
	public final static String ID = "speed_upgrade";

	public SpeedUpgrade(int tier) 
	{
		super(tier, ID);
		this.hasSubtypes = true;
	}
	
	
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
    	for(int i=1; i <= 3; i++)
    	{
    		subItems.add(new ItemStack(itemIn, 1, i));
    	}
    }

}
