package com.gendeathrow.hatchery.block.feeder;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class FeederTileEntity extends TileEntity  implements IInventory
{

	
	private int seedInventory = 0;
	private int maxSeedInventory = 200;
	
	
	
	
	ItemStack[] inventory = new ItemStack[1];
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
//        NBTTagList nbttaglist = compound.getTagList("Items", 1);
//
//        for (int i = 0; i < nbttaglist.tagCount(); ++i)
//        {
//            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
//            int j = nbttagcompound.getByte("Slot") & 255;
//
//            if (j >= 0 && j < this.inventory.length)
//            {
//                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound);
//            }
//        }
		
		
		this.seedInventory = compound.getInteger("seeds");
		
		super.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{

//		NBTTagList nbttaglist = new NBTTagList();
//
//        for (int i = 0; i < this.inventory.length; ++i)
//        {
//            if (this.inventory[i] != null)
//            {
//                NBTTagCompound nbttagcompound = new NBTTagCompound();
//                nbttagcompound.setByte("Slot", (byte)i);
//                this.inventory[i].writeToNBT(nbttagcompound);
//                nbttaglist.appendTag(nbttagcompound);
//            }
//        }
//        
//        compound.setTag("Items", nbttaglist);
		
		compound.setInteger("seeds", this.seedInventory);
		return super.writeToNBT(compound);
	}
	
	
	public int getSeedsInv()
	{
		return this.seedInventory;
	}
	
	public void decrSeedsInv()
	{
		this.seedInventory--;
	}
	
	public int getMaxSeedInv()
	{
		return this.maxSeedInventory;
	}
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////
	// Inventory
	////////////////////////////////////////////////////
	public void dropContents()
	{
		
        for (int i = 0; i < this.inventory.length; ++i)
        {
        	ItemStack stack = ItemStackHelper.getAndRemove(this.inventory, i);
        	
        	if(stack != null)
        	{
        		this.worldObj.spawnEntityInWorld(new EntityItem(worldObj, this.pos.getX(), this.pos.getY()+1, this.pos.getZ(), stack));
        	}
        }
	}
    
    @Override
	public String getName() 
	{
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public int getSizeInventory() 
	{
		return 1;
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
	public void setInventorySlotContents(int index,@Nullable ItemStack stack) 
	{
		if(isItemValidForSlot(index, stack))
		{
			if (stack != null && seedInventory < this.maxSeedInventory)
			{
					
				this.seedInventory++;
				stack.stackSize--;
				if(stack.stackSize <= 0) stack= null;
		

			}
		}
        
		this.markDirty();
	}
	
	public void setSeeds(int qty, ItemStack stack)
	{
		if(isItemValidForSlot(0, stack))
		{
			if (stack != null && seedInventory < this.maxSeedInventory)
			{
				this.seedInventory += qty;
				if(this.seedInventory > this.maxSeedInventory)
				{
					qty = qty - (this.seedInventory - this.maxSeedInventory);
					this.seedInventory = this.maxSeedInventory;
				}
				
				stack.stackSize -= qty;
				
				if(stack.stackSize <= 0) stack= null;
			}
		}
        
		this.markDirty();	
	}
	

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		return stack.getItem() instanceof ItemSeeds;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {return 0;}

	@Override
	public void clear() 
	{
	
	        for (int i = 0; i < this.inventory.length; ++i)
	        {
	            this.inventory[i] = null;
	        }
	}
	
    public static IItemHandler getItemHandler(TileEntity tile, EnumFacing side) 
    {
        if (tile == null) 
        {
            return null;
        }

        IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);

        if (handler == null) 
        {
            if (side != null && tile instanceof ISidedInventory) 
            {
                handler = new SidedInvWrapper((ISidedInventory) tile, side);
            } else if (tile instanceof IInventory) 
            {
                handler = new InvWrapper((IInventory) tile);
            }
        }

        return handler;
    }
    
    
    public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, @Nullable EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory && side != null)
        {
            ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k = 0; k < aint.length && stack != null && stack.stackSize > 0; ++k)
            {
                stack = insertStack(inventoryIn, stack, aint[k], side);
            }
        }
        else
        {
            int i = inventoryIn.getSizeInventory();

            for (int j = 0; j < i && stack != null && stack.stackSize > 0; ++j)
            {
                stack = insertStack(inventoryIn, stack, j, side);
            }
        }

        if (stack != null && stack.stackSize == 0)
        {
            stack = null;
        }

        return stack;
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
    
    /**
     * Can this hopper insert the specified item from the specified slot on the specified side?
     */
    private static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
    {
        return !inventoryIn.isItemValidForSlot(index, stack) ? false : !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
    }
    
    private static boolean canCombine(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() != stack2.getItem() ? false : (stack1.getMetadata() != stack2.getMetadata() ? false : (stack1.stackSize > stack1.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(stack1, stack2)));
    }

}
