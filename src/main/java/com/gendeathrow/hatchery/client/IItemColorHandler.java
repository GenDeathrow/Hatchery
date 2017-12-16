package com.gendeathrow.hatchery.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IItemColorHandler implements IItemColor
{

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
    	if(!stack.hasTagCompound()) {return 0xdfce9b;}
    	return stack.getTagCompound().getInteger("eggColor"); 
	}
}
