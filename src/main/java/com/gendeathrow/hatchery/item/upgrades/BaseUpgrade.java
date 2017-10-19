package com.gendeathrow.hatchery.item.upgrades;

import java.util.List;

import com.gendeathrow.hatchery.api.items.IUpgradeItem;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseUpgrade extends Item implements IUpgradeItem
{
	protected int upgradeTier;
	protected String upgradeType;
	
	
	public BaseUpgrade(int tier, String type)
	{
		super();
		this.setMaxStackSize(1);
		this.upgradeTier = tier;
		this.upgradeType = type;
		
	}

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
    	for(int i=1; i <= this.upgradeTier; i++)
    	{
    		subItems.add(new ItemStack(itemIn, 1, i-1));
    	}
    }
    
    
	@Override
	public int getUpgradeTier(ItemStack stack, String type) 
	{
		//return this.upgradeTier;
		return this.getMetadata(stack) + 1;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
	    return super.getUnlocalizedName() + "_" + this.getUpgradeTier(stack, this.upgradeType);
	}

	@Override
	public void updateTick(ItemStack stack, String type) 
	{
		
	}

}
