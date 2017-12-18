package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.CraftingHelper.ShapedPrimer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber
public class ModRecipes 
{
	
	public static IForgeRegistry<IRecipe> recipeRegistry;
	public static List<IRecipe> RECIPELIST = new ArrayList<IRecipe>();
	
	public static void RegisterOreDic()
	{
		OreDictionary.registerOre("dirt", ModBlocks.fertlizedDirt);
		OreDictionary.registerOre("fertilizedDirt", ModBlocks.fertlizedDirt);
		OreDictionary.registerOre("manure", ModItems.manure);
		OreDictionary.registerOre("manureBlock", ModBlocks.manureBlock);
		OreDictionary.registerOre("egg", ModItems.hatcheryEgg);
		OreDictionary.registerOre("listAllegg", ModItems.hatcheryEgg);
		OreDictionary.registerOre("plasticEgg", ModItems.prizeEgg);
	}
	
	
	public static void initRecipes() {
	
		//Animal Net
		addShapedOreRecipe("net_recipe", "held", new ItemStack(ModItems.animalNet), new Object[] {
			" SS",
            "SAA",
            "  A",
            'S', "stickWood",  
            'A', "string",  
	    });
    
		//Lucky Egg Machine
		addShapedOreRecipe("luckyegg_machine_recipe", "machine", new ItemStack(ModItems.chickenmachine), new Object[] {
            " W ",
            "WEW",
            "ICI",
            'W', "plankWood",   // can use ordinary items, blocks, itemstacks in ShapedOreRecipe
            'E', "egg",
            'I', Blocks.IRON_BLOCK,
            'C', ModItems.circuitBoard, // look in OreDictionary for vanilla definitions
	    });
    
		// NestingPen
		addShapedOreRecipe("nestingpen_recipe", "block", new ItemStack(ModBlocks.pen), new Object[] {	    
    		"W W", 
	    	"WNW", 
	    	"WWW",
	    	'W', "plankWood", 
	    	'N', ModBlocks.nest
		});
	 
		//Fertilized Dirt
		addShapedOreRecipe("dirt_recipe", "block", new ItemStack(ModBlocks.fertlizedDirt), new Object[] {
			"PPP", 
			"PDP", 
			"PPP",
			'P', "manure",
			'D', "dirt"
		});

		//Feeder
		addShapedOreRecipe("feeder_recipe", "machine", new ItemStack(ModBlocks.feeder), new Object[] {
			" I ",
			"SBS",
			"SSS",
			'I', "ingotIron",
			'B', "blockIron",
			'S', "slabWood"
		});
		
		// Nursery Block
		addShapedOreRecipe("nursery_block_recipe", "machine", new ItemStack(ModBlocks.nuseryBlock), new Object[] {
		    	"III", 
		    	"IPI", 
		    	"SSS",
		    	'I', "ingotIron",
		    	'P', Blocks.PISTON,
		    	'S', "slabWood"
			});
		
		// Sprayer
		addShapedOreRecipe("nest_recipe", "held", new ItemStack(ModItems.sprayer), new Object[] {
		    	"DD ", 
		    	" IB", 
		    	"IGI",
		    	'I', "ingotIron",
		    	'G', ModFluids.getFertilizerBucket(),
		    	'D', "ingotIron",
		    	'B',  new ItemStack(Blocks.STONE_BUTTON)
		    	
			});
		
		addShapedRecipes("nest_recipe", "held",  new ItemStack(ModBlocks.nest), new Object[] { "   ", "A A", " A ",'A', Blocks.HAY_BLOCK});		
		
		addShapedRecipes("manureitem_to_block", "held",  new ItemStack(ModBlocks.manureBlock), new Object[] { "XXX", "XXX", "XXX",'X', ModItems.manure});		
		
		addShapelessRecipes("manureblock_to_item", "held", new ItemStack(ModItems.manure,9),  new ItemStack(ModBlocks.manureBlock));

		addShapedRecipes("digeter_gen_recipe", "machine", new ItemStack(ModBlocks.digesterGenerator), new Object[] {
				"III",
				"PRB",
				"I I", 
				'I', Items.IRON_INGOT,
				'R', Blocks.REDSTONE_BLOCK,
				'P', Blocks.PISTON, 
				'B', Items.BUCKET
			});
		
		
		addShapedRecipes("mixer_recipe", "machine", new ItemStack(ModBlocks.fertilizerMixer), new Object[] {
				" H ",
				"IPI",
				"IBI", 
				'H', Blocks.HOPPER, 
				'I', Items.IRON_INGOT, 
				'P', Blocks.PISTON, 
				'B', Items.BUCKET
			});
		
		// UPGRADES
		addShapedRecipes("rfupgrade_1_recipe", "upgrade", new ItemStack(ModItems.rfUpgradeTier), new Object[] {
				"TRT",
				"RGR",
				"TRT",
				'T', Blocks.REDSTONE_TORCH,
				'R', Items.REDSTONE,
				'G', ModItems.circuitBoard
			});
		
		addShapedRecipes("rfupgrade_2_recipe", "upgrade",new ItemStack(ModItems.rfUpgradeTier, 1, 1), new Object[] {
				"SXS",
				"RUR",
				"GXG",
				'S', Items.GLOWSTONE_DUST,
				'U', ModItems.rfUpgradeTier,
				'R', Items.REDSTONE,
				'X', ModItems.circuitBoard,
				'G', Items.GOLD_INGOT
			});
		
		addShapedRecipes("rfupgrade_3_recipe", "upgrade",new ItemStack(ModItems.rfUpgradeTier, 1, 2), new Object[] {
				"EXD",
				"XUX",
				"GXG",
				'E', Items.EMERALD,
				'D', Items.DIAMOND,
				'U', new ItemStack(ModItems.rfUpgradeTier,1,1),
				'X', ModItems.circuitBoard,
				'G', Blocks.GOLD_BLOCK
			});
		
		//RF CAPACITY UPGRADE
		
		addShapedRecipes("rfcapupgrade_1_recipe", "upgrade",new ItemStack(ModItems.rfCapacityUpgradeTier1), new Object[] {
				"XRX",
				"XGX",
				"XTX",
				'T', Blocks.REDSTONE_TORCH,
				'R', Items.REDSTONE,
				'X', Blocks.HARDENED_CLAY,
				'G', ModItems.circuitBoard
			});
		
		
		addShapedRecipes("rfcapupgrade_2_recipe", "upgrade",new ItemStack(ModItems.rfCapacityUpgradeTier1, 1, 1), new Object[] {
				"BTB",
				"RGR",
				"TCT",
				'T', Items.ENDER_PEARL,
				'R', Blocks.REDSTONE_BLOCK,
				'C', Blocks.HARDENED_CLAY,
				'B', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.rfCapacityUpgradeTier1)
			});
		
		
		addShapedRecipes("rfcapupgrade_3_recipe", "upgrade",new ItemStack(ModItems.rfCapacityUpgradeTier1, 1, 2), new Object[] {
				"TBT",
				"RGR",
				"TCT",
				'T', Items.CHORUS_FRUIT,
				'R', Blocks.REDSTONE_BLOCK,
				'C', Blocks.PRISMARINE,
				'B', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.rfCapacityUpgradeTier1, 1, 1)
			});
		
//		//Tank CAPACITY UPGRADE
		
		addShapedRecipes("tank_1_recipe", "upgrade",new ItemStack(ModItems.tankUpgradeTier1), new Object[] {
				"BBB",
				"BGB",
				"BBB",
				'B', Items.BUCKET,
				'G', ModItems.circuitBoard
			});

		addShapedRecipes("tank_2_recipe", "upgrade",new ItemStack(ModItems.tankUpgradeTier1, 1, 1), new Object[] {
				"IBI",
				"OGO",
				"CCC",
				'C', Blocks.IRON_BLOCK,
				'B', Items.BUCKET,
				'I', Items.IRON_INGOT,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.tankUpgradeTier1, 1, 0)
			});
		
		addShapedRecipes("tank_3_recipe", "upgrade", new ItemStack(ModItems.tankUpgradeTier1, 1, 2), new Object[] {
				"BCB",
				"BGB",
				"OCO",
				'B', Blocks.IRON_BLOCK,
				'C', Items.CAULDRON,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.tankUpgradeTier1,1,1)
			});

//		//Speed UPGRADE
		
		
		addShapedRecipes("speed_1_recipe", "upgrade",new ItemStack(ModItems.speedUpgradeTier), new Object[] {
				"FRF",
				"NGN",
				"FRF",
				'F', Items.FEATHER,
				'N', Items.GOLD_NUGGET,
				'R', Items.REDSTONE,
				'G', ModItems.circuitBoard
			});
		
		addShapedRecipes("speed_2_recipe", "upgrade",new ItemStack(ModItems.speedUpgradeTier, 1, 1), new Object[] {
				"OCO",
				"GRG",
				"QCQ",
				'Q', Items.QUARTZ,
				'R', Blocks.REDSTONE_BLOCK,
				'C', Items.REDSTONE,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.speedUpgradeTier)
			});
		
		addShapedRecipes("speed_3_recipe", "upgrade",new ItemStack(ModItems.speedUpgradeTier, 1, 2), new Object[] {
				"OFO",
				"GRG",
				"QOQ",
				'Q', Blocks.QUARTZ_BLOCK,
				'R', Items.REPEATER,
				'F', Items.RABBIT_FOOT,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.speedUpgradeTier,1,1)
			});


		addShapedRecipes("circuit_recipe", "upgrade",new ItemStack(ModItems.circuitBoard), new Object[] {
				"GGG",
				"PDP",
				"PPP",
				'D', new ItemStack(Items.DYE, 1, 2),
				'P', ModItems.plastic,
				'G', Items.GOLD_NUGGET
			});
		
//		////SEEDS
		
		addShapedRecipes("seeds_1_recipe", "feed",new ItemStack(ModItems.chickenFeed, 9), new Object[] {
				"SFS",
				"FSF",
				"SFS",
				'F', ModItems.featherMeal,
				'S', Items.WHEAT_SEEDS
			});
		
		addShapedRecipes("seeds_2_recipe", "feed",new ItemStack(ModItems.chickenFeed, 9), new Object[] {
				"SFS",
				"FSF",
				"SFS",
				'F', ModItems.featherMeal,
				'S', Items.PUMPKIN_SEEDS
			});
		
		addShapedRecipes("seeds_3_recipe", "feed",new ItemStack(ModItems.chickenFeed, 9), new Object[] {
				"SFS",
				"FSF",
				"SFS",
				'F', ModItems.featherMeal,
				'S', Items.MELON_SEEDS
			});
		
		addShapedRecipes("shredder_recipe", "machine", new ItemStack(ModBlocks.shredder), new Object[] {
				"I I",
				"ISI",
				"IRI", 
				'I', Items.IRON_INGOT,
				'R', Blocks.REDSTONE_BLOCK,
				'S', Items.DIAMOND_SWORD
			});
		
		addShapedRecipes("fiberpad_recipe", "held",new ItemStack(ModItems.fiberPad), new Object[] {
				"fff",
				"fff",
				"fff", 
				'f', ModItems.featherFiber
			});

		addShapedRecipes("string_recipe", "held",new ItemStack(Items.STRING), new Object[] {
				"  f",
				" f ",
				"f  ", 
				'f', ModItems.featherFiber
			});
		
		
		
		addShapedRecipes("wool_recipe", "held",new ItemStack(Blocks.WOOL), new Object[] {
				"fff",
				"fff",
				"fff", 
				'f', ModItems.fiberPad
			});
		
		addShapedOreRecipe("sprayer_recipe", "held", new ItemStack(ModItems.sprayer), new Object[] {
		    	"DD ", 
		    	" IB", 
		    	"IGI",
		    	'I', "ingotIron",
		    	'G', Items.BUCKET,
		    	'D', "ingotIron",
		    	'B',  new ItemStack(Blocks.STONE_BUTTON)
			});
		
		    	
		
		addShapelessRecipes("wool_recipe", "held", new ItemStack(ModItems.mealPulp, 4),  new ItemStack(ModItems.featherMeal), new ItemStack(ModItems.featherMeal), new ItemStack(ModItems.featherMeal),new ItemStack(ModItems.featherMeal), new ItemStack(ModItems.featherMeal), new ItemStack(ModItems.featherMeal), new ItemStack(ModItems.featherMeal), new ItemStack(ModItems.featherMeal), new ItemStack(Items.WATER_BUCKET));
		
		addShapelessRecipes("egg_null_recipe", "held",new ItemStack(Items.EGG), new ItemStack(ModItems.hatcheryEgg));

		GameRegistry.addSmelting(ModItems.featherFiber, new ItemStack(ModItems.plastic), 0);
		GameRegistry.addSmelting(ModItems.mealPulp, new ItemStack(Items.PAPER), 0);
	}
	
	public static void addShapedRecipes(String registryname, String group, ItemStack result, Object... recipe) {
		ShapedPrimer primer = CraftingHelper.parseShaped(recipe); 
		addToList(new ShapedRecipes(group.toString(), primer.height, primer.width, primer.input, result).setRegistryName(new ResourceLocation(Hatchery.MODID, registryname)));
	}
	
	public static void addShapedOreRecipe(String registryname, String group, ItemStack result, Object[] recipe) {
		addToList(new ShapedOreRecipe(new ResourceLocation(Hatchery.MODID, group), result, recipe).setRegistryName(new ResourceLocation(Hatchery.MODID, registryname)));
	}
    
	
	protected static void addShapelessRecipes(String registryname, String group, ItemStack result, Item... ingredints) {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		for(Item input: ingredints)
			list.add(new ItemStack(input));
			
	    addShapelessRecipes(registryname, group, result, (ItemStack[]) list.toArray());
	}
	
	public static void addShapelessRecipes(String registryname, String group, ItemStack result, ItemStack... ingredints) {
		NonNullList<Ingredient> list = NonNullList.create();
		for(ItemStack input: ingredints) list.add(Ingredient.fromStacks(input));
		addToList(new ShapelessRecipes(group.toString(), result, list).setRegistryName(new ResourceLocation(Hatchery.MODID, registryname)));
	}
	
    public static IRecipe addToList(IRecipe recipe) {
    		RECIPELIST.add(recipe);
    	return recipe;	
    }
    
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {

		recipeRegistry = event.getRegistry();
		
		initRecipes();
		
		ShredderTileEntity.registerShredderRecipes();

		for(IRecipe recipe : RECIPELIST) {
			System.out.println(recipe.getRegistryName().toString());
			recipeRegistry.register(recipe);
		}

	}
	
	
//	public static class RefillSprayer implements IRecipe
//	{
//		
//		private ItemStack sprayerIn =  ItemStack.EMPTY;
//		
//		private ItemStack sprayerOut = new ItemStack(ModItems.sprayer);
//		
//		public ArrayList<ItemStack> fertBuckets = new ArrayList<ItemStack>();
//		
//		private int FertBucketCnt = 0;
//		
//		
////		if(sprayerOut != null)
////		{
////			ItemStack newStack = FluidUtil.tryFillContainer(sprayerOut ,FluidUtil.getFluidHandler(sprayerOut), (1000 * this.FertBucketCnt), null, true);
////		}
////		
//
//		
//		@Override
//	    public boolean matches(InventoryCrafting inv, World worldIn)
//	    {
//			fertBuckets = new ArrayList<ItemStack>();
//			this.sprayerIn = ItemStack.EMPTY;
//			this.sprayerOut = ItemStack.EMPTY;
//			
//	 	    for (int i = 0; i < inv.getHeight(); ++i)
//	        {
//	            for (int j = 0; j < inv.getWidth(); ++j)
//	            {
//	                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
//
//	                if (!itemstack.isEmpty())
//	                {
//	                    boolean flag = false;
//
//	                    	if(itemstack.getItem() == ModFluids.getFertilizerBucket().getItem())
//	                    	{
//	                    		itemstack.getItem().setContainerItem(Items.BUCKET);
//	                    		fertBuckets.add(itemstack);
//	                    		flag = true;
//	                    	}
//	                    	else if(itemstack.getItem() == ModItems.sprayer && this.sprayerIn.isEmpty())
//	                    	{
//	                    		this.sprayerIn = itemstack;
//	                    		this.sprayerOut = itemstack.copy();
//	                    		
//	                    		flag = true;
//	                    	}
//	                    
//	                    if (!flag)
//	                    {
//	                        return false;
//	                    }
//	                }
//	            }
//	        }
//
//	        return !this.fertBuckets.isEmpty() && this.sprayerIn != null;
//	    }
//		
//
//		@Override
//	    @Nullable
//	    public ItemStack getRecipeOutput()
//	    {
//			if(!sprayerOut.isEmpty())
//			{
//				FluidStack fluid = FluidUtil.getFluidContained(this.sprayerOut);
//				
//				IFluidHandler test = FluidUtil.getFluidHandler(this.sprayerOut);
//				
//				test.fill(new FluidStack(ModFluids.liquidfertilizer, 1000*this.fertBuckets.size()), true);
//			}
//			
//	        return this.sprayerOut;
//	    }
//		
//
//	    @Override
//	    public ItemStack getCraftingResult(InventoryCrafting inv)
//	    {
//	    	ItemStack out = this.sprayerOut.copy();
//			if(out != null)
//			{
//				FluidStack fluid = FluidUtil.getFluidContained(out);
//				
//				IFluidHandler test = FluidUtil.getFluidHandler(out);
//				
//				test.fill(new FluidStack(ModFluids.liquidfertilizer, 1000*this.fertBuckets.size()), true);
//			}
//	    	
//	    	
//	        return out;
//	    }
//	    
//	    public int getRecipeSize()
//	    {
//	    	return 4;
//	    }
//
//
//		@Override
//	    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
//	    {
//		    NonNullList<ItemStack> aitemstack = NonNullList.<ItemStack>withSize(inv.getSizeInventory(), ItemStack.EMPTY);
//
//	        for (int i = 0; i < aitemstack.size(); ++i)
//	        {
//	            ItemStack itemstack = inv.getStackInSlot(i);
//	            aitemstack.add(net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
//	        }
//
//	        return aitemstack;
//	    }
//	}
}
