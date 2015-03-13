package me.planetguy.remaininmotion.network;

import me.planetguy.remaininmotion.util.Position.BlockPosition;
import me.planetguy.remaininmotion.util.Position.BlockRecord;
import me.planetguy.remaininmotion.util.Position.BlockRecordSet;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageRotator;
import me.planetguy.remaininmotion.render.CarriageRenderCache;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public abstract class RenderPacket {
	public static void Dispatch(CarriagePackage Package) {

		// Debug.dbg("Dispatching render packet");
		NBTTagCompound Packet = new NBTTagCompound();

		Packet.setInteger("DriveX", Package.driveRecord.X);
		Packet.setInteger("DriveY", Package.driveRecord.Y);
		Packet.setInteger("DriveZ", Package.driveRecord.Z);

		Packet.setBoolean("Anchored", Package.DriveIsAnchored);

		Packet.setInteger("Dimension", Package.RenderCacheKey.Dimension);

		NBTTagList Body = new NBTTagList();

		for (BlockRecord Record : Package.Body) {
			NBTTagCompound Tag = new NBTTagCompound();

			Tag.setInteger("X", Record.X);
			Tag.setInteger("Y", Record.Y);
			Tag.setInteger("Z", Record.Z);

			Body.appendTag(Tag);
		}

		Packet.setTag("Body", Body);

		Packet.setInteger("axis", Package.axis);

		// Debug.dbg("RemIM tags:"+((NBTTagList)
		// Packet.getTag("Body")).tagCount());

		if (Package.MotionDirection == null) {
			PacketManager.BroadcastPacketFromBlock(Package.AnchorRecord.X, Package.AnchorRecord.Y,
					Package.AnchorRecord.Z, Package.world, PacketTypes.Render, Packet);

			PacketManager.BroadcastPacketFromBlock(Package.AnchorRecord.X - Package.driveRecord.X
					+ Package.Translocator.xCoord, Package.AnchorRecord.Y - Package.driveRecord.Y
					+ Package.Translocator.yCoord, Package.AnchorRecord.Z - Package.driveRecord.Z
					+ Package.Translocator.zCoord, Package.Translocator.getWorldObj(), PacketTypes.Render, Packet);
		} else {
			PacketManager.BroadcastPacketFromBlock(Package.AnchorRecord.X + Package.MotionDirection.DeltaX,
					Package.AnchorRecord.Y + Package.MotionDirection.DeltaY, Package.AnchorRecord.Z
							+ Package.MotionDirection.DeltaZ, Package.world, PacketTypes.Render, Packet);
		}
	}

	public static void Handle(NBTTagCompound Packet, World World) {
		int DriveX = Packet.getInteger("DriveX");
		int DriveY = Packet.getInteger("DriveY");
		int DriveZ = Packet.getInteger("DriveZ");

		boolean DriveIsAnchored = Packet.getBoolean("Anchored");

		int Dimension = Packet.getInteger("Dimension");

		NBTTagList Body = (NBTTagList) Packet.getTag("Body");

		// Debug.dbg("<Body= ("+Body.tagCount()+")");

		BlockRecordSet Blocks = new BlockRecordSet();

		BlockRecordSet TileEntities = new BlockRecordSet();

		for (int Index = 0; Index < Body.tagCount(); Index++) {
			NBTTagCompound Tag = Body.getCompoundTagAt(Index);

			BlockRecord Record = new BlockRecord(Tag.getInteger("X"), Tag.getInteger("Y"), Tag.getInteger("Z"));

			Record.Identify(World);

			Blocks.add(Record);

			if (Record.entity != null) {
				TileEntities.add(Record);
			}

			if (!DriveIsAnchored) {
				if (Record.X == DriveX) {
					if (Record.Y == DriveY) {
						if (Record.Z == DriveZ) {
							try {
								((TileEntityCarriageDrive) Record.entity).Active = true;

								if (Record.entity instanceof TileEntityCarriageRotator) {
									((TileEntityCarriageRotator) Record.entity).setAxis(Packet.getInteger("axis"));
								}

							} catch (Throwable Throwable) {
								Throwable.printStackTrace();
							}
						}
					}
				}
			}
		}

		try {
			CarriageRenderCache.Assemble(Blocks, TileEntities, World, new BlockPosition(DriveX, DriveY, DriveZ,
					Dimension));
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}
	}
}
