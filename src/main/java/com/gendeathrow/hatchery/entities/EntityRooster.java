package com.gendeathrow.hatchery.entities;

import java.util.Set;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;
import com.gendeathrow.hatchery.entities.ai.EntityAIRoosterMating;
import com.gendeathrow.hatchery.storage.InventoryStroageModifiable;
import com.google.common.collect.Sets;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class EntityRooster extends EntityChicken {
	public static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
	InventoryStroageModifiable inventory = new InventoryStroageModifiable("Items", 1);
	public static final int SEED_SLOT = 0;
	public static final int MAX_SEEDS = 20;
	private static final DataParameter<Integer> SEEDS = EntityDataManager.<Integer>createKey(EntityRooster.class, DataSerializers.VARINT);

	public EntityRooster(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SEEDS, Integer.valueOf(0));
	}

	@Override
	protected void initEntityAI() {
		tasks.addTask(0, new EntityAISwimming(this));
		tasks.addTask(1, new EntityAIAttackMelee(this, 1D, false));
		tasks.addTask(2, new EntityAIRoosterMating(this, 1.0D));
		tasks.addTask(3, new EntityAITempt(this, 1.0D, false, TEMPTATION_ITEMS));
		tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		tasks.addTask(5, new EntityAIWander(this, 1.0D));
		tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		tasks.addTask(7, new EntityAILookIdle(this));
		targetTasks.addTask(0, new EntityAIHurtByTarget(this, false));
		targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityRooster.class, false));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(4.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1D);
		getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(8.0D);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		oFlap = wingRotation;
		oFlapSpeed = destPos;
		destPos = (float) ((double) destPos + (double) (onGround ? -1 : 4) * 0.3D);
		destPos = MathHelper.clamp(destPos, 0.0F, 1.0F);

		if (!onGround && wingRotDelta < 1.0F)
			wingRotDelta = 1.0F;

		wingRotDelta = (float) ((double) wingRotDelta * 0.9D);

		if (!onGround && motionY < 0.0D)
			motionY *= 0.6D;

		wingRotation += wingRotDelta * 2.0F;

		if(world.getWorldTime()%5 == 0 && !world.isRemote)
			convertSeeds();
		
		
		//Never drop eggs
		this.timeUntilNextEgg = 500;
	}
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) 
	{
		if (!world.isRemote) 
		{
			ItemStack stack = player.getHeldItem(hand);
			
			if(hand == EnumHand.MAIN_HAND && (stack.isEmpty() || stack.getItem() instanceof ItemSeeds))
			{
				//if(stack !=null) System.out.println(stack.getDisplayName());
				player.openGui(Hatchery.INSTANCE, CommonProxy.GUI_ID_ROOSTER, player.world, getEntityId(), 0, 0);
				
				return true;
			}
			
			
		}
		return super.processInteract(player, hand);
	}

	@Override
	public boolean canMateWith(EntityAnimal otherAnimal) 
	{
		return otherAnimal == this ? false : ((!(otherAnimal instanceof EntityChicken) || (otherAnimal instanceof EntityRooster)) ? false : isInLove() && otherAnimal.isInLove());
	}

	@Override
	public EntityRooster createChild(EntityAgeable entityageable) 
	{
		return null;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) 
	{
		boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) ((int) getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue()));

		if (flag)
			applyEnchantments(this, entityIn);

		return flag;
	}

	public void convertSeeds() 
	{
		if(getHasSeeds() && getSeeds() <= MAX_SEEDS -2) 
		{
			setSeeds(getSeeds() + 2);
			inventory.getStackInSlot(SEED_SLOT).shrink(2);
			if(inventory.getStackInSlot(SEED_SLOT).getCount() <= 0)
				inventory.setStackInSlot(SEED_SLOT, ItemStack.EMPTY);
		}
	}

	public boolean getHasSeeds() 
	{
		return inventory.getStackInSlot(SEED_SLOT) != null && TEMPTATION_ITEMS.contains(inventory.getStackInSlot(SEED_SLOT).getItem()) && inventory.getStackInSlot(SEED_SLOT).getCount() >= 2;
	}

	public void setSeeds(int size) 
	{
		dataManager.set(SEEDS, size);
	}

	public int getSeeds() 
	{
		return dataManager.get(SEEDS).intValue();
	}
	
	public int getScaledSeeds(int scale) 
	{
		return getSeeds() != 0 ? (int) ((float) getSeeds() / (float) MAX_SEEDS * scale) : 0;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) 
	{
		super.writeEntityToNBT(nbt);
		nbt.setInteger("Seeds", getSeeds());
		inventory.writeToNBT(nbt);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) 
	{
		super.readEntityFromNBT(nbt);
		setSeeds(nbt.getInteger("Seeds"));
		inventory.readFromNBT(nbt);
	}

//	// Inventory
//	
//	@Override
//	public int getSizeInventory()  { return inventory.length; }
//
//	@Override
//	public ItemStack getStackInSlot(int slot) {	return inventory[slot]; }
//
//	@Override
//	public ItemStack decrStackSize(int slot, int size) 
//	{
//		if (!inventory[slot].isEmpty()) 
//		{
//			ItemStack itemstack;
//			if (inventory[slot].getCount() <= size) 
//			{
//				itemstack = inventory[slot];
//				inventory[slot] = ItemStack.EMPTY;
//				return itemstack;
//			} else 
//			{
//				itemstack = inventory[slot].splitStack(size);
//				if (inventory[slot].getCount() == 0)
//					inventory[slot] = ItemStack.EMPTY;
//				return itemstack;
//			}
//		} else
//			return null;
//	}
//
//	public ItemStack getStackInSlotOnClosing(int slot) 
//	{
//		if (inventory[slot] != null) 
//		{
//			ItemStack itemstack = inventory[slot];
//			inventory[slot] = null;
//			return itemstack;
//		} else
//			return null;
//	}
//
//	@Override
//	public void setInventorySlotContents(int slot, ItemStack stack) 
//	{
//		inventory[slot] = stack;
//
//		if (stack != null && stack.getCount() > getInventoryStackLimit())
//			stack.setCount(getInventoryStackLimit());
//	}
//
//	@Override
//	public int getInventoryStackLimit() { return 64; }
//
//	@Override
//	public void markDirty() { }
//
//	@Override
//	public boolean isItemValidForSlot(int slot, ItemStack stack) 
//	{
//		return false;
//	}
//
//	@Override
//	public ItemStack removeStackFromSlot(int index) 
//	{
//		return null;
//	}
//
//	@Override
//	public void openInventory(EntityPlayer player) { }
//
//	@Override
//	public void closeInventory(EntityPlayer player) { }
//
//	@Override
//	public int getField(int id) { return 0; }
//
//	@Override
//	public void setField(int id, int value) {	
//	}
//
//	@Override
//	public int getFieldCount() { return 0; }
//
//	@Override
//	public void clear() {	}
//
//	@Override
//	public boolean isEmpty() {
//		return false;
//	}
//
//	@Override
//	public boolean isUsableByPlayer(EntityPlayer player) {
//		return true;
//	}
	
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