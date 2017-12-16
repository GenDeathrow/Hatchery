package com.gendeathrow.hatchery.core.jei.generator;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModFluids;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;


public class GeneratorCategory extends BlankRecipeCategory<GeneratorRecipeWrapper>
{
	
    public static final String UID = "hatchery.generator.recipe";
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated rf;
    private final IDrawableAnimated poop;
    private final IDrawableStatic icon;


    public GeneratorCategory(IGuiHelper guiHelper) 
    {
        title = I18n.format("jei.gui.generator_recipes");

        ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/generator_jei.png");
        background = guiHelper.createDrawable(location, 0, 0, 91, 78);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 94, 0, 15, 17);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);


        IDrawableStatic poopDrawable = guiHelper.createDrawable(location, 110, 0, 13, 58);
        poop = guiHelper.createAnimatedDrawable(poopDrawable, 200, IDrawableAnimated.StartDirection.TOP, true);

        
        IDrawableStatic rfDrawable = guiHelper.createDrawable(location, 123, 0, 15, 58);
        rf = guiHelper.createAnimatedDrawable(rfDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);

        
        icon = guiHelper.createDrawable(location, 91, 17, 16, 16);	
    }
    
	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		arrow.draw(minecraft, 60, 26);
		rf.draw(minecraft, 5, 6);
	}

	@Override
	public IDrawable getBackground() 
	{
		return background;
	}

	@Override
	public String getTitle() 
	{
		return title;
	}

	@Override
	public String getUid() 
	{
		return UID;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, GeneratorRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
	        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	        IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
	        
	    	guiItemStacks.init(0, true, 60, 8);
	    	
	    	guiFluidStacks.init(1, true, 42, 6, 12, 57, 20000, true, poop);

	        guiItemStacks.set(ingredients);
	        
	        guiFluidStacks.set(1, new FluidStack(ModFluids.liquidfertilizer, 0));
	}

	@Override
	public String getModName() {
		return Hatchery.NAME;
	}
}
