package com.gendeathrow.hatchery.item;

import com.gendeathrow.hatchery.core.init.ModItems;
import com.gendeathrow.hatchery.util.ItemStackEntityNBTHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HatcheryEggThrown extends EntityThrowable
{

    private static final DataParameter<Integer> EGGTYPE = EntityDataManager.createKey(HatcheryEggThrown.class, DataSerializers.VARINT);

	private NBTTagCompound spawnEntity;
	private ItemStack itemstack = ItemStack.EMPTY;
	
	
	public HatcheryEggThrown(World worldIn) 
	{
		super(worldIn);
	}
	
    public void setEggColore(int color) {
        this.dataManager.set(EGGTYPE, color);
    }

    private int getEggColor() {
        return this.dataManager.get(EGGTYPE);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EGGTYPE, 0);
    }
    
	
	public HatcheryEggThrown(World worldIn, EntityLivingBase throwerIn, ItemStack stack) 
	{
		super(worldIn, throwerIn);
		
		spawnEntity = ItemStackEntityNBTHelper.getEntityTagFromStack(stack);
		
		this.setEggColore(HatcheryEgg.getColor(stack));
	}

	
	public HatcheryEggThrown(World worldIn, ItemStack stackIn, double x, double y, double z) 
	{
		super(worldIn, x, y, z);
		
		spawnEntity = ItemStackEntityNBTHelper.getEntityTagFromStack(stackIn);
		
		this.itemstack = stackIn;
		
		this.setEggColore(HatcheryEgg.getColor(stackIn));
	}


	@Override
	protected void onImpact(RayTraceResult result)
	    {
	        if (result.entityHit != null)
	        {
	            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
	        }
	        
	        if (!this.world.isRemote)
	        {
	            if (this.rand.nextInt(8) == 0)
	            {
	            	Entity entitychicken = null; 
	        	
	            	if(spawnEntity == null) 
	            	{
	            		entitychicken = new EntityChicken(this.world);
	            		if(entitychicken instanceof EntityAgeable) ((EntityAgeable)entitychicken).setGrowingAge(-24000);
	            	}
	            	else 
	            	{
	            		entitychicken = EntityList.createEntityFromNBT(spawnEntity, this.world);
	            	}
    	        
	            	if(entitychicken != null)
	            	{
	            		entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
	            		this.world.spawnEntity(entitychicken);
	            	}
	            }

	            this.world.setEntityState(this, (byte)3);
	            this.setDead();
	        }
	        
	    }


    /**
     * Handler for {@link World#setEntityState}
     */
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            double d0 = 0.08D;

            for (int i = 0; i < 8; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(Items.EGG));
            }
        }
    }
    
	public ItemStack getItemStacktoRender() {
		ItemStack stack = new ItemStack(ModItems.hatcheryEgg, 1);
		HatcheryEgg.setColor(stack, getEggColor());
		return stack;
	}
}
