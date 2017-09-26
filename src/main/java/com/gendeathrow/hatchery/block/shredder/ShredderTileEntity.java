package com.gendeathrow.hatchery.block.shredder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import cofh.api.energy.IEnergyReceiver;

import com.gendeathrow.hatchery.api.tileentities.IContainerUpdate;
import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.item.upgrades.BaseUpgrade;
import com.gendeathrow.hatchery.item.upgrades.RFEfficiencyUpgrade;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

public class ShredderTileEntity extends TileUpgradable implements ITickable, IContainerUpdate, IEnergyReceiver
{
	public EnergyStorageRF energy= new EnergyStorageRF(100000).setMaxReceive(100);
	   
	public int animationTicks;  
	public int prevAnimationTicks;  
	
	int baseRFStorage = 100000;
	double rfEffencyMultpyler = 1;
	double upgradeSpeedMulipyler = 1;
	
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
		
		shredderRecipes.add(new ShredderRecipe(new ItemStack(ModItems.featherFiber), new ItemStack(ModItems.featherMeal)));
	}

	private int shreddingTime;
    private int currentItemShreddingTime;
    private int shredTime;
    private int totalshredTime;
    
    private int rfTick = 20;
    
    private ItemStack shreddedItem;
	
	@Override
	public void update() 
	{

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

		
		
        if (this.worldObj != null && !this.worldObj.isRemote)
        {
        	updateUpgrades();

    		boolean flag = this.isShredding();
    		boolean flag1 = false;
    		
            --this.transferCooldown;
            
            if (!this.isOnTransferCooldown())
            {
            	  this.captureDroppedItems();
                  this.setTransferCooldown(8);
            }
            
               
   			if (!this.isShredding() && this.canShred() && hasPower())
   			{
   	           	this.shreddingTime = setShredTime(this.inventory.getStackInSlot(0));
               	this.currentItemShreddingTime = this.shreddingTime;
              	
               	if(this.isShredding())
               	{
               		shreddedItem = this.inventory.extractItemInternal(0, 1, false);
               	}
   			}
   			
   			if(this.isShredding() && hasPower())
   			{
   				this.shreddingTime -= 1 * this.upgradeSpeedMulipyler;
   				
   				this.energy.extractEnergy((int)((rfTick * this.upgradeSpeedMulipyler) * this.rfEffencyMultpyler), false);
   				
   				if(this.shreddingTime <= 0)
   					this.shredItem();
   			}
   			
   			
   			if(flag != (this.isShredding() && hasPower())){
   				flag1 = true;
   				ShredderBlock.setActive(this.worldObj, this.pos, this.worldObj.getBlockState(this.pos), this.isShredding() && hasPower());			//  BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
   			}
   			
   	        if (flag1)
   	            this.markDirty();

   		}

//			else if(!this.isShredding() && this.shredTime > 0)
//			{
//				 this.shredTime = MathHelper.clamp_int(this.shredTime - 2, 0, this.totalshredTime);
//			}
			

   }
	@Override
	public boolean canUseUpgrade(ItemStack item)
	{
		return item.getItem() == ModItems.rfCapacityUpgradeTier1 || item.getItem() == ModItems.speedUpgradeTier || item.getItem() instanceof RFEfficiencyUpgrade;
	}
	
	protected void updateUpgrades()
	{
		boolean rfupgrade = false;
		boolean rfcapacity = false;
		boolean speedupgrade = false;
		
		for(ItemStack upgrade : this.getUpgrades())
		{
			if(upgrade == null) continue;
		
			
			if(upgrade.getItem() instanceof RFEfficiencyUpgrade)
			{
				rfupgrade = true;
				this.rfEffencyMultpyler = 1 - ((BaseUpgrade)upgrade.getItem()).getUpgradeTier(upgrade, "") * 0.10;
			}
			if(upgrade.getItem() == ModItems.speedUpgradeTier && speedupgrade == false)
			{
				speedupgrade = true;
				this.upgradeSpeedMulipyler = 1 + ((BaseUpgrade)upgrade.getItem()).getUpgradeTier(upgrade, "") * 1 ;
			}
			
						
			if(upgrade.getItem() == ModItems.rfCapacityUpgradeTier1 && rfcapacity == false)
			{
				int tier = upgrade.getMetadata()+1;
				
				int newEnergy = this.baseRFStorage;
				
				rfcapacity = true;

				if(tier == 1) {
					newEnergy += (int)(newEnergy * 0.5); 
				}
				else if(tier == 2) {
					newEnergy += (int)(newEnergy * 0.75); 
				}
				else if(tier == 3) {
					newEnergy += (int)(newEnergy * 1); 
				}

				if(newEnergy != this.energy.getMaxEnergyStored())
				{
					this.energy.setCapacity(newEnergy);
				}
			}
		}
		
		if(!rfcapacity && this.energy.getMaxEnergyStored() != this.baseRFStorage)
		{
			this.energy.setCapacity(this.baseRFStorage);
		}
		
		if(!rfupgrade && this.rfEffencyMultpyler != 1)
		{
			this.rfEffencyMultpyler = 1;
		}
		
		if(!speedupgrade && this.upgradeSpeedMulipyler != 1)
		{
			this.upgradeSpeedMulipyler = 1;
		}
	}
	
	public int getShreddingTime()
	{
		return this.shreddingTime;
	}
	
	public int getTotalShredTime()
	{
		return this.shredTime;
	}
	
	public int setShredTime(ItemStack stack)
	{
		return this.shredTime = this.getRecipe(stack).shredTime;
	}
	
	public void shredItem()
	{
		ItemStack stack = this.inventory.getStackInSlot(0);
		boolean flag = false;		
		
		if(shreddedItem != null && isShreddableItem(shreddedItem))
		{
			ShredderRecipe recipe = getRecipe(shreddedItem);
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
    
    public boolean hasPower()
    {
    	return energy.getEnergyStored() >= (rfTick * this.upgradeSpeedMulipyler) * this.rfEffencyMultpyler;
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
		if(stack == null) return false;
		
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

        for (EntityItem entityitem : getCaptureItems(this.worldObj, this.getXPos(), this.getYPos(), this.getZPos()))
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
	
	
    public static List<EntityItem> getCaptureItems(World worldIn, double x, double y, double z)
    {
        return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(x - 0.5D, y, z - 0.5D, x + 0.5D, y + 1.5D, z + 0.5D), EntitySelectors.IS_ALIVE);
    }

		
	
    public double getXPos()
    {
        return (double)this.pos.getX() + 0.5D;
    }

    /**
     * Gets the world Y position for this hopper entity.
     */
    public double getYPos()
    {
        return (double)this.pos.getY() + 0.5D;
    }

    /**
     * Gets the world Z position for this hopper entity.
     */
    public double getZPos()
    {
        return (double)this.pos.getZ() + 0.5D;
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
			case 2:
				return this.currentItemShreddingTime;
			case 3:
				return this.energy.getMaxEnergyStored();
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
				break;
			case 2:
				this.currentItemShreddingTime = value;
				break;
			case 3:
				this.energy.setCapacity(value);
				break;

		}
	}

	@Override
	public int getFieldCount() { return 4; }

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.energy.readFromNBT(tag);
		this.inventory.deserializeNBT(tag.getCompoundTag("inventory"));
	        
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag = super.writeToNBT(tag);

		tag.setTag("inventory",this.inventory.serializeNBT());
		this.energy.writeToNBT(tag);
		return tag;
	}
	    
	    //RF ENERGY
	@Override
	public int getEnergyStored(EnumFacing from) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energy.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return energy.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		if(oldState.getBlock() == ModBlocks.shredder && newSate.getBlock() == ModBlocks.shredder){
			return false;
		}
		else 
			return oldState != newSate;
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
			return (T) this.inventory;
		else if (capability == CapabilityEnergy.ENERGY) 
			return (T) this.energy;
		
	        return super.getCapability(capability, facing);
	}

	    
	public class ShredderRecipe
	{
		private ItemStack itemIn;
		private ItemStack itemOut;
		private ItemStack itemExtra;
		private int chance;
		private int shredTime;
		private Random rand = new Random();
	    	
		public ShredderRecipe(ItemStack itemIn, ItemStack itemOut)
		{
			this(itemIn, itemOut, (ItemStack)null);
		}
	    	
		public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra)
		{
			this(itemIn, itemOut, itemExtra, 3, 100);
		}
	    	
		public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra, int chance, int shredTime)
		{
			this.itemIn = itemIn;
			this.itemOut = itemOut;
			this.itemExtra = itemExtra;
			this.chance = chance;
			this.shredTime = shredTime;
		}
	    	
		public boolean isInputItem(ItemStack stack)
		{
			return this.itemIn.isItemEqual(stack); 
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
