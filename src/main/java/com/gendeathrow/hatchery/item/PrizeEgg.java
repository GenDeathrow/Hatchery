package com.gendeathrow.hatchery.item;

import java.util.Random;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.config.ConfigLootHandler;
import com.gendeathrow.hatchery.core.config.ConfigLootHandler.ItemDrop;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

public class PrizeEgg extends Item{
	
	public PrizeEgg()
	{
		super();
		this.setCreativeTab(Hatchery.hatcheryTabs);
		this.setNoRepair();
		this.setMaxStackSize(16);
	}
	
	public static final Random rand = new Random();
	
	
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (!playerIn.capabilities.isCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

		if(!worldIn.isRemote)
		{
			ItemDrop drop = (ItemDrop)WeightedRandom.getRandomItem(rand, ConfigLootHandler.drops);
			
			if(!playerIn.inventory.addItemStackToInventory(drop.getItemStack()))
				playerIn.dropItem(drop.getItemStack(), true);

		}

        playerIn.addStat(StatList.getObjectUseStats(this));
		
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
