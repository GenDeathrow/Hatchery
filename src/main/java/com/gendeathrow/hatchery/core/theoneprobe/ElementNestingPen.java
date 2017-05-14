package com.gendeathrow.hatchery.core.theoneprobe;

import io.netty.buffer.ByteBuf;
import mcjty.theoneprobe.api.IElement;

public class ElementNestingPen implements IElement 
{
	private long nextDrop;
	private int[] stats;
	private String entityName;
	
	public ElementNestingPen()
	{

	}
	
    public ElementNestingPen(ByteBuf buf) 
    {

    }

	@Override
	public void render(int x, int y) 
	{
		System.out.println("test");
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
		return TheOneProbeSupport.ELEMENT_NESTING_PEN;
	}

}
