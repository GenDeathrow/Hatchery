package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChickenManure extends Item
{
	public ChickenManure()
	{
		super();
		
		this.setUnlocalizedName("chickenmanure");
		//this.setRegistryName("chickenmanure");
		this.setCreativeTab(Hatchery.hatcheryTabs);
	}
	
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
        {
            return EnumActionResult.FAIL;
        }
        else
        {
                if (ItemDye.applyBonemeal(stack, worldIn, pos, playerIn))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.playEvent(2005, pos, 0);
                    }
                }

            return EnumActionResult.SUCCESS;
        }
        

    }
}
