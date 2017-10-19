package com.gendeathrow.hatchery.block.nest;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.network.HatcheryPacket;
import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;
import com.setycz.chickens.ChickensRegistry;
import com.setycz.chickens.ChickensRegistryItem;
import com.setycz.chickens.chicken.EntityChickensChicken;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;


@Optional.InterfaceList(
		value = 
		{
				@Interface(iface = "com.setycz.chickens.chicken.EntityChickensChicken", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.ChickensRegistry", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.ChickensRegistryItem", modid = "chickens")
		}
)

public class EggNestTileEntity extends TileEntity implements ITickable//, IInventory
{

	boolean hasEgg;
	
	ItemStack[] eggSlot = new ItemStack[1];
	
	int hatchingTick= 0;
	private final int timeToHatch = 300;
	
	boolean bonusLight = false;
	boolean bonusPlayer = false;
	
	private float prevPercentage = 0;
	
	int ticks;
	boolean firstload = true;
	
	@Override
	public void update() 
	{
		if(this.worldObj.isRemote) 
		{
			
			updateClient();
			return;
		}
		ticks++;
		if (this.worldObj.getTotalWorldTime() % 80L == 0L)
		{	
	        boolean flag = this.getWorld() != null;
	        boolean flag1 = !flag || this.getBlockType() == ModBlocks.nest;
			
	        if(flag1)
	        {
			if(EggNestBlock.doesHaveEgg(this.worldObj.getBlockState(this.getPos())))
			{
				int randint = 2;
				
				if(this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.pos)).size() > 0)
				{
					randint += 5;
				}
				
				hatchingTick += this.worldObj.rand.nextInt(randint)+ (checkForHeatLamp() ? 2 : 1);
      
				ticks = 0;
				
				if(hatchingTick > timeToHatch)
				{
				try
				{	
					if(this.eggSlot[0].getItem() == ModItems.hatcheryEgg)
					{
						Entity entitychicken = null;

						if(this.eggSlot[0].getTagCompound() == null) 
						{
							spawnMCChicken();
						}
						else
						{
							NBTTagCompound entityTag = ItemStackEntityNBTHelper.getEntityTagFromStack(this.eggSlot[0]);
						
							try
							{
								entitychicken = EntityList.createEntityFromNBT(entityTag, this.worldObj);
							}
							catch (Throwable e)
							{
								Hatchery.logger.error("Error trying to spawn Hatchery Egg 'Null NBT' " + e);
							}
						
							if(entitychicken != null)
							{
								entitychicken.setLocationAndAngles(getPos().getX() + .5, getPos().getY() + .5, getPos().getZ() + .5, 0.0F, 0.0F);
								this.worldObj.spawnEntityInWorld(entitychicken);
								worldObj.playSound((EntityPlayer)null, getPos().getX(), getPos().getY() + 1, getPos().getZ(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.AMBIENT, 0.5F, 0.4F / (worldObj.rand.nextFloat() * 0.4F + 0.8F));
							}
							else spawnMCChicken();
						}
					}
					else if(this.eggSlot[0].getItem() == Items.EGG)
					{
						spawnMCChicken();
					}
					else
					{
						spawnChickensModChicken();
	    	        	worldObj.playSound((EntityPlayer)null, getPos().getX(), getPos().getY() + 1, getPos().getZ(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.AMBIENT, 0.5F, 0.4F / (worldObj.rand.nextFloat() * 0.4F + 0.8F));
					}
				}
				catch (Throwable e)
				{
					Hatchery.logger.error("Error trying to spawn Egg in the nest ("+this.getPos().toString()+") 'Null NBT' " + e);
				}
				
					EggNestBlock.removeEgg(worldObj, worldObj.getBlockState(getPos()), getPos());
					
					markDirty();
				}
			}
	        }//flag1
		}
		else if(hatchingTick > timeToHatch) {hatchingTick = 0; ticks = 0;}
	}
	
	private boolean sentRequest = false;
	public void updateClient()
	{
		if(this.eggSlot[0] != null) return;
		
		boolean hasEgg = EggNestBlock.doesHaveEgg(this.getWorld().getBlockState(this.getPos()));
    
		if(!sentRequest && hasEgg)
		{
			Hatchery.network.sendToServer(HatcheryPacket.requestItemstackTE(this.getPos()));
    			
			//System.out.println("Requesting Packet");
			sentRequest = true;
		}
		else if ( Minecraft.getSystemTime() % 80 == 0 && sentRequest && hasEgg)
		{
			sentRequest = false;
			//System.out.println("Resending Packet");
		}
	}
	
	public float getPercentage()
	{

		return ((hatchingTick* 100)/timeToHatch);
	}
	
	private boolean checkForHeatLamp()
	{
		RayTraceResult result = this.worldObj.rayTraceBlocks(new Vec3d(this.pos), new Vec3d(this.pos.up(3)));
		
		if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState state = this.worldObj.getBlockState(result.getBlockPos());
			if(state.getBlock() == Blocks.REDSTONE_LAMP);
			{
				return this.worldObj.isBlockPowered(result.getBlockPos());
			}
		}
		return false;
	}
	
    public void readFromNBT(NBTTagCompound compound)
    {
    	this.hatchingTick = compound.getInteger("hatchTime");
    	
    	this.eggSlot[0] = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("egg"));
    	super.readFromNBT(compound);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	compound.setInteger("hatchTime", hatchingTick);
  
        if (this.eggSlot[0] != null)
        {
        	NBTTagCompound nbttagcompound = new NBTTagCompound();
        	this.eggSlot[0].writeToNBT(nbttagcompound);
        	compound.setTag("egg", nbttagcompound);
        }

    	
    	return super.writeToNBT(compound);
    }
    
    @Override
    public boolean receiveClientEvent(int id, int type)
    {
    	this.worldObj.setBlockState(getPos(), this.worldObj.getBlockState(pos));
    	
        if (id == 1)
        {
        	
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }

    private void spawnMCChicken()
    {
		EntityChicken chicken = new EntityChicken(worldObj);
		chicken.setPosition(getPos().getX(), getPos().getY() + .5, getPos().getZ());
		chicken.setGrowingAge(-24000);
		worldObj.spawnEntityInWorld(chicken);
    	worldObj.playSound((EntityPlayer)null, getPos().getX() + .5, getPos().getY() + 1, getPos().getZ() + .5, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.AMBIENT, 0.5F, 0.4F / (worldObj.rand.nextFloat() * 0.4F + 0.8F));
    }
    
    @Optional.Method(modid = "chickens")
    public void spawnChickensModChicken()
    {
        EntityChickensChicken entitychicken = new EntityChickensChicken(this.worldObj);
        entitychicken.setChickenType(getChickenType(this.eggSlot[0]));
        entitychicken.setGrowingAge(-24000);
        entitychicken.setPosition(getPos().getX() + .5 , getPos().getY() + .5, getPos().getZ() + .5);
        this.worldObj.spawnEntityInWorld(entitychicken);
    }
    
    @Optional.Method(modid = "chickens")
    private int getChickenType(ItemStack itemStack) 
    {
        ChickensRegistryItem chicken = ChickensRegistry.findDyeChicken(itemStack.getMetadata());
        if (chicken == null) {
            return -1;
        }
        return chicken.getId();
    }
    //////////////////////////////////////////////////////////////////////////
    // INVENTORY 
    /////////////////////////////////////////////////////////////////////////
    
	//@Override
	public String getName() 
	{
		return null;
	}

	//@Override
	public boolean hasCustomName() {
		return false;
	}

	//
	public int getSizeInventory() {
		return 1;
	}

	//@Override
	public ItemStack getStackInSlot(int index) {
		return this.eggSlot[index];
	}

	//@Override
	public ItemStack decrStackSize(int index, int count) 
	{
	    ItemStack itemstack = ItemStackHelper.getAndSplit(this.eggSlot, index, count);

        if (itemstack != null)
        {
            this.markDirty();
            
        }
		return itemstack;
	}

	//@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		
		ItemStack stack = ItemStackHelper.getAndRemove(this.eggSlot, index);
		
		if(stack != null) 
		{
			this.markDirty();
		}
		
		
		
		return stack;
	}

	//@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		this.eggSlot[index] = stack;
		
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
		
		this.markDirty();
	}

	//@Override
	public int getInventoryStackLimit() 
	{
		return 1;
	}

	//@Override
	public boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	//@Override
	public void openInventory(EntityPlayer player) { }

	//@Override
	public void closeInventory(EntityPlayer player) { }

	//@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		if(stack.getItem() instanceof ItemEgg)
			return true;
		
		return false;
	}

	//@Override
	public int getField(int id) {
		return 0;
	}

	//@Override
	public void setField(int id, int value) {}

	//@Override
	public int getFieldCount() {return 0;}

	//@Override
	public void clear() 
	{
      this.eggSlot[0] = null;
	}

	////////////////////////////////////////////////////////////
	// PacketUpdate
	////////////////////////////////////////////////////////////
    
	@Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
    	//System.out.println("[DEBUG]:Server sent tile sync packet");
		NBTTagCompound sendnbt = new NBTTagCompound();  	
		sendnbt = this.writeToNBT(sendnbt);
		NBTTagCompound egg = sendnbt.getCompoundTag("egg");
		ItemStack test = ItemStack.loadItemStackFromNBT(egg);
       return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), sendnbt);
    }

	
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
    	//System.out.println("[DEBUG]:Client recived tile sync packet");
		NBTTagCompound egg = pkt.getNbtCompound().getCompoundTag("egg");
		ItemStack test = ItemStack.loadItemStackFromNBT(egg);
  		this.readFromNBT(pkt.getNbtCompound());
    	this.markDirty();
    }
         
   ////////////////////////////////////////////////////////////////
    //Wailia Intergration
    ////////////////////////////////////////////////////////////////
    
    public NBTTagCompound getWailaUpdatePacket()
    {
		NBTTagCompound sendnbt = new NBTTagCompound();
		
		sendnbt.setInteger("hatchTime", this.hatchingTick);
		
		return sendnbt;
    }
    
    public int getWailaHatchTime(NBTTagCompound nbt)
    {
		return nbt.getInteger("hatchTime");
    }

}
