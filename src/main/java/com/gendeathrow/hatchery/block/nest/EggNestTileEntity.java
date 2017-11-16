package com.gendeathrow.hatchery.block.nest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.init.ModBlocks;
import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.modaddons.ChickensHelper;
import com.gendeathrow.hatchery.network.HatcheryPacket;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;
import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;

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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;



public class EggNestTileEntity extends TileEntity implements ITickable//, IInventory
{

	boolean hasEgg;
	
	public final InventoryStroageModifiable inventory = new InventoryStroageModifiable("egg", 1) {
		
	    @Override
	    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
	    {
			System.out.println("item added + "+ stack.getDisplayName());
			super.setStackInSlot(slot, stack);
			
			
	    }
	    
	    @Override
	    protected void onContentsChanged(int slot)
	    {
	    	System.out.println("Changed + "+ this.stacks.get(0).getDisplayName());
	    }
//	 @Override
//		protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
//	    	return 1;
//	    }
//	 
//		public boolean canInsertSlot(int slot, ItemStack stack){
//			return stack.getItem() == ModItems.hatcheryEgg || stack.getItem() == Items.EGG;
//		}
	};
	
	int hatchingTick= 0;
	//private final int timeToHatch = 300;
	private final int timeToHatch = 300;
	
	boolean bonusLight = false;
	boolean bonusPlayer = false;
	
	int ticks;
	boolean firstload = true;
	
	public ItemStack getEgg(){
		return this.inventory.getStackInSlot(0);
	}
	
	public void insertEgg(ItemStack eggIn){
		if(!eggIn.isEmpty() && this.inventory.getStackInSlot(0).isEmpty())
		this.inventory.setStackInSlot(0, eggIn);
		this.markDirty();
	}
	
	public ItemStack removeEgg(){
		ItemStack stack = this.inventory.getAndRemoveSlot(0);
		this.markDirty();
		return stack;
	}
	
	@Override
	public void update() 
	{
		
/*		if(this.world.isRemote)
		{
		//	updateClient();
			return;
		}
		ticks++;
		if (this.world.getTotalWorldTime() % 80L == 0L)
		{	
	        boolean flag = this.getWorld() != null;
	        boolean flag1 = !flag || this.getBlockType() == ModBlocks.nest;
			
	        if(flag1)
	        {
	        	System.out.println(getEgg().getDisplayName()+"--");
	        	
	        	if(EggNestBlock.doesHaveEgg(this.world.getBlockState(this.getPos())))
	        	{
	        		int randint = 2;
				
	        		if(this.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(this.pos)).size() > 0) {
	        			randint += 5;
	        		}
				
	        		hatchingTick += this.world.rand.nextInt(randint)+ (checkForHeatLamp() ? 2 : 1);
      
	        		ticks = 0;
				
	        		if(hatchingTick > timeToHatch)
	        		{
	        			try
	        			{	
	        				if(this.getEgg().getItem() == ModItems.hatcheryEgg)
	        				{
	        					Entity entitychicken = null;
	        					
	        					if(this.getEgg().getTagCompound() == null) 
	        					{
	        						spawnMCChicken();
	        						Hatchery.logger.error("Error trying to spawn Hatchery Egg 'Null NBT' ");
	        					}
	        					else
	        					{
	        						NBTTagCompound entityTag = ItemStackEntityNBTHelper.getEntityTagFromStack(this.getEgg());
						
	        						try	{
	        							entitychicken = EntityList.createEntityFromNBT(entityTag, this.world);
	        						}
	        						catch (Throwable e)	{
	        							Hatchery.logger.error("Error trying to spawn Hatchery Egg from NBT ' " + e);
	        						}
						
	        						if(entitychicken != null)
	        						{
	        							entitychicken.setLocationAndAngles(getPos().getX() + .5, getPos().getY() + .5, getPos().getZ() + .5, 0.0F, 0.0F);
	        							this.world.spawnEntity(entitychicken);
	        							world.playSound((EntityPlayer)null, getPos().getX(), getPos().getY() + 1, getPos().getZ(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.AMBIENT, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
	        						}
	        						else {
	        							spawnMCChicken();
	        						}
	        					}
	        				}
	        				else if(this.getEgg().getItem() == Items.EGG)
	        				{
	        					spawnMCChicken();
	        				}
	        				else if(ChickensHelper.isLoaded())
	        				{
	        					ChickensHelper.spawnChickenType(getWorld(), getPos(), ChickensHelper.getDyeChickenfromItemStack(getEgg()));	
	        					world.playSound((EntityPlayer)null, getPos().getX(), getPos().getY() + 1, getPos().getZ(), SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.AMBIENT, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
	        				}
	        			}
	        			catch (Throwable e)
	        			{
	        				Hatchery.logger.error("Error trying to spawn Egg in the nest ("+this.getPos().toString()+") 'Null NBT' " + e);
	        			}
				
	        			EggNestBlock.removeEgg(world, world.getBlockState(getPos()), getPos());
	        		}
	        	}
        	}//flag1
		}
		else if(hatchingTick > timeToHatch) {hatchingTick = 0; ticks = 0;}
		
		*/
	}
	
//	private boolean sentRequest = false;
//	public void updateClient()
//	{
////		if(this.getEgg().isEmpty()) return;
//		
//		boolean hasEgg = EggNestBlock.doesHaveEgg(this.getWorld().getBlockState(this.getPos()));
//    
//		if(!sentRequest && hasEgg)
//		{
//			Hatchery.network.sendToServer(HatcheryPacket.requestItemstackTE(this.getPos()));
//			sentRequest = true;
//		}
//		else if ( Minecraft.getSystemTime() % 80 == 0 && sentRequest && hasEgg)
//		{
//			sentRequest = false;
//		}
//	}
	
	public float getPercentage()
	{

		return ((hatchingTick* 100)/timeToHatch);
	}
	
	private boolean checkForHeatLamp()
	{
		RayTraceResult result = this.world.rayTraceBlocks(new Vec3d(this.pos), new Vec3d(this.pos.up(3)));
		
		if(result != null && result.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			IBlockState state = this.world.getBlockState(result.getBlockPos());
			if(state.getBlock() == Blocks.REDSTONE_LAMP);
			{
				return this.world.isBlockPowered(result.getBlockPos());
			}
		}
		return false;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
    	this.hatchingTick = compound.getInteger("hatchTime");
    	
    	this.inventory.readFromNBT(compound);
    	
    System.out.println("read -- "+ compound.toString());
    	
    	super.readFromNBT(compound);
    }

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
    	compound.setInteger("hatchTime", hatchingTick);
    	
    	this.inventory.writeToNBT(compound);
    	
    	System.out.println("write -- "+ compound.toString());
    	return super.writeToNBT(compound);
    }
//    
//    @Override
//    public boolean receiveClientEvent(int id, int type)
//    {
//    	this.world.setBlockState(getPos(), this.world.getBlockState(pos));
//    	
//        if (id == 1)
//        {
//        	
//            return true;
//        }
//        else
//        {
//            return super.receiveClientEvent(id, type);
//        }
//    }

    private void spawnMCChicken()
    {
		EntityChicken chicken = new EntityChicken(world);
		chicken.setPosition(getPos().getX(), getPos().getY() + .5, getPos().getZ());
		chicken.setGrowingAge(-24000);
		world.spawnEntity(chicken);
		world.playSound((EntityPlayer)null, getPos().getX() + .5, getPos().getY() + 1, getPos().getZ() + .5, SoundEvents.ENTITY_CHICKEN_HURT, SoundCategory.AMBIENT, 0.5F, 0.4F / (world.rand.nextFloat() * 0.4F + 0.8F));
    }
    
	////////////////////////////////////////////////////////////
	// PacketUpdate
	////////////////////////////////////////////////////////////
    
//    @Override
//	@Nullable
//    public SPacketUpdateTileEntity getUpdatePacket()
//    {
//
//		NBTTagCompound sendnbt = new NBTTagCompound();  	
//		sendnbt = this.writeToNBT(sendnbt);
//		
//    	System.out.println("[DEBUG]:Server sent tile sync packet: "+ sendnbt);
//       return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), sendnbt);
//    }
//
//	@Override
//    public void onDataPacket(net.minecraft.network.NetworkManager net, net.minecraft.network.play.server.SPacketUpdateTileEntity pkt)
//    {
//    	System.out.println("[DEBUG]:Client recived tile sync packet: "+ pkt.getNbtCompound());
//  		this.readFromNBT(pkt.getNbtCompound());
//    	this.markDirty();
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
