package me.planetguy.remaininmotion.spectre;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buildcraft.core.Box;
import buildcraft.factory.TileQuarry;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.TravelingItem;
import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.BlockPosition;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordList;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.ModInteraction;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.RiMConfiguration.CarriageMotion;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.network.MultipartPropagationPacket;
import me.planetguy.remaininmotion.render.CarriageRenderCache;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class TileEntityMotiveSpectre extends TileEntityRiM {
	public Directions MotionDirection = Directions.Null;

	public BlockPosition RenderCacheKey;

	public NBTTagList PendingBlockUpdates;

	public BlockRecord DriveRecord;

	public boolean DriveIsAnchored;

	public BlockRecordSet body;

	public int TicksExisted;

	public static double Velocity;

	TeleportativeSpectreTeleporter Teleporter;

	public void ShiftBlockPosition(BlockRecord Record) {
		Record.Shift(MotionDirection);
	}

	@Override
	public void validate() {
		if (worldObj instanceof WorldServer) {
			Teleporter = new TeleportativeSpectreTeleporter(worldObj);
		}
	}

	public void ScheduleShiftedBlockUpdate(
			NBTTagCompound PendingBlockUpdateRecord) {
		worldObj.func_147446_b // scheduleBlockUpdateFromLoad
		(PendingBlockUpdateRecord.getInteger("X") + MotionDirection.DeltaX,
				PendingBlockUpdateRecord.getInteger("Y")
						+ MotionDirection.DeltaY,
				PendingBlockUpdateRecord.getInteger("Z")
						+ MotionDirection.DeltaZ,

				Block.getBlockById(PendingBlockUpdateRecord.getInteger("Id")),

				PendingBlockUpdateRecord.getInteger("Delay"),

				PendingBlockUpdateRecord.getInteger("Priority"));
	}

	@Override
	public void updateEntity() {
		TicksExisted++;

		for (CapturedEntity Entity : CapturedEntities) {
			Entity.Update();
		}

		if (worldObj.isRemote) {
			return;
		}

		if (TicksExisted > 0
				&& TicksExisted < RiMConfiguration.CarriageMotion.MotionDuration
				&& TicksExisted % 20 == 0) {
			if (bodyHasCarriageDrive()) {
				ModRiM.plHelper.playSound(worldObj, xCoord, yCoord, zCoord,
						CarriageMotion.SoundFile, 0.8f, 1f);
			}
		}

		if (TicksExisted < RiMConfiguration.CarriageMotion.MotionDuration) {
			return;
		}

		Release();
	}

	private boolean bodyHasCarriageDrive() {
		if (body == null || body.isEmpty()) {
			return false;
		}
		for (BlockRecord temp : body) {
			if (temp.block instanceof BlockCarriageDrive) {
				return true;
			}
			if (temp.entity != null
					&& temp.entity instanceof TileEntityCarriageDrive) {
				return true;
			}
		}
		return false;
	}

	public static Block MultipartContainerBlockId;

	public void Release() {
		for (BlockRecord record : body) {
			ShiftBlockPosition(record);
		}
		if (!body.isEmpty()) {
			doRelease();
			body = new BlockRecordSet(); // clear list - prevents giga-dupe with
			// Gizmos temporal dislocator
		}
	}

	public void doRelease() {

		for (BlockRecord record : body) {
			SneakyWorldUtil.SetBlock(worldObj, record.X, record.Y, record.Z,
					record.block, record.Meta);
		}

		BlockRecordList pipesToInitialize = new BlockRecordList();

		if (ModInteraction.ForgeMultipart.MultipartSaveLoad_loadingWorld_$eq != null) {
			try {
				ModInteraction.ForgeMultipart.MultipartSaveLoad_loadingWorld_$eq
						.invoke(null, worldObj);
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		BlockRecordList multipartTilesToInitialize = new BlockRecordList();

		HashMap<Chunk, HashMap<Object, TileEntity>> MultipartTileSetsToPropagate = new HashMap<Chunk, HashMap<Object, TileEntity>>();

		for (BlockRecord record : body) {

			if (record.entityRecord != null) {
				record.entityRecord.setInteger("x", record.X);
				record.entityRecord.setInteger("y", record.Y);
				record.entityRecord.setInteger("z", record.Z);

				if (record.entityRecord.getString("id")
						.equals("savedMultipart")) {
					try {
						if (ModInteraction.ForgeMultipart.MultipartHelper_createTileFromNBT != null) {
							record.entity = (TileEntity) ModInteraction.ForgeMultipart.MultipartHelper_createTileFromNBT
									.invoke(null, worldObj, record.entityRecord);
						} else {
							record.entity = (TileEntity) ModInteraction.ForgeMultipart.TileMultipart_createFromNBT
									.invoke(null, record.entityRecord);

							MultipartContainerBlockId = record.block;

							Chunk Chunk = worldObj.getChunkFromBlockCoords(
									record.X, record.Z);

							HashMap<Object, TileEntity> MultipartTilesToPropagate = MultipartTileSetsToPropagate
									.get(Chunk);

							if (MultipartTilesToPropagate == null) {
								MultipartTilesToPropagate = new HashMap<Object, TileEntity>();

								MultipartTileSetsToPropagate.put(Chunk,
										MultipartTilesToPropagate);
							}

							MultipartTilesToPropagate.put(record.entity,
									record.entity);
						}

						multipartTilesToInitialize.add(record);
					} catch (Throwable Throwable) {
						Throwable.printStackTrace();

						continue;
					}
				} else {
					record.entity = TileEntity
							.createAndLoadEntity(record.entityRecord);
					Debug.dbg(record.entity + " @ " + record);
				}

				if (record.entity != null) {
					SneakyWorldUtil.SetTileEntity(worldObj, record.X, record.Y,
							record.Z, record.entity);
				}

			}

		}

		for (BlockRecord record : multipartTilesToInitialize) {
			try {
				ModInteraction.ForgeMultipart.TileMultipart_onChunkLoad
						.invoke(record.entity);
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		if (ModInteraction.ForgeMultipart.MultipartHelper_sendDescPacket != null) {
			for (BlockRecord record : multipartTilesToInitialize) {
				try {
					ModInteraction.ForgeMultipart.MultipartHelper_sendDescPacket
							.invoke(null, worldObj, record.entity);
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}
			}
		} else {
			for (Map.Entry<Chunk, HashMap<Object, TileEntity>> MultipartTilesToPropagate : MultipartTileSetsToPropagate
					.entrySet()) {
				Chunk Chunk = MultipartTilesToPropagate.getKey();

				Map SavedTileEntityMap = Chunk.chunkTileEntityMap;

				Chunk.chunkTileEntityMap = MultipartTilesToPropagate.getValue();

				try {
					for (EntityPlayerMP Player : ((List<EntityPlayerMP>)

					Reflection
							.get(Class
									.forName("net.minecraft.server.management.PlayerManager.PlayerInstance"),
									Reflection.runMethod(WorldServer.class,
											(((WorldServer) worldObj)
													.getPlayerManager()),
											"getOrCreateChunkWatcher",
											Chunk.xPosition, Chunk.zPosition,
											false), "playersWatchingChunk")))

					{
						if (!Player.loadedChunks.contains(Chunk
								.getChunkCoordIntPair())) {
							try {
								if (ModInteraction.ForgeMultipart.MultipartSaveLoad_loadingWorld_$eq == null) {
									MultipartPropagationPacket.Dispatch(Player,
											MultipartTilesToPropagate
													.getValue().values());
								}

								ModInteraction.ForgeMultipart.MultipartSPH_onChunkWatch
										.invoke(null, Player, Chunk);
							} catch (Throwable Throwable) {
								Throwable.printStackTrace();
							}
						}
					}
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}

				Chunk.chunkTileEntityMap = SavedTileEntityMap;
			}
		}

		for (BlockRecord record : pipesToInitialize) {
			if (ModInteraction.BCInstalled) {
				try {

					if (record.entity instanceof TileGenericPipe) {
						TileGenericPipe tile = (TileGenericPipe) record.entity;
						Pipe pipe = tile.pipe;
						if (!tile.initialized) {
							tile.initialize(pipe);
						}

						if (pipe.transport instanceof PipeTransportItems) {
							if (!((PipeTransportItems) pipe.transport).items.iterating) {
								for (TravelingItem item : ((PipeTransportItems) pipe.transport).items) {
									// to set up for correct displacement when teleporting
									offsetBuildcraftTravelingItem(item);
								}
							}
						}
					}/* else if (record.entity instanceof TileQuarry) {
						((TileQuarry) record.entity).invalidate();
						// box is protected, so we actually need reflection
						Field field = TileQuarry.class.getDeclaredField("box");
						field.setAccessible(true);
						Box box = (Box) field.get(((TileQuarry) record.entity));
						box.initialized = false;
						field.setAccessible(false);
						
						((TileQuarry) record.entity).initialize();
						
						((TileQuarry) record.entity).createUtilsIfNeeded();
					}*/
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}
			}
		}

		try {
			TileEntityCarriageDrive Drive = (TileEntityCarriageDrive) worldObj
					.getTileEntity(DriveRecord.X, DriveRecord.Y, DriveRecord.Z);

			if (!DriveIsAnchored) {
				Drive.Active = true;
			}

			Drive.ToggleActivity();
		} catch (Throwable Throwable) {
			// Throwable . printStackTrace ( ) ;
		}

		SneakyWorldUtil.RefreshBlock(worldObj, xCoord, yCoord, zCoord,
				RIMBlocks.Spectre, Blocks.air);

		for (BlockRecord Record : body) {
			SneakyWorldUtil.RefreshBlock(worldObj, Record.X, Record.Y,
					Record.Z, Blocks.air, Record.block);
		}

		int PendingBlockUpdateCount = PendingBlockUpdates.tagCount();

		for (int Index = 0; Index < PendingBlockUpdateCount; Index++) {
			ScheduleShiftedBlockUpdate(PendingBlockUpdates
					.getCompoundTagAt(Index));

		}
		for (BlockRecord Record : body) {
			onMotionFinalized(Record);
		}

		if (worldObj.getBlock(xCoord, yCoord, zCoord) == RIMBlocks.Spectre) {
			worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
		}

	}

	public void offsetBuildcraftTravelingItem(TravelingItem item) {
		item.xCoord += MotionDirection.DeltaX;
		item.yCoord += MotionDirection.DeltaY;
		item.zCoord += MotionDirection.DeltaZ;
	}

	public void onMotionFinalized(BlockRecord Record) {

	}

	@Override
	public void Finalize() {
		if (worldObj.isRemote) {
			CarriageRenderCache.Release(RenderCacheKey);
		}
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		TagCompound.setInteger("Motion", MotionDirection.ordinal());

		TagCompound.setInteger("RenderCacheKeyX", RenderCacheKey.X);
		TagCompound.setInteger("RenderCacheKeyY", RenderCacheKey.Y);
		TagCompound.setInteger("RenderCacheKeyZ", RenderCacheKey.Z);

		TagCompound.setInteger("RenderCacheKeyD", RenderCacheKey.Dimension);
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		MotionDirection = Directions.values()[TagCompound.getInteger("Motion")];

		RenderCacheKey = new BlockPosition(
				TagCompound.getInteger("RenderCacheKeyX"),
				TagCompound.getInteger("RenderCacheKeyY"),
				TagCompound.getInteger("RenderCacheKeyZ"),
				TagCompound.getInteger("RenderCacheKeyD"));
	}

	@Override
	public void WriteServerRecord(NBTTagCompound TagCompound) {
		TagCompound.setInteger("DriveX", DriveRecord.X);
		TagCompound.setInteger("DriveY", DriveRecord.Y);
		TagCompound.setInteger("DriveZ", DriveRecord.Z);

		TagCompound.setBoolean("DriveIsAnchored", DriveIsAnchored);

		TagCompound.setTag("PendingBlockUpdates", PendingBlockUpdates);

		{
			NBTTagList BodyRecord = new NBTTagList();

			for (BlockRecord Record : body) {
				NBTTagCompound BodyBlockRecord = new NBTTagCompound();

				BodyBlockRecord.setInteger("X", Record.X);
				BodyBlockRecord.setInteger("Y", Record.Y);
				BodyBlockRecord.setInteger("Z", Record.Z);

				BodyBlockRecord.setInteger("Id",
						Block.getIdFromBlock(Record.block));

				BodyBlockRecord.setInteger("Meta", Record.Meta);

				if (Record.entityRecord != null) {
					BodyBlockRecord.setTag("EntityRecord", Record.entityRecord);
				}

				BodyRecord.appendTag(BodyBlockRecord);

			}

			TagCompound.setTag("Body", BodyRecord);

		}
	}

	@Override
	public void ReadServerRecord(NBTTagCompound TagCompound) {
		DriveRecord = new BlockRecord(TagCompound.getInteger("DriveX"),
				TagCompound.getInteger("DriveY"),
				TagCompound.getInteger("DriveZ"));

		DriveIsAnchored = TagCompound.getBoolean("DriveIsAnchored");

		PendingBlockUpdates = TagCompound.getTagList("PendingBlockUpdates", 10);

		body = new BlockRecordSet();

		{
			NBTTagList BodyRecord = TagCompound.getTagList("Body", 10);

			int BodyBlockCount = BodyRecord.tagCount();

			for (int Index = 0; Index < BodyBlockCount; Index++) {
				NBTTagCompound BodyBlockRecord = BodyRecord
						.getCompoundTagAt(Index);

				BlockRecord Record = new BlockRecord(
						BodyBlockRecord.getInteger("X"),
						BodyBlockRecord.getInteger("Y"),
						BodyBlockRecord.getInteger("Z"));

				Record.block = Block.getBlockById(BodyBlockRecord
						.getInteger("Id"));

				Record.Meta = BodyBlockRecord.getInteger("Meta");

				if (BodyBlockRecord.hasKey("EntityRecord")) {
					Record.entityRecord = BodyBlockRecord
							.getCompoundTag("EntityRecord");
				}

				body.add(Record);
			}
		}
	}

	@Override
	public void WriteClientRecord(NBTTagCompound TagCompound) {
		NBTTagList CapturedEntityRecords = new NBTTagList();

		int i = 0;

		for (CapturedEntity Entity : CapturedEntities) {
			if (i++ == RiMConfiguration.Cosmetic.maxTags) { // not >= to allow
				// no
				// limit (eg.
				// singleplayer
				// only)
				break;
			}
			NBTTagCompound CapturedEntityRecord = new NBTTagCompound();

			CapturedEntityRecord.setInteger("Id", Entity.entity.getEntityId());

			CapturedEntityRecord.setDouble("InitialX", Entity.InitialX);
			CapturedEntityRecord.setDouble("InitialY", Entity.InitialY);
			CapturedEntityRecord.setDouble("InitialZ", Entity.InitialZ);

			CapturedEntityRecords.appendTag(CapturedEntityRecord);
		}

		if (RiMConfiguration.Debug.verbose) {
			Debug.dbg("Captured " + i + " tile entities.");
		}

		TagCompound.setTag("CapturedEntities", CapturedEntityRecords);
	}

	@Override
	public void ReadClientRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {
		net.minecraft.nbt.NBTTagList CapturedEntityRecords = TagCompound
				.getTagList("CapturedEntities", 10);

		CapturedEntities.clear();

		int CapturedEntityCount = CapturedEntityRecords.tagCount();

		for (int Index = 0; Index < CapturedEntityCount; Index++) {
			net.minecraft.nbt.NBTTagCompound EntityRecord = CapturedEntityRecords
					.getCompoundTagAt(Index);

			net.minecraft.entity.Entity Entity = worldObj
					.getEntityByID(EntityRecord.getInteger("Id"));

			if (Entity == null) {
				continue;
			}

			CapturedEntities.add(new CapturedEntity(Entity, EntityRecord
					.getDouble("InitialX"), EntityRecord.getDouble("InitialY"),
					EntityRecord.getDouble("InitialZ")));
		}
	}

	public class CapturedEntity {
		public Entity entity;

		public double InitialX;
		public double InitialY;
		public double InitialZ;

		boolean WasOnGround;

		boolean WasAirBorne;

		public CapturedEntity(Entity entity) {
			this(entity, entity.posX, entity.posY, entity.posZ);
		}

		public CapturedEntity(Entity entity, double InitialX, double InitialY,
				double InitialZ) {
			this.entity = entity;

			this.InitialX = InitialX;
			this.InitialY = InitialY;
			this.InitialZ = InitialZ;

			WasOnGround = entity.onGround;

			WasAirBorne = entity.isAirBorne;

			Update();
		}

		public void SetPosition(double OffsetX, double OffsetY, double OffsetZ) {
			entity.setPosition(InitialX + OffsetX, InitialY + OffsetY
					+ entity.yOffset, InitialZ + OffsetZ);
		}

		public void Update() {
			doPerSpectreUpdate(this, entity);
		}

		public void stop(Entity e) {
			entity.motionX = 0;
			entity.motionY = 0;
			entity.motionZ = 0;
			entity.prevPosX = entity.posX;
			entity.prevPosY = entity.posY;
			entity.prevPosZ = entity.posZ;
		}
	}

	public void doPerSpectreUpdate(CapturedEntity capture, Entity entity) {
		entity.fallDistance = 0;
		if (TicksExisted >= RiMConfiguration.CarriageMotion.MotionDuration) {
			capture.SetPosition(MotionDirection.DeltaX, MotionDirection.DeltaY,
					MotionDirection.DeltaZ);
			capture.stop(entity);
			entity.onGround = capture.WasOnGround;
			entity.isAirBorne = capture.WasAirBorne;
			return;
		}
		entity.onGround = false;
		entity.isAirBorne = true;
		entity.motionX = Velocity * MotionDirection.DeltaX;
		entity.motionY = Velocity * MotionDirection.DeltaY;
		entity.motionZ = Velocity * MotionDirection.DeltaZ;
		capture.SetPosition(entity.motionX * TicksExisted, entity.motionY
				* TicksExisted, entity.motionZ * TicksExisted);
		entity.prevPosX = entity.posX - entity.motionX;
		entity.prevPosY = entity.posY - entity.motionY;
		entity.prevPosZ = entity.posZ - entity.motionZ;
	}

	public java.util.ArrayList<CapturedEntity> CapturedEntities = new ArrayList<CapturedEntity>();

	public boolean ShouldCaptureEntity(Entity Entity) {
		if (Entity instanceof EntityPlayer) {
			return (RiMConfiguration.CarriageMotion.CapturePlayerEntities);
		}

		if (Entity instanceof EntityLiving) {
			return (RiMConfiguration.CarriageMotion.CaptureOtherLivingEntities);
		}

		if (Entity instanceof EntityItem) {
			return (RiMConfiguration.CarriageMotion.CaptureItemEntities);
		}

		return (RiMConfiguration.CarriageMotion.CaptureOtherEntities);
	}

	public void ProcessCapturedEntity(Entity Entity) {
		CapturedEntities.add(new CapturedEntity(Entity));
	}

	public void CaptureEntities(int MinX, int MinY, int MinZ, int MaxX,
			int MaxY, int MaxZ) {

		AxisAlignedBB EntityCaptureBox = AxisAlignedBB.getBoundingBox(MinX - 5,
				MinY - 5, MinZ - 5, MaxX + 5, MaxY + 5, MaxZ + 5);

		List EntitiesFound = worldObj.getEntitiesWithinAABB(Entity.class,
				EntityCaptureBox);

		for (Object EntityObject : EntitiesFound) {
			Entity entity = (Entity) EntityObject;

			BlockRecord PositionCheck = new BlockRecord(
					(int) Math.floor(entity.posX),
					(int) Math.floor(entity.posY),
					(int) Math.floor(entity.posZ));

			if (!body.contains(PositionCheck)) {
				PositionCheck.Y--;

				if (!body.contains(PositionCheck)) {
					PositionCheck.Y--;

					if (!body.contains(PositionCheck)) {
						entity = null;
					}
				}
			}

			if (entity == null) {
				continue;
			}

			if (ShouldCaptureEntity(entity)) {
				try {
					ProcessCapturedEntity(entity);
				} catch (Throwable Throwable) {
					Throwable.printStackTrace();
				}
			}
		}
	}

	public void Absorb(CarriagePackage Package) {
		MotionDirection = Package.MotionDirection;

		body = Package.Body;

		RenderCacheKey = Package.RenderCacheKey;

		PendingBlockUpdates = Package.PendingBlockUpdates;

		DriveRecord = new BlockRecord(Package.DriveRecord);

		if (!Package.DriveIsAnchored) {
			DriveRecord.Shift(Package.MotionDirection);
		}

		if (Package.MotionDirection != null) {
			CaptureEntities(Package.MinX, Package.MinY, Package.MinZ,
					Package.MaxX, Package.MaxY, Package.MaxZ);
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return (INFINITE_EXTENT_AABB);
	}

	@Override
	public boolean shouldRenderInPass(int Pass) {
		return (true);
	}

	@Override
	public boolean canUpdate() {
		return true;
	}

}
