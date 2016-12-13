package com.gendeathrow.hatchery.core.init;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagCompound;
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
		OreDictionary.registerOre("manure", ModItems.manure);
	}
	
	public static void RegisterRecipes()
	{
	    IRecipe animalNetRecipe = new ShapedOreRecipe(new ItemStack(ModItems.animalNet), new Object[] {
            "xSS",
            "SAA",
            "xxA",
            'S', "stickWood",   // can use ordinary items, blocks, itemstacks in ShapedOreRecipe
            'A', "string",  // look in OreDictionary for vanilla definitions
	    });
	    GameRegistry.addRecipe(animalNetRecipe);
	    
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
	    
		GameRegistry.addRecipe(new ItemStack(ModBlocks.nest), "xxx", "AxA", "AAA",'A', Blocks.HAY_BLOCK);

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
	    
	    NBTTagCompound tag = new NBTTagCompound();
	    tag.setString("FluidName", "liquid_fertilizer");
	    tag.setInteger("Amount", 1000);
	    ItemStack bucket = new ItemStack(Items.BUCKET);
	    bucket.setTagCompound(tag);
	    
	    
//	    IRecipe bucketFert = new ShapedOreRecipe(ModFluids.getFertilizerBucket(), new Object[] {
//	    	"FFF", 
//	    	"FBF", 
//	    	"FFF",
//	    	'F', ModBlocks.fertlizedDirt,
//	    	'B',  Items.WATER_BUCKET
//	    	
//		}); 
	    
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
	    
	    
		GameRegistry.addRecipe(new ItemStack(ModBlocks.nest), "xxx", "AxA", "AAA",'A', Blocks.HAY_BLOCK);

		GameRegistry.addRecipe(new ItemStack(ModBlocks.manureBlock), "XXX", "XXX", "XXX",'X', ModItems.manure);
		//GameRegistry.addRecipe(new ItemStack(ModItems.manure,9), ModBlocks.manureBlock);
		
		GameRegistry.addRecipe(new RefillSprayer());

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
