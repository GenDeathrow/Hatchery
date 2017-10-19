package com.gendeathrow.hatchery.entities;

import java.util.Set;

import com.gendeathrow.hatchery.Hatchery;
import com.gendeathrow.hatchery.core.proxies.CommonProxy;
import com.gendeathrow.hatchery.entities.ai.EntityAIRoosterMating;
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
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityRooster extends EntityChicken implements IInventory {
	public static final Set<Item> TEMPTATION_ITEMS = Sets.newHashSet(new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
	public ItemStack[] inventory;
	public static final int SEED_SLOT = 0;
	public static final int MAX_SEEDS = 20;
	private static final DataParameter<Integer> SEEDS = EntityDataManager.<Integer>createKey(EntityRooster.class, DataSerializers.VARINT);

	public EntityRooster(World world) {
		super(world);
		inventory = new ItemStack[1];
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
		destPos = MathHelper.clamp_float(destPos, 0.0F, 1.0F);

		if (!onGround && wingRotDelta < 1.0F)
			wingRotDelta = 1.0F;

		wingRotDelta = (float) ((double) wingRotDelta * 0.9D);

		if (!onGround && motionY < 0.0D)
			motionY *= 0.6D;

		wingRotation += wingRotDelta * 2.0F;

		if(worldObj.getWorldTime()%5 == 0 && !worldObj.isRemote)
			convertSeeds();
		
		
		//Never drop eggs
		this.timeUntilNextEgg = 500;
	}
	
	@Override
    public boolean processInteract(EntityPlayer player, EnumHand hand, ItemStack stack) 
	{
		if (!worldObj.isRemote) 
		{
			if(hand == EnumHand.MAIN_HAND && (stack == null || stack.getItem() instanceof ItemSeeds))
			{
				//if(stack !=null) System.out.println(stack.getDisplayName());
				player.openGui(Hatchery.INSTANCE, CommonProxy.GUI_ID_ROOSTER, player.worldObj, getEntityId(), 0, 0);
				
				return true;
			}
			
			
		}
		return super.processInteract(player, hand, stack);
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
			getStackInSlot(SEED_SLOT).stackSize -= 2;
			if(getStackInSlot(SEED_SLOT).stackSize <= 0)
				setInventorySlotContents(SEED_SLOT, null);
		}
	}

	public boolean getHasSeeds() 
	{
		return getStackInSlot(SEED_SLOT) != null && TEMPTATION_ITEMS.contains(getStackInSlot(SEED_SLOT).getItem()) && getStackInSlot(SEED_SLOT).stackSize >= 2;
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
		NBTTagList tags = new NBTTagList();
		for (int i = 0; i < inventory.length; i++)
			if (inventory[i] != null) 
			{
				NBTTagCompound data = new NBTTagCompound();
				data.setByte("Slot", (byte) i);
				inventory[i].writeToNBT(data);
				tags.appendTag(data);
			}
		nbt.setTag("Items", tags);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) 
	{
		super.readEntityFromNBT(nbt);
		setSeeds(nbt.getInteger("Seeds"));
		NBTTagList tags = nbt.getTagList("Items", 10);
		inventory = new ItemStack[getSizeInventory()];
		for (int i = 0; i < tags.tagCount(); i++) 
		{
			NBTTagCompound data = tags.getCompoundTagAt(i);
			int j = data.getByte("Slot") & 255;
			if (j >= 0 && j < inventory.length)
				inventory[j] = ItemStack.loadItemStackFromNBT(data);
		}
	}

	// Inventory
	
	@Override
	public int getSizeInventory()  { return inventory.length; }

	@Override
	public ItemStack getStackInSlot(int slot) {	return inventory[slot]; }

	@Override
	public ItemStack decrStackSize(int slot, int size) 
	{
		if (inventory[slot] != null) 
		{
			ItemStack itemstack;
			if (inventory[slot].stackSize <= size) 
			{
				itemstack = inventory[slot];
				inventory[slot] = null;
				return itemstack;
			} else 
			{
				itemstack = inventory[slot].splitStack(size);
				if (inventory[slot].stackSize == 0)
					inventory[slot] = null;
				return itemstack;
			}
		} else
			return null;
	}

	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		if (inventory[slot] != null) 
		{
			ItemStack itemstack = inventory[slot];
			inventory[slot] = null;
			return itemstack;
		} else
			return null;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) 
	{
		inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit())
			stack.stackSize = getInventoryStackLimit();
	}

	@Override
	public int getInventoryStackLimit() { return 64; }

	@Override
	public void markDirty() { }

	@Override
	public final boolean isUseableByPlayer(EntityPlayer player) 
	{
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		return false;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public int getField(int id) { return 0; }

	@Override
	public void setField(int id, int value) {	
	}

	@Override
	public int getFieldCount() { return 0; }

	@Override
	public void clear() {	}
}