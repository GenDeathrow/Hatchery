package com.gendeathrow.hatchery.core.jei.shredder;

import java.util.Collections;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.api.crafting.ShredderRecipe;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;


public class ShredderCategory  implements IRecipeCategory<ShredderRecipeWrapper>, IRecipeWrapperFactory<ShredderRecipe>{

    public static final String UID = "hatchery.shredder.recipe";
    private final ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/shredder_recipes.png");
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;


    public ShredderCategory(IGuiHelper guiHelper) 
    {

        title = I18n.format("jei.gui.shredder_recipes");
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
	public void setRecipe(IRecipeLayout recipeLayout, ShredderRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
	        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	    	guiItemStacks.init(0, true, 28, 9);
	        guiItemStacks.init(1, false, 18, 47);
	        guiItemStacks.init(2, false, 39, 47);
	        guiItemStacks.init(3, true, 68, 27);
	        guiItemStacks.set(ingredients);
	}

	@Override
	public String getModName() {
		return Hatchery.NAME;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return  Collections.emptyList();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ShredderRecipe recipe) {
		return new ShredderRecipeWrapper(recipe);
	}

}
