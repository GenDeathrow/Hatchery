package com.gendeathrow.hatchery.network;

import com.gendeathrow.hatchery.Hatchery;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HatcheryWindowPacket implements IMessage 
	{
	    private int windowId;
	    private int property;
	    private int value;

	    public HatcheryWindowPacket()
	    {
	    }

	    public HatcheryWindowPacket(int windowIdIn, int propertyIn, int valueIn)
	    {
	        this.windowId = windowIdIn;
	        this.property = propertyIn;
	        this.value = valueIn;
	    }

	    /**
	     * Passes this Packet on to the NetHandler for processing.
	     */
	    public void processPacket(INetHandlerPlayClient handler)
	    {
	        //handler.handleWindowProperty(this);
	    }

	    
	    public static void sendProgressBarUpdate(IContainerListener listener, Container container, int varToUpdate, int newValue)
	    {
			if(listener instanceof EntityPlayerMP)
				Hatchery.network.sendTo(new HatcheryWindowPacket(container.windowId, varToUpdate, newValue), (EntityPlayerMP)listener);
	    }
	    /**
	     * Reads the raw packet data from the data stream.
	     */
		@Override
		public void fromBytes(ByteBuf buf)
	    {
	        this.windowId = buf.readUnsignedByte();
	        this.property = buf.readShort();
	        this.value = buf.readInt();
	    }

	    /**
	     * Writes the raw packet data to the data stream.
	     */
		@Override
		public void toBytes(ByteBuf buf)
	    {
	        buf.writeByte(this.windowId);
	        buf.writeShort(this.property);
	        buf.writeInt(this.value);
	    }

	    @SideOnly(Side.CLIENT)
	    public int getWindowId()
	    {
	        return this.windowId;
	    }

	    @SideOnly(Side.CLIENT)
	    public int getProperty()
	    {
	        return this.property;
	    }

	    @SideOnly(Side.CLIENT)
	    public int getValue()
	    {
	        return this.value;
	    }


		///////////////////////////////////////////
		// Client Handler
		///////////////////////////////////////////
		public static class ClientHandler implements IMessageHandler<HatcheryWindowPacket, IMessage> 
		{

			@Override
			public IMessage onMessage(final HatcheryWindowPacket message, MessageContext ctx) 
			{
				
			    IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
	            mainThread.addScheduledTask(new Runnable() 
	            {
	                @Override
	                public void run() 
	                {
	        			
	        	        EntityPlayer entityplayer = Minecraft.getMinecraft().thePlayer;
	        	        
	        	        if (entityplayer.openContainer != null && entityplayer.openContainer.windowId == message.getWindowId())
	        	        {
	        	            entityplayer.openContainer.updateProgressBar(message.getProperty(), message.getValue());
	        	        }
	        	        
	                }
	            });
	            
	            
				
				return null;
			}
			
		}

	}