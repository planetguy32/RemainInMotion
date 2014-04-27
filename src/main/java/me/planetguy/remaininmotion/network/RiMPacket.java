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
import io.netty.buffer.EmptyByteBuf;

public class RiMPacket {

	public int type;
	public NBTTagCompound body;

	public RiMPacket(){}

	public RiMPacket(NBTTagCompound body) {
		this.body=body;
	}

	public void readBytes(ByteBuf bytes) throws IOException {
		System.out.println("Length="+bytes.readableBytes());
		if(!(bytes instanceof EmptyByteBuf)){
			this.body=CompressedStreamTools.decompress(bytes.array());
			this.type=body.getInteger("packetType");
		}
	}

	public void writeBytes(ByteBuf bytes) throws IOException {
		System.out.println("Length="+bytes.readableBytes());
		body.setInteger("packetType", type);
		bytes.writeBytes(CompressedStreamTools.compress(body));
		System.out.println("Length="+bytes.readableBytes());

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
