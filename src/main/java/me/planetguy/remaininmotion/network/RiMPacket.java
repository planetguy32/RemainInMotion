package me.planetguy.remaininmotion.network;

import java.io.IOException;
import java.io.OutputStream;

import me.planetguy.remaininmotion.core.Core;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import io.netty.buffer.ByteBuf;

public class RiMPacket {
	
	public int type;
	public NBTTagCompound body;

	public RiMPacket(){}
	
	public RiMPacket(NBTTagCompound body) {
		this.body=body;
	}

	public void readBytes(ByteBuf bytes) throws IOException {
		this.type=bytes.readInt();
		this.body=CompressedStreamTools.decompress(bytes.array());
	}
	
	public void writeBytes(ByteBuf bytes) throws IOException {
		bytes.setInt(0, type);
		bytes.setBytes(0, CompressedStreamTools.compress(body));
	}
	
	public void executeClient(EntityPlayer player) {
		this.executeServer(player);
	}
	
	public void executeServer(EntityPlayer player) {
		try
		{
			Core . HandlePacket ( type , body, player ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}
}
