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
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;

public class AnimalNet extends Item
{
		
	public AnimalNet()
	{
		super();
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
           	
	    if (target.worldObj.isRemote || !(target instanceof EntityAnimal) || (target instanceof EntityMob) || (hand == EnumHand.OFF_HAND)) 
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
	  		player.setActiveHand(hand);
	  		
	  		target.worldObj.removeEntity(target);
	  		return true;
	    }
	    
		
    }
	
	public static boolean hasCapturedAnimal(ItemStack stack)
	{
		if(stack.getTagCompound() == null) return false;
		
		if(stack.getTagCompound().getTag("storedEntity") == null) return false;
		
		if(stack.getTagCompound().getTag("storedEntity").hasNoTags()) return false;

		return true;
	}
	
	public static NBTTagCompound getCapturedAnimalNBT(ItemStack stack)
	{
		return (NBTTagCompound) stack.getTagCompound().getTag("storedEntity");
	}
	
	public static void setCapturedNBT(ItemStack stack, Entity entity)
	{
		NBTTagCompound stacknbt = getNBT(stack);
			NBTTagCompound eNBT = new NBTTagCompound();
			eNBT = entity.writeToNBT(eNBT);
			eNBT.setString("id", EntityList.getEntityString(entity));
		stacknbt.setTag("storedEntity", eNBT);
		
  		stack.setTagCompound(stacknbt);
  		
  		stack.setStackDisplayName(I18n.translateToLocal(stack.getUnlocalizedName()+".name")+ " ("+ entity.getDisplayName().getFormattedText() +")");
  		
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
    		if(playerIn.worldObj.getBlockState(pos).getBlock() == ModBlocks.pen_chicken)
    		{
    			NestPenTileEntity pen = (NestPenTileEntity)playerIn.worldObj.getTileEntity(pos);
    			
    			if(!hasCapturedAnimal(stack) && pen.storedEntity() != null)
    			{	
    				setCapturedNBT(stack, pen.storedEntity());
    				pen.tryGetRemoveEntity();
     			}
    		}
    		else if(stack.getTagCompound() != null)
        	{
          		NBTTagCompound entitynbt = (NBTTagCompound) stack.getTagCompound().getTag("storedEntity");
          		
         		if(entitynbt == null) 
         			return  EnumActionResult.FAIL; 
        		
        		Entity entity = buildEntity(worldIn, entitynbt); 
        		
        		if(entity == null) 
        			return  EnumActionResult.FAIL; 

        		entity.setPositionAndRotation(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, Math.abs(playerIn.rotationYaw), 0);
	  			
        		if(playerIn.worldObj.getBlockState(pos).getBlock() == ModBlocks.pen)
        		{
         			NestPenTileEntity pen = (NestPenTileEntity)playerIn.worldObj.getTileEntity(pos);
         			pen.trySetEntity(entity);
        		}
        		else 
        			worldIn.spawnEntityInWorld(entity);
	  			
	  	        playerIn.addStat(StatList.getObjectUseStats(this));

	  	        //stack.getTagCompound().setTag("storedEntity", new NBTTagCompound());
	  	        stack.getTagCompound().removeTag("storedEntity");
	  	        
	  	        stack.setStackDisplayName(I18n.translateToLocal(stack.getUnlocalizedName()+".name"));
	  	        
	  	      return EnumActionResult.PASS;
        	}
        }
       return EnumActionResult.FAIL;
    }
	
	private static Entity buildEntity(World worldIn, NBTTagCompound entitynbt)
	{
		try
		{
			Entity entity = EntityList.createEntityFromNBT(entitynbt, worldIn);

			return entity;
		}
		catch (Throwable e)
		{
			Hatchery.logger.error("Error trying to spawn Animal 'Null NBT' " + e);
			return null;
		}
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
