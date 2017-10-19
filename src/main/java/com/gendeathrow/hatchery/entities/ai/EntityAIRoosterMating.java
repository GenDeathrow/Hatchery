package com.gendeathrow.hatchery.entities.ai;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.entities.EntityRooster;
import com.gendeathrow.hatchery.item.HatcheryEgg;
import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

public class EntityAIRoosterMating extends EntityAIBase {
	private final EntityRooster roosterEntity;
	World theWorld;
	private EntityAnimal targetChicken;
	int spawnBabyDelay;
	double moveSpeed;

	public EntityAIRoosterMating(EntityRooster animal, double speedIn) {
		roosterEntity = animal;
		theWorld = animal.worldObj;
		moveSpeed = speedIn;
		setMutexBits(3);
	}

	public boolean shouldExecute() {
		if (roosterEntity.getGrowingAge() != 0 || roosterEntity.getSeeds() < 2) {
			return false;
		} else {
			targetChicken = getNearbyMate();
			return targetChicken != null;
		}
	}

	@Override	
	public void startExecuting() {
		if (targetChicken.getGrowingAge() == 0) {
			ItemStack stack = new ItemStack(Items.WHEAT_SEEDS);
			EntityPlayer player = getPlayer(theWorld);
			player.setHeldItem(EnumHand.MAIN_HAND, stack);
			rightClickItemAt(player, EnumHand.MAIN_HAND, stack);
		}
	}

	@Override
	public boolean continueExecuting() {
		return targetChicken.isEntityAlive() && targetChicken.isInLove() && spawnBabyDelay < 60;
	}

	public void resetTask() {
		targetChicken = null;
		spawnBabyDelay = 0;
	}

	public void updateTask() 
	{
		roosterEntity.getLookHelper().setLookPositionWithEntity(targetChicken, 10.0F, (float) roosterEntity.getVerticalFaceSpeed());
		roosterEntity.getNavigator().tryMoveToEntityLiving(targetChicken, moveSpeed);
		++spawnBabyDelay;
		if (spawnBabyDelay >= 60 && roosterEntity.getDistanceSqToEntity(targetChicken) < 9.0D) 
		{
			spawnChild();
		}
	}

	public boolean rightClickItemAt(EntityPlayer player, EnumHand hand, @Nullable ItemStack stack) 
	{
		if (theWorld.isRemote || stack == null || stack.getItem() == null)
			return false;
		try {
			return targetChicken.processInteract(player, EnumHand.MAIN_HAND, player.getHeldItemMainhand());
		} finally {
			roosterEntity.processInteract(player, EnumHand.MAIN_HAND, player.getHeldItemMainhand());
			roosterEntity.setSeeds(roosterEntity.getSeeds() - 2);
		}
	}

	public static EntityPlayer getPlayer(World world) {
		if (world.isRemote || !(world instanceof WorldServer))
			return null;
		return FakePlayerFactory.get((WorldServer) world, new GameProfile(UUID.nameUUIDFromBytes("rooster".getBytes()), "rooster"));
	}

	 private void spawnChild()
	    {
	        EntityAgeable entityageable = this.targetChicken.createChild(this.targetChicken);

	        if (entityageable != null)
	        {
	            EntityPlayer entityplayer = this.targetChicken.getPlayerInLove();


	            this.roosterEntity.setGrowingAge(100);
	            this.targetChicken.setGrowingAge(6000);
	            this.roosterEntity.resetInLove();
	            this.targetChicken.resetInLove();
	            
	            entityageable.setGrowingAge(-24000);
	            entityageable.setLocationAndAngles(this.targetChicken.posX, this.targetChicken.posY, this.targetChicken.posZ, 0.0F, 0.0F);
	            
	    		final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(roosterEntity, targetChicken, entityageable);
	    		final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
	    		entityageable = event.getChild();
	    		if (cancelled) 
	    		{
	    			// Reset the "inLove" state for the animals
	    			roosterEntity.setGrowingAge(0);
	    			targetChicken.setGrowingAge(6000);
	    			roosterEntity.resetInLove();
	    			targetChicken.resetInLove();
	    			return;
	    		}

	    		
	            
	            if(Settings.IS_EGG_BREEDING)
	            {
	            	ItemStack egg = new ItemStack(ModItems.hatcheryEgg, 1, 0);
	            
	            	ItemStackEntityNBTHelper.addEntitytoItemStack(egg, entityageable);

	            	egg.setStackDisplayName(entityageable.getDisplayName().getFormattedText() +" Egg");
	            
	            	HatcheryEgg.setColor(egg, entityageable);
	        	
	            	EntityItem entityItem = new EntityItem(this.theWorld, this.targetChicken.posX, this.targetChicken.posY, this.targetChicken.posZ, egg);
	            
	            	this.theWorld.spawnEntityInWorld(entityItem);
	            }
	            else this.theWorld.spawnEntityInWorld(entityageable);
	            
            	Random random = this.targetChicken.getRNG();

            	for (int i = 0; i < 7; ++i)
            	{
            		double d0 = random.nextGaussian() * 0.02D;
            		double d1 = random.nextGaussian() * 0.02D;
            		double d2 = random.nextGaussian() * 0.02D;
            		double d3 = random.nextDouble() * (double)this.targetChicken.width * 2.0D - (double)this.targetChicken.width;
            		double d4 = 0.5D + random.nextDouble() * (double)this.targetChicken.height;
            		double d5 = random.nextDouble() * (double)this.targetChicken.width * 2.0D - (double)this.targetChicken.width;
            		this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.targetChicken.posX + d3, this.targetChicken.posY + d4, this.targetChicken.posZ + d5, d0, d1, d2, new int[0]);
            	}
            
	            
            	if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
            	{
            		this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.roosterEntity.posX, this.roosterEntity.posY, this.roosterEntity.posZ, random.nextInt(7) + 1));
            	}
	        }
	 
	    }
	
	
	private EntityAnimal getNearbyMate() 
	{
		List<EntityAnimal> list = theWorld.<EntityAnimal>getEntitiesWithinAABB(EntityChicken.class, roosterEntity.getEntityBoundingBox().expandXyz(8.0D));
		double d0 = Double.MAX_VALUE;
		EntityAnimal entityanimal = null;

		for (EntityAnimal entityanimal1 : list) 
		{
			if (roosterEntity.canEntityBeSeen(entityanimal1) && roosterEntity.getDistanceSqToEntity(entityanimal1) < d0 && !(entityanimal1 instanceof EntityRooster) && (entityanimal1.isInLove() || entityanimal1.getGrowingAge() == 0)) 
			{
				entityanimal = entityanimal1;
				d0 = roosterEntity.getDistanceSqToEntity(entityanimal1);
			}
		}
		return entityanimal;
	}
//
//	private void spawnBaby() {
//		EntityAgeable entityageable = targetChicken.createChild(targetChicken);
//
//		final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(roosterEntity, targetChicken, entityageable);
//		final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
//		entityageable = event.getChild();
//		if (cancelled) {
//			// Reset the "inLove" state for the animals
//			roosterEntity.setGrowingAge(0);
//			targetChicken.setGrowingAge(6000);
//			roosterEntity.resetInLove();
//			targetChicken.resetInLove();
//			return;
//		}
//
//		if (entityageable != null) {
//			EntityPlayer entityplayer = roosterEntity.getPlayerInLove();
//
//			if (entityplayer == null && targetChicken.getPlayerInLove() != null) {
//				entityplayer = targetChicken.getPlayerInLove();
//			}
//
//			if (entityplayer != null) {
//				entityplayer.addStat(StatList.ANIMALS_BRED);
//			}
//
//			roosterEntity.setGrowingAge(0);
//			targetChicken.setGrowingAge(6000);
//			roosterEntity.resetInLove();
//			targetChicken.resetInLove();
//			entityageable.setGrowingAge(-24000);
//			entityageable.setLocationAndAngles(targetChicken.posX, targetChicken.posY, targetChicken.posZ, 0.0F, 0.0F);
//			theWorld.spawnEntityInWorld(entityageable);
//			Random random = roosterEntity.getRNG();
//
//			for (int i = 0; i < 7; ++i) {
//				double d0 = random.nextGaussian() * 0.02D;
//				double d1 = random.nextGaussian() * 0.02D;
//				double d2 = random.nextGaussian() * 0.02D;
//				double d3 = random.nextDouble() * (double) roosterEntity.width * 2.0D - (double) roosterEntity.width;
//				double d4 = 0.5D + random.nextDouble() * (double) roosterEntity.height;
//				double d5 = random.nextDouble() * (double) roosterEntity.width * 2.0D - (double) roosterEntity.width;
//				theWorld.spawnParticle(EnumParticleTypes.HEART, roosterEntity.posX + d3, roosterEntity.posY + d4, roosterEntity.posZ + d5, d0, d1, d2, new int[0]);
//			}
//
//			if (theWorld.getGameRules().getBoolean("doMobLoot")) {
//				theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, roosterEntity.posX, roosterEntity.posY, roosterEntity.posZ, random.nextInt(7) + 1));
//			}
//		}
//	}
}
