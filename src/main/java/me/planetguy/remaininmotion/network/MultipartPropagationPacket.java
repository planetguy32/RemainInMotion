package me.planetguy.remaininmotion.network;

import java.util.Collection;

import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class MultipartPropagationPacket {
	public static void Dispatch(EntityPlayerMP Player, Collection<TileEntity> Tiles) {
		NBTTagCompound Packet = new NBTTagCompound();

		Packet.setInteger("Id", Block.getIdFromBlock(TileEntityMotiveSpectre.MultipartContainerBlockId));

		NBTTagList Body = new NBTTagList();

		for (TileEntity Tile : Tiles) {
			NBTTagCompound Tag = new NBTTagCompound();

			Tag.setInteger("X", Tile.xCoord);
			Tag.setInteger("Y", Tile.yCoord);
			Tag.setInteger("Z", Tile.zCoord);

			Body.appendTag(Tag);
		}

		Packet.setTag("Body", Body);

		PacketManager.SendPacketToPlayer(Player, PacketTypes.MultipartPropagation, Packet);
	}

	public static void Handle(NBTTagCompound Packet, World World) {
		Block Id = Block.getBlockById(Packet.getInteger("Id"));

		NBTTagList Body = (NBTTagList) Packet.getTag("Body");

		for (int Index = 0; Index < Body.tagCount(); Index++) {
			NBTTagCompound Tag = Body.getCompoundTagAt(Index);

			World.setBlock(Tag.getInteger("X"), Tag.getInteger("Y"), Tag.getInteger("Z"), Id, 0, 0);
		}
	}
}
