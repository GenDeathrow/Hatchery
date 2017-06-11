package com.gendeathrow.hatchery.block.shredder;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import com.gendeathrow.hatchery.api.tileentities.IContainerUpdate;
import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

public class ShredderTileEntity extends TileUpgradable implements ITickable, IContainerUpdate
{
	public EnergyStorageRF energy= new EnergyStorageRF(100000);
	   
	public int animationTicks;  
	public int prevAnimationTicks;  

	protected InventoryStroageModifiable inventory = new InventoryStroageModifiable(3)
	{
		@Override
		public boolean canExtractSlot(int slot)	{
			if(slot > 0) 
				return true;
			return false;
		}
		
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			if(slot == 0 && isShreddableItem(stack)) return true;
			return false;
		}
	};
	
	public static ArrayList<ShredderRecipe> shredderRecipes = new ArrayList<ShredderRecipe>();
	
    private int transferCooldown = -1;
	int slotIn = 0;
	
	public ShredderTileEntity() 
	{
		super(2);
		
		shredderRecipes.add(new ShredderRecipe(new ItemStack(Items.FEATHER), new ItemStack(ModItems.featherFiber), new ItemStack(ModItems.featherMeal)));
	}

	private int shreddingTime;
    private int currentItemShreddingTime;
    private int shredTime;
    private int totalshredTime;
	
	@Override
	public void update() 
	{

		boolean flag = this.isShredding();
		boolean flag1 = false;
		
		if (worldObj.isRemote && ShredderBlock.isActive(this.worldObj.getBlockState(this.pos))) 
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
		
		if(this.isShredding())
		{
			this.shreddingTime--;
		}
		
		
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
            --this.transferCooldown;
            
            
            
            if (!this.isOnTransferCooldown())
            {
            	  this.captureDroppedItems();
                  this.setTransferCooldown(8);
                  //this.shredItem();
            }
            
               
    		if ((energy.getEnergyStored() > 10)) 
			{
    			if (this.isShredding() || this.inventory.getStackInSlot(0) != null)
    			{
                	this.shreddingTime = 200;
                	this.currentItemShreddingTime = this.shreddingTime;
                	
                	if(this.isShredding())
                	{
                		flag1 = true;
               		 	if(this.inventory.getStackInSlot(0) != null)
               		 	{
               		 		this.inventory.extractItem(0, 1, false);
               		 		
               		 		if(this.inventory.getStackInSlot(0) != null)
               		 			this.inventory.setStackInSlot(0, null);
               		 	}
                		
                	}
    			}
    			
    			if(this.isShredding() && this.canShred() && this.shreddingTime > 0)
    			{
    				this.shreddingTime--;
    				this.energy.extractEnergy(20, false);
    				
    				if(this.shreddingTime <= 0)
    					this.shredItem();
    				
    				this.energy.extractEnergy(20,false);
    				
    				flag1 =true;
    			}

    		}
			else if(!this.isShredding() && this.shredTime > 0)
			{
				 this.shredTime = MathHelper.clamp_int(this.shredTime - 2, 0, this.totalshredTime);
			}
			
        }
        
		if(flag != this.isShredding())
		{
			flag1 = true;
			ShredderBlock.setActive(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos), this.isShredding());			//  BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
		}
		
        if (flag1)
        {
            this.markDirty();
        }
	}
	
	public void shredItem()
	{
		ItemStack stack = this.inventory.getStackInSlot(0);
		boolean flag = false;		
		
		if(stack != null && this.isShreddableItem(stack) && canShred())
		{
			ShredderRecipe recipe = getRecipe(stack);
			if(recipe != null)
			{
				if(recipe.hasOutput())
				{
					this.inventory.insertItemInternal(1, recipe.getOutputItem(), false);
					
					if(recipe.hasExtraOutput())
					{
						ItemStack extra = recipe.getExtraItem();
						if(extra != null)
							this.inventory.insertItemInternal(2, extra, false);
					}
					flag = true;
				}

			}

			if(flag)
				this.inventory.extractItemInternal(0, 1, false);
		}
	}
	
	private boolean canShred()
	{
        if (this.inventory.getStackInSlot(0) == null)
        {
            return false;
        }
        else
        {
            ShredderRecipe recipe = this.getRecipe(this.inventory.getStackInSlot(0));
            
            if (recipe == null) return false;
            
            ItemStack itemstack = recipe.itemOut;
            if( itemstack == null) return false;
            if (this.inventory.getStackInSlot(1) == null) return true;
            if (!this.inventory.getStackInSlot(1).isItemEqual(itemstack)) return false;
            int result = inventory.getStackInSlot(1).stackSize + itemstack.stackSize;
            return result <= 64 && result <= this.inventory.getStackInSlot(1).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
	}
	
    public boolean isShredding()
    {
        return this.shreddingTime > 0;
    }
    
    @SideOnly(Side.CLIENT)
    public static boolean isShredding(IInventory inventory)
    {
        return inventory.getField(1) > 0;
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

	public static boolean isShreddableItem(ItemStack stack)
	{
		
		for(ShredderRecipe recipe : shredderRecipes)
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
        		//ItemStack itemstack1 = insertStack(this, itemstack, 0, enumfacing);
                ItemStack itemstack1 = this.inventory.insertItemInternal(0, itemstack, false);
                
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
		return flag;
	}
	

		@Override
		public int getField(int id) 
		{ 
	        switch (id)
	        {
	            case 0:
	            	return this.energy.getEnergyStored();
	            case 1:
	            	return this.shreddingTime;
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
	            case 1:
	            	this.shreddingTime = value;

	        }
		}

		@Override
		public int getFieldCount() { return 2; }

	    @Override
	    public void readFromNBT(NBTTagCompound tag)
	    {
	        super.readFromNBT(tag);
	        this.energy.readFromNBT(tag);
	        //.getInteger("energy")
	       // this.inventory.readFromNBT(tag);
	        this.inventory.deserializeNBT(tag.getCompoundTag("inventory"));
	        
	    }

	    @Override
	    public NBTTagCompound writeToNBT(NBTTagCompound tag)
	    {
	    	tag = super.writeToNBT(tag);
	    	
	        //this.inventory.writeToNBT(tag);
	    	tag.setTag("inventory",this.inventory.serializeNBT());
	        this.energy.writeToNBT(tag);
	        return tag;
	    }


	    @Override
	    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	    {
	        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || super.hasCapability(capability, facing);
	    }

	    @SuppressWarnings("unchecked")
	    @Override
	    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	    {
	    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
	        {
	    		 return (T) this.inventory;
	        }
	    	if (capability == CapabilityEnergy.ENERGY) 
	        {
	    		return (T) this.energy;
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
	    	
	    	
	    	public boolean hasOutput()
	    	{
	    		return itemOut != null;
	    	}
	    	
	    	public boolean hasExtraOutput()
	    	{
	    		return itemExtra != null;
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
