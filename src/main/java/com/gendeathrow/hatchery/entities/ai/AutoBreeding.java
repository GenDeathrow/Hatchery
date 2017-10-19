package com.gendeathrow.hatchery.entities.ai;

import java.util.Random;

import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.item.HatcheryEgg;
import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class AutoBreeding extends EntityAIBase
{

    protected final EntityAnimal theAnimal;
    protected World theWorld;
    protected EntityAnimal targetMate;
    /** Delay preventing a baby from spawning immediately when two mate-able animals find each other. */
    protected int spawnBabyDelay;
    /** The speed the creature moves at during mating behavior. */
    protected double moveSpeed;
    
	public AutoBreeding(EntityAnimal animal, double speedIn) 
	{
	    this.theAnimal = animal;
        this.theWorld = animal.worldObj;
        this.moveSpeed = speedIn;
        this.setMutexBits(3);
	}

    public boolean shouldExecute()
    {
    	if(theAnimal.getLeashed() && theAnimal.getLeashedToEntity() != null && theAnimal.getLeashedToEntity() instanceof EntityAnimal && this.theAnimal.canMateWith((EntityAnimal) theAnimal.getLeashedToEntity()))
    	{
            this.targetMate = (EntityAnimal) theAnimal.getLeashedToEntity();
            return this.targetMate != null;
    	}
    	
    	return false;
    }
    
    
    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.spawnBabyDelay < 60;
    }

    
    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.targetMate = null;
        this.spawnBabyDelay = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
        this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
        ++this.spawnBabyDelay;

        if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D)
        {
          //  this.spawnEgg();
        }
    }
    
    
    
    private void spawnEgg()
    {
        EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);

        if (entityageable != null)
        {
            EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();

            if (entityplayer == null && this.targetMate.getPlayerInLove() != null)
            {
                entityplayer = this.targetMate.getPlayerInLove();
            }

            if (entityplayer != null)
            {
                entityplayer.addStat(StatList.ANIMALS_BRED);

                if (this.theAnimal instanceof EntityCow)
                {
                    entityplayer.addStat(AchievementList.BREED_COW);
                }
            }

            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
            
            Entity entity = entityageable;
            
            if(Settings.IS_EGG_BREEDING)
            {
            	ItemStack egg = new ItemStack(ModItems.hatcheryEgg, 1, 0);
            
            	ItemStackEntityNBTHelper.addEntitytoItemStack(egg, entityageable);

            	egg.setStackDisplayName(entityageable.getDisplayName().getFormattedText() +" Egg");
            
            	HatcheryEgg.setColor(egg, entityageable);
            	
            	entity = new EntityItem(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, egg);
            }
            
            this.theWorld.spawnEntityInWorld(entity);
            
            Random random = this.theAnimal.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                double d4 = 0.5D + random.nextDouble() * (double)this.theAnimal.height;
                double d5 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
            }

            if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
            {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
            }
        }
 
    }
}
