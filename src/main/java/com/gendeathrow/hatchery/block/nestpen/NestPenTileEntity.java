package com.gendeathrow.hatchery.block.nestpen;

import java.util.Random;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.api.tileentities.IChickenNestingPen;
import com.gendeathrow.hatchery.core.Settings;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.item.HatcheryEgg;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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


public class NestPenTileEntity extends TileEntity  implements ITickable
{
	private static final AxisAlignedBB EMPTY_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

	private EntityChicken chickenStored;

	private NBTTagCompound entityNBT;
	private int TimetoNextEgg = 0;
	private Random rand = new Random();  
	//ItemStack[] inventory = new ItemStack[5];
	
	InventoryStroageModifiable inventory = new InventoryStroageModifiable("Items", 5);
	
	private boolean updateEntity = false;
	
    private boolean wasChild = false;
    private boolean notFlapping = false;
    
    private float newWingRotDelta;
    private float newWingRotation;

	
	public NestPenTileEntity()
	{
		super();
		entityNBT = new NBTTagCompound();
		this.TimetoNextEgg = this.rand.nextInt(3000) + 3000;
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

			entityin.writeToNBT(entityNBT);
			entityNBT.setString("id", EntityList.getKey(entityin).toString());
			
			//entityin.setPosition(this.pos.getX(),this.pos.getY() , this.pos.getZ());
			entityin.setPositionAndRotation(this.pos.getX(),this.pos.getY() , this.pos.getZ(), 0, 0);
			entityin.motionY = 0;

			if(!((EntityChicken) entityin).isChild()) ((EntityChicken) entityin).setGrowingAge(6000);
			
			NestingPenBlock.setState(true, this.world, this.pos);
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
		
		NestingPenBlock.setState(false, this.world, this.pos);
		
		this.markDirty();
		return returnEntity;
	}

	// Currently this handles the old ModBlocks.pn
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
		if(oldState.getBlock() == ModBlocks.pen && newSate.getBlock() == ModBlocks.pen )
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
		if (this.entityNBT.isEmpty()) {
			chickenStored = null;
		} else {
			try {
				this.chickenStored = (EntityChicken) EntityList.createEntityFromNBT(this.entityNBT, this.getWorld());
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
		EntityChicken mate  = (EntityChicken) NestingPenBlock.getNearByMate(world, this.world.getBlockState(pos), pos);
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
			return ItemStack.EMPTY;
		
		if(baby == null) return ItemStack.EMPTY;
		
    	baby.resetInLove();
    	baby.setHealth(baby.getMaxHealth());
    	baby.setGrowingAge(-24000);
    	baby.resetInLove();
    	
    	if(baby instanceof EntityChicken)
    		((EntityChicken)baby).timeUntilNextEgg = 6000;
    	
		chickenStored.setGrowingAge(6000 + this.rand.nextInt(6000));

		return HatcheryEgg.createEggFromEntity(world, baby);
	}


	
	@Override
	public void update() 
	{
		if((!this.entityNBT.isEmpty() && chickenStored == null) || this.updateEntity)
		{
			this.createEntity();
			this.updateEntity = false;
		}
		
		if(this.chickenStored == null) return;
		
		if(this.chickenStored.isChild()) wasChild = true;
	
		this.chickenStored.noClip = true;
		this.chickenStored.onGround = true;
		this.chickenStored.setNoGravity(true);
		this.chickenStored.setNoAI(true);
		this.chickenStored.rotationYaw = 0;
		
			this.chickenStored.onLivingUpdate();
			
		this.chickenStored.setPositionAndRotation(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 0, 0);
		this.chickenStored.motionY = 0; this.chickenStored.motionX = 0;	this.chickenStored.motionZ = 0;
		this.chickenStored.noClip = false;
		this.chickenStored.setNoGravity(false);
		this.chickenStored.setNoAI(false);		
		
		this.chickenStored.captureDrops = true;

		this.playChickenSound();
		
		if(this.world.isRemote) 
		{
			if(!Settings.SHOULD_RENDER_CHICKEN_FLAPS) 
				return;
			
			this.chickenStored.oFlap = this.chickenStored.wingRotation;
			this.chickenStored.oFlapSpeed = this.chickenStored.destPos;
			this.chickenStored.destPos = (float)((double)this.chickenStored.destPos + (double)(this.notFlapping ? -1 : 4) * 0.3D);
			this.chickenStored.destPos = MathHelper.clamp(this.chickenStored.destPos, 0.0F, 1.0F);
	        
	        
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
		
		
		if (!this.world.isRemote && !chickenStored.isChild() && !chickenStored.isChickenJockey())
		{
			if(wasChild && !(this.chickenStored.isChild())) 
			{
				this.wasChild = false;
				this.updateClient();
			}
			
			if(chickenStored.getGrowingAge() == 0 && !(this.chickenStored.getClass() == EntityChicken.class))
			{
				inventory.insertItemFirstAvaliableSlot(createEgg(), false);
			}

			if(--TimetoNextEgg <= 0)
			{
				if(this.rand.nextInt(1) == 0)
					inventory.insertItemFirstAvaliableSlot(new ItemStack(Items.FEATHER, 1), false);
				
				inventory.insertItemFirstAvaliableSlot(new ItemStack(ModItems.manure, rand.nextInt(2)+1), false);
				this.TimetoNextEgg = this.rand.nextInt(2000) + 6000;
			}
			
			if(this.chickenStored.capturedDrops != null && this.chickenStored.capturedDrops.size() > 0)
			{
				for(EntityItem entity : this.chickenStored.capturedDrops)
					inventory.insertItemFirstAvaliableSlot(entity.getItem(), false);

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
				//this.world.playSound((EntityPlayer)null, this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_CHICKEN_AMBIENT, chickenStored.getSoundCategory(), 1, 1);
			}
		}
	}
	
	/**
	 * Sends a block update to the clients to update the nbt data. 
	 */
	public void updateClient()
	{
        if(!this.world.isRemote)
        	world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2); 
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
			storedEntity.setString("id", EntityList.getKey(chickenStored).toString());
			chickenStored.writeToNBT(storedEntity);
		}
		else if(!this.entityNBT.isEmpty())
		{
			storedEntity = this.entityNBT;
		}
		
		compound.setTag("storedEntity", storedEntity);
		
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
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
        	ItemStack stack = inventory.getAndRemoveSlot(i);
        	
        	if(!stack.isEmpty()){
        		this.world.spawnEntity(new EntityItem(world, this.pos.getX(), this.pos.getY()+1, this.pos.getZ(), stack));
        	}
        }
	}
	
	public boolean grabItems(EntityPlayer playerIn)
	{
		boolean flag = false;
        for (int i = 0; i < this.inventory.getSlots(); ++i)
        {
        	ItemStack stack = inventory.getAndRemoveSlot(i);
        	
        	if(!stack.isEmpty()){
	    		if(!playerIn.inventory.addItemStackToInventory(stack))
	    			ForgeHooks.onPlayerTossEvent(playerIn, stack, false);
	    		flag = true;
    		}
        }
        return flag;
	}
  
    /**
     * Used for sending Waila Infomation
     * @param te
     * @return
     */
    public static NBTTagList getInventoryContents(NestPenTileEntity te)
    {
    	NBTTagList nbttaglist = new NBTTagList();
    	
        for (int i = 0; i < te.inventory.getSlots(); ++i)
        {
            if (!te.inventory.getStackInSlot(i).isEmpty())
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                nbttagcompound.setString("id", te.inventory.getStackInSlot(i).getDisplayName());
                nbttagcompound.setInteger("cnt", te.inventory.getStackInSlot(i).getCount());
                nbttaglist.appendTag(nbttagcompound);
            }
        }
    	return nbttaglist;
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
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) this.inventory;
        }
        return super.getCapability(capability, facing);
    }

}
