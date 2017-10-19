package com.gendeathrow.hatchery.block.nestpen;

import java.util.Random;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.api.tileentities.IChickenNestingPen;
import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.inventory.InventoryStorage;
import com.gendeathrow.hatchery.item.HatcheryEgg;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;


public class NestPenTileEntity extends TileEntity  implements ITickable, IInventory
{
	private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

	private EntityChicken chickenStored;

	private NBTTagCompound entityNBT;
	private int TimetoNextEgg = 0;
	private Random rand = new Random();  
	//ItemStack[] inventory = new ItemStack[5];
	
	InventoryStorage inventory = new InventoryStorage(this, 5);
	
	private int isMating = 600;
	private boolean updateEntity = false;
	
    private boolean wasChild = false;
    private boolean notFlapping = false;
    
    private float newWingRotDelta;
    private float newWingRotation;

	
	public NestPenTileEntity()
	{
		super();
		entityNBT = new NBTTagCompound();
		this.TimetoNextEgg = this.rand.nextInt(5000) + 2000;
	}
	
	public int getTimeToNextDrop()
	{
		return this.TimetoNextEgg;
	}
	
	public EntityAgeable storedEntity()
	{
		return this.chickenStored;
	}
	
	/**
	 * Set the Entity into the Nesting Pen
	 * <br><br>
	 * <b>Also Updates Client
	 * 
	 * @param entityin
	 * @return
	 */
	public boolean trySetEntity(Entity entityin)
	{
		if(this.storedEntity() != null) return false;
		
		if(entityin instanceof EntityChicken)
		{
			this.chickenStored = (EntityChicken) entityin;
			
			entityNBT = new NBTTagCompound();
			((EntityChicken) entityin).writeEntityToNBT(entityNBT);
			
			entityin.setPosition(this.pos.getX(),this.pos.getY() , this.pos.getZ());
			entityin.motionY = 0;

			if(!((EntityChicken) entityin).isChild()) ((EntityChicken) entityin).setGrowingAge(6000);
			
			NestingPenBlock.setState(true, this.worldObj, this.pos);
			this.markDirty();
			return true;
		}else return false;
		
	}
	
	/**
	 * Grab the stored entity from the tile entity
	 * <br><br>
	 * <b>Also Updates Client
	 * @return
	 */
	public Entity tryGetRemoveEntity()
	{
		if(this.storedEntity() == null) return null;
		
		this.chickenStored.setNoAI(false);
		this.chickenStored.captureDrops = false;
		this.chickenStored.setNoGravity(false);
		
		EntityAgeable returnEntity = this.storedEntity();

		
		entityNBT = new NBTTagCompound();	
		this.chickenStored = null;
		
		NestingPenBlock.setState(false, this.worldObj, this.pos);
		
		this.markDirty();
		return returnEntity;
	}

	// Currently this handles the old ModBlocks.pn
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
		if((oldState.getBlock() == ModBlocks.pen || oldState.getBlock() == ModBlocks.pen_chicken) && (newSate.getBlock() == ModBlocks.pen || newSate.getBlock() == ModBlocks.pen_chicken))
		{
			return false;
		}
		else 
		  return oldState != newSate;
    }
	
	/**
	 * Creates an entity from the nesting pens stored entity
	 */
	private void createEntity()
	{
		if (this.entityNBT.hasNoTags()) {
			chickenStored = null;
		} else {
			try {
				chickenStored = (EntityChicken) EntityList.createEntityFromNBT(this.entityNBT, this.getWorld());
			} catch (Throwable e) {
				chickenStored = null;
				this.entityNBT = new NBTTagCompound();
				Hatchery.logger.error("Error trying to add chicken tp pen 'Null NBT' " + e);
			}
		}

		if (this.chickenStored != null)
		{
			this.chickenStored.setEntityBoundingBox(EMPTY_AABB); // prevent collision calculations
			this.chickenStored.setNoAI(true); // prevent path navigation calculations
		}
	}
	
	/**
	 * Creates an egg itemstack with a stored entity
	 * @return
	 */
	private ItemStack createEgg()
	{
		ItemStack egg = new ItemStack(ModItems.hatcheryEgg, 1, 0);
    	
		EntityChicken mate  = (EntityChicken) NestingPenBlock.getNearByMate(worldObj, this.worldObj.getBlockState(pos), pos);
		EntityAnimal baby = null;
		if(mate != null)
		{
			
			if(this.chickenStored instanceof IChickenNestingPen)
			{
				baby = (EntityAnimal) ((IChickenNestingPen) this.chickenStored).getChild(this.chickenStored, mate);
			}
			else
			{
				baby = this.chickenStored.createChild(mate);
				mate.setGrowingAge(6000 + this.rand.nextInt(5500));
			}
		}
		else if(this.rand.nextInt(99)+1 < Settings.EGG_NESTINGPEN_DROP_RATE)
			baby = this.chickenStored.createChild((EntityAgeable) this.storedEntity());
		else 
			return null;
		
		if(baby == null) return null;
		
    	baby.resetInLove();
    	baby.setHealth(baby.getMaxHealth());
    	baby.setGrowingAge(-24000);
    	baby.resetInLove();
    	if(baby instanceof EntityChicken)
    		((EntityChicken)baby).timeUntilNextEgg = 6000;
    	
		chickenStored.setGrowingAge(6000 + this.rand.nextInt(6000));

		return HatcheryEgg.createEggFromEntity(worldObj, baby);
	}


	
	@Override
	public void update() 
	{
		if((!this.entityNBT.hasNoTags() && chickenStored == null) || this.updateEntity)
		{
			this.createEntity();
			this.updateEntity = false;
		}
		
		if(chickenStored == null) return;
		
		if(this.chickenStored.isChild()) wasChild = true;
	
		this.chickenStored.noClip = true;
		this.chickenStored.onGround = true;
		this.chickenStored.setNoGravity(true);
		this.chickenStored.setNoAI(true);
			this.chickenStored.onLivingUpdate();

		this.chickenStored.motionY = 0;
		this.chickenStored.motionX = 0;
		this.chickenStored.motionZ = 0;
		this.chickenStored.noClip = false;
		this.chickenStored.setNoGravity(false);
		this.chickenStored.setNoAI(false);		
		
		this.chickenStored.captureDrops = true;

		this.playChickenSound();
		
		//System.out.println(this.chickenStored.posX +":"+ this.chickenStored.posY +":"+ this.chickenStored.posZ);
		
		if(this.worldObj.isRemote) 
		{
			if(!Settings.SHOULD_RENDER_CHICKEN_FLAPS) 
			{
				return;
			}
			
			this.chickenStored.oFlap = this.chickenStored.wingRotation;
			this.chickenStored.oFlapSpeed = this.chickenStored.destPos;
			this.chickenStored.destPos = (float)((double)this.chickenStored.destPos + (double)(this.notFlapping ? -1 : 4) * 0.3D);
			this.chickenStored.destPos = MathHelper.clamp_float(this.chickenStored.destPos, 0.0F, 1.0F);
	        
	        
			if(notFlapping || this.chickenStored.getRNG().nextFloat() < 0.02F)
			{
				this.notFlapping = true;
				
				 if(newWingRotDelta < 1.0F)
					 newWingRotDelta = 1.0F;
				 
				 newWingRotDelta = (float)((double)newWingRotDelta * 0.9D);
				 
				if(this.chickenStored.getRNG().nextFloat() < 0.02F)
				{
					this.notFlapping = false;
				}

			}

			newWingRotation += newWingRotDelta * 2.0F;		
			
			this.chickenStored.wingRotDelta = newWingRotDelta;
			this.chickenStored.wingRotation = newWingRotation;
			
			this.chickenStored.onGround = true;
		}
		
		
		if (!this.worldObj.isRemote && !chickenStored.isChild() && !chickenStored.isChickenJockey())
		{
			if(wasChild && !(this.chickenStored.isChild())) 
			{
				this.wasChild = false;
				this.updateClient();
			}
			
			if(chickenStored.getGrowingAge() == 0 && !(this.chickenStored.getClass() == EntityChicken.class))
			{
				putStackInInventoryAllSlots(this, createEgg(), EnumFacing.DOWN);
			}

			if(--TimetoNextEgg <= 0)
			{
				if(this.rand.nextInt(1) == 0)
					putStackInInventoryAllSlots(this, new ItemStack(Items.FEATHER, 1), EnumFacing.DOWN);
				putStackInInventoryAllSlots(this, new ItemStack(ModItems.manure, rand.nextInt(1)+1), EnumFacing.DOWN);
				this.TimetoNextEgg = this.rand.nextInt(5000) + 2000;
			}
			
			
			if(this.chickenStored.capturedDrops != null && this.chickenStored.capturedDrops.size() > 0)
			{
				for(EntityItem entity : this.chickenStored.capturedDrops)
				{
					putStackInInventoryAllSlots(this, entity.getEntityItem(), EnumFacing.DOWN);
				}
				this.chickenStored.capturedDrops.clear();
			}
		}
		
	}
	
	/**
	 * Plays chicken sound while in the nesting pen. 
	 */
	private void playChickenSound()
	{
		if (chickenStored.isEntityAlive() && this.rand.nextInt(1000) < chickenStored.livingSoundTime++)
		{
			chickenStored.livingSoundTime = -chickenStored.getTalkInterval();
			
			if (!chickenStored.isSilent())
			{
				chickenStored.playLivingSound();
				//this.worldObj.playSound((EntityPlayer)null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_CHICKEN_AMBIENT, chickenStored.getSoundCategory(), 1, 1);
			}
		}
	}
	
	/**
	 * Sends a block update to the clients to update the nbt data. 
	 */
	public void updateClient()
	{
        if(!this.worldObj.isRemote)
        	worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 2); 
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("storedEntity"))
		{
			this.entityNBT = (NBTTagCompound) compound.getTag("storedEntity");
		}
	
		this.inventory.readFromNBT(compound);
        this.updateEntity = true;
        super.readFromNBT(compound);
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		NBTTagCompound storedEntity = new NBTTagCompound();
		
		if(this.chickenStored != null)
		{
			storedEntity.setString("id", EntityList.getEntityString(chickenStored));
			chickenStored.writeEntityToNBT(storedEntity);
		}
		else if(!this.entityNBT.hasNoTags())
		{
			storedEntity = this.entityNBT;
		}
		
		compound.setTag("storedEntity", storedEntity);
		
        NBTTagList nbttaglist = new NBTTagList();

        this.inventory.writeToNBT(compound);

		return super.writeToNBT(compound);
	}
	
	@Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), getUpdateTag());
    }

	@Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }
	
	@Override
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
  		this.readFromNBT(pkt.getNbtCompound());
    }
	
	//////////////////////////////////////////////////////////////////
	// Inventory
	/////////////////////////////////////////////////////////////////
	public void dropContents()
	{
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
        	ItemStack stack = ItemStackHelper.getAndRemove(this.inventory.getInventory(), i);
        	
        	if(stack != null)
        	{
        		this.worldObj.spawnEntityInWorld(new EntityItem(worldObj, this.pos.getX(), this.pos.getY()+1, this.pos.getZ(), stack));
        	}
        }
	}
	
	public boolean grabItems(EntityPlayer playerIn)
	{
		boolean flag = false;
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
        {
        	ItemStack stack = ItemStackHelper.getAndRemove(this.inventory.getInventory(), i);
        	
    		if(stack != null)
    		{
	    		if(!playerIn.inventory.addItemStackToInventory(stack))
	    		{		    			
	    			ForgeHooks.onPlayerTossEvent(playerIn, stack, false);
	    		}

	    		flag = true;
    		}
        }
        
        return flag;
	}
	
    @Override
	public String getName() { return null; }

	@Override
	public boolean hasCustomName() { return false; }

	@Override
	public int getSizeInventory() {	return this.inventory.getSizeInventory(); }

	@Override
	public ItemStack getStackInSlot(int index) 
	{
		return this.inventory.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
        return this.inventory.decrStackSize(index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return this.inventory.removeStackFromSlot(index); //ItemStackHelper.getAndRemove(this.inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index,@Nullable ItemStack stack) 
	{
		this.inventory.setInventorySlotContents(index, stack);
	}

	@Override
	public int getInventoryStackLimit() { return 64; }

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {	return true;}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {	return true; }

	@Override
	public int getField(int id) { return 0; }
	@Override
	public void setField(int id, int value) {}
	@Override
	public int getFieldCount() {return 0;}

	@Override
	public void clear() 
	{
		this.inventory.clear();
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
    
    
    /**
     * Used for sending Waila Infomation
     * @param te
     * @return
     */
    public static NBTTagList getInventoryContents(NestPenTileEntity te)
    {
    	NBTTagList nbttaglist = new NBTTagList();
    	
        for (int i = 0; i < te.inventory.getSizeInventory(); ++i)
        {
            if (te.inventory.getStackInSlot(i) != null)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                nbttagcompound.setString("id", te.inventory.getStackInSlot(i).getDisplayName());
                nbttagcompound.setInteger("cnt", te.inventory.getStackInSlot(i).stackSize);
                nbttaglist.appendTag(nbttagcompound);
            }
        }
    	return nbttaglist;
    }
   
    public static IItemHandler getItemHandler(TileEntity tile, EnumFacing side) 
    {
        if (tile == null) return null;
   
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
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) 
    {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
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

}
