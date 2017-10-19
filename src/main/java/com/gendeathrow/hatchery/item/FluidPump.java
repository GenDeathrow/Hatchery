package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.Hatchery;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class FluidPump extends Item
{

	
	public FluidPump()
	{
		this.setCreativeTab(Hatchery.hatcheryTabs);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		InventoryPlayer inventory = playerIn.inventory;
		for(int i=0; i < inventory.getSizeInventory(); i++)
		{
			stack = inventory.getStackInSlot(i);
			
			if(stack != null && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN))
			{
				System.out.println("Has fluids:"+ stack.getDisplayName());
			}
			else if(stack != null )
			{
				System.out.println(stack.getDisplayName() + "not a tank" );
			}
				
		}
		return null;
	}
	
}
