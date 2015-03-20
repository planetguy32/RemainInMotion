package me.planetguy.remaininmotion.spectre;

import me.planetguy.remaininmotion.util.Position.BlockRecord;
import me.planetguy.remaininmotion.util.Position.BlockRecordSet;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.drive.TileEntityCarriageTranslocator;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class TileEntityTeleportativeSpectre extends TileEntityMotiveSpectre {


	public boolean	Source;

	public int		ShiftX;
	public int		ShiftY;
	public int		ShiftZ;

	public int		MinX;
	public int		MinY;
	public int		MinZ;
	public int		MaxX;
	public int		MaxY;
	public int		MaxZ;

	public int		TargetDimension;

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);

		TagCompound.setBoolean("Source", Source);

		TagCompound.setInteger("ShiftX", ShiftX);
		TagCompound.setInteger("ShiftY", ShiftY);
		TagCompound.setInteger("ShiftZ", ShiftZ);

		TagCompound.setInteger("MinX", MinX);
		TagCompound.setInteger("MinY", MinY);
		TagCompound.setInteger("MinZ", MinZ);
		TagCompound.setInteger("MaxX", MaxX);
		TagCompound.setInteger("MaxY", MaxY);
		TagCompound.setInteger("MaxZ", MaxZ);

		TagCompound.setInteger("TargetDimension", TargetDimension);
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);

		Source = TagCompound.getBoolean("Source");

		ShiftX = TagCompound.getInteger("ShiftX");
		ShiftY = TagCompound.getInteger("ShiftY");
		ShiftZ = TagCompound.getInteger("ShiftZ");

		MinX = TagCompound.getInteger("MinX");
		MinY = TagCompound.getInteger("MinY");
		MinZ = TagCompound.getInteger("MinZ");
		MaxX = TagCompound.getInteger("MaxX");
		MaxY = TagCompound.getInteger("MaxY");
		MaxZ = TagCompound.getInteger("MaxZ");

		TargetDimension = TagCompound.getInteger("TargetDimension");
	}

	public void AbsorbCommon(CarriagePackage Package) {
		ShiftX = -Package.driveRecord.X + Package.Translocator.xCoord;
		ShiftY = -Package.driveRecord.Y + Package.Translocator.yCoord;
		ShiftZ = -Package.driveRecord.Z + Package.Translocator.zCoord;

		MinX = Package.MinX;
		MinY = Package.MinY;
		MinZ = Package.MinZ;
		MaxX = Package.MaxX;
		MaxY = Package.MaxY;
		MaxZ = Package.MaxZ;

		TargetDimension = Package.Translocator.getWorldObj().provider.dimensionId;

		motionDirection = Directions.values()[0];
	}

	public void AbsorbSource(CarriagePackage Package) {
		AbsorbCommon(Package);

		Source = true;

		renderCacheKey = Package.RenderCacheKey;

		driveRecord = new BlockRecord(Package.driveRecord);

		{
			body = new BlockRecordSet();

			for (BlockRecord Record : Package.Body) {
				body.add(new BlockRecord(Record));
			}
		}

		pendingBlockUpdates = new NBTTagList();
	}

	public void AbsorbSink(CarriagePackage Package) {
		Absorb(Package);

		AbsorbCommon(Package);

		driveRecord = new BlockRecord(Package.Translocator.xCoord, Package.Translocator.yCoord,
				Package.Translocator.zCoord);
	}

	@Override
	public void ShiftBlockPosition(BlockRecord Record) {
		Record.X += ShiftX;
		Record.Y += ShiftY;
		Record.Z += ShiftZ;
	}

	@Override
	public void ScheduleShiftedBlockUpdate(NBTTagCompound PendingBlockUpdateRecord) {
		worldObj.func_147446_b(PendingBlockUpdateRecord.getInteger("X") + ShiftX,
				PendingBlockUpdateRecord.getInteger("Y") + ShiftY, PendingBlockUpdateRecord.getInteger("Z") + ShiftZ,

				Block.getBlockById(PendingBlockUpdateRecord.getInteger("Id")),

				PendingBlockUpdateRecord.getInteger("Delay"),

				PendingBlockUpdateRecord.getInteger("Priority"));
	}

	@Override
	public void updateEntity() {
		ticksExisted++;

		if (worldObj.isRemote) { return; }

		if (ticksExisted < RiMConfiguration.CarriageMotion.TeleportationDuration) { return; }

		if (Source) {
			try {
				((TileEntityCarriageTranslocator) worldObj.getTileEntity(driveRecord.X, driveRecord.Y, driveRecord.Z))
						.ToggleActivity();
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}

			for (BlockRecord Record : body) {
				WorldUtil.ClearBlock(worldObj, Record.X, Record.Y, Record.Z);
			}

			CaptureEntities(MinX, MinY, MinZ, MaxX, MaxY, MaxZ);

			return;
		}

		Release();
	}

	@Override
	public boolean ShouldCaptureEntity(Entity Entity) {
		if (!RiMConfiguration.CarriageMotion.TeleportEntities) { return (false); }

		return (super.ShouldCaptureEntity(Entity));
	}

	@Override
	public void ProcessCapturedEntity(Entity Entity) {
		if (Entity.riddenByEntity == null) {
			TeleportEntity(Entity);
		}
	}

	public Entity TeleportEntity(Entity entity) {
		MinecraftServer Server = FMLCommonHandler.instance().getMinecraftServerInstance();

		WorldServer HomeWorld = (WorldServer) worldObj;

		WorldServer TargetWorld = Server.worldServerForDimension(TargetDimension);

		TeleportativeSpectreTeleporter Teleporter = new TeleportativeSpectreTeleporter(worldObj);

		boolean Transdimensional = (HomeWorld.provider.dimensionId != TargetWorld.provider.dimensionId);

		double X = entity.posX + ShiftX;
		double Y = entity.posY + ShiftY;
		double Z = entity.posZ + ShiftZ;
		float Yaw = entity.rotationYaw;
		float Pitch = entity.rotationPitch;

		Entity Mount = entity.ridingEntity;

		if (Mount != null) {
			entity.mountEntity(null);

			Mount = TeleportEntity(Mount);
		}

		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP Player = (EntityPlayerMP) entity;

			if (Transdimensional) {
				Server.getConfigurationManager().transferPlayerToDimension(Player, TargetDimension, Teleporter);
			}

			Player.playerNetServerHandler.setPlayerLocation(X, Y, Z, Yaw, Pitch);

			Player.setLocationAndAngles(X, Y, Z, Yaw, Pitch);
		} else {
			if (Transdimensional) {
				entity.dimension = TargetDimension;

				HomeWorld.removeEntity(entity);

				entity.isDead = false;

				Server.getConfigurationManager().transferEntityToWorld(entity, TargetDimension, HomeWorld, TargetWorld);

				Entity newEntity = EntityList.createEntityByName(EntityList.getEntityString(entity), TargetWorld);

				newEntity.copyDataFrom(entity, true);

				newEntity.setLocationAndAngles(X, Y, Z, Yaw, Pitch);

				TargetWorld.spawnEntityInWorld(newEntity);

				entity.isDead = true;

				HomeWorld.resetUpdateEntityTick();

				TargetWorld.resetUpdateEntityTick();

				entity = newEntity;
			} else {
				entity.setLocationAndAngles(X, Y, Z, Yaw, Pitch);

				TargetWorld.updateEntityWithOptionalForce(entity, false);
			}
		}

		if (Mount != null) {
			entity.mountEntity(Mount);
		}

		return (entity);
	}

	@Override
	public int[] getOffset() {
		return new int[] { ShiftX, ShiftY, ShiftZ };
	}
}
