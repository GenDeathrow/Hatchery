package com.gendeathrow.hatchery.block.nestblock;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.Optional.Interface;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.ModItems;
import com.gendeathrow.hatchery.network.HatcheryPacket;
import com.setycz.chickens.ChickensRegistry;
import com.setycz.chickens.ChickensRegistryItem;
import com.setycz.chickens.chicken.EntityChickensChicken;


@Optional.InterfaceList(
		value = 
		{
				@Interface(iface = "com.setycz.chickens.chicken.EntityChickensChicken", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.ChickensRegistry", modid = "chickens"),
				@Interface(iface = "com.setycz.chickens.ChickensRegistryItem", modid = "chickens")
		}
)

public class NestTileEntity extends TileEntity implements ITickable, IInventory
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
			if(NestBlock.doesHaveEgg(this.worldObj.getBlockState(this.getPos())))
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
					
					if(this.eggSlot[0].getItem() == ModItems.hatcheryEgg && this.eggSlot[0].getTagCompound() != null)
					{
						Entity entitychicken = null;
						
						try
						{
							entitychicken = EntityList.createEntityFromNBT(this.eggSlot[0].getTagCompound(), this.worldObj);
						}
						catch (Throwable e)
						{
							Hatchery.logger.error("Error trying to spawn Hatchery Egg 'Null NBT' " + e);
						}
						
		    	        if(entitychicken != null)
		    	        {
		    	        	entitychicken.setLocationAndAngles(getPos().getX(), getPos().getY() + 1, getPos().getZ(), 0.0F, 0.0F);
		    	        	this.worldObj.spawnEntityInWorld(entitychicken);
		    	        }
					}
					else if(this.eggSlot[0].getItem() == Items.EGG || this.eggSlot[0].getTagCompound() == null)
					{
						EntityChicken chicken = new EntityChicken(worldObj);
						chicken.setPosition(getPos().getX(), getPos().getY() + 1, getPos().getZ());
						chicken.setGrowingAge(-24000);
						worldObj.spawnEntityInWorld(chicken);
					}
					else
					{
						spawnChickensModChicken();
					}
					
					NestBlock.removeEgg(worldObj, worldObj.getBlockState(getPos()), getPos());
					
					markDirty();
				}
			}
		}
		else if(hatchingTick > timeToHatch) {hatchingTick = 0; ticks = 0;}
	}
	
	private boolean sentRequest = false;
	public void updateClient()
	{
		if(this.eggSlot[0] != null) return;
		
		boolean hasEgg = NestBlock.doesHaveEgg(this.getWorld().getBlockState(this.getPos()));
    
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

    
    @Optional.Method(modid = "chickens")
    public void spawnChickensModChicken()
    {
        EntityChickensChicken entitychicken = new EntityChickensChicken(this.worldObj);
        entitychicken.setChickenType(getChickenType(this.eggSlot[0]));
        entitychicken.setGrowingAge(-24000);
        entitychicken.setPosition(getPos().getX(), getPos().getY() + 1, getPos().getZ());
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
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.eggSlot[0];
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		return this.eggSlot[0];
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return ItemStackHelper.getAndRemove(this.eggSlot, 0);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		this.eggSlot[0] = stack;
		
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
        {
            stack.stackSize = this.getInventoryStackLimit();
        }
        
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 1;
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
		return true;
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
	public void clear() {}

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
		
		if(test != null)
		{
			//System.out.println("Sending ItemStack "+ test.getDisplayName());
		}
		
       return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), sendnbt);
    }

	
    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
    {
    	//System.out.println("[DEBUG]:Client recived tile sync packet");


		NBTTagCompound egg = pkt.getNbtCompound().getCompoundTag("egg");
		
		ItemStack test = ItemStack.loadItemStackFromNBT(egg);
		
		if(test != null)
		{
			System.out.println("Sending ItemStack "+ test.getDisplayName());
		}
    	//this.eggSlot[0] = ItemStack.loadItemStackFromNBT(pkt.getNbtCompound().getCompoundTag("egg"));

  
  		this.readFromNBT(pkt.getNbtCompound());
    	this.markDirty();
    }
         
    
//    public boolean receiveClientEvent(int id, int type)
//    {
//    	return true;
//    }
    
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
