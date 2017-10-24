package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
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
		
		spawnEntity = ItemStackEntityNBTHelper.getEntityTagFromStack(stack);
	}

	
	public HatcheryEggThrown(World worldIn, ItemStack stackIn, double x, double y, double z) 
	{
		super(worldIn, x, y, z);
		
		spawnEntity = ItemStackEntityNBTHelper.getEntityTagFromStack(stackIn);

	}


	@Override
	protected void onImpact(RayTraceResult result)
	    {
	        if (result.entityHit != null)
	        {
	            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
	        }
	        
	        if (!this.world.isRemote && this.rand.nextInt(8) == 0)
	        {
	        	Entity entitychicken = null; 
	        	
    	        if(spawnEntity == null) 
    	        {
    	        	entitychicken = new EntityChicken(this.world);
    	        	if(entitychicken instanceof EntityAgeable) ((EntityAgeable)entitychicken).setGrowingAge(-24000);
    	        }
    	        else 
    	        {
    	        	entitychicken = EntityList.createEntityFromNBT(spawnEntity, this.world);
    	        }
    	        
    	        if(entitychicken != null)
    	        {
    	        	entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
    	        	this.world.spawnEntity(entitychicken);
    	        }
	        }

	        double d0 = 0.08D;

	        for (int k = 0; k < 8; ++k)
	        {
	            this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(Items.EGG)});
	        }

	        if (!this.world.isRemote)
	        {
	            this.setDead();
	        }
	    }
}
