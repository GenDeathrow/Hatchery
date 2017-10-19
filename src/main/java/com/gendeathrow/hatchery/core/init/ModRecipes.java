package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.block.shredder.ShredderTileEntity;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes 
{

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
	
	public static void RegisterRecipes()
	{
		
		ShredderTileEntity.registerShredderRecipes();
		
	    IRecipe animalNetRecipe = new ShapedOreRecipe(new ItemStack(ModItems.animalNet), new Object[] {
            "xSS",
            "SAA",
            "xxA",
            'S', "stickWood",   // can use ordinary items, blocks, itemstacks in ShapedOreRecipe
            'A', "string",  // look in OreDictionary for vanilla definitions
	    });
	    GameRegistry.addRecipe(animalNetRecipe);
	    
	    IRecipe chickenMachineRecipe = new ShapedOreRecipe(new ItemStack(ModItems.chickenmachine), new Object[] {
	            "xWx",
	            "WEW",
	            "ICI",
	            'W', "plankWood",   // can use ordinary items, blocks, itemstacks in ShapedOreRecipe
	            'E', "egg",
	            'I', Blocks.IRON_BLOCK,
	            'C', ModItems.circuitBoard, // look in OreDictionary for vanilla definitions
		    });
		    GameRegistry.addRecipe(chickenMachineRecipe);
	    
	    IRecipe penRecipe = new ShapedOreRecipe(new ItemStack(ModBlocks.pen), new Object[] {	    
	    	"WxW", 
	    	"WNW", 
	    	"WWW",
	    	'W', "plankWood", 
	    	'N', ModBlocks.nest
	    });
	    
	    GameRegistry.addRecipe(penRecipe);
	    
	    IRecipe fertDirtRecipe = new ShapedOreRecipe(new ItemStack(ModBlocks.fertlizedDirt), new Object[] {
	    	"PPP", 
	    	"PDP", 
	    	"PPP",
	    	'P', "manure",
	    	'D', "dirt"
		});
	    GameRegistry.addRecipe(fertDirtRecipe);
	    
	    IRecipe feederRecipe = new ShapedOreRecipe(new ItemStack(ModBlocks.feeder), new Object[] {
	    	".I.",
	    	"SBS",
	    	"SSS",
	    	'I', "ingotIron",
	    	'B', "blockIron",
	    	'S', "slabWood"
		});
	    GameRegistry.addRecipe(feederRecipe);
	    
	    
	    IRecipe NurseryRecipe = new ShapedOreRecipe(new ItemStack(ModBlocks.nuseryBlock), new Object[] {
	    	"III", 
	    	"IPI", 
	    	"SSS",
	    	'I', "ingotIron",
	    	'P', Blocks.PISTON,
	    	'S', "slabWood"
		});
	    GameRegistry.addRecipe(NurseryRecipe);
	    
		//GameRegistry.addRecipe(new ItemStack(ModBlocks.nest), "xxx", "AxA", "xAx",'A', Blocks.HAY_BLOCK);

	    IRecipe sprayerRecipe = new ShapedOreRecipe(new ItemStack(ModItems.sprayer), new Object[] {
	    	"DD.", 
	    	".IB", 
	    	"IGI",
	    	'I', "ingotIron",
	    	'G', ModFluids.getFertilizerBucket(),
	    	'D', "ingotIron",
	    	'B',  new ItemStack(Blocks.STONE_BUTTON)
	    	
		});
	    GameRegistry.addRecipe(sprayerRecipe);
	    
	    List<ItemStack> bucketFertIngre = new ArrayList<ItemStack>(); 
	    	bucketFertIngre.add(new ItemStack(ModBlocks.manureBlock));
	    	bucketFertIngre.add(new ItemStack(Items.WATER_BUCKET));

	    	IRecipe bucketFert = new ShapelessRecipes(ModFluids.getFertilizerBucket(), bucketFertIngre)
	    {
	    	@Override
	        public ItemStack[] getRemainingItems(InventoryCrafting inv)
	        {
				return new ItemStack[inv.getSizeInventory()];
	    	}
	    	
	    };
	    GameRegistry.addRecipe(bucketFert);
	    
	    
		GameRegistry.addRecipe(new ItemStack(ModBlocks.nest), "xxx", "AxA", "xAx",'A', Blocks.HAY_BLOCK);

		GameRegistry.addRecipe(new ItemStack(ModBlocks.manureBlock), "XXX", "XXX", "XXX",'X', ModItems.manure);
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.manure,9), ModBlocks.manureBlock);
		
		GameRegistry.addRecipe(new RefillSprayer());
		
		GameRegistry.addRecipe(
				new ItemStack(ModBlocks.digesterGenerator),
				"III",
				"PRB",
				"IxI", 
				'I', Items.IRON_INGOT,
				'R', Blocks.REDSTONE_BLOCK,
				'P', Blocks.PISTON, 
				'B', Items.BUCKET);
		
		GameRegistry.addRecipe(
				new ItemStack(ModBlocks.fertilizerMixer), 
				"xHx",
				"IPI",
				"IBI", 
				'H', Blocks.HOPPER, 
				'I', Items.IRON_INGOT, 
				'P', Blocks.PISTON, 
				'B', Items.BUCKET);
		
		
		// UPGRADES
		
		GameRegistry.addRecipe(new ItemStack(ModItems.rfUpgradeTier1), 
				"TRT",
				"RGR",
				"TRT",
				'T', Blocks.REDSTONE_TORCH,
				'R', Items.REDSTONE,
				'G', ModItems.circuitBoard);
		
		GameRegistry.addRecipe(new ItemStack(ModItems.rfUpgradeTier2), 
				"SXS",
				"RUR",
				"GXG",
				'S', Items.GLOWSTONE_DUST,
				'U', ModItems.rfUpgradeTier1,
				'R', Items.REDSTONE,
				'X', ModItems.circuitBoard,
				'G', Items.GOLD_INGOT);
				
		GameRegistry.addRecipe(new ItemStack(ModItems.rfUpgradeTier3), 
				"EXD",
				"XUX",
				"GXG",
				'E', Items.EMERALD,
				'D', Items.DIAMOND,
				'U', ModItems.rfUpgradeTier2,
				'X', ModItems.circuitBoard,
				'G', Blocks.GOLD_BLOCK);
		
		//RF CAPACITY UPGRADE
		GameRegistry.addRecipe(new ItemStack(ModItems.rfCapacityUpgradeTier1), 
				"XRX",
				"XGX",
				"XTX",
				'T', Blocks.REDSTONE_TORCH,
				'R', Items.REDSTONE,
				'X', Blocks.HARDENED_CLAY,
				'G', ModItems.circuitBoard);
		
		GameRegistry.addRecipe(new ItemStack(ModItems.rfCapacityUpgradeTier1, 1, 1), 
				"BTB",
				"RGR",
				"TCT",
				'T', Items.ENDER_PEARL,
				'R', Blocks.REDSTONE_BLOCK,
				'C', Blocks.HARDENED_CLAY,
				'B', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.rfCapacityUpgradeTier1));
		
		GameRegistry.addRecipe(new ItemStack(ModItems.rfCapacityUpgradeTier1, 1, 2), 
				"TBT",
				"RGR",
				"TCT",
				'T', Items.CHORUS_FRUIT,
				'R', Blocks.REDSTONE_BLOCK,
				'C', Blocks.PRISMARINE,
				'B', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.rfCapacityUpgradeTier1, 1, 1));
		
		//Tank CAPACITY UPGRADE
		GameRegistry.addRecipe(new ItemStack(ModItems.tankUpgradeTier1), 
				"BBB",
				"BGB",
				"BBB",
				'B', Items.BUCKET,
				'G', ModItems.circuitBoard);
		
		GameRegistry.addRecipe(new ItemStack(ModItems.tankUpgradeTier1, 1, 1), 
				"IBI",
				"OGO",
				"CCC",
				'C', Blocks.IRON_BLOCK,
				'B', Items.BUCKET,
				'I', Items.IRON_INGOT,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.tankUpgradeTier1, 1, 0));
		
		GameRegistry.addRecipe(new ItemStack(ModItems.tankUpgradeTier1, 1, 2), 
				"BCB",
				"BGB",
				"OCO",
				'B', Blocks.IRON_BLOCK,
				'C', Items.CAULDRON,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.tankUpgradeTier1,1,1));
		
		
		//Speed UPGRADE
		GameRegistry.addRecipe(new ItemStack(ModItems.speedUpgradeTier), 
				"FRF",
				"NGN",
				"FRF",
				'F', Items.FEATHER,
				'N', Items.GOLD_NUGGET,
				'R', Items.REDSTONE,
				'G', ModItems.circuitBoard);
		
		GameRegistry.addRecipe(new ItemStack(ModItems.speedUpgradeTier,1,1), 
				"OCO",
				"GRG",
				"QCQ",
				'Q', Items.QUARTZ,
				'R', Blocks.REDSTONE_BLOCK,
				'C', Items.REDSTONE,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.speedUpgradeTier));
		
		GameRegistry.addRecipe(new ItemStack(ModItems.speedUpgradeTier, 1, 2), 
				"OFO",
				"GRG",
				"QOQ",
				'Q', Blocks.QUARTZ_BLOCK,
				'R', Items.REPEATER,
				'F', Items.RABBIT_FOOT,
				'O', ModItems.circuitBoard,
				'G', new ItemStack(ModItems.speedUpgradeTier,1,1));
		
		GameRegistry.addRecipe(new ItemStack(ModItems.circuitBoard), 
				"GGG",
				"PDP",
				"PPP",
				'D', new ItemStack(Items.DYE, 1, 2),
				'P', ModItems.plastic,
				'G', Items.GOLD_NUGGET);
		
		
		
		
		
		////SEEDS
		GameRegistry.addRecipe(new ItemStack(ModItems.chickenFeed, 9), 
				"SFS",
				"FSF",
				"SFS",
				'F', ModItems.featherMeal,
				'S', Items.WHEAT_SEEDS);
		
		GameRegistry.addRecipe(new ItemStack(ModItems.chickenFeed, 9), 
				"SFS",
				"FSF",
				"SFS",
				'F', ModItems.featherMeal,
				'S', Items.PUMPKIN_SEEDS);
		
		GameRegistry.addRecipe(new ItemStack(ModItems.chickenFeed, 9), 
				"SFS",
				"FSF",
				"SFS",
				'F', ModItems.featherMeal,
				'S', Items.MELON_SEEDS);
		//END SEEDS
		
		GameRegistry.addRecipe(
				new ItemStack(ModBlocks.shredder),
				"IxI",
				"ISI",
				"IRI", 
				'I', Items.IRON_INGOT,
				'R', Blocks.REDSTONE_BLOCK,
				'S', Items.DIAMOND_SWORD, 
				'B', Items.BUCKET);
		
		GameRegistry.addRecipe(
				new ItemStack(ModItems.fiberPad),
				"fff",
				"fff",
				"fff", 
				'f', ModItems.featherFiber);
		
		GameRegistry.addRecipe(
				new ItemStack(Items.STRING),
				"XXf",
				"XfX",
				"fXX", 
				'f', ModItems.featherFiber);
		
		GameRegistry.addRecipe(
				new ItemStack(Blocks.WOOL),
				"fff",
				"fff",
				"fff", 
				'f', ModItems.fiberPad);
		

		
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.mealPulp, 4), ModItems.featherMeal, ModItems.featherMeal, ModItems.featherMeal,ModItems.featherMeal, ModItems.featherMeal, ModItems.featherMeal, ModItems.featherMeal, ModItems.featherMeal, Items.WATER_BUCKET);
		
		GameRegistry.addShapelessRecipe(new ItemStack(Items.EGG), ModItems.hatcheryEgg);
		
		GameRegistry.addSmelting(ModItems.featherFiber, new ItemStack(ModItems.plastic), 0);
		GameRegistry.addSmelting(ModItems.mealPulp, new ItemStack(Items.PAPER), 0);

	}
	
	
	public static class RefillSprayer implements IRecipe
	{
		
		private ItemStack sprayerIn;
		
		private ItemStack sprayerOut = new ItemStack(ModItems.sprayer);
		
		public ArrayList<ItemStack> fertBuckets = new ArrayList<ItemStack>();
		
		private int FertBucketCnt = 0;
		
		
//		if(sprayerOut != null)
//		{
//			ItemStack newStack = FluidUtil.tryFillContainer(sprayerOut ,FluidUtil.getFluidHandler(sprayerOut), (1000 * this.FertBucketCnt), null, true);
//		}
//		

		
		@Override
	    public boolean matches(InventoryCrafting inv, World worldIn)
	    {
			fertBuckets = new ArrayList<ItemStack>();
			this.sprayerIn = null;
			this.sprayerOut = null;
			
	 	    for (int i = 0; i < inv.getHeight(); ++i)
	        {
	            for (int j = 0; j < inv.getWidth(); ++j)
	            {
	                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);

	                if (itemstack != null)
	                {
	                    boolean flag = false;

	                    	if(itemstack.getItem() == ModFluids.getFertilizerBucket().getItem())
	                    	{
	                    		itemstack.getItem().setContainerItem(Items.BUCKET);
	                    		fertBuckets.add(itemstack);
	                    		flag = true;
	                    	}
	                    	else if(itemstack.getItem() == ModItems.sprayer && this.sprayerIn == null)
	                    	{
	                    		this.sprayerIn = itemstack;
	                    		this.sprayerOut = itemstack.copy();
	                    		
	                    		flag = true;
	                    	}
	                    
	                    if (!flag)
	                    {
	                        return false;
	                    }
	                }
	            }
	        }

	        return !this.fertBuckets.isEmpty() && this.sprayerIn != null;
	    }
		

		@Override
	    @Nullable
	    public ItemStack getRecipeOutput()
	    {
			if(sprayerOut != null)
			{
				FluidStack fluid = FluidUtil.getFluidContained(this.sprayerOut);
				
				IFluidHandler test = FluidUtil.getFluidHandler(this.sprayerOut);
				
				test.fill(new FluidStack(ModFluids.liquidfertilizer, 1000*this.fertBuckets.size()), true);
			}
			
	        return this.sprayerOut;
	    }
		
	    @Nullable
	    public ItemStack getCraftingResult(InventoryCrafting inv)
	    {
	    	ItemStack out = this.sprayerOut.copy();
			if(out != null)
			{
				FluidStack fluid = FluidUtil.getFluidContained(out);
				
				IFluidHandler test = FluidUtil.getFluidHandler(out);
				
				test.fill(new FluidStack(ModFluids.liquidfertilizer, 1000*this.fertBuckets.size()), true);
			}
	    	
	    	
	        return out;
	    }
	    
	    public int getRecipeSize()
	    {
	    	return 4;
	    }


		@Override
	    public ItemStack[] getRemainingItems(InventoryCrafting inv)
	    {
	        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];

	        for (int i = 0; i < aitemstack.length; ++i)
	        {
	            ItemStack itemstack = inv.getStackInSlot(i);
	            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
	        }

	        return aitemstack;
	    }
	}
}
