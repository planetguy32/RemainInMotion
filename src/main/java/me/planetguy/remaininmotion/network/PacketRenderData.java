package me.planetguy.remaininmotion.network;

import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageRotator;
import me.planetguy.remaininmotion.render.CarriageRenderCache;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class PacketRenderData {
	
	public static void send(CarriagePackage Package) {
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

		if (Package.MotionDirection == null) {
			PacketManager.BroadcastPacketFromBlock(Package.AnchorRecord.X, Package.AnchorRecord.Y,
					Package.AnchorRecord.Z, Package.world, TypesDown.RENDER, Packet);

			PacketManager.BroadcastPacketFromBlock(Package.AnchorRecord.X - Package.driveRecord.X
					+ Package.Translocator.xCoord, Package.AnchorRecord.Y - Package.driveRecord.Y
					+ Package.Translocator.yCoord, Package.AnchorRecord.Z - Package.driveRecord.Z
					+ Package.Translocator.zCoord, Package.Translocator.getWorldObj(), TypesDown.RENDER, Packet);
		} else {
			PacketManager.BroadcastPacketFromBlock(Package.AnchorRecord.X + Package.MotionDirection.deltaX,
					Package.AnchorRecord.Y + Package.MotionDirection.deltaY, Package.AnchorRecord.Z
							+ Package.MotionDirection.deltaZ, Package.world, TypesDown.RENDER, Packet);
		}
	}

	public static void receive(NBTTagCompound Packet, World World) {
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
							if(Record.entity instanceof TileEntityCarriageDrive){
								((TileEntityCarriageDrive) Record.entity).Active = true;

								if (Record.entity instanceof TileEntityCarriageRotator) {
									((TileEntityCarriageRotator) Record.entity).setAxis(Packet.getInteger("axis"));
								}
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
