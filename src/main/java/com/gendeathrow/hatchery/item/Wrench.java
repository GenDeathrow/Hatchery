package com.gendeathrow.hatchery.item;

import java.util.Arrays;
import java.util.List;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.storage.ISidedInterface;
import com.gendeathrow.hatchery.storage.SidedInterface.InterfaceOption;
import com.gendeathrow.hatchery.storage.SidedInterface.InterfaceType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Wrench extends Item
{
		
	public static List<String> modes = Arrays.asList("info", "power", "item", "fluid");
	
	public int currentMode = 0;
	
	public Wrench()
	{
		super();
		this.setCreativeTab(Hatchery.hatcheryTabs);
        this.setMaxStackSize(1);
	}
	
	@Override
    public String getItemStackDisplayName(ItemStack stack)
    {
		
		return super.getItemStackDisplayName(stack);
    	
    }
	 
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		if (!worldIn.isRemote)
        {
			if(playerIn.isSneaking())
			{
	    	//switch modes
				if(currentMode < modes.size()-1)
					currentMode++;
				else
					currentMode = 0;
				System.out.println("--"+ modes.get(currentMode)); 	
	    	
			}
        }
    	
        return new ActionResult<ItemStack>(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
    }
    
    
	@Override
	public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		worldIn.playSound((EntityPlayer)null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.NEUTRAL, 0.08F, 1F / (itemRand.nextFloat() * 0.2F + 0.9F));
        
		if (!worldIn.isRemote)
        {
			ItemStack wrench = playerIn.getHeldItem(hand);
			
    	    if (hand == EnumHand.OFF_HAND && playerIn.getHeldItemMainhand().getItem() == ModItems.wrench) 
    	    {
    	    	return EnumActionResult.FAIL;
    	    }
    	    

    	    if(modes.get(currentMode) == "info") {
    	    	System.out.println("++ info");
    	    }
    	    else if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof ISidedInterface)
    	    {

    	    	ISidedInterface interfaceTile = (ISidedInterface)playerIn.world.getTileEntity(pos);

   	    		if(interfaceTile.getInterface().tryToggleInterface(facing, getTypeForMode()))
   	    			System.out.println(interfaceTile.getInterface().getInterfaceOnFacing(facing, getTypeForMode()));
    	    	
    	    	return EnumActionResult.PASS;
        	}
        }
       return EnumActionResult.PASS;
    }
	
	private InterfaceType getTypeForMode() {
		if(modes.get(currentMode) == "power")
			return InterfaceType.Power;
		else if(modes.get(currentMode) == "fluid")
			return InterfaceType.Fluid;
		else if(modes.get(currentMode) == "item")
			return InterfaceType.Item;
		else return null;
	}
	
	static NBTTagCompound getNBT(ItemStack stack) 
	{
	    if (stack.getTagCompound() == null) 
	    {
	        stack.setTagCompound(new NBTTagCompound());
	    }
	    return stack.getTagCompound();
	}
    
}
