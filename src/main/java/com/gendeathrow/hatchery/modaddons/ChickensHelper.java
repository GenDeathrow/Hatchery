package com.gendeathrow.hatchery.modaddons;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.api.crafting.NestingPenDropRecipe;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.jei.fertilizermixer.FertilizerMixerRecipeWrapper;
import com.gendeathrow.hatchery.core.jei.nestingpen.NestingPenCategory;
import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.item.ItemSpawnEgg;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

@Optional.InterfaceList(
		value = 
		{
				@Interface(iface = "com.setycz.chickens.ChickensMod", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.entity.EntityChickensChicken", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.registry.ChickensRegistry", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.registry.ChickensRegistryItem", modid = "chickens")
		}
)

public class ChickensHelper {

	public static final String ChickensModID = "chickens";
	
	/**
	 * Is Chickens Mod Loadede
	 * @return
	 */
	public static boolean isLoaded() {
		return Loader.isModLoaded(ChickensModID);
	}
	
	/**
	 * Spawn a Chickens Chicken with a certain type. qq
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
   
    /**
     * Jei Helper, grabs all the Drop items for Chickens mod chickens.
     * @param nestingCat
     * @return
     */
    public static List<IRecipeWrapper> getChickensModDropRecipes(NestingPenCategory nestingCat) {
    	if(ChickensHelper.isLoaded())
    		return getDropRecipes(nestingCat);
    	return  new ArrayList<IRecipeWrapper>();
    }
    
    
	@Optional.Method(modid = ChickensModID)
    private static List<IRecipeWrapper> getDropRecipes(NestingPenCategory nestingCat) 
    {
     	List<IRecipeWrapper> recipes = new ArrayList<IRecipeWrapper>();
        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) 
        {
         	List<ItemStack> output = new ArrayList<ItemStack>();
        	output.add(chicken.createLayItem());
        	output.add(new ItemStack(ModItems.manure));
        	output.add(new ItemStack(Items.FEATHER));
        	output.add(new ItemStack(ModItems.hatcheryEgg));

        	EntityChickensChicken entity = new EntityChickensChicken(null);
        	entity.setChickenType(chicken.getRegistryName().toString());
        	
        	ItemStack spawnEgg = new ItemStack(ChickensMod.spawnEgg, 1);
        	ItemSpawnEgg.applyEntityIdToItemStack(spawnEgg, chicken.getRegistryName());
        	
            recipes.add(nestingCat.getRecipeWrapper(new NestingPenDropRecipe(entity, spawnEgg, output)));
        }
        
        return recipes;
    }

}
