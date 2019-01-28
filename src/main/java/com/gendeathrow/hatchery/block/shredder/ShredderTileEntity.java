package com.gendeathrow.hatchery.block.shredder;

import java.util.ArrayList;
import java.util.List;

import com.gendeathrow.hatchery.api.crafting.ShredderRecipe;
import com.gendeathrow.hatchery.api.tileentities.IContainerUpdate;
import com.gendeathrow.hatchery.block.TileUpgradable;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.item.upgrades.BaseUpgrade;
import com.gendeathrow.hatchery.storage.EnergyStorageRF;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

public class ShredderTileEntity extends TileUpgradable implements ITickable, IContainerUpdate
{
	
	
	public static ArrayList<ShredderRecipe> shredderRecipes = new ArrayList<ShredderRecipe>();
	
	
	public EnergyStorageRF energy= new EnergyStorageRF(100000).setMaxReceive(100);
	   
	public int animationTicks;  
	public int prevAnimationTicks;  
	
	int baseRFStorage = 100000;
	double rfEffencyMultpyler = 1;
	double upgradeSpeedMulipyler = 1;
	
	
	protected InventoryStroageModifiable inputInventory = new InventoryStroageModifiable("inputItems", 1)
	{
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			return isShreddableItem(stack);
		}
	};
	
	
	protected InventoryStroageModifiable outputInventory = new InventoryStroageModifiable("outputItems", 2)
	{
		@Override
		public boolean canInsertSlot(int slot, ItemStack stack)	{
			return false;
		}
	};

	
    private int transferCooldown = -1;
	int slotIn = 0;
	
	public ShredderTileEntity() 
	{
		super(2);
		
	}
	
	
	public static void registerShredderRecipes() {
		
		addRecipe(new ItemStack(Items.FEATHER), new ItemStack(ModItems.featherFiber), new ItemStack(ModItems.featherMeal));
		addRecipe(new ItemStack(ModItems.featherFiber), new ItemStack(ModItems.featherMeal));

		//All the Dyes
		addRecipe(new ItemStack(Items.BONE), new ItemStack(Items.DYE, 4, EnumDyeColor.WHITE.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.YELLOW_FLOWER, 1, BlockFlower.EnumFlowerType.DANDELION.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.YELLOW.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.POPPY.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.RED.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.BLUE_ORCHID.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.LIGHT_BLUE.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.ALLIUM.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.MAGENTA.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.HOUSTONIA.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.SILVER.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.RED_TULIP.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.RED.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.ORANGE_TULIP.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.ORANGE.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.WHITE_TULIP.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.SILVER.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.PINK_TULIP.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.PINK.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta()), new ItemStack(Items.DYE, 2, EnumDyeColor.SILVER.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.SUNFLOWER.getMeta()), new ItemStack(Items.DYE, 3, EnumDyeColor.YELLOW.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.SYRINGA.getMeta()), new ItemStack(Items.DYE, 3, EnumDyeColor.MAGENTA.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.ROSE.getMeta()), new ItemStack(Items.DYE, 3, EnumDyeColor.RED.getDyeDamage()));
		addRecipe(new ItemStack(Blocks.DOUBLE_PLANT, 1, BlockDoublePlant.EnumPlantType.PAEONIA.getMeta()), new ItemStack(Items.DYE, 3, EnumDyeColor.PINK.getDyeDamage()));
		addRecipe(new ItemStack(Items.BEETROOT, 1), new ItemStack(Items.DYE, 2, EnumDyeColor.RED.getDyeDamage()));
    
	}
	
	
	private static void addRecipe(ItemStack input, ItemStack output) {
		addRecipe(input, output, ItemStack.EMPTY);
	}
	
	private static void addRecipe(ItemStack input, ItemStack output, ItemStack extra) {
		shredderRecipes.add(new ShredderRecipe(input, output, extra));
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

		if (world.isRemote && ShredderBlock.isActive(this.world.getBlockState(this.pos))) 
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

		
		
        if (this.world != null && !this.world.isRemote)
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
   	           	this.shreddingTime = setShredTime(this.inputInventory.getStackInSlot(0));
               	this.currentItemShreddingTime = this.shreddingTime;
              	
               	if(this.isShredding())
               	{
               		shreddedItem = this.inputInventory.extractItemInternal(0, 1, false);
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
   				ShredderBlock.setActive(this.world, this.pos, this.world.getBlockState(this.pos), this.isShredding() && hasPower());			//  BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
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
		return item.getItem() == ModItems.rfCapacityUpgradeTier1 || item.getItem() == ModItems.speedUpgradeTier || item.getItem() == ModItems.rfUpgradeTier;
	}
	
	protected void updateUpgrades()
	{
		boolean rfupgrade = false;
		boolean rfcapacity = false;
		boolean speedupgrade = false;
		
		for(ItemStack upgrade : this.getAllUpgrades())
		{
			if(upgrade.isEmpty()) continue;
		
			if(upgrade.getItem() == ModItems.rfUpgradeTier)
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
		ItemStack stack = this.inputInventory.getStackInSlot(0);
		boolean flag = false;		
		
		if(shreddedItem != null && isShreddableItem(shreddedItem))
		{
			ShredderRecipe recipe = getRecipe(shreddedItem);
			if(recipe != null)
			{
				if(recipe.hasOutput())
				{
					this.outputInventory.insertItemInternal(0, recipe.getOutputItem(), false);
					
					if(!recipe.getExtraItemByChance().isEmpty())
					{
						ItemStack extra = recipe.getExtraItemByChance();
						if(extra != null)
							this.outputInventory.insertItemInternal(1, extra, false);
					}
					flag = true;
				}

			}

		}
	}
	
	private boolean canShred()
	{
        if (this.inputInventory.getStackInSlot(0).isEmpty())
        {
            return false;
        }
        else
        {
            ShredderRecipe recipe = this.getRecipe(this.inputInventory.getStackInSlot(0));
            
            if (recipe == null) return false;
            
            ItemStack itemstack = recipe.itemOut;
            if( itemstack.isEmpty()) return false;
            if (this.outputInventory.getStackInSlot(0).isEmpty()) return true;
            if (!this.outputInventory.getStackInSlot(0).isItemEqual(itemstack)) return false;
            int result = outputInventory.getStackInSlot(0).getCount() + itemstack.getCount();
            return result <= 64 && result <= this.outputInventory.getStackInSlot(0).getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
	}
	
    public boolean isShredding()
    {
        return this.shreddingTime > 0;
    }
    
    public boolean hasPower()
    {
    	return !world.isBlockPowered(pos) && energy.getEnergyStored() >= (rfTick * this.upgradeSpeedMulipyler) * this.rfEffencyMultpyler;
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
		if(stack.isEmpty()) return false;
		
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

        for (EntityItem entityitem : getCaptureItems(this.world, this.getXPos(), this.getYPos(), this.getZPos()))
        {
        	if(isShreddableItem(entityitem.getItem()))
        	{
                ItemStack itemstack = entityitem.getItem().copy();
        		//ItemStack itemstack1 = insertStack(this, itemstack, 0, enumfacing);
                ItemStack itemstack1 = this.inputInventory.insertItemInternal(0, itemstack, false);
                
                if (!itemstack1.isEmpty() && itemstack1.getCount() != 0)
                {
                	entityitem.getItem().setCount(itemstack1.getCount());
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
		this.outputInventory.deserializeNBT(tag.getCompoundTag("inventory"));
		this.inputInventory.deserializeNBT(tag.getCompoundTag("inputinventory"));
	        
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag = super.writeToNBT(tag);

		tag.setTag("inventory",this.outputInventory.serializeNBT());
		tag.setTag("inputinventory",this.inputInventory.serializeNBT());
		this.energy.writeToNBT(tag);
		return tag;
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
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) 
			return (T) this.outputInventory;
		else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) 
			return (T) this.inputInventory;
		else if (capability == CapabilityEnergy.ENERGY) 
			return (T) this.energy;
		
	        return super.getCapability(capability, facing);
	}

	    
//	public static class ShredderRecipe
//	{
//		private ItemStack itemIn;
//		private ItemStack itemOut;
//		private ItemStack itemExtra;
//		private int chance;
//		private int shredTime;
//		private Random rand = new Random();
//	    	
//		public ShredderRecipe(ItemStack itemIn, ItemStack itemOut)
//		{
//			this(itemIn, itemOut, (ItemStack)null);
//		}
//	    	
//		public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra)
//		{
//			this(itemIn, itemOut, itemExtra, 3, 100);
//		}
//	    	
//		public ShredderRecipe(ItemStack itemIn, ItemStack itemOut, ItemStack itemExtra, int chance, int shredTime)
//		{
//			this.itemIn = itemIn;
//			this.itemOut = itemOut;
//			this.itemExtra = itemExtra;
//			this.chance = chance;
//			this.shredTime = shredTime;
//		}
//	    	
//		public boolean isInputItem(ItemStack stack)
//		{
//			return this.itemIn.isItemEqual(stack); 
//		}
//	    	
//		public ItemStack getInputItem()
//		{
//			return this.itemIn;
//		}
//		
//		public boolean hasOutput()
//		{
//			return itemOut != null;
//		}
//	    	
//		public boolean hasExtraOutput()
//		{
//			return itemExtra != null;
//		}
//		
//		public ItemStack getOutputItem()
//		{
//			return itemOut.copy();
//		}
//	    	
//		@Nullable
//		public ItemStack getExtraItem()
//		{
//			if(rand.nextInt(chance) == 1)
//				return itemExtra.copy();
//			else return null;
//		}
//	    	
//	}

}
