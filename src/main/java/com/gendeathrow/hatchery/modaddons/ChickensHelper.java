package com.gendeathrow.hatchery.modaddons;

import javax.annotation.Nullable;

import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

@Optional.InterfaceList(
		value = 
		{
				@Interface(iface = "com.setycz.chickens.entity.EntityChickensChicken", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.registry.ChickensRegistry", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.registry.ChickensRegistryItem", modid = "chickens")
		}
)
public class ChickensHelper {

	public static final String ChickensModID = "chickens";
	
	
	public static boolean isLoaded() {
		return Loader.isModLoaded(ChickensModID);
	}
	
	
	
	/**
	 * Spawn a Chickens Chicken with a certain type. 
	 * @param world
	 * @param pos
	 * @param registryName
	 */
    @Optional.Method(modid = ChickensModID)
    public static void spawnChickenType(World world, BlockPos pos, String registryName){
        EntityChickensChicken entitychicken = new EntityChickensChicken(world);
        entitychicken.setChickenType(registryName);
       	entitychicken.setGrowingAge(-24000);
      	entitychicken.setPosition(pos.getX() + .5 , pos.getY() + .5, pos.getZ() + .5);
       	world.spawnEntity(entitychicken);
    }
    
    
    
    /**
     * Gets a chicken of a certain dye type from an itemStack 
     * @param itemStack
     * @return
     */
    @Nullable
    @Optional.Method(modid = ChickensModID)
    public static String getDyeChickenfromItemStack(ItemStack itemStack) 
    {
        ChickensRegistryItem chicken = ChickensRegistry.findDyeChicken(itemStack.getMetadata());
        if (chicken == null)
            return null;
        return chicken.getRegistryName().toString();
    }
   
    
}
