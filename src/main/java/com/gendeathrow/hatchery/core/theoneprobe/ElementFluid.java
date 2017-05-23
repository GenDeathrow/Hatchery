package com.gendeathrow.hatchery.core.theoneprobe;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;

public class ElementFluid implements IElement 
{
	private long nextDrop;
	private int[] stats;
	private String entityName;
	
	public ElementFluid()
	{

	}
	
    public ElementFluid(ByteBuf buf) 
    {

    }

	@Override
	public void render(int x, int y) 
	{

	}

	@Override
	public int getWidth() 
	{
		return 30;
	}

	@Override
	public int getHeight() 
	{
		return 30;
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		
	}

	@Override
	public int getID() 
	{
		return 0; //TheOneProbeSupport.ELEMENT_NESTING_PEN;
	}

}
