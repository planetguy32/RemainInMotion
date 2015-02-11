package me.planetguy.remaininmotion.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.client.FMLClientHandler;

public abstract class TileEntityRiM extends TileEntity {
	public void WriteCommonRecord(NBTTagCompound TagCompound) {}

	public void WriteServerRecord(NBTTagCompound TagCompound) {}

	public void WriteClientRecord(NBTTagCompound TagCompound) {}

	@Override
	public void writeToNBT(NBTTagCompound TagCompound) {
		super.writeToNBT(TagCompound);

		WriteCommonRecord(TagCompound);

		WriteServerRecord(TagCompound);
	}

	// make all carriages render as far as the render distance
	@Override
	public double getMaxRenderDistanceSquared() {
		return (16 * FMLClientHandler.instance().getClient().gameSettings.renderDistanceChunks)
				* (16 * FMLClientHandler.instance().getClient().gameSettings.renderDistanceChunks);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();

		WriteCommonRecord(tag);

		WriteClientRecord(tag);

		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, tag);
	}

	public void ReadCommonRecord(NBTTagCompound TagCompound) {}

	public void ReadServerRecord(NBTTagCompound TagCompound) {}

	public void ReadClientRecord(NBTTagCompound TagCompound) {}

	@Override
	public void readFromNBT(NBTTagCompound TagCompound) {
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

	public void Setup(EntityPlayer Player, ItemStack Item) {}

	public void EmitDrops(BlockRiM Block, int Meta) {}

	public void EmitDrop(BlockRiM Block, ItemStack Drop) {
		Block.dropBlockAsItem(worldObj, xCoord, yCoord, zCoord, Drop);
	}

	public void rotateSpecial(ForgeDirection axis) {}
}
