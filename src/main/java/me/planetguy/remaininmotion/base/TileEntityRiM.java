package me.planetguy.remaininmotion.base;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public abstract class TileEntityRiM extends net.minecraft.tileentity.TileEntity {
	public void WriteCommonRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {}

	public void WriteServerRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {}

	public void WriteClientRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {}

	@Override
	public void writeToNBT(net.minecraft.nbt.NBTTagCompound TagCompound) {
		super.writeToNBT(TagCompound);

		WriteCommonRecord(TagCompound);

		WriteServerRecord(TagCompound);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();

		WriteCommonRecord(tag);

		WriteClientRecord(tag);

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	public void ReadCommonRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {}

	public void ReadServerRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {}

	public void ReadClientRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {}

	@Override
	public void readFromNBT(net.minecraft.nbt.NBTTagCompound TagCompound) {
		super.readFromNBT(TagCompound);

		ReadCommonRecord(TagCompound);

		ReadServerRecord(TagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager nm, S35PacketUpdateTileEntity Packet) {
		ReadCommonRecord(Packet.func_148857_g());

		ReadClientRecord(Packet.func_148857_g());

		MarkRenderRecordDirty();
	}

	public void MarkServerRecordDirty() {
		worldObj.getChunkFromBlockCoords(xCoord, zCoord).setChunkModified();
	}

	public void MarkClientRecordDirty() {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	public void Propagate() {
		MarkServerRecordDirty();

		MarkClientRecordDirty();
	}

	public void MarkRenderRecordDirty() {
		worldObj.markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
	}

	public void Initialize() {}

	@Override
	public void validate() {
		super.validate();

		Initialize();
	}

	public void Finalize() {}

	@Override
	public void invalidate() {
		Finalize();

		super.invalidate();
	}

	public void Setup(net.minecraft.entity.player.EntityPlayer Player, ItemStack Item) {}

	public void EmitDrops(BlockRiM Block, int Meta) {}

	public void EmitDrop(BlockRiM Block, ItemStack Drop) {
		Block.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, Drop);
	}
}
