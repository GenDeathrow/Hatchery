package com.gendeathrow.hatchery.core.jei.nestingpen;

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

public class NestingPenCategory extends BlankRecipeCategory<NestingPenDropRecipeWrapper>
{
	
    public static final String UID = "hatchery.nesting_pen.drops";
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;


    public NestingPenCategory(IGuiHelper guiHelper) 
    {
        title = I18n.translateToLocal("jei.gui.nesting_pen_drop");

        ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/nestingpen_drops.png");
        background = guiHelper.createDrawable(location, 0, 0, 91, 78);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 91, 0, 15, 17);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);


        icon = guiHelper.createDrawable(location, 91, 17, 16, 16);	
    }
    
	@Override
	public void drawAnimations(Minecraft arg0) 
	{

	}

	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		arrow.draw(minecraft, 38, 35);
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
	public void setRecipe(IRecipeLayout recipeLayout, NestingPenDropRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
	        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	        
	    	guiItemStacks.init(0, true, 7, 7);
	    	guiItemStacks.init(1, true, 35, 12);
	        guiItemStacks.init(2, false, 12, 55);
	        guiItemStacks.init(3, false, 32, 55);
	        guiItemStacks.init(4, false, 47, 55);
	        guiItemStacks.init(5, false, 62, 55);
	        
	        guiItemStacks.set(ingredients);
	}


	
}
