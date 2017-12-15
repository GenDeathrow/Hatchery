package com.gendeathrow.hatchery.core.jei.nestingpen;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.api.crafting.NestingPenDropRecipe;
import com.gendeathrow.hatchery.core.init.ModBlocks;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;

public class NestingPenDropRecipeWrapper extends BlankRecipeWrapper{

	private final List<ItemStack> output;
	private final List<ItemStack> inputs;
	
	public final EntityLivingBase chickenEnityIn;
	
	
	public NestingPenDropRecipeWrapper(ItemStack chickenIn, List<ItemStack> dropped) 
	{
		this.inputs = new ArrayList<ItemStack>();
		this.inputs.add(chickenIn);
		this.inputs.add(new ItemStack(ModBlocks.pen));
		
		chickenEnityIn = null;
		
		output = dropped;
	}

	public NestingPenDropRecipeWrapper(NestingPenDropRecipe recipe) {
	
		this.inputs = recipe.getInputItem();
		this.output = recipe.getOutputItems();
		chickenEnityIn = recipe.getEntity();
	}

	@Override
	public void drawInfo(Minecraft minecraft, int arg1, int arg2, int arg3, int arg4) 
	{
		if(chickenEnityIn != null) {
			if(chickenEnityIn.world == null) 
				chickenEnityIn.setWorld(minecraft.world);
			chickenEnityIn.setPositionAndRotation(0, 0, 0, 45, 0);
			drawEntityOnScreen(88, 35, 50, chickenEnityIn);
		}


	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(ItemStack.class, output);
	}

	public List getInput() {
		return inputs;
	}

	public List<ItemStack> getOutput() {
		return output;
	}
	
	

	
	public static void drawEntityOnScreen(int posX, int posY, int scale, EntityLivingBase ent)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)posX, (float)posY, 50.0F);
        GlStateManager.scale((float)(-scale), (float)scale, (float)scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f = ent.renderYawOffset;
        float f1 = ent.rotationYaw;
        float f2 = ent.rotationPitch;
        float f3 = ent.prevRotationYawHead;
        float f4 = ent.rotationYawHead;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        ent.rotationYawHead = ent.rotationYaw;
        ent.prevRotationYawHead = ent.rotationYaw;
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(ent, 0.0D, 0.0D, 0.0D, ent.rotationYaw, 1.0F, false);
        rendermanager.setRenderShadow(true);
        ent.renderYawOffset = f;
        ent.rotationYaw = f1;
        ent.rotationPitch = f2;
        ent.prevRotationYawHead = f3;
        ent.rotationYawHead = f4;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}
