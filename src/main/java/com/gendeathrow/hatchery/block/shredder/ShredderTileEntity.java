package com.gendeathrow.hatchery.block.shredder;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;

import com.gendeathrow.hatchery.block.InventoryStorage;
import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModItems;

public class ShredderTileEntity extends TileUpgradable implements ITickable, IInventory, IEnergyReceiver
{
	protected EnergyStorage energy= new EnergyStorage(100000);
	protected net.minecraftforge.energy.EnergyStorage energy2 = new net.minecraftforge.energy.EnergyStorage(100000);
	
	   
	public int animationTicks;
	public int prevAnimationTicks;

	protected InventoryStorage inventory = new InventoryStorage(this, 3);
	protected InventoryStorage upgrades = new InventoryStorage(this, 2);
	
	protected ArrayList<ShredderRecipe> shredderRecipes = new ArrayList<ShredderRecipe>();
	
	
	
    private int transferCooldown = -1;
	int slotIn = 0;
	
	public ShredderTileEntity() 
	{
		super(2);
		
		shredderRecipes.add(new ShredderRecipe(new ItemStack(Items.FEATHER), new ItemStack(ModItems.featherFiber), new ItemStack(ModItems.featherMeal)));
	}

	@Override
	public void update() 
	{
		if (worldObj.isRemote) 
		{
			prevAnimationTicks = animationTicks;
			if (animationTicks < 360)
				animationTicks += 5;
			if (animationTicks >= 360) 
			{
				animationTicks -= 360;
				prevAnimationTicks -= 360;
			}
		}
		
		
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.transferCooldown;
            
            
            if (!this.isOnTransferCooldown())
            {
            	  this.captureDroppedItems();
                  this.setTransferCooldown(8);
                  this.shredItem();
            }
            
    		if ((energy.getEnergyStored() > 0)) 
			{
				for (EnumFacing facing : EnumFacing.VALUES) 
				{
						TileEntity tile = worldObj.getTileEntity(pos.offset(facing));
						if (tile != null && tile instanceof IEnergyReceiver) 
						{
							//int received = ((IEnergyReceiver) tile).receiveEnergy(facing.getOpposite(), storage.getEnergyStored(), false);
							//this.energy.extractEnergy(facing, received, false);
						}
				}
			}
        }
	}
	
	public void shredItem()
	{
		ItemStack stack = this.inventory.getStackInSlot(0);
		boolean flag = false;		
		
		if(stack != null && this.isShreddableItem(stack))
		{
			ShredderRecipe recipe = getRecipe(stack);

			
			if(recipe != null)
			{
				if(recipe.getOutputItem() != null)
				{
					if(this.insertStack(inventory, recipe.getOutputItem(), animationTicks, EnumFacing.DOWN) == null)
						flag = true;
						
				}
				ItemStack extra = recipe.getExtraItem();
				if(extra != null)
				{
					if(this.insertStack(inventory, extra, animationTicks, EnumFacing.DOWN) == null)
						flag = true;
				}
			}

			if(flag)
				this.inventory.decrStackSize(0, 1);
		}
	}
	
	protected ShredderRecipe getRecipe(ItemStack stack)
	{
		for(ShredderRecipe recipe : this.shredderRecipes)
		{
			if(recipe.isInputItem(stack))
			{
				return recipe;
			}
		}
		return null;
	}

	protected boolean isShreddableItem(ItemStack stack)
	{
		for(ShredderRecipe recipe : this.shredderRecipes)
		{
			if(recipe.isInputItem(stack))
			{
				return true;
			}
		}
		return false;
	}
	
    public boolean isOnTransferCooldown()
    {
        return this.transferCooldown > 0;
    }
    
    public void setTransferCooldown(int cooldown)
    {
    	this.transferCooldown = cooldown;
    }
    
	public boolean captureDroppedItems()
	{
        EnumFacing enumfacing = EnumFacing.DOWN;

        boolean flag = false;

        for (EntityItem entityitem : TileEntityHopper.getCaptureItems(this.worldObj, this.pos.getX(), this.pos.getY(), this.pos.getZ()))
        {
        	if(isShreddableItem(entityitem.getEntityItem()))
        	{
                ItemStack itemstack = entityitem.getEntityItem().copy();
        		ItemStack itemstack1 = insertStack(this.inventory, entityitem.getEntityItem(), 0, enumfacing);
        		
                if (itemstack1 != null && itemstack1.stackSize != 0)
                {
                	entityitem.setEntityItemStack(itemstack1);
                }
                else
                {
                    flag = true;
                    entityitem.setDead();
                }
                
        		return flag;
        	}
        }
		return false;
	}
	

	
	/// INVENTORY
    private boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1 == null || itemstack1.stackSize != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }
        else
        {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i; ++j)
            {
                ItemStack itemstack = inventoryIn.getStackInSlot(j);

                if (itemstack == null || itemstack.stackSize != itemstack.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    /**
     * Insert the specified stack to the specified inventory and return any leftover items
     */
    private static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
    {
        ItemStack itemstack = inventoryIn.getStackInSlot(index);

        if (canInsertItemInSlot(inventoryIn, stack, index, side))
        {
            boolean flag = false;

            if (itemstack == null)
            {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
                if (max >= stack.stackSize)
                {
                inventoryIn.setInventorySlotContents(index, stack);
                stack = null;
                }
                else
                {
                    inventoryIn.setInventorySlotContents(index, stack.splitStack(max));
                }
                flag = true;
            }
            else if (canCombine(itemstack, stack))
            {
                //Forge: BUGFIX: Again, make things respect max stack sizes.
                int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
                if (max > itemstack.stackSize)
                {
                int i = max - itemstack.stackSize;
                int j = Math.min(stack.stackSize, i);
                stack.stackSize -= j;
                itemstack.stackSize += j;
                flag = j > 0;
                }
            }
        }

        return stack;
    }
    
    private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.stackSize > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }
    
    /**
     * Can this hopper insert the specified item from the specified slot on the specified side?
     */
    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
    {
        return !inventoryIn.isItemValidForSlot(index, stack) ? false : !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
    }
    
    
    private static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int i : aint)
            {
                if (isidedinventory.getStackInSlot(i) != null)
                {
                    return false;
                }
            }
        }
        else
        {
            int j = inventoryIn.getSizeInventory();

            for (int k = 0; k < j; ++k)
            {
                if (inventoryIn.getStackInSlot(k) != null)
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    
		@Override
		public String getName() { return null; }

		@Override
		public boolean hasCustomName() { return false; }

		@Override
		public int getSizeInventory() 
		{
			return this.inventory.getSizeInventory();
		}

		@Override
		public ItemStack getStackInSlot(int slot) 
		{
			return this.inventory.getStackInSlot(slot);
		}

		@Override
		public ItemStack decrStackSize(int slot, int count) 
		{
			return this.inventory.decrStackSize(slot, count);
		}

		@Override
		public ItemStack removeStackFromSlot(int slot) 
		{
			return this.inventory.removeStackFromSlot(slot);
		}

		@Override
		public void setInventorySlotContents(int slot, ItemStack stack) 
		{
			this.inventory.setInventorySlotContents(slot, stack);
		}

		@Override
		public int getInventoryStackLimit() 
		{
			return 64;
		}

		@Override
		public boolean isUseableByPlayer(EntityPlayer player) { return true; }

		@Override
		public void openInventory(EntityPlayer player) { this.inventory.openInventory(player); }

		@Override
		public void closeInventory(EntityPlayer player) { this.inventory.closeInventory(player); }

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) 
		{
			if(index == this.slotIn && this.isShreddableItem(stack))
					return true;
			else if(index > this.slotIn && (stack.getItem() == ModItems.featherFiber || stack.getItem() == ModItems.featherMeal))
				return false;
			return false; 
		}

		@Override
		public int getField(int id) 
		{ 
	        switch (id)
	        {
	            case 0:
	            	return this.energy.getEnergyStored();
	            default:
	                return 0;
	        }

		}
		
		@Override
		public void setField(int id, int value) 
		{ 
			
	        switch (id)
	        {
	            case 0:
	            	this.energy.setEnergyStored(value);
	                break;

	        }
		}

		@Override
		public int getFieldCount() { return 1; }

		@Override
		public void clear() { 
			this.inventory.clear();
		}
		
		

	    @Override
	    public void readFromNBT(NBTTagCompound tag)
	    {
	        super.readFromNBT(tag);
	        this.energy.readFromNBT(tag);
	        this.inventory.readFromNBT(tag);
	    }

	    @Override
	    public NBTTagCompound writeToNBT(NBTTagCompound tag)
	    {
	    	tag = super.writeToNBT(tag);
	    	
	        this.inventory.writeToNBT(tag);
	        this.energy.writeToNBT(tag);
	        return tag;
	    }


	    // ENERGY
	    
		@Override
		public int getEnergyStored(EnumFacing from) {
			return this.energy.getEnergyStored();
		}

		@Override
		public int getMaxEnergyStored(EnumFacing from) {
			return this.energy.getMaxEnergyStored();
		}

		@Override
		public boolean canConnectEnergy(EnumFacing from) {
			return true;
		}

		@Override
		public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
			return this.energy.receiveEnergy(maxReceive, simulate);
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
	    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
	        {
	            return (T) new InvWrapper(this);
	        }
	        return super.getCapability(capability, facing);
	    }

	    
	    
	    
	    public class ShredderRecipe
	    {
	    	
	    	private ItemStack itemIn;
	    	private ItemStack itemOut;
	    	private ItemStack itemExtra;
	    	private int chance;
	    	private Random rand = new Random();
	    	
	    	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut)
	    	{
	    		this.itemIn = itemIn;
	    		this.itemOut = itemOut;
	    		this.itemExtra = null;
	    	}
	    	
	    	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra)
	    	{
	    		this(itemIn, itemOut, itemExtra, 3);
	    	}
	    	
	    	public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra, int chance)
	    	{
	    		this.itemIn = itemIn;
	    		this.itemOut = itemOut;
	    		this.itemExtra = itemExtra;
	    		this.chance = chance;
	    	}
	    	
	    	public boolean isInputItem(ItemStack stack)
	    	{
	    		return this.itemIn.getItem() == stack.getItem() && this.itemIn.getMetadata() == stack.getMetadata(); 
	    	}
	    	
	    	public ItemStack getOutputItem()
	    	{
				return itemOut.copy();
	    	}
	    	
	    	@Nullable
	    	public ItemStack getExtraItem()
	    	{
	    		if(rand.nextInt(chance) == 1)
	    			return itemExtra.copy();
	    		else return null;
	    	}
	    	
	    }

}
