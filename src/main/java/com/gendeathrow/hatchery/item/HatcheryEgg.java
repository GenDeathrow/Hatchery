package com.gendeathrow.hatchery.item;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;
import com.gendeathrow.hatchery.util.RegisterEggsUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HatcheryEgg extends ItemEgg
{
	
	public static HashMap<String, Integer> ENTITYTORGB = new HashMap<String, Integer>();
	
	public HatcheryEgg()
	{
		super();
		
		this.setUnlocalizedName("hatcheryegg");
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

    public static void setColor(ItemStack itemstackIn, Entity entity)
    {
    	if(!itemstackIn.hasTagCompound()) itemstackIn.setTagCompound(new NBTTagCompound());
    	
    	NBTTagCompound entitytag = entity.writeToNBT(new NBTTagCompound());
      	 
    	itemstackIn.getTagCompound().setInteger("eggColor", RegisterEggsUtil.getEggColor(entitytag, EntityList.getEntityString(entity)));
    }
    
    
    /**
     * Use this to create an egg from an entity pass in it. 
     * 
     * @param entity
     */
    @Nullable
    public static ItemStack createEggFromEntity(World worldIn, EntityAgeable entity)
    {
		if(entity == null) return null;

		ItemStack egg = new ItemStack(ModItems.hatcheryEgg, 1, 0);
		egg.setStackDisplayName(entity.getDisplayName().getFormattedText() +" Egg");
	  	HatcheryEgg.setColor(egg, entity);
    	ItemStackEntityNBTHelper.addEntitytoItemStack(egg, (EntityLiving)entity);
    	
    	return egg;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
    	if(stack.hasTagCompound())
    	{
    		//Support for DNA on Chickens Mod
    		NBTTagCompound tag = stack.getTagCompound();
    		
    		if(tag.hasKey("storedEntity"))
    		{
    			NBTTagCompound entityNBT = tag.getCompoundTag("storedEntity");
    			
    			if(entityNBT.hasKey("Growth"))
    			{
    				tooltip.add("Growth: "+ entityNBT.getInteger("Growth"));
    			}
    			if(entityNBT.hasKey("Gain"))
    			{
    				tooltip.add("Gain: "+ entityNBT.getInteger("Gain"));
    			}
    			if(entityNBT.hasKey("Strength"))
    			{
    				tooltip.add("Strength: "+ entityNBT.getInteger("Strength"));
    			}

    		}
    		
    	}
    }

}
