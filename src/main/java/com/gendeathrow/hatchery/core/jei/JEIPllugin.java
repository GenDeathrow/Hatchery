package com.gendeathrow.hatchery.core.jei;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gendeathrow.hatchery.api.crafting.EggMachineRecipe;
import com.gendeathrow.hatchery.api.crafting.NestingPenDropRecipe;
import com.gendeathrow.hatchery.api.crafting.ShredderRecipe;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;
import com.gendeathrow.hatchery.core.config.ConfigLootHandler;
import com.gendeathrow.hatchery.core.config.ConfigLootHandler.ItemDrop;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.core.jei.eggmachine.EggMachineCategory;
import com.gendeathrow.hatchery.core.jei.eggmachine.EggMachineWrapper;
import com.gendeathrow.hatchery.core.jei.luckyegg.LuckyEggCategory;
import com.gendeathrow.hatchery.core.jei.luckyegg.LuckyEggWrapper;
import com.gendeathrow.hatchery.core.jei.nestingpen.NestingPenCategory;
import com.gendeathrow.hatchery.core.jei.shredder.ShredderCategory;
import com.gendeathrow.hatchery.modaddons.ChickensHelper;

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
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;


@JEIPlugin
public class JEIPllugin implements IModPlugin 
{
	IJeiHelpers jeiHelpers;
	IGuiHelper guiHelper;
	IIngredientRegistry ingredientRegistry;
	
	private ShredderCategory shredderCat;
	private NestingPenCategory nestingCat;
	private EggMachineCategory eggmachineCat;
	private LuckyEggCategory luckyEggCat;

		@Override
    public void register (IModRegistry registry) 
    {

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
        
        registry.addRecipeCatalyst(new ItemStack(ModItems.chickenmachine), EggMachineCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.shredder), ShredderCategory.UID);
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.pen), NestingPenCategory.UID);
                 
        registry.handleRecipes(ShredderRecipe.class, shredderCat, ShredderCategory.UID);
        registry.addRecipes(getShredderRecipes(), ShredderCategory.UID);
        
        //Nesting Pens
        registry.handleRecipes(NestingPenDropRecipe.class, nestingCat, NestingPenCategory.UID);
        if(ChickensHelper.isLoaded())
        	registry.addRecipes(ChickensHelper.getChickensModDropRecipes(nestingCat), NestingPenCategory.UID);
        registry.addRecipes(getVanillaDropRecipe(), NestingPenCategory.UID);
        
        //Egg Machine
        registry.handleRecipes(EggMachineRecipe.class, eggmachineCat, EggMachineCategory.UID);
        	ArrayList<IRecipeWrapper> wrap = new ArrayList<IRecipeWrapper>();
        	wrap.add(new EggMachineWrapper(new EggMachineRecipe()));
        registry.addRecipes(wrap, EggMachineCategory.UID);
        
        registry.addRecipes(getLuckyEggs(), LuckyEggCategory.UID);
        
        
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
		guiHelper = registry.getJeiHelpers().getGuiHelper();
		
		//		
		registry.addRecipeCategories(nestingCat = new NestingPenCategory(guiHelper));
		registry.addRecipeCategories(shredderCat = new ShredderCategory(guiHelper));
		registry.addRecipeCategories(luckyEggCat = new LuckyEggCategory(guiHelper));
		registry.addRecipeCategories(eggmachineCat = new EggMachineCategory(guiHelper));
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
	
	private List<LuckyEggWrapper> getLuckyEggs()
	{
		List<LuckyEggWrapper> recipes = new ArrayList<LuckyEggWrapper>();
		
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
	
	private List<IRecipeWrapper> getShredderRecipes()
	{
		List<IRecipeWrapper > recipes = new ArrayList<IRecipeWrapper>();
		
		for(ShredderRecipe recipe : ShredderTileEntity.shredderRecipes)
			recipes.add(shredderCat.getRecipeWrapper(recipe));
			
		return recipes;
	}
	
	private List<IRecipeWrapper> getVanillaDropRecipe()
	{
        ResourceLocation name = EntityList.getKey(EntityChicken.class);
        List<IRecipeWrapper> recipes = new ArrayList<IRecipeWrapper>();
        if (EntityList.ENTITY_EGGS.containsKey(name))
        {
            ItemStack chicken = new ItemStack(net.minecraft.init.Items.SPAWN_EGG);
            net.minecraft.item.ItemMonsterPlacer.applyEntityIdToItemStack(chicken, name);

            List<ItemStack> output = new ArrayList<ItemStack>();
    	
            output.add(new ItemStack(Items.EGG));
            output.add(new ItemStack(ModItems.manure));
            output.add(new ItemStack(Items.FEATHER));
            
            recipes.add(nestingCat.getRecipeWrapper(new NestingPenDropRecipe(new EntityChicken(null), chicken, output)));
            
        }
        return recipes;
	}
	

}
