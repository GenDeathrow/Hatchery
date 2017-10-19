package com.gendeathrow.hatchery.network;

import org.apache.logging.log4j.Level;

import com.gendeathrow.hatchery.Hatchery;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class HatcheryPacket implements IMessage 
{
	public int requestID = 0;
	public NBTTagCompound tags = new NBTTagCompound();
	
	public HatcheryPacket(){	}

	public HatcheryPacket(NBTTagCompound tags) // Use PacketDataTypes to instantiate new packets
	{
		this.requestID = 0;
		this.tags = tags;
	}
	
	protected HatcheryPacket(int requestID, NBTTagCompound tags) // Use PacketDataTypes to instantiate new packets
	{
		this.requestID = requestID;
		this.tags = tags;
	}
	
	
	public static HatcheryPacket requestItemstackTE(BlockPos pos)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("bposX", pos.getX());
		nbt.setDouble("bposY", pos.getY());
		nbt.setDouble("bposZ", pos.getZ());
		return new HatcheryPacket(1, nbt);
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) 
	{
		requestID = ByteBufUtils.readVarInt(buf, 3);
		tags = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		ByteBufUtils.writeVarInt(buf, requestID, 3);
		ByteBufUtils.writeTag(buf, tags);
	}
	
	/////////////////////////////////////////////
	// Server Message
	////////////////////////////////////////////
	public static class ServerHandler implements IMessageHandler<HatcheryPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final HatcheryPacket message, final MessageContext ctx) 
		{
			if(message == null || message.tags == null)
			{
				Hatchery.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Hatchery packet server side", new NullPointerException());
				return null;
			}
		
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
	          
			mainThread.addScheduledTask(new Runnable() 
				{
					@Override
	                public void run() 
					{

						EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
						NBTTagCompound nbt = message.tags;
						
						if(message.requestID == 1) 
						{
				   			//System.out.println("Server Recieved:"+ nbt.getDouble("bposX")+","+ nbt.getDouble("bposY")+","+ nbt.getDouble("bposZ"));
				   	     
				   			
							TileEntity te = sender.worldObj.getTileEntity(new BlockPos(nbt.getDouble("bposX"), nbt.getDouble("bposY"), nbt.getDouble("bposZ")));
							
							NBTTagCompound requestNBT = new NBTTagCompound();
								
								requestNBT = te.writeToNBT(requestNBT);
									
								requestNBT.setDouble("bposX", te.getPos().getX());
								requestNBT.setDouble("bposY", te.getPos().getY());
								requestNBT.setDouble("bposZ", te.getPos().getZ());
									
							Hatchery.network.sendTo(new HatcheryPacket(requestNBT), sender);
								
								
									//System.out.println("Sent Message");
//							}
//							else if(te instanceof NestPenTileEntity)
//							{
//								NestPenTileEntity hte = (NestPenTileEntity) te;
//								
//								ItemStack itemstack = hte.getStackInSlot(0);
//								
//								if(itemstack != null)
//								{
//									NBTTagCompound requestNBT = new NBTTagCompound();
//									
//									requestNBT = hte.writeToNBT(requestNBT);
//									
//									requestNBT.setDouble("bposX", hte.getPos().getX());
//									requestNBT.setDouble("bposY", hte.getPos().getY());
//									requestNBT.setDouble("bposZ", hte.getPos().getZ());
//									
//									Hatchery.network.sendTo(new HatcheryPacket(requestNBT), sender);
//									
//									//System.out.println("Sent Message");
//								}
//							}
						}				
						
	                }
	            });
	            
			
			return null;
		}
		
		
	}
	

	///////////////////////////////////////////
	// Client Handler
	///////////////////////////////////////////
	public static class ClientHandler implements IMessageHandler<HatcheryPacket, IMessage> 
	{

		@Override
		public IMessage onMessage(final HatcheryPacket message, MessageContext ctx) 
		{
			if(message == null || message.tags == null)
			{
				Hatchery.logger.log(Level.ERROR, "A critical NPE error occured during while handling a Hatchery packet Client side", new NullPointerException());
				return null;
			}
			
		    IThreadListener mainThread = Minecraft.getMinecraft(); // or Minecraft.getMinecraft() on the client
            mainThread.addScheduledTask(new Runnable() 
            {
                @Override
                public void run() 
                {


        			NBTTagCompound nbt = message.tags;
        			
        			if(message.requestID == 0) 
        			{
            			//System.out.println("Client Recieved"+ nbt.getDouble("bposX")+","+ nbt.getDouble("bposY")+","+ nbt.getDouble("bposZ"));

        				TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(nbt.getDouble("bposX"), nbt.getDouble("bposY"), nbt.getDouble("bposZ")));
        				
        				te.readFromNBT(nbt);
        				
        			}

                }
            });
            
            
			
			return null;
		}
		
	}

}
