package me.planetguy.remaininmotion.spectre;

import buildcraft.core.TileBuildCraft;
import buildcraft.factory.TileQuarry;
import buildcraft.transport.Pipe;
import buildcraft.transport.PipeTransportItems;
import buildcraft.transport.TileGenericPipe;
import buildcraft.transport.TravelingItem;
import codechicken.chunkloader.ChunkLoaderManager;
import codechicken.chunkloader.TileChunkLoaderBase;
import codechicken.multipart.MultipartHelper;
import codechicken.multipart.TileMultipart;
import codechicken.multipart.handler.MultipartSaveLoad;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.*;
import me.planetguy.remaininmotion.api.IMotionCallback;
import me.planetguy.remaininmotion.base.BlockCamouflageable;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.RiMConfiguration.CarriageMotion;
import me.planetguy.remaininmotion.core.interop.DummyChickenChunkLoader;
import me.planetguy.remaininmotion.core.interop.ModInteraction;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.render.CarriageRenderCache;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMotiveSpectre extends TileEntityRiM {
    public static double Velocity;
    public static Block MultipartContainerBlockId;
    public Directions MotionDirection = Directions.Null;
    public BlockPosition RenderCacheKey;
    public NBTTagList PendingBlockUpdates;
    public BlockRecord DriveRecord;
    public boolean DriveIsAnchored;
    public BlockRecordSet body;
    public int TicksExisted;
    public java.util.ArrayList<CapturedEntity> CapturedEntities = new ArrayList<CapturedEntity>();
    TeleportativeSpectreTeleporter Teleporter;
    private boolean initialized;

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

        if (ModInteraction.MPInstalled) MultipartSaveLoad.loadingWorld_$eq(worldObj);

        for (BlockRecord record : body) {

            int[] offset = getOffset();

            if (record.entityRecord != null) {
                record.entityRecord.setInteger("x", record.X);
                record.entityRecord.setInteger("y", record.Y);
                record.entityRecord.setInteger("z", record.Z);

                if (ModInteraction.MPInstalled
                        && record.entityRecord.getString("id").equals(
                        "savedMultipart")) {
                    try {
                        record.entity = MultipartHelper.createTileFromNBT(worldObj, record.entityRecord);
                        if (record.entity != null) {
                            SneakyWorldUtil.SetTileEntity(worldObj, record.X, record.Y,
                                    record.Z, record.entity);
                        }
                        ((TileMultipart) record.entity).onChunkLoad();
                        MultipartHelper.sendDescPacket(worldObj, record.entity);
                    } catch (Throwable Throwable) {
                        Throwable.printStackTrace();
                        continue;
                    }
                } else {
                    if (ModInteraction.BCInstalled)
                        performBuildcraftPreInit(record, offset);

                    record.entity = TileEntity
                            .createAndLoadEntity(record.entityRecord);

                    if (record.entity != null) {
                        SneakyWorldUtil.SetTileEntity(worldObj, record.X, record.Y,
                                record.Z, record.entity);
                    }
                    if (ModInteraction.BCInstalled)
                        performBuildcraftPostInit(record);

                    if (ModInteraction.ChickenChunksInstalled)
                        performChickenChunksPostInit(record);
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

        for (BlockRecord record : body) {
            if (ModInteraction.fmpProxy.isMultipart(record.entity))
                ModInteraction.fmpProxy.loadMultipartTick(record.entity, record.entityRecord);
            onMotionFinalized(record);
            record.block.onBlockAdded(worldObj,record.X,record.Y,record.Z);
        }

        for(BlockRecord record:body) {
            if(record.entity instanceof IMotionCallback) {
                ((IMotionCallback) record.entity).onPlacedFromMotion();
            }
        }

        cleanupSpecter();

    }

    public void cleanupSpecter()
    {
        if (worldObj.getBlock(xCoord, yCoord, zCoord) == RIMBlocks.Spectre) {
            worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
        }
    }

    public int[] getOffset() {
        return new int[]{MotionDirection.DeltaX, MotionDirection.DeltaY,
                MotionDirection.DeltaZ};
    }

    public int[] getOffset(BlockRecord record) {
        return getOffset();
    }

    private void performBuildcraftPreInit(BlockRecord record, int[] offset) {
        // Screw PostInit access, I'll just modify the blocks at save level.
        // This will work for anything with a box (filler, quarry, architect table)
        if (record.entityRecord.hasKey("box")) {
            NBTTagCompound boxNBT = record.entityRecord.getCompoundTag("box");
            if (!boxNBT.hasNoTags()) {
                int xMax = boxNBT.getInteger("xMax");
                int xMin = boxNBT.getInteger("xMin");
                int yMax = boxNBT.getInteger("yMax");
                int yMin = boxNBT.getInteger("yMin");
                int zMax = boxNBT.getInteger("zMax");
                int zMin = boxNBT.getInteger("zMin");

                boxNBT.setInteger("xMax", xMax += offset[0]);
                boxNBT.setInteger("xMin", xMin += offset[0]);
                boxNBT.setInteger("yMax", yMax += offset[1]);
                boxNBT.setInteger("yMin", yMin += offset[1]);
                boxNBT.setInteger("zMax", zMax += offset[2]);
                boxNBT.setInteger("zMin", zMin += offset[2]);

            }
        }

        // reset states on filler and mining well (and anything based off it)
        if(record.entityRecord.hasKey("done")) {
            record.entityRecord.setBoolean("done", false);
        }
        if(record.entityRecord.hasKey("digging")) {
            record.entityRecord.setBoolean("digging", true);
        }
        // reset quarry
        if (record.entityRecord.getString("id").equals("Machine")) {
            record.entityRecord.setInteger("targetX", 0);
            record.entityRecord.setInteger("targetY", 0);
            record.entityRecord.setInteger("targetZ", 0);

            record.entityRecord.setDouble("headPosX", 0.0D);
            record.entityRecord.setDouble("headPosY", 0.0D);
            record.entityRecord.setDouble("headPosZ", 0.0D);
        }
    }

    private void performBuildcraftPostInit(BlockRecord record) {
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
                            // to set up for correct displacement when
                            // teleporting
                            int[] offset = getOffset(record);
                            item.xCoord += offset[0];
                            item.yCoord += offset[1];
                            item.zCoord += offset[2];
                        }
                    }
                }
            } else if (record.entity instanceof TileBuildCraft) {

                record.entity.invalidate();
                ((TileBuildCraft)record.entity).initialize();
                if(record.entity instanceof TileQuarry) {
                    ((TileQuarry) record.entity).createUtilsIfNeeded();
                }

            }
        } catch (Throwable Throwable) {
            //Throwable.printStackTrace();
        }
    }

    private void performChickenChunksPostInit(BlockRecord record){
        try{
            if(record.entity instanceof TileChunkLoaderBase){
                if(record.entityRecord.hasKey("ChickenChunkLoader")){
                    if(!worldObj.isRemote) {
                        ((TileChunkLoaderBase) record.entity).activate();
                        // remove chunk loader afterwards
                        NBTTagCompound tag = record.entityRecord.getCompoundTag("ChickenChunkLoader");
                        DummyChickenChunkLoader loader = new DummyChickenChunkLoader(tag);
                        ChunkLoaderManager.remChunkLoader(loader);
                        // make sure those chunks are loaded
                        ((TileChunkLoaderBase) record.entity).activate();
                    }
                }
            }
        }catch(Throwable t) {}
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
    public void writeToNBT(NBTTagCompound TagCompound) {
        super.writeToNBT(TagCompound);

        // Don't need to send this whole thing over network all the time
        TagCompound.setTag("PendingBlockUpdates", PendingBlockUpdates);

        NBTTagList BodyRecord = new NBTTagList();

        for (BlockRecord Record : body) {
            NBTTagCompound BodyBlockRecord = new NBTTagCompound();

            Record.writeToNBT(BodyBlockRecord);

            BodyRecord.appendTag(BodyBlockRecord);

        }

        TagCompound.setTag("Body", BodyRecord);

    }

    @Override
    public void readFromNBT(NBTTagCompound TagCompound) {
        super.readFromNBT(TagCompound);

        // Don't need to send this whole thing over network all the time
        PendingBlockUpdates = TagCompound.getTagList("PendingBlockUpdates", 10);

        body = new BlockRecordSet();

        NBTTagList BodyRecord = TagCompound.getTagList("Body", 10);

        int BodyBlockCount = BodyRecord.tagCount();

        for (int Index = 0; Index < BodyBlockCount; Index++) {
            NBTTagCompound BodyBlockRecord = BodyRecord.getCompoundTagAt(Index);

            BlockRecord Record = BlockRecord.createFromNBT(BodyBlockRecord);
            body.add(Record);
        }

    }

    @Override
    public void WriteServerRecord(NBTTagCompound TagCompound) {
        if(DriveRecord != null) {
            NBTTagCompound tag = new NBTTagCompound();
            DriveRecord.writeToNBT(tag);
            TagCompound.setTag("DriveRecord", tag);
        }

        TagCompound.setBoolean("DriveIsAnchored", DriveIsAnchored);

        TagCompound.setInteger("TicksExisted",TicksExisted);
    }

    @Override
    public void ReadServerRecord(NBTTagCompound TagCompound) {
        if(TagCompound.hasKey("DriveX")) {
            DriveRecord = new BlockRecord(TagCompound.getInteger("DriveX"),
                    TagCompound.getInteger("DriveY"),
                    TagCompound.getInteger("DriveZ"));
        }

        if(TagCompound.hasKey("DriveRecord")) {
            DriveRecord = BlockRecord.createFromNBT(TagCompound.getCompoundTag("DriveRecord"));
        }

        DriveIsAnchored = TagCompound.getBoolean("DriveIsAnchored");

        TicksExisted = TagCompound.getInteger("TicksExisted");
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

        DriveRecord = new BlockRecord(Package.driveRecord);

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

    public int getLightValue()
    {
        Block b = null;
        NBTTagCompound nbt = null;
        if(body == null) return RIMBlocks.Spectre.getLightValue();
        for(BlockRecord temp : body){
            if(temp.X == xCoord && temp.Y == yCoord && temp.Z == zCoord)
            {
                b = temp.block;
                nbt = temp.entityRecord;
            }
        }
        if(b instanceof  BlockCamouflageable) {
            if(nbt != null) {
                Block b2 = Block.getBlockById(nbt.getInteger("DecorationId"));
                if(b2 != null){
                    return b2.getLightValue();
                }
            }
        }
        if(b != null) return b.getLightValue();
        return RIMBlocks.Spectre.getLightValue();
    }

    public int getLightOpacity()
    {
        Block b = null;
        NBTTagCompound nbt = null;
        if(body == null) return RIMBlocks.Spectre.getLightOpacity();
        for(BlockRecord temp : body){
            if(temp.X == xCoord && temp.Y == yCoord && temp.Z == zCoord)
            {
                b = temp.block;
                nbt = temp.entityRecord;
                break;
            }
        }
        if(b instanceof  BlockCamouflageable) {
            if(nbt != null) {
                Block b2 = Block.getBlockById(nbt.getInteger("DecorationId"));
                if(b2 != null){
                    return b2.getLightOpacity();
                }
            }
        }
        if(b != null) return b.getLightOpacity();
        return RIMBlocks.Spectre.getLightOpacity();
    }
}
