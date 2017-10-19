package com.gendeathrow.hatchery.core.jei.fertilizermixer;

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

public class FertilizerMixerCategory extends BlankRecipeCategory<FertilizerMixerRecipeWrapper>
{
	
    public static final String UID = "hatchery.fertilizermixer.recipe";
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated arrow2;
    private final IDrawableStatic icon;
    private final IDrawableAnimated poop;
    private final IDrawableAnimated rf;
    private final IDrawableAnimated water;

    public FertilizerMixerCategory(IGuiHelper guiHelper) 
    {
        title = I18n.translateToLocal("jei.gui.fertilizermixer_recipes");

        ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/mixer_jei.png");
        background = guiHelper.createDrawable(location, 0, 0, 159, 67);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 163, 28, 10, 15);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);

        IDrawableStatic arrow2Drawable = guiHelper.createDrawable(location, 162, 17, 15, 10);
        arrow2 = guiHelper.createAnimatedDrawable(arrow2Drawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        IDrawableStatic poopDrawable = guiHelper.createDrawable(location, 177, 0, 13, 58);
        poop = guiHelper.createAnimatedDrawable(poopDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM, false);

        
        IDrawableStatic rfDrawable = guiHelper.createDrawable(location, 204, 0, 13, 58);
        rf = guiHelper.createAnimatedDrawable(rfDrawable, 400, IDrawableAnimated.StartDirection.TOP, true);
        
        IDrawableStatic waterDrawable = guiHelper.createDrawable(location, 190, 0, 13, 58);
        water = guiHelper.createAnimatedDrawable(waterDrawable, 100, IDrawableAnimated.StartDirection.TOP, true);
        
        icon = guiHelper.createDrawable(location, 91, 17, 16, 16);	
    }
    
	@Override
	public void drawAnimations(Minecraft arg0) 
	{

	}

	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		arrow.draw(minecraft, 70, 28);
		arrow.draw(minecraft, 102, 28);
		arrow2.draw(minecraft, 32, 12);
		poop.draw(minecraft, 121, 7);
		rf.draw(minecraft, 143, 7);
		water.draw(minecraft, 48, 7);
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
	public void setRecipe(IRecipeLayout recipeLayout, FertilizerMixerRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
	        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	        
	    	guiItemStacks.init(0, true, 11, 8);
	    	//guiItemStacks.init(0, true, 11, 8);
	    	guiItemStacks.init(1, true, 66, 8);
	    	
	        guiItemStacks.init(2, false, 98, 44);

	        //guiItemStacks.init(3, true, 68, 27);
	        
	        
	        guiItemStacks.set(ingredients);
	}

	
}
