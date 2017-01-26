package com.gendeathrow.hatchery.fluid;

import com.gendeathrow.hatchery.core.init.ModFluids;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class DoubleFluidTank implements IFluidTank, IFluidHandler
{

	private FluidTank waterTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 0), 20000);
	
	private FluidTank fertilizerTank = new FluidTank(new FluidStack(ModFluids.liquidfertilizer, 0), 20000);
	
	
	@Override
	public FluidStack getFluid() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getFluidAmount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public FluidTankInfo getInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	
	

	@Override
	public IFluidTankProperties[] getTankProperties() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int fill(FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

}
