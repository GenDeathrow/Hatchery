package com.gendeathrow.hatchery.fluid;

import javax.annotation.Nonnull;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

  public final @Nonnull Fluid fluid;
  public final @Nonnull ModelResourceLocation location;

  public FluidStateMapper(@Nonnull Fluid fluid) {
    this.fluid = fluid;
    location = new ModelResourceLocation(Hatchery.MODID + ":liquid_fertilizer", fluid.getName());
  }

  @Override
  protected @Nonnull ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
    return location;
  }

  @Override
  public @Nonnull ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
    return location;
  }
  
}