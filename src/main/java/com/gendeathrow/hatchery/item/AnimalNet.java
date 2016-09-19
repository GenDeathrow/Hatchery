package com.gendeathrow.hatchery.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.ModItems;

public class AnimalNet extends Item
{
		
	public AnimalNet()
	{
		super();
		
		this.setUnlocalizedName("animalnet");
		this.setRegistryName("animalnet");
		this.setCreativeTab(Hatchery.hatcheryTabs);
        this.setMaxStackSize(1);
	}
	
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        if (target.worldObj.isRemote)
        {
            return false;
        }
           	
	    if (!(target instanceof EntityAnimal)) 
	    {
	        return false;
	    }
		
	    if ((target instanceof EntityMob)) 
	    {
	        return false;
	    }
	    
	    if (hand == EnumHand.OFF_HAND && player.getHeldItemMainhand().getItem() == ModItems.animalNet) 
	    {
	        return false;
	    }


	    
	    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("storedEntity") && !stack.getTagCompound().getTag("storedEntity").hasNoTags())
    	{
	    	return false;
    	}
	    else
	    {
			NBTTagCompound eTag = this.getNBT(stack);
			
			NBTTagCompound storedEntity = new NBTTagCompound();
				storedEntity.setString("id", EntityList.getEntityString(target));
				((EntityLiving)target).writeEntityToNBT(storedEntity);
	  		
	  		eTag.setTag("storedEntity", storedEntity);
	  		
	  		stack.setTagCompound(eTag);
	  		
	  		stack.setStackDisplayName(I18n.translateToLocal(stack.getUnlocalizedName()+".name")+ " ("+ target.getDisplayName().getFormattedText() +")");
	  		
	  		player.getHeldItem(hand).setTagCompound(eTag);
	  		
	  		target.worldObj.removeEntity(target);
	  		return true;
	    }
	    
		
    }
	
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
    	    
    	    if (hand == EnumHand.OFF_HAND && playerIn.getHeldItemMainhand().getItem() == ModItems.animalNet) 
    	    {
    	    	return EnumActionResult.FAIL;
    	    }
        	if(stack.getTagCompound() != null)
        	{
        		stack.getTagCompound().toString();
        		NBTTagCompound entitynbt = (NBTTagCompound) stack.getTagCompound().getTag("storedEntity");
        		if(entitynbt == null) { return  EnumActionResult.FAIL; }
        		Entity entity = null; 
    	  		try
    	  		{
    	  			System.out.println(playerIn.rotationYaw);
    	  			entity = EntityList.createEntityFromNBT(entitynbt, worldIn);
    	  			entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, Math.abs(playerIn.rotationYaw), 0);
    	  			worldIn.spawnEntityInWorld(entity);
    	  			
    	  			
    	  	        playerIn.addStat(StatList.getObjectUseStats(this));

    	  	        stack.getTagCompound().setTag("storedEntity", new NBTTagCompound());
    	  	        
    	  	        stack.setStackDisplayName(I18n.translateToLocal(stack.getUnlocalizedName()+".name"));
    	  	        
    	  	        return EnumActionResult.PASS;
    	  		}
    	  		catch (Throwable e)
    	  		{
    					Hatchery.logger.error("Error trying to spawn Animal 'Null NBT' " + e);
    	  		}
    	  		
    	  		
        	}
        }


        return EnumActionResult.FAIL;
    }
	
	
	static NBTTagCompound getNBT(ItemStack stack) 
	{
	    if (stack.getTagCompound() == null) 
	    {
	        stack.setTagCompound(new NBTTagCompound());
	    }
	    return stack.getTagCompound();
	}
    
}
