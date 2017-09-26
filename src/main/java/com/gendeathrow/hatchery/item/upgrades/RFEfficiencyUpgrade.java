package com.gendeathrow.hatchery.item.upgrades;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO this is Legacy now.
public class RFEfficiencyUpgrade extends BaseUpgrade
{
	protected final static String ID = "rfupgrade";

	public RFEfficiencyUpgrade(int tier) 
	{
		super(tier, ID);
	}
	
	
	@Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
    		subItems.add(new ItemStack(itemIn));
    }
	
	@Override
	public int getUpgradeTier(ItemStack stack, String type) 
	{
		return this.upgradeTier;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String[] split = this.getRegistryName().toString().split(":");
		if(split.length > 1)
			return "item." + split[0] +"."+ split[1];
			
		else 	
			return "item." + this.getRegistryName().toString();
	}

}
