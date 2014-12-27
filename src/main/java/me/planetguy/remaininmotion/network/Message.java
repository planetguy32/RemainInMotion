package me.planetguy.remaininmotion.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Message implements IMessage, IMessageHandler<Message, IMessage> {

	@Deprecated
	// Only for use by Netty
	public Message() {}

	public Message(PacketTypes type, NBTTagCompound body) {
		this.type = type;
		this.body = body;
	}

	public NBTTagCompound	body;
	public PacketTypes		type;

	@Override
	public IMessage onMessage(Message message, MessageContext ctx) {
		// Debug.dbg("Handling message "+message.type);
		if (message.type.equals(PacketTypes.Render)) {
			RenderPacket.Handle(message.body, Minecraft.getMinecraft().thePlayer.worldObj);
		} else if (message.type.equals(PacketTypes.MultipartPropagation)) {
			MultipartPropagationPacket.Handle(message.body, Minecraft.getMinecraft().thePlayer.worldObj);
		}
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf) {

		try {
			int tagLength = buf.readInt();
			buf.discardReadBytes();

			type = PacketTypes.values()[buf.readByte()];
			buf.discardReadBytes();

			byte[] bytes = buf.array();

			byte[] fixedBytes = new byte[bytes.length - 6]; // Truly foul hack
			for (int i = 1; i < fixedBytes.length + 1; i++) {
				fixedBytes[i - 1] = bytes[i];
			}

			body = CompressedStreamTools.func_152457_a(fixedBytes, NBTSizeTracker.field_152451_a);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void toBytes(ByteBuf buf) {
		try {
			byte[] tagBytes = CompressedStreamTools.compress(body);

			buf.writeInt(tagBytes.length);
			buf.writeByte(type.ordinal());

			buf.writeBytes(tagBytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String print(ByteBuf buf) {
		String str = "";
		return toStr(buf.array());
	}

	public String toStr(byte[] data) {
		String str = "";
		for (byte b : data) {
			str += Byte.toString(b) + " ";
		}
		return str;
	}

	// Sent packet: 0 0 31 -117 8 0 0 0 0 0 0 0 -29 98 96 96 100 -32 112 -52 75
	// -50 -56 47 74 77 97 96 102 96 115 41 -54 44 75 -115 -8 -1 -1 -1 44 24 39
	// -110 -127 -127 -63 -98 -109 -127 -59 41 63 -91 -110 11 -56 102 98 102 96
	// 4 -117 1 105 -88 66 -58 40 32 -1 4 3 84 -62 1 -117 4 -89 75 102 110 106
	// 94 113 102 126 30 80 0 110 15 68 22 0 42 -43 24 -1 -123 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// Got packet: 0 0 31 -117 8 0 0 0 0 0 0 0 -29 98 96 96 100 -32 112 -52 75
	// -50 -56 47 74 77 97 96 102 96 115 41 -54 44 75 -115 -8 -1 -1 -1 44 24 39
	// -110 -127 -127 -63 -98 -109 -127 -59 41 63 -91 -110 11 -56 102 98 102 96
	// 4 -117 1 105 -88 66 -58 40 32 -1 4 3 84 -62 1 -117 4 -89 75 102 110 106
	// 94 113 102 126 30 80 0 110 15 68 22 0 42 -43 24 -1 -123 0 0 0

	// Sent packet: 0 0 0 0 101 31 -117 8 0 0 0 0 0 0 0 -29 98 96 96 100 -32 112
	// -52 75 -50 -56 47 74 77 97 96 102 96 115 41 -54 44 75 -115 -8 -1 -1 -1 76
	// 24 39 -110 -127 -127 -63 -98 -109 -127 -59 41 63 -91 -110 11 -56 102 98
	// 102 96 4 -117 1 105 -88 66 -58 40 32 -1 56 3 84 -62 1 -117 4 -89 75 102
	// 110 106 94 113 102 126 30 80 0 110 15 68 22 0 42 -58 44 76 -123 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
	// Got packet: 0 0 0 0 101 31 -117 8 0 0 0 0 0 0 0 -29 98 96 96 100 -32 112
	// -52 75 -50 -56 47 74 77 97 96 102 96 115 41 -54 44 75 -115 -8 -1 -1 -1 76
	// 24 39 -110 -127 -127 -63 -98 -109 -127 -59 41 63 -91 -110 11 -56 102 98
	// 102 96 4 -117 1 105 -88 66 -58 40 32 -1 56 3 84 -62 1 -117 4 -89 75 102
	// 110 106 94 113 102 126 30 80 0 110 15 68 22 0 42 -58 44 76 -123 0 0 0 0

}
