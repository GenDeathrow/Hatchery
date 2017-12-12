package com.gendeathrow.hatchery.core.jei;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity.ShredderRecipe;
import com.gendeathrow.hatchery.core.config.ConfigLootHandler;
import com.gendeathrow.hatchery.core.config.ConfigLootHandler.ItemDrop;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.jei.old.eggmachine.LuckyEggWrapper;
import com.gendeathrow.hatchery.core.jei.old.nestingpen.NestingPenDropRecipeWrapper;
import com.gendeathrow.hatchery.core.jei.old.shredder.ShredderRecipeWrapper;
import com.gendeathrow.hatchery.core.jei.shredder.ShredderCategory;
import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.oredict.OreDictionary;


@Optional.InterfaceList({
	@Optional.Interface(iface = "com.setycz.chickens.ChickensMod", modid = "chickens"),
	@Optional.Interface(iface = "com.setycz.chickens.registry.ChickensRegistry", modid = "chickens"),
	@Optional.Interface(iface = "com.setycz.chickens.registry.ChickensRegistryItem", modid = "chickens")

})


@JEIPlugin
public class JEIPllugin implements IModPlugin 
{
	
    
		IJeiHelpers jeiHelpers;
		IGuiHelper guiHelper;
		IIngredientRegistry ingredientRegistry;
	
	
    
	@Override
    public void register (IModRegistry registry) 
    {
		System.out.println("REGISTER JEI");
		System.out.println("REGISTER JEI");
		System.out.println("REGISTER JEI");
		System.out.println("REGISTER JEI");
		System.out.println("REGISTER JEI");
		System.out.println("REGISTER JEI");
    	jeiHelpers = registry.getJeiHelpers();
    	guiHelper = jeiHelpers.getGuiHelper();
    	ingredientRegistry = registry.getIngredientRegistry();
    		
        for (Block block : ModBlocks.BLOCKS) {
            //registry.addIngredientInfo(block, block.getClass(), "jei." + block.getUnlocalizedName());
        	ItemStack stack =  new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE);
        	if(!stack.isEmpty()) registry.addDescription(stack, "jei." + block.getUnlocalizedName());
        }
        
        for (Item item : ModItems.ITEMS) {
        	ItemStack stack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
        	if(!stack.isEmpty()) registry.addDescription(stack, "jei." + item.getUnlocalizedName());
        }
            
        registry.handleRecipes(ShredderCategory.class, new ShredderCategory(guiHelper), ShredderCategory.UID);
//        registry.addRecipeHandlers(new NestingPenDropRecipeHandler());
//        List<NestingPenDropRecipeWrapper> recipes = new ArrayList<NestingPenDropRecipeWrapper>();
//        recipes = getVanillaDropRecipe(recipes);
//     
//        if(Loader.isModLoaded("chickens"));
//        	recipes = getDropRecipes(recipes);
//        
//        registry.addRecipes(recipes);
//        
//        // Shredder Recipes
//		registry.addRecipeCategories(new ShredderCategory(guiHelper));
//        registry.addRecipeHandlers(new ShredderRecipeHandler());
//            List<ShredderRecipeWrapper> shredderRecipes = new ArrayList<ShredderRecipeWrapper>();
//        	shredderRecipes = this.getShredderRecipes(shredderRecipes);
//        		registry.addRecipes(shredderRecipes);
//        
//        // Lucky Egg Prizes
//        registry.addRecipeCategories(new LuckyEggCategory(guiHelper));
//        registry.addRecipeHandlers(new LuckyEggHandler());
//        	List<LuckyEggWrapper> luckyeggsdrops = new ArrayList<LuckyEggWrapper>();
//        	luckyeggsdrops = this.getLuckyEggs(luckyeggsdrops);
//        		registry.addRecipes(luckyeggsdrops);
//        
//        // EggMachine
//        registry.addRecipeCategories(new EggMachineCategory(guiHelper));
//        registry.addRecipeHandlers(new EggMachineRecipeHandler());
//        	ArrayList<EggMachineWrapper> luckyegg = new ArrayList<EggMachineWrapper>();
//        	luckyegg.add(new EggMachineWrapper());
//        		registry.addRecipes(luckyegg);
//        
//        	
//        //Generator
//        registry.addRecipeCategories(new GeneratorCategory(guiHelper));
//        registry.addRecipeHandlers(new GeneratorRecipeHandler());  	
//        	ArrayList<GeneratorRecipeWrapper> generator = new ArrayList<GeneratorRecipeWrapper>();
//        	generator.add(new GeneratorRecipeWrapper());
//    			registry.addRecipes(generator);
//    		
//    	//FertilizerMixer
//        registry.addRecipeCategories(new FertilizerMixerCategory(guiHelper));
//        registry.addRecipeHandlers(new FertilizerMixerRecipeHandler());  	
//          	ArrayList<FertilizerMixerRecipeWrapper> mixer = new ArrayList<FertilizerMixerRecipeWrapper>();
//          	mixer.add(new FertilizerMixerRecipeWrapper(new ItemStack(ModItems.manure)));
//          	mixer.add(new FertilizerMixerRecipeWrapper(new ItemStack(ModBlocks.manureBlock)));
//           	registry.addRecipes(mixer);    		
//        		
//         IItemBlacklist itemBlacklist = registry.getJeiHelpers().getItemBlacklist();
//        
//        itemBlacklist.addItemToBlacklist(new ItemStack(ModBlocks.pen_chicken));
//        itemBlacklist.addItemToBlacklist(new ItemStack(ModBlocks.digesterGeneratorOn));
//        itemBlacklist.addItemToBlacklist(new ItemStack(ModBlocks.chickenMachine));
//        
//        
//        
//        registry.addRecipeCategoryCraftingItem(new ItemStack(ModItems.chickenmachine), EggMachineCategory.UID);
//        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.pen), NestingPenCategory.UID);
//        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.shredder), ShredderCategory.UID);
//        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.digesterGenerator), GeneratorCategory.UID);
//        registry.addRecipeCategoryCraftingItem(new ItemStack(ModBlocks.fertilizerMixer), FertilizerMixerCategory.UID);
//        
     }
    
    
	Map<Class, HRecipeCategory> categories = new LinkedHashMap<>();
	
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
//		
//		registry.addRecipeCategories(new NestingPenCategory(guiHelper));
//		registry.addRecipeCategories(new ShredderCategory(guiHelper));
//		registry.addRecipeCategories(new LuckyEggCategory(guiHelper));
//		registry.addRecipeCategories(new EggMachineCategory(guiHelper));
//		registry.addRecipeCategories(new EggMachineCategory(guiHelper));
//		registry.addRecipeCategories(new GeneratorCategory(guiHelper));
//		registry.addRecipeCategories(new FertilizerMixerCategory(guiHelper));
	}


	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0) 
	{
		
	}

	@Override
	public void registerIngredients(IModIngredientRegistration arg0) 
	{
		
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry arg0) 
	{
		
	}
	
	private List<LuckyEggWrapper> getLuckyEggs(List<LuckyEggWrapper> recipes)
	{
		List<ItemStack> droplist = new ArrayList<ItemStack>();
		
		Iterator<ItemDrop> itr = ConfigLootHandler.drops.iterator();
		
		
		while(itr.hasNext())
		{
			ItemDrop item = itr.next();
			droplist.add(item.getItemStack());
			if(droplist.size() == 36 || !itr.hasNext()) {
				recipes.add(new LuckyEggWrapper(droplist));
				droplist.clear();
			}
		}
			
		return recipes;
	}
	
	private List<ShredderRecipeWrapper> getShredderRecipes(List<ShredderRecipeWrapper> recipes)
	{
		for(ShredderRecipe recipe : ShredderTileEntity.shredderRecipes)
		{
			recipes.add(new ShredderRecipeWrapper(recipe));
		}
			
		return recipes;
	}
	
	private List<NestingPenDropRecipeWrapper> getVanillaDropRecipe(List<NestingPenDropRecipeWrapper> recipes)
	{
        ResourceLocation name = EntityList.getKey(EntityChicken.class);
        
        if (EntityList.ENTITY_EGGS.containsKey(name))
        {
            ItemStack chicken = new ItemStack(net.minecraft.init.Items.SPAWN_EGG);
            net.minecraft.item.ItemMonsterPlacer.applyEntityIdToItemStack(chicken, name);

            List<ItemStack> output = new ArrayList<ItemStack>();
    	
            output.add(new ItemStack(Items.EGG));
            output.add(new ItemStack(ModItems.manure));
            output.add(new ItemStack(Items.FEATHER));
            
            recipes.add(new NestingPenDropRecipeWrapper(chicken, output));
            
        }
        return recipes;
	}
	
	@Optional.Method(modid = "chickens")
    private List<NestingPenDropRecipeWrapper> getDropRecipes(List<NestingPenDropRecipeWrapper> recipes) 
    {

        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) 
        {
         	List<ItemStack> output = new ArrayList();
        	output.add(chicken.createLayItem());
        	output.add(new ItemStack(ModItems.manure));
        	output.add(new ItemStack(Items.FEATHER));
        	output.add(new ItemStack(ModItems.hatcheryEgg));

        	ItemStack spawnEgg = new ItemStack(ChickensMod.spawnEgg, 1);
        	
            recipes.add(new NestingPenDropRecipeWrapper(spawnEgg, output));
        }
        
        return recipes;
    }


}
