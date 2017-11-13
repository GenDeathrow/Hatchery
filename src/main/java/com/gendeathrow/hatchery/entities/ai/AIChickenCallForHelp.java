package com.gendeathrow.hatchery.entities.ai;

import com.gendeathrow.hatchery.entities.EntityRooster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.math.AxisAlignedBB;

public class AIChickenCallForHelp extends EntityAIHurtByTarget
{
        public AIChickenCallForHelp(EntityChicken p_i45828_1_)
        {
            super(p_i45828_1_, true, new Class[0]);
        }
        
        protected void setEntityAttackTarget(EntityRooster rooster, EntityLivingBase target)
        {
        	rooster.setAngryTarget(target);
        }
        
        @Override
        protected void alertOthers()
        {
            double d0 = this.getTargetDistance();
            for (EntityRooster entitycreature : this.taskOwner.world.getEntitiesWithinAABB(EntityRooster.class, (new AxisAlignedBB(this.taskOwner.getPosition()).grow(d0, 10.0D, d0))))
            {
                if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getRevengeTarget()))
                {
                        this.setEntityAttackTarget(entitycreature, this.taskOwner.getRevengeTarget());
                }
            }
        }
}
