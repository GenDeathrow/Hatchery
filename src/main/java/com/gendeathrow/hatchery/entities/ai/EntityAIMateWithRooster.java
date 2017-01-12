package com.gendeathrow.hatchery.entities.ai;

import java.util.List;
import java.util.Random;

import com.gendeathrow.hatchery.entities.EntityRooster;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class EntityAIMateWithRooster extends EntityAIBase {
	private final EntityAnimal theAnimal;
	World theWorld;
	private EntityAnimal targetMate;
	int spawnBabyDelay;
	double moveSpeed;

	public EntityAIMateWithRooster(EntityAnimal animal, double speedIn) {
		theAnimal = animal;
		theWorld = animal.worldObj;
		moveSpeed = speedIn;
		setMutexBits(3);
	}

	public boolean shouldExecute() {
		if (!theAnimal.isInLove()) {
			return false;
		} else {
			targetMate = getNearbyMate();
			return targetMate != null;
		}
	}

	public boolean continueExecuting() {
		return targetMate.isEntityAlive() && targetMate.isInLove() && spawnBabyDelay < 60;
	}

	public void resetTask() {
		targetMate = null;
		spawnBabyDelay = 0;
	}

	public void updateTask() {
		theAnimal.getLookHelper().setLookPositionWithEntity(targetMate, 10.0F, (float) theAnimal.getVerticalFaceSpeed());
		theAnimal.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
		++spawnBabyDelay;
		if (spawnBabyDelay >= 60 && theAnimal.getDistanceSqToEntity(targetMate) < 9.0D) {
			NBTTagCompound nbt = new NBTTagCompound();
			theAnimal.writeEntityToNBT(nbt);
			if (Loader.isModLoaded("chickens") && nbt.hasKey("Type")) {
				int type = nbt.getInteger("Type");
				spawnEgg(type);
			}
			else
				spawnBaby();
		}
	}

	private void spawnEgg(int type) {
		ItemStack mobEgg = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("chickens:spawn_egg")), 1, type);
		theAnimal.entityDropItem(mobEgg, 0.0F);
		theAnimal.setGrowingAge(6000);
		targetMate.setGrowingAge(6000);
		theAnimal.resetInLove();
		targetMate.resetInLove();
		return;
	}

	private EntityAnimal getNearbyMate() {
		List<EntityAnimal> list = theWorld.<EntityAnimal>getEntitiesWithinAABB(EntityRooster.class, theAnimal.getEntityBoundingBox().expandXyz(8.0D));
		double d0 = Double.MAX_VALUE;
		EntityAnimal entityanimal = null;

		for (EntityAnimal entityanimal1 : list) {
			if (theAnimal.getDistanceSqToEntity(entityanimal1) < d0 && entityanimal1.isInLove()) {
				entityanimal = entityanimal1;
				d0 = theAnimal.getDistanceSqToEntity(entityanimal1);
			}
		}

		return entityanimal;
	}

	private void spawnBaby() {
		EntityAgeable entityageable = theAnimal.createChild(theAnimal);

		final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(theAnimal, targetMate, entityageable);
		final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
		entityageable = event.getChild();
		if (cancelled) {
			// Reset the "inLove" state for the animals
			theAnimal.setGrowingAge(6000);
			targetMate.setGrowingAge(6000);
			theAnimal.resetInLove();
			targetMate.resetInLove();
			return;
		}

		if (entityageable != null) {
			EntityPlayer entityplayer = theAnimal.getPlayerInLove();

			if (entityplayer == null && targetMate.getPlayerInLove() != null) {
				entityplayer = targetMate.getPlayerInLove();
			}

			if (entityplayer != null) {
				entityplayer.addStat(StatList.ANIMALS_BRED);

				if (theAnimal instanceof EntityCow) {
					entityplayer.addStat(AchievementList.BREED_COW);
				}
			}

			theAnimal.setGrowingAge(6000);
			targetMate.setGrowingAge(6000);
			theAnimal.resetInLove();
			targetMate.resetInLove();
			entityageable.setGrowingAge(-24000);
			entityageable.setLocationAndAngles(theAnimal.posX, theAnimal.posY, theAnimal.posZ, 0.0F, 0.0F);
			theWorld.spawnEntityInWorld(entityageable);
			Random random = theAnimal.getRNG();

			for (int i = 0; i < 7; ++i) {
				double d0 = random.nextGaussian() * 0.02D;
				double d1 = random.nextGaussian() * 0.02D;
				double d2 = random.nextGaussian() * 0.02D;
				double d3 = random.nextDouble() * (double) theAnimal.width * 2.0D - (double) theAnimal.width;
				double d4 = 0.5D + random.nextDouble() * (double) theAnimal.height;
				double d5 = random.nextDouble() * (double) theAnimal.width * 2.0D - (double) theAnimal.width;
				theWorld.spawnParticle(EnumParticleTypes.HEART, theAnimal.posX + d3, theAnimal.posY + d4, theAnimal.posZ + d5, d0, d1, d2, new int[0]);
			}

			if (theWorld.getGameRules().getBoolean("doMobLoot")) {
				theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, theAnimal.posX, theAnimal.posY, theAnimal.posZ, random.nextInt(7) + 1));
			}
		}
	}
}
