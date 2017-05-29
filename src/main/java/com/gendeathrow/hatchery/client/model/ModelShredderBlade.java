package com.gendeathrow.hatchery.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelShredderBlade  extends ModelBase {
	   public ModelRenderer shaft;
	    public ModelRenderer teeth1;
	    public ModelRenderer teeth2;
	    public ModelRenderer teeth3;
	    public ModelRenderer teeth4;
	    public ModelRenderer teeth5;
	    public ModelRenderer teeth6;
	    public ModelRenderer teeth7;
	    public ModelRenderer teeth8;
	    public ModelRenderer teeth9;
	    public ModelRenderer teeth10;
	    public ModelRenderer teeth11;
	    public ModelRenderer teeth12;
	    public ModelRenderer teeth13;
	    public ModelRenderer teeth14;
	    public ModelRenderer teeth15;
	    public ModelRenderer teeth16;
	    public ModelRenderer teeth17;
	    public ModelRenderer teeth18;
	    public ModelRenderer teeth19;
	    public ModelRenderer teeth20;
	    public ModelRenderer teeth21;
	    public ModelRenderer teeth22;
	    public ModelRenderer teeth23;
	    public ModelRenderer teeth24;
	    public ModelRenderer teeth25;

	    public ModelShredderBlade() {
	        this.textureWidth = 32;
	        this.textureHeight = 32;
	        
	        this.shaft = new ModelRenderer(this, 0, 4);
	        this.shaft.setRotationPoint(0.0F, 0.0F, 0.0F);
	        this.shaft.addBox(-7.5F, -1.0F, -1.0F, 15, 2, 2, 0.0F);
	        
	        
	        this.teeth8 = new ModelRenderer(this, 0, 0);
	        this.teeth8.setRotationPoint(5.6F, -1.0F, -2.0F);
	        this.teeth8.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth14 = new ModelRenderer(this, 0, 0);
	        this.teeth14.setRotationPoint(2.0F, -2.0F, 0.0F);
	        this.teeth14.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth3 = new ModelRenderer(this, 0, 0);
	        this.teeth3.setRotationPoint(-2.0F, 0.0F, 1.0F);
	        this.teeth3.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth25 = new ModelRenderer(this, 0, 0);
	        this.teeth25.setRotationPoint(-6.4F, 0.0F, -2.0F);
	        this.teeth25.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth1 = new ModelRenderer(this, 0, 0);
	        this.teeth1.setRotationPoint(-6.0F, 0.0F, 1.0F);
	        this.teeth1.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth17 = new ModelRenderer(this, 0, 0);
	        this.teeth17.setRotationPoint(-4.0F, 1.0F, 0.0F);
	        this.teeth17.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth9 = new ModelRenderer(this, 0, 0);
	        this.teeth9.setRotationPoint(2.6F, 0.0F, -2.0F);
	        this.teeth9.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth10 = new ModelRenderer(this, 0, 0);
	        this.teeth10.setRotationPoint(-6.0F, -2.0F, 0.0F);
	        this.teeth10.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth12 = new ModelRenderer(this, 0, 0);
	        this.teeth12.setRotationPoint(-2.0F, -2.0F, 0.0F);
	        this.teeth12.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth16 = new ModelRenderer(this, 0, 0);
	        this.teeth16.setRotationPoint(-6.0F, 1.0F, -1.0F);
	        this.teeth16.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth11 = new ModelRenderer(this, 0, 0);
	        this.teeth11.setRotationPoint(-4.0F, -2.0F, -1.0F);
	        this.teeth11.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth15 = new ModelRenderer(this, 0, 0);
	        this.teeth15.setRotationPoint(4.0F, -2.0F, -1.0F);
	        this.teeth15.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth23 = new ModelRenderer(this, 0, 0);
	        this.teeth23.setRotationPoint(3.0F, 0.0F, 1.0F);
	        this.teeth23.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth5 = new ModelRenderer(this, 0, 0);
	        this.teeth5.setRotationPoint(5.0F, -1.0F, 1.0F);
	        this.teeth5.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth20 = new ModelRenderer(this, 0, 0);
	        this.teeth20.setRotationPoint(6.0F, 1.0F, -1.0F);
	        this.teeth20.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth22 = new ModelRenderer(this, 0, 0);
	        this.teeth22.setRotationPoint(-2.0F, 1.0F, -1.0F);
	        this.teeth22.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth2 = new ModelRenderer(this, 0, 0);
	        this.teeth2.setRotationPoint(-4.0F, -1.0F, 1.0F);
	        this.teeth2.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth21 = new ModelRenderer(this, 0, 0);
	        this.teeth21.setRotationPoint(-0.2F, 1.0F, 0.0F);
	        this.teeth21.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth4 = new ModelRenderer(this, 0, 0);
	        this.teeth4.setRotationPoint(1.0F, -1.0F, 1.0F);
	        this.teeth4.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth19 = new ModelRenderer(this, 0, 0);
	        this.teeth19.setRotationPoint(4.0F, 1.0F, 0.0F);
	        this.teeth19.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth24 = new ModelRenderer(this, 0, 0);
	        this.teeth24.setRotationPoint(-4.4F, -1.0F, -2.0F);
	        this.teeth24.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth7 = new ModelRenderer(this, 0, 0);
	        this.teeth7.setRotationPoint(0.6F, -1.0F, -2.0F);
	        this.teeth7.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth13 = new ModelRenderer(this, 0, 0);
	        this.teeth13.setRotationPoint(0.0F, -2.0F, -1.0F);
	        this.teeth13.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth18 = new ModelRenderer(this, 0, 0);
	        this.teeth18.setRotationPoint(2.0F, 1.0F, -1.0F);
	        this.teeth18.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.teeth6 = new ModelRenderer(this, 0, 0);
	        this.teeth6.setRotationPoint(-1.4F, 0.0F, -2.0F);
	        this.teeth6.addBox(0.0F, 0.0F, 0.0F, 1, 1, 1, 0.0F);
	        this.shaft.addChild(this.teeth8);
	        this.shaft.addChild(this.teeth14);
	        this.shaft.addChild(this.teeth3);
	        this.shaft.addChild(this.teeth25);
	        this.shaft.addChild(this.teeth1);
	        this.shaft.addChild(this.teeth17);
	        this.shaft.addChild(this.teeth9);
	        this.shaft.addChild(this.teeth10);
	        this.shaft.addChild(this.teeth12);
	        this.shaft.addChild(this.teeth16);
	        this.shaft.addChild(this.teeth11);
	        this.shaft.addChild(this.teeth15);
	        this.shaft.addChild(this.teeth23);
	        this.shaft.addChild(this.teeth5);
	        this.shaft.addChild(this.teeth20);
	        this.shaft.addChild(this.teeth22);
	        this.shaft.addChild(this.teeth2);
	        this.shaft.addChild(this.teeth21);
	        this.shaft.addChild(this.teeth4);
	        this.shaft.addChild(this.teeth19);
	        this.shaft.addChild(this.teeth24);
	        this.shaft.addChild(this.teeth7);
	        this.shaft.addChild(this.teeth13);
	        this.shaft.addChild(this.teeth18);
	        this.shaft.addChild(this.teeth6);
	    }
    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    { 
        GlStateManager.rotate(limbSwingAmount, 1.0F, 0.0F, 0.0F);  
        this.shaft.render(scale);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
