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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class LuckyEggCategory extends BlankRecipeCategory<LuckyEggWrapper>
{
	
    public static final String UID = "hatchery.luckyeggs.drop";
    private final String title;
    private final IDrawableStatic background;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic icon;


    public LuckyEggCategory(IGuiHelper guiHelper) 
    {
        title = I18n.translateToLocal("jei.gui.luckyegg_drops");

        ResourceLocation location = new ResourceLocation(Hatchery.MODID, "textures/gui/luckyeggs.png");
        background = guiHelper.createDrawable(location, 0, 0, 169, 119);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(location, 169, 0, 15, 17);
        arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.TOP, false);


        icon = guiHelper.createDrawable(location, 169, 17, 16, 16);	
    }
    
	@Override
	public void drawAnimations(Minecraft arg0) 
	{

	}

	@Override
	public void drawExtras(Minecraft minecraft) 
	{
		arrow.draw(minecraft, 75, 27);
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
	public void setRecipe(IRecipeLayout recipeLayout, LuckyEggWrapper recipeWrapper, IIngredients ingredients) 
	{
	        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
	        
	    	guiItemStacks.init(0, true, 74, 8);
	    	
	    	for(int y = 0; y <= 4; y++ )
	    		for(int x = 0; x <= 9; x++)
	    			guiItemStacks.init(1+(y*9)+x, false, 3 + (x * 18), 44 + (y*18));
	        
	        guiItemStacks.set(ingredients);
	}

	@Override
	public IDrawable getIcon() 
	{
		return icon;
	}

	
	
}
