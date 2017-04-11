package com.gendeathrow.hatchery.block.fertilizermixer;

import javax.annotation.Nullable;

import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.core.init.ModItems;

public class FertilizerMixerTileEntity extends TileEntity implements IInventory, ITickable
{
	private ItemStack[] inventory = new ItemStack[5];
	
	private FluidTank waterTank = new FluidTank(new FluidStack(FluidRegistry.WATER, 0), 20000){
		@Override
	    public boolean canDrain()
	    {
			return false;
		}
	};
	
	private FluidTank fertilizerTank = new FluidTank(new FluidStack(ModFluids.liquidfertilizer, 0), 20000){
		@Override
		public boolean canFill()
		{
			return false;
		}
	};
	
	public FluidTank getWaterTank()
	{
		return waterTank;
	}
	
	public FluidTank getFertilizerTank()
	{
		return fertilizerTank;
	}
	
	public ItemStack[] getItemInventory()
	{
		return inventory;
	}
		
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    
    
    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

	private int furnaceBurnTime;
    /** The number of ticks that a fresh copy of the currently-burning item would keep the furnace burning for */
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;
	
	@Override
	public void update() 
	{

		if(waterTank.getFluidAmount() > 0)
		{
			/* used for reference. 
		}
			 boolean flag = this.isBurning();
		        boolean flag1 = false;

		        if (this.isBurning())
		        {
		            --this.furnaceBurnTime;
		        }

		        if (!this.worldObj.isRemote)
		        {
		            if (this.isBurning() || this.furnaceItemStacks[1] != null && this.furnaceItemStacks[0] != null)
		            {
		                if (!this.isBurning() && this.canSmelt())
		                {
		                    this.furnaceBurnTime = getItemBurnTime(this.furnaceItemStacks[1]);
		                    this.currentItemBurnTime = this.furnaceBurnTime;

		                    if (this.isBurning())
		                    {
		                        flag1 = true;

		                        if (this.furnaceItemStacks[1] != null)
		                        {
		                            --this.furnaceItemStacks[1].stackSize;

		                            if (this.furnaceItemStacks[1].stackSize == 0)
		                            {
		                                this.furnaceItemStacks[1] = furnaceItemStacks[1].getItem().getContainerItem(furnaceItemStacks[1]);
		                            }
		                        }
		                    }
		                }

		                if (this.isBurning() && this.canSmelt())
		                {
		                    ++this.cookTime;

		                    if (this.cookTime == this.totalCookTime)
		                    {
		                        this.cookTime = 0;
		                        this.totalCookTime = this.getCookTime(this.furnaceItemStacks[0]);
		                        this.smeltItem();
		                        flag1 = true;
		                    }
		                }
		                else
		                {
		                    this.cookTime = 0;
		                }
		            }
		            else if (!this.isBurning() && this.cookTime > 0)
		            {
		                this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
		            }

		            if (flag != this.isBurning())
		            {
		                flag1 = true;
		                BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
		            }
		        }

		        if (flag1)
		        {
		            this.markDirty();
		        }
		        */
		    }
	}

		    public int getCookTime(@Nullable ItemStack stack)
		    {
		        return 200;
		    }
	
	
	
	
	/// INVENTORY
	@Override
	public String getName() { return null; }

	@Override
	public boolean hasCustomName() { return false; }

	@Override
	public int getSizeInventory() 
	{
		return 5;
	}

	@Override
	public ItemStack getStackInSlot(int index) 
	{
		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.inventory, index, count);

        if (itemstack != null)
        {
            this.markDirty();
        }

			
		return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		this.inventory[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) { return true; }

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		if(index == 0 && (stack.getItem() == ModItems.manure || stack.getItem() == Item.getItemFromBlock(ModBlocks.manureBlock)))
				return true;
		else if(index == 1 && stack.getItem() == Items.WATER_BUCKET)
			return true;
		else if(index == 3 && stack.getItem() == ModFluids.getFertilizerBucket().getItem())
			return true;

		return false; 
	}

	@Override
	public int getField(int id) 
	{ 
		if(id == 0)
		{
			return this.waterTank.getFluidAmount();
		}
		else if(id == 1)
		{
			return this.fertilizerTank.getFluidAmount();
		}
		return 0; 
	}
	
	public int waterLevel = 0;
	public int fertilizerLevel = 0;

	@Override
	public void setField(int id, int value) 
	{ 

			if(id == 0)
			{
				waterLevel = value;
			}
			else if(id == 1)
			{
				fertilizerLevel = value;
			}
	}

	@Override
	public int getFieldCount() { return 0; }

	@Override
	public void clear() { }

	
	
	// Capabilities
	
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
       	this.waterTank.readFromNBT(tag.getCompoundTag("waterTank"));
        this.fertilizerTank.readFromNBT(tag.getCompoundTag("fertilizerTank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
    	tag = super.writeToNBT(tag);
    	
    	  NBTTagCompound waterTTag = new NBTTagCompound();
          NBTTagCompound fertTTag = new NBTTagCompound();
    		this.waterTank.writeToNBT(waterTTag);
    		this.fertilizerTank.writeToNBT(fertTTag);
        tag.setTag("waterTank", waterTTag);
        tag.setTag("fertilizerTank", fertTTag);
        return tag;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing == EnumFacing.DOWN)
            return (T) this.fertilizerTank;
        else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) this.waterTank;
        else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
        {
            return (T) new InvWrapper(this);
        }
        return super.getCapability(capability, facing);
    }

    
}
