package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.entities.EntityRooster;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber
public class ModEntities {

	public static void register() {
		
		EntityRegistry.registerModEntity(new ResourceLocation(Hatchery.MODID,"roosterentity"), EntityRooster.class, "Rooster", 1, Hatchery.INSTANCE, 120, 1, true, 0x592C00, 0xC10000);
	}
	
	
	public static void registerSpawns() {
		
	   	List<Biome> roosterSpawn = new ArrayList<Biome>();
		for (Biome biome : ForgeRegistries.BIOMES.getValues())
		{
			List<SpawnListEntry> list = biome.getSpawnableList(EnumCreatureType.CREATURE);
			for(SpawnListEntry item : list) {
				if(item.entityClass == EntityChicken.class) {
					roosterSpawn.add(biome);
				}
			}
		}
		
		EntityRegistry.addSpawn(EntityRooster.class, Settings.ROOSTER_SPAWN_PROBABILITY, Settings.ROOSTER_MIN_SPAWN_SIZE, Settings.ROOSTER_MAX_SPAWN_SIZE, EnumCreatureType.CREATURE, roosterSpawn.toArray(new Biome[roosterSpawn.size()]));
	}
	
}
