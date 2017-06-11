package com.gendeathrow.hatchery.api.tileentities;

public abstract interface IContainerUpdate 
{
	
    int getField(int id);

    void setField(int id, int value);

    int getFieldCount();

}
