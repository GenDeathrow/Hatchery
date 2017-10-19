package com.gendeathrow.hatchery.core.jei.generator;

import com.gendeathrow.hatchery.Hatchery;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

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
        title = I18n.translateToLocal("jei.gui.generator_recipes");

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
	public void drawAnimations(Minecraft arg0) 
	{

	}

	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		arrow.draw(minecraft, 60, 26);
		poop.draw(minecraft, 42, 6);
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
	        
	    	guiItemStacks.init(0, true, 60, 8);
	    	
	       //guiItemStacks.init(1, false, 18, 47);

	        guiItemStacks.set(ingredients);
	}

	
}
