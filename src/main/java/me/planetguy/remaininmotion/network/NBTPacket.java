package me.planetguy.remaininmotion.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class NBTPacket {
	
	/* Only for Netty's use */
	@Deprecated
	public NBTPacket(){}
	
	public NBTPacket(int type, NBTTagCompound body) {
		this.type = (byte) type;
		this.body = body;
	}

	public NBTTagCompound	body;
	public byte		type;

	public void fromBytes(ByteBuf buf) {

		try {
			int tagLength = buf.readInt();
			buf.discardReadBytes();

			type = buf.readByte();
			buf.discardReadBytes();

			byte[] bytes = buf.array();

			byte[] fixedBytes = new byte[bytes.length - 6]; // Truly foul hack
			for (int i = 1; i < fixedBytes.length + 1; i++) {
				fixedBytes[i - 1] = bytes[i];
			}
			
			//Use size limiter
			body = CompressedStreamTools.func_152457_a(fixedBytes, createTracker());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract NBTSizeTracker createTracker();

	public void toBytes(ByteBuf buf) {
		try {
			byte[] tagBytes = CompressedStreamTools.compress(body);

			buf.writeInt(tagBytes.length);
			buf.writeByte(type);

			buf.writeBytes(tagBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
