package com.gendeathrow.hatchery.core.jei.eggmachine;

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
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;


public class EggMachineCategory extends BlankRecipeCategory<EggMachineWrapper>
{
	
    public static final String UID = "hatchery.eggmachine.egg";
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;
    ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/eggmachine_recipe.png");

    public EggMachineCategory(IGuiHelper guiHelper) 
    {
        title = I18n.format("jei.gui.eggmachine_egg");
        background = guiHelper.createDrawable(location, 0, 0, 91, 78);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 91, 0, 15, 17);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);
        icon = guiHelper.createDrawable(location, 91, 17, 16, 16);	
    }
    
	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		arrow.draw(minecraft, 29, 28);
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
	public void setRecipe(IRecipeLayout recipeLayout, EggMachineWrapper recipeWrapper, IIngredients ingredients) 
	{
	        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	        
	    	guiItemStacks.init(0, true, 17, 9);
	        guiItemStacks.init(1, true, 38, 9);
	        guiItemStacks.init(3, true, 68, 27);

	        guiItemStacks.init(2, false, 28, 48);

	        //guiItemStacks.set(ingredients);
	        
	    	guiItemStacks.set(1, recipeWrapper.getInput().get(0));
	    	guiItemStacks.set(3, recipeWrapper.getInput().get(1));
	    	guiItemStacks.set(0, recipeWrapper.getAllEggs());
	    	guiItemStacks.set(2, recipeWrapper.getOutput());
	}

	@Override
	public String getModName() {
		return Hatchery.NAME;
	}
	
}
