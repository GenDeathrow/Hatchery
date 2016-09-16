package com.gendeathrow.hatchery.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class HatcheryEggThrown extends EntityEgg
{

	private NBTTagCompound spawnEntity; 
	public HatcheryEggThrown(World worldIn, EntityLivingBase throwerIn, ItemStack stack) 
	{
		super(worldIn, throwerIn);
		
		spawnEntity = stack.getTagCompound();
	}

	
	@Override
	protected void onImpact(RayTraceResult result)
	    {
	        if (result.entityHit != null)
	        {
	            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
	        }
	        

            
            
	        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0)
	        {
	            int i = 1;

	            if (this.rand.nextInt(32) == 0)
	            {
	                i = 4;
	            }

	            for (int j = 0; j < i; ++j)
	            {
	            	
	                //System.out.println(spawnEntity);
	    	        
	    	        Entity entitychicken = EntityList.createEntityFromNBT(spawnEntity, this.worldObj);
	    	        
	    	        if(entitychicken != null)
	    	        {
	    	        	entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
	    	        	this.worldObj.spawnEntityInWorld(entitychicken);
	    	        }
	    	        
	            	
	             //   EntityChicken entitychicken = new EntityChicken(this.worldObj);
//	                entitychicken.setGrowingAge(-24000);
	//                entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
//	                this.worldObj.spawnEntityInWorld(entitychicken);
	            }
	        }

	        double d0 = 0.08D;

	        for (int k = 0; k < 8; ++k)
	        {
	            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(Items.EGG)});
	        }

	        if (!this.worldObj.isRemote)
	        {
	            this.setDead();
	        }
	    }
}
