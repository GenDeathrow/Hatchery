package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.nestpen.NestPenTileEntity;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;

import net.minecraft.block.BlockFence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
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

public class AnimalNet extends Item
{
		
	public AnimalNet()
	{
		super();
		this.setCreativeTab(Hatchery.hatcheryTabs);
        this.setMaxStackSize(16);
	}
	
	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
		
		if(this.hasCapturedAnimal(stack))
		{
			if(stack.getTagCompound() != null && stack.getTagCompound().hasKey("entityDisplayName"))
			{
				return I18n.translateToLocal(stack.getUnlocalizedName()+".name")+ " ("+ stack.getTagCompound().getString("entityDisplayName") +")";
			}
		}
		return super.getItemStackDisplayName(stack);
    	
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
			stack = addEntitytoNet(player, stack, target);

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
	
	public static ItemStack addEntitytoNet(EntityPlayer player, ItemStack stack, Entity entity)
	{
    	ItemStack newstack = new ItemStack(ModItems.animalNet);
    	addEntityNBT(newstack, entity);        

        if (--stack.stackSize <= 0 && !player.capabilities.isCreativeMode)
        {
        	stack.stackSize = 1;
       		return addEntityNBT(stack, entity);     
        }
        else
        {
        	if (!player.inventory.addItemStackToInventory(newstack))
            {
                player.dropItem(newstack, false);
            }
            return stack;
        }

	}
	
	
	private static ItemStack addEntityNBT(ItemStack stack, Entity entity)
	{
		NBTTagCompound stacknbt = getNBT(stack);
			NBTTagCompound eNBT = new NBTTagCompound();
			entity.onGround = true;
			eNBT = entity.writeToNBT(eNBT);
			eNBT.setString("id", EntityList.getEntityString(entity));
		stacknbt.setTag("storedEntity", eNBT);
	
		stacknbt.setString("entityDisplayName", entity.getDisplayName().getUnformattedText());
		stack.setTagCompound(stacknbt);
		
		return stack;
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
    	    
    	    if(playerIn.worldObj.getBlockState(pos).getBlock() == ModBlocks.pen || playerIn.worldObj.getBlockState(pos).getBlock() == ModBlocks.pen_chicken)
    		{
    			NestPenTileEntity pen = (NestPenTileEntity)playerIn.worldObj.getTileEntity(pos);
    			
    			if(!hasCapturedAnimal(stack) && pen.storedEntity() != null)
    			{	
    				stack = addEntitytoNet(playerIn, stack, pen.storedEntity());
    				pen.tryGetRemoveEntity();
     			}
    			else if(stack.getTagCompound() != null &&  pen.storedEntity() == null)
    			{
            		NBTTagCompound entitynbt =  getEntityTag(stack);
              		
             		if(entitynbt == null) 
             			return  EnumActionResult.FAIL; 
            		
            		Entity entity = buildEntity(worldIn, entitynbt); 
            		
            		if(entity == null) 
            			return  EnumActionResult.FAIL; 
            		
         			pen.trySetEntity(entity);
         			
    	  	        playerIn.addStat(StatList.getObjectUseStats(this));

    	  	        //stack.getTagCompound().setTag("storedEntity", new NBTTagCompound());
    	  	        //stack.getTagCompound().removeTag("storedEntity");
    	  	        stack.setTagCompound(null);
    	  	        
    	  	        //stack.setStackDisplayName(I18n.translateToLocal(stack.getUnlocalizedName()+".name"));
    	  	        
    	  	      worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    			}

    		}
    		else if(stack.getTagCompound() != null)
        	{
          		NBTTagCompound entitynbt = getEntityTag(stack);
          		
         		if(entitynbt == null) 
         			return  EnumActionResult.FAIL; 
        		
        		Entity entity = buildEntity(worldIn, entitynbt); 
        		
        		if(entity == null) 
        			return  EnumActionResult.FAIL; 
        		

        		BlockPos pos2 = pos.offset(facing);
                double d0 = 0.0D;
                if (facing == EnumFacing.UP && playerIn.worldObj.getBlockState(pos2).getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block
                {
                    d0 = 0.5D;
                }
   
        		entity.setPositionAndRotation(pos2.getX() + 0.5D, pos2.getY() + d0, pos2.getZ() + 0.5D , Math.abs(playerIn.rotationYaw), 0);
	  			
       			worldIn.spawnEntityInWorld(entity);
       			
       			if(entity instanceof EntityAnimal){
       				if(((EntityAnimal)entity).isAIDisabled())
       					((EntityAnimal)entity).setNoAI(false);
       			}
	  	        playerIn.addStat(StatList.getObjectUseStats(this));
	  	        stack.setTagCompound(null);
	  	        
  	  	      worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	  	      return EnumActionResult.PASS;
        	}
        }
       return EnumActionResult.FAIL;
    }
	
	private static NBTTagCompound getEntityTag(ItemStack stack)
	{
		return (NBTTagCompound) stack.getTagCompound().getTag("storedEntity");
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
