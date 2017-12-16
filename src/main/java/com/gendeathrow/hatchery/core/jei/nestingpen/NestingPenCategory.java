package com.gendeathrow.hatchery.core.jei.nestingpen;

import java.util.Collections;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.api.crafting.NestingPenDropRecipe;

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


public class NestingPenCategory  implements IRecipeCategory<NestingPenDropRecipeWrapper>, IRecipeWrapperFactory<NestingPenDropRecipe>{

    public static final String UID = "hatchery.nesting_pen.drops";
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;

    private final ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/nestingpen_drops.png");
    

    public NestingPenCategory(IGuiHelper guiHelper) 
    {
        title = I18n.format("jei.gui.nesting_pen_drop");
        background = guiHelper.createDrawable(location, 0, 0, 91, 78);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 91, 0, 15, 17);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);
        icon = guiHelper.createDrawable(location, 91, 17, 16, 16);	
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

	@Override
	public String getModName() {
		return Hatchery.NAME;
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return  Collections.emptyList();
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(NestingPenDropRecipe recipe) {
		return new NestingPenDropRecipeWrapper(recipe);
	}

	@Override
	public IDrawable getIcon() {
		return null;
	}

}
