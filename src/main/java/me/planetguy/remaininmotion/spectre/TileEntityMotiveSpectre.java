package me.planetguy.remaininmotion.spectre;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.planetguy.lib.util.Debug;
import me.planetguy.lib.util.Reflection;
import me.planetguy.remaininmotion.BlockPosition;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordList;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.ModInteraction;
import me.planetguy.remaininmotion.core.RIMBlocks;
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
	public Directions				MotionDirection	= Directions.Null;

	public BlockPosition			RenderCacheKey;

	public NBTTagList				PendingBlockUpdates;

	public BlockRecord				DriveRecord;

	public boolean					DriveIsAnchored;

	public BlockRecordSet			Body;

	public int						TicksExisted;

	public static double			Velocity;

	TeleportativeSpectreTeleporter	Teleporter;

	public void ShiftBlockPosition(BlockRecord Record) {
		Record.Shift(MotionDirection);
	}

	@Override
	public void validate() {
		if (worldObj instanceof WorldServer) {
			Teleporter = new TeleportativeSpectreTeleporter(worldObj);
		}
	}

	public void ScheduleShiftedBlockUpdate(NBTTagCompound PendingBlockUpdateRecord) {
		worldObj.func_147446_b // scheduleBlockUpdateFromLoad
		(PendingBlockUpdateRecord.getInteger("X") + MotionDirection.DeltaX, PendingBlockUpdateRecord.getInteger("Y")
				+ MotionDirection.DeltaY, PendingBlockUpdateRecord.getInteger("Z") + MotionDirection.DeltaZ,

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

		if (worldObj.isRemote) { return; }

		if (TicksExisted > 0 && TicksExisted < RiMConfiguration.CarriageMotion.MotionDuration && TicksExisted % 20 == 0) {
			if(bodyHasCarriageDrive())
			{
				ModRiM.plHelper.playSound(worldObj, xCoord, yCoord, zCoord, CarriageMotion.SoundFile, 0.8f, 1f);
			}
		}

		if (TicksExisted < RiMConfiguration.CarriageMotion.MotionDuration) { return; }

		Release();
	}
	
	private boolean bodyHasCarriageDrive()
	{
		if(Body == null || Body.isEmpty()) return false;
		for(BlockRecord body : Body)
		{
			if(body.block instanceof BlockCarriageDrive) return true;
			if(body.Entity != null && body.Entity instanceof TileEntityCarriageDrive) return true;
		}
		return false;
	}

	public static Block	MultipartContainerBlockId;

	public void Release() {
		for (BlockRecord Record : Body) {
			ShiftBlockPosition(Record);
		}
		if (!Body.isEmpty()) {
			doRelease();
			Body = new BlockRecordSet(); // clear list - prevents giga-dupe with
			// Gizmos temporal dislocator
		}
	}

	public void doRelease() {

		for (BlockRecord Record : Body) {
			SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, Record.block, Record.Meta);
		}

		BlockRecordList PipesToInitialize = new BlockRecordList();

		if (ModInteraction.ForgeMultipart.MultipartSaveLoad_loadingWorld_$eq != null) {
			try {
				ModInteraction.ForgeMultipart.MultipartSaveLoad_loadingWorld_$eq.invoke(null, worldObj);
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		BlockRecordList MultipartTilesToInitialize = new BlockRecordList();

		HashMap<Chunk, HashMap<Object, TileEntity>> MultipartTileSetsToPropagate = new HashMap<Chunk, HashMap<Object, TileEntity>>();

		for (BlockRecord Record : Body) {

			if (Record.EntityRecord != null) {
				Record.EntityRecord.setInteger("x", Record.X);
				Record.EntityRecord.setInteger("y", Record.Y);
				Record.EntityRecord.setInteger("z", Record.Z);

				if (Record.EntityRecord.getString("id").equals("savedMultipart")) {
					try {
						if (ModInteraction.ForgeMultipart.MultipartHelper_createTileFromNBT != null) {
							Record.Entity = (TileEntity) ModInteraction.ForgeMultipart.MultipartHelper_createTileFromNBT
									.invoke(null, worldObj, Record.EntityRecord);
						} else {
							Record.Entity = (TileEntity) ModInteraction.ForgeMultipart.TileMultipart_createFromNBT
									.invoke(null, Record.EntityRecord);

							MultipartContainerBlockId = Record.block;

							Chunk Chunk = worldObj.getChunkFromBlockCoords(Record.X, Record.Z);

							HashMap<Object, TileEntity> MultipartTilesToPropagate = MultipartTileSetsToPropagate
									.get(Chunk);

							if (MultipartTilesToPropagate == null) {
								MultipartTilesToPropagate = new HashMap<Object, TileEntity>();

								MultipartTileSetsToPropagate.put(Chunk, MultipartTilesToPropagate);
							}

							MultipartTilesToPropagate.put(Record.Entity, Record.Entity);
						}

						MultipartTilesToInitialize.add(Record);
					} catch (Throwable Throwable) {
						Throwable.printStackTrace();

						continue;
					}
				} else {
					Record.Entity = TileEntity.createAndLoadEntity(Record.EntityRecord);
					Debug.dbg(Record.Entity + " @ "+Record);
				}

				if(Record.Entity != null)
					SneakyWorldUtil.SetTileEntity(worldObj, Record.X, Record.Y, Record.Z, Record.Entity);

			}

		}

		for (BlockRecord Record : MultipartTilesToInitialize) {
			try {
				ModInteraction.ForgeMultipart.TileMultipart_onChunkLoad.invoke(Record.Entity);
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		if (ModInteraction.ForgeMultipart.MultipartHelper_sendDescPacket != null) {
			for (BlockRecord Record : MultipartTilesToInitialize) {
				try {
					ModInteraction.ForgeMultipart.MultipartHelper_sendDescPacket.invoke(null, worldObj, Record.Entity);
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

					Reflection.get(Class.forName("net.minecraft.server.management.PlayerManager.PlayerInstance"),
							Reflection.runMethod(WorldServer.class, (((WorldServer) worldObj).getPlayerManager()),
									"getOrCreateChunkWatcher", Chunk.xPosition, Chunk.zPosition, false),
							"playersWatchingChunk")))

					{
						if (!Player.loadedChunks.contains(Chunk.getChunkCoordIntPair())) {
							try {
								if (ModInteraction.ForgeMultipart.MultipartSaveLoad_loadingWorld_$eq == null) {
									MultipartPropagationPacket.Dispatch(Player, MultipartTilesToPropagate.getValue()
											.values());
								}

								ModInteraction.ForgeMultipart.MultipartSPH_onChunkWatch.invoke(null, Player, Chunk);
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

		for (BlockRecord Record : PipesToInitialize) {
			try {
				Object Pipe = ModInteraction.BC_TileGenericPipe_pipe.get(Record.Entity);

				ModInteraction.BC_TileGenericPipe_initialize.invoke(Record.Entity, Pipe);

				Object Transport = ModInteraction.BC_Pipe_transport.get(Pipe);

				if (!ModInteraction.BC_PipeTransportItems.isInstance(Transport)) {
					continue;
				}

				ModInteraction.BC_PipeTransportItems_delay.set(Transport, -1);

				List DelayedEntities = (List) ModInteraction.BC_PipeTransportItems_delayedEntitiesToLoad.get(Transport);

				Map EntityMap = (Map) ModInteraction.BC_PipeTransportItems_travelingEntities.get(Transport);

				for (Object Entity : DelayedEntities) {
					Object Item = ModInteraction.BC_EntityData_item.get(Entity);

					ModInteraction.BC_EntityPassiveItem_setWorld.invoke(Item, worldObj);

					int Id = (Integer) ModInteraction.BC_EntityPassiveItem_getEntityId.invoke(Item);

					EntityMap.put(Id, Entity);

					Object ItemPosition = ModInteraction.BC_EntityPassiveItem_position.get(Item);

				}

				DelayedEntities.clear();
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		try {
			TileEntityCarriageDrive Drive = (TileEntityCarriageDrive) worldObj.getTileEntity(DriveRecord.X,
					DriveRecord.Y, DriveRecord.Z);

			if (!DriveIsAnchored) {
				Drive.Active = true;
			}

			Drive.ToggleActivity();
		} catch (Throwable Throwable) {
			// Throwable . printStackTrace ( ) ;
		}

		SneakyWorldUtil.RefreshBlock(worldObj, xCoord, yCoord, zCoord, RIMBlocks.Spectre, Blocks.air);

		for (BlockRecord Record : Body) {
			SneakyWorldUtil.RefreshBlock(worldObj, Record.X, Record.Y, Record.Z, Blocks.air, Record.block);
		}

		int PendingBlockUpdateCount = PendingBlockUpdates.tagCount();

		for (int Index = 0; Index < PendingBlockUpdateCount; Index++) {
			ScheduleShiftedBlockUpdate(PendingBlockUpdates.getCompoundTagAt(Index));

		}
		for (BlockRecord Record : Body) {
			onMotionFinalized(Record);
		}

		if (worldObj.getBlock(xCoord, yCoord, zCoord) == RIMBlocks.Spectre) {
			worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
		}

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

		RenderCacheKey = new BlockPosition(TagCompound.getInteger("RenderCacheKeyX"),
				TagCompound.getInteger("RenderCacheKeyY"), TagCompound.getInteger("RenderCacheKeyZ"),
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

			for (BlockRecord Record : Body) {
				NBTTagCompound BodyBlockRecord = new NBTTagCompound();

				BodyBlockRecord.setInteger("X", Record.X);
				BodyBlockRecord.setInteger("Y", Record.Y);
				BodyBlockRecord.setInteger("Z", Record.Z);

				BodyBlockRecord.setInteger("Id", Block.getIdFromBlock(Record.block));

				BodyBlockRecord.setInteger("Meta", Record.Meta);

				if (Record.EntityRecord != null) {
					BodyBlockRecord.setTag("EntityRecord", Record.EntityRecord);
				}

				BodyRecord.appendTag(BodyBlockRecord);

			}

			TagCompound.setTag("Body", BodyRecord);

		}
	}

	@Override
	public void ReadServerRecord(NBTTagCompound TagCompound) {
		DriveRecord = new BlockRecord(TagCompound.getInteger("DriveX"), TagCompound.getInteger("DriveY"),
				TagCompound.getInteger("DriveZ"));

		DriveIsAnchored = TagCompound.getBoolean("DriveIsAnchored");

		PendingBlockUpdates = TagCompound.getTagList("PendingBlockUpdates", 10);

		Body = new BlockRecordSet();

		{
			NBTTagList BodyRecord = TagCompound.getTagList("Body", 10);

			int BodyBlockCount = BodyRecord.tagCount();

			for (int Index = 0; Index < BodyBlockCount; Index++) {
				NBTTagCompound BodyBlockRecord = BodyRecord.getCompoundTagAt(Index);

				BlockRecord Record = new BlockRecord(BodyBlockRecord.getInteger("X"), BodyBlockRecord.getInteger("Y"),
						BodyBlockRecord.getInteger("Z"));

				Record.block = Block.getBlockById(BodyBlockRecord.getInteger("Id"));

				Record.Meta = BodyBlockRecord.getInteger("Meta");

				if (BodyBlockRecord.hasKey("EntityRecord")) {
					Record.EntityRecord = BodyBlockRecord.getCompoundTag("EntityRecord");
				}

				Body.add(Record);
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

		if(RiMConfiguration.Debug.verbose) Debug.dbg("Captured " + i + " tile entities.");

		TagCompound.setTag("CapturedEntities", CapturedEntityRecords);
	}

	@Override
	public void ReadClientRecord(net.minecraft.nbt.NBTTagCompound TagCompound) {
		net.minecraft.nbt.NBTTagList CapturedEntityRecords = TagCompound.getTagList("CapturedEntities", 10);

		CapturedEntities.clear();

		int CapturedEntityCount = CapturedEntityRecords.tagCount();

		for (int Index = 0; Index < CapturedEntityCount; Index++) {
			net.minecraft.nbt.NBTTagCompound EntityRecord = CapturedEntityRecords.getCompoundTagAt(Index);

			net.minecraft.entity.Entity Entity = worldObj.getEntityByID(EntityRecord.getInteger("Id"));

			if (Entity == null) {
				continue;
			}

			CapturedEntities.add(new CapturedEntity(Entity, EntityRecord.getDouble("InitialX"), EntityRecord
					.getDouble("InitialY"), EntityRecord.getDouble("InitialZ")));
		}
	}

	public class CapturedEntity {
		public Entity	entity;

		public double	InitialX;
		public double	InitialY;
		public double	InitialZ;

		boolean			WasOnGround;

		boolean			WasAirBorne;

		public CapturedEntity(Entity entity) {
			this(entity, entity.posX, entity.posY, entity.posZ);
		}

		public CapturedEntity(Entity entity, double InitialX, double InitialY, double InitialZ) {
			this.entity = entity;

			this.InitialX = InitialX;
			this.InitialY = InitialY;
			this.InitialZ = InitialZ;

			WasOnGround = entity.onGround;

			WasAirBorne = entity.isAirBorne;

			Update();
		}

		public void SetPosition(double OffsetX, double OffsetY, double OffsetZ) {
			entity.setPosition(InitialX + OffsetX, InitialY + OffsetY + entity.yOffset, InitialZ + OffsetZ);
		}

		public void Update() {
			entity.fallDistance = 0;
			if (TicksExisted >= RiMConfiguration.CarriageMotion.MotionDuration) {
				entity.motionX = 0;
				entity.motionY = 0;
				entity.motionZ = 0;
				SetPosition(MotionDirection.DeltaX, MotionDirection.DeltaY, MotionDirection.DeltaZ);
				entity.prevPosX = entity.posX;
				entity.prevPosY = entity.posY;
				entity.prevPosZ = entity.posZ;
				entity.onGround = WasOnGround;
				entity.isAirBorne = WasAirBorne;
				return;
			}
			entity.onGround = false;
			entity.isAirBorne = true;
			entity.motionX = Velocity * MotionDirection.DeltaX;
			entity.motionY = Velocity * MotionDirection.DeltaY;
			entity.motionZ = Velocity * MotionDirection.DeltaZ;
			SetPosition(entity.motionX * TicksExisted, entity.motionY * TicksExisted, entity.motionZ * TicksExisted);
			doSpecialMotion(entity);
			entity.prevPosX = entity.posX - entity.motionX;
			entity.prevPosY = entity.posY - entity.motionY;
			entity.prevPosZ = entity.posZ - entity.motionZ;
		}
	}

	public void doSpecialMotion(Entity e) {

	}

	public java.util.ArrayList<CapturedEntity>	CapturedEntities	= new ArrayList<CapturedEntity>();

	public boolean ShouldCaptureEntity(Entity Entity) {
		if (Entity instanceof EntityPlayer) { return (RiMConfiguration.CarriageMotion.CapturePlayerEntities); }

		if (Entity instanceof EntityLiving) { return (RiMConfiguration.CarriageMotion.CaptureOtherLivingEntities); }

		if (Entity instanceof EntityItem) { return (RiMConfiguration.CarriageMotion.CaptureItemEntities); }

		return (RiMConfiguration.CarriageMotion.CaptureOtherEntities);
	}

	public void ProcessCapturedEntity(Entity Entity) {
		CapturedEntities.add(new CapturedEntity(Entity));
	}

	public void CaptureEntities(int MinX, int MinY, int MinZ, int MaxX, int MaxY, int MaxZ) {

		AxisAlignedBB EntityCaptureBox = AxisAlignedBB.getBoundingBox(MinX - 5, MinY - 5, MinZ - 5, MaxX + 5, MaxY + 5,
				MaxZ + 5);

		List EntitiesFound = worldObj.getEntitiesWithinAABB(Entity.class, EntityCaptureBox);

		for (Object EntityObject : EntitiesFound) {
			Entity entity = (Entity) EntityObject;

			BlockRecord PositionCheck = new BlockRecord((int) Math.floor(entity.posX), (int) Math.floor(entity.posY),
					(int) Math.floor(entity.posZ));

			if (!Body.contains(PositionCheck)) {
				PositionCheck.Y--;

				if (!Body.contains(PositionCheck)) {
					PositionCheck.Y--;

					if (!Body.contains(PositionCheck)) {
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

		Body = Package.Body;

		RenderCacheKey = Package.RenderCacheKey;

		PendingBlockUpdates = Package.PendingBlockUpdates;

		DriveRecord = new BlockRecord(Package.DriveRecord);

		if (!Package.DriveIsAnchored) {
			DriveRecord.Shift(Package.MotionDirection);
		}

		if (Package.MotionDirection != null) {
			CaptureEntities(Package.MinX, Package.MinY, Package.MinZ, Package.MaxX, Package.MaxY, Package.MaxZ);
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
