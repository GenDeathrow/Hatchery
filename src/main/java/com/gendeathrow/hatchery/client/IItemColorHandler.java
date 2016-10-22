package com.gendeathrow.hatchery.client;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class IItemColorHandler implements IItemColor
{

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) 
    { 	
    	if(!stack.hasTagCompound()) {return 0xdfce9b;}
    	return stack.getTagCompound().getInteger("eggColor"); 
    }
}
