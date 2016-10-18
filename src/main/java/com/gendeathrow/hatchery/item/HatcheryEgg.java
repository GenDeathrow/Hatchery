package com.gendeathrow.hatchery.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import com.gendeathrow.hatchery.Hatchery;

public class HatcheryEgg extends ItemEgg
{
	
	public HatcheryEgg()
	{
		super();
		
		this.setUnlocalizedName("hatcheryegg");
		//this.setRegistryName("hatcheryegg");
		this.setCreativeTab(Hatchery.hatcheryTabs);
	}
	
	
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        if (!playerIn.capabilities.isCreativeMode)
        {
            --itemStackIn.stackSize;
        }

        worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
        	HatcheryEggThrown entityegg = new HatcheryEggThrown(worldIn, playerIn, itemStackIn);
            entityegg.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntityInWorld(entityegg);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
    
    
//    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
//    {
//        if (!playerIn.capabilities.isCreativeMode)
//        {
//            --stack.stackSize;
//        }
//
//        return EnumActionResult.PASS;
//    }
    
    
    protected NBTTagList newDoubleNBTList(double[] dob)
    {
      NBTTagList nbttaglist = new NBTTagList();
      double[] adouble = dob;
      int i = dob.length;

      for (int j = 0; j < i; j++) {
        double d1 = adouble[j];
        nbttaglist.appendTag(new NBTTagDouble(d1));
      }

      return nbttaglist;
    }


}
