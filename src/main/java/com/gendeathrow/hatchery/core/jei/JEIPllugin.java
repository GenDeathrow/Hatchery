package com.gendeathrow.hatchery.core.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;

@JEIPlugin
public class JEIPllugin extends BlankModPlugin
{
    @Override
    public void register (IModRegistry registry) 
    {
        for (final Block block : ModBlocks.BLOCKS)
            registry.addDescription(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE), "jei." + block.getUnlocalizedName());
        
        for (final Item item : ModItems.ITEMS)
            registry.addDescription(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE), "jei." + item.getUnlocalizedName());
    }
}
