package com.gendeathrow.hatchery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRooster extends ModelBase 
{
	ModelRenderer head;
	ModelRenderer rightLeg;
	ModelRenderer chin;
	ModelRenderer bill;
	ModelRenderer leftLeg;
	ModelRenderer leftWing;
	ModelRenderer rightWing;
	ModelRenderer tailBottom;
	ModelRenderer crest;
	ModelRenderer tailMain;
	ModelRenderer tailTop;
	ModelRenderer body;
	ModelRenderer neck;

	public ModelRooster() 
	{
		textureWidth = 64;
		textureHeight = 32;
		leftWing = new ModelRenderer(this, 24, 13);
		leftWing.setRotationPoint(4.0F, 13.0F, 0.0F);
		leftWing.addBox(-1.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F);
		tailMain = new ModelRenderer(this, 39, 13);
		tailMain.setRotationPoint(-0.5F, 14.0F, 4.0F);
		tailMain.addBox(0.0F, -2.3F, -1.5F, 1, 4, 6, 0.0F);
		setRotateAngle(tailMain, 0.7285004297824331F, 0.0F, 0.0F);
		tailBottom = new ModelRenderer(this, 39, 16);
		tailBottom.setRotationPoint(-0.5F, 14.0F, 4.0F);
		tailBottom.addBox(0.0F, 1.5F, 1.5F, 1, 1, 1, 0.0F);
		setRotateAngle(tailBottom, 0.7285004297824331F, 0.0F, 0.0F);
		head = new ModelRenderer(this, 0, 0);
		head.setRotationPoint(0.0F, 14.0F, -4.0F);
		head.addBox(-2.0F, -6.0F, -2.0F, 4, 7, 3, 0.0F);
		body = new ModelRenderer(this, 0, 10);
		body.setRotationPoint(0.0F, 16.0F, 0.0F);
		body.addBox(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
		setRotateAngle(body, 1.3089969389957472F, 0.0F, 0.0F);
		rightLeg = new ModelRenderer(this, 26, 0);
		rightLeg.setRotationPoint(-2.0F, 19.0F, 1.0F);
		rightLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F);
		crest = new ModelRenderer(this, 18, 7);
		crest.setRotationPoint(0.0F, 15.0F, -4.0F);
		crest.addBox(-0.5F, -8.0F, -3.0F, 1, 4, 5, 0.0F);
		chin = new ModelRenderer(this, 14, 4);
		chin.setRotationPoint(0.0F, 14.0F, -4.0F);
		chin.addBox(-1.0F, -2.0F, -3.0F, 2, 2, 2, 0.0F);
		rightWing = new ModelRenderer(this, 24, 13);
		rightWing.setRotationPoint(-4.0F, 13.0F, 0.0F);
		rightWing.addBox(0.0F, 0.0F, -3.0F, 1, 4, 6, 0.0F);
		tailTop = new ModelRenderer(this, 39, 13);
		tailTop.setRotationPoint(-0.5F, 14.0F, 4.0F);
		tailTop.addBox(0.0F, 1.5F, 3.5F, 1, 1, 1, 0.0F);
		setRotateAngle(tailTop, 0.7285004297824331F, 0.0F, 0.0F);
		bill = new ModelRenderer(this, 14, 0);
		bill.setRotationPoint(0.0F, 14.0F, -4.0F);
		bill.addBox(-2.0F, -4.0F, -4.0F, 4, 2, 2, 0.0F);
		leftLeg = new ModelRenderer(this, 26, 0);
		leftLeg.setRotationPoint(1.0F, 19.0F, 1.0F);
		leftLeg.addBox(-1.0F, 0.0F, -3.0F, 3, 5, 3, 0.0F);
		neck = new ModelRenderer(this, 33, 3);
		neck.setRotationPoint(0.0F, 16.0F, 0.0F);
		neck.addBox(-2.5F, -5.0F, -4.5F, 5, 3, 6, 0.0F);
		setRotateAngle(neck, 0.7853981633974483F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		leftWing.render(scale);
		tailMain.render(scale);
		tailBottom.render(scale);
		head.render(scale);
		body.render(scale);
		rightLeg.render(scale);
		crest.render(scale);
		chin.render(scale);
		rightWing.render(scale);
		tailTop.render(scale);
		bill.render(scale);
		leftLeg.render(scale);
		neck.render(scale);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		head.rotateAngleX = headPitch * 0.017453292F;
		head.rotateAngleY = netHeadYaw * 0.017453292F;
		bill.rotateAngleX = head.rotateAngleX;
		bill.rotateAngleY = head.rotateAngleY;
		chin.rotateAngleX = head.rotateAngleX;
		chin.rotateAngleY = head.rotateAngleY;
		crest.rotateAngleX = head.rotateAngleX;
		crest.rotateAngleY = head.rotateAngleY;

		//neck.rotateAngleX = ((float) Math.PI / 2F);

		//tailMain.rotateAngleX = ((float) Math.PI / 2F);
		//tailBottom.rotateAngleX = ((float) Math.PI / 2F);
		//tailTop.rotateAngleX = ((float) Math.PI / 2F);

		//body.rotateAngleX = ((float) Math.PI / 2F);
		rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		leftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		rightWing.rotateAngleZ = ageInTicks;
		leftWing.rotateAngleZ = -ageInTicks;
	}
}
