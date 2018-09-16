package com.gendeathrow.hatchery.modaddons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.api.crafting.NestingPenDropRecipe;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.jei.nestingpen.NestingPenCategory;
import com.gendeathrow.hatchery.util.RegisterEggsUtil;
import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityChickensChicken;
import com.setycz.chickens.item.ItemSpawnEgg;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;

@Optional.InterfaceList(
		value = 
		{
				@Interface(iface = "com.setycz.chickens.ChickensMod", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.entity.EntityChickensChicken", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.item.ItemSpawnEgg", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.registry.ChickensRegistry", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.registry.ChickensRegistryItem", modid = "chickens")
		}
)

public class ChickensHelper {

	public static final String ChickensModID = "chickens";
	
	 
    @ObjectHolder("chickens:analyzer")
	public static Item chickenAnalyzer;
    
    
    @ObjectHolder("chickens:spawn_egg")
	public static Item spawnEgg;	
	
	public static boolean isLoaded() {
		return Loader.isModLoaded(ChickensModID);
	}
	
	/**
	 * Get all chicken stats
	 *  
	 * @param Entity
	 */
    @Nullable
    @Optional.Method(modid = ChickensModID)
    public static HashMap<String, Integer> getChickenStats(Entity entity){
    	
    	if(entity instanceof EntityChickensChicken) {
    		EntityChickensChicken chicken = (EntityChickensChicken) entity;

    		if (chicken.getStatsAnalyzed() || ChickensMod.instance.getAlwaysShowStats()) {
    			HashMap<String, Integer> list = new HashMap<String, Integer>();

    			list.put("entity.ChickensChicken.growth", chicken.getGrowth());    
    			list.put("entity.ChickensChicken.gain", chicken.getGain());
    			list.put("entity.ChickensChicken.strength", chicken.getStrength());          
    		
    			return list;
    		}
    	}
    	
    	return null;
    }
	
    @Optional.Method(modid = ChickensModID)
    public static boolean checkForAnalyzer(ItemStack stack, Entity entity) {
    	
    	if(chickenAnalyzer == null || stack.isEmpty() || entity == null) return false;
    	
		if(stack.getItem() == chickenAnalyzer) {	
			if(entity instanceof EntityChickensChicken) {
				EntityChickensChicken chicken = (EntityChickensChicken) entity;
				if(chicken.getStatsAnalyzed()) return false;
				
    	        chicken.setStatsAnalyzed(true);
    			return true;
    		} 
    	}
    	return false;
    }
    
    @Optional.Method(modid = ChickensModID)
    public static EntityLiving getChickenFromEgg(ItemStack spawnEgg, World worldIn, BlockPos pos) {
    	if(spawnEgg == null || worldIn == null) return null;
    	
        EntityChickensChicken entitychicken = new EntityChickensChicken(worldIn);
        entitychicken.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        entitychicken.rotationYawHead = entitychicken.rotationYaw;
        entitychicken.renderYawOffset = entitychicken.rotationYaw;
        entitychicken.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entitychicken)), (IEntityLivingData)null);
        entitychicken.setChickenType(ItemSpawnEgg.getTypeFromStack(spawnEgg));
        
		return entitychicken;
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
   
    
    
    
    public static List<IRecipeWrapper> getChickensModDropRecipes(NestingPenCategory nestingCat) {
    	if(ChickensHelper.isLoaded())
    		return getDropRecipes(nestingCat);
    	
    	return null;
    }
    
    
	@Optional.Method(modid = "chickens")
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
	
	@Optional.Method(modid = "chickens")
	public static int getChickensColor(String type)
	{
		if(ChickensRegistry.getByRegistryName(type) == null || FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) 
			return 0xdfce9b;
		
		if(ChickensRegistry.getByRegistryName(type).isDye())
		{
				return RegisterEggsUtil.getRGB(EnumDyeColor.byDyeDamage(ChickensRegistry.getByRegistryName(type).getDyeMetadata()).getColorComponentValues());
		}
		else return ChickensRegistry.getByRegistryName(type).getBgColor();
	}

}
