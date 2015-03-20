package me.planetguy.remaininmotion.spectre;

import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.*;
import me.planetguy.remaininmotion.base.BlockCamouflageable;
import me.planetguy.remaininmotion.base.TileEntityRiM;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.RiMConfiguration.CarriageMotion;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive;
import me.planetguy.remaininmotion.drive.TileEntityCarriageDrive;
import me.planetguy.remaininmotion.render.CarriageRenderCache;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.util.transformations.Directions;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class TileEntityMotiveSpectre extends TileEntityRiM {
    public static double velocity;
    public static Block MultipartContainerBlockId;
    public Directions motionDirection = Directions.Null;
    public BlockPosition renderCacheKey;
    public NBTTagList pendingBlockUpdates;
    public BlockRecord driveRecord;
    public boolean driveIsAnchored;
    public BlockRecordSet body;
    public BlockRecordSet spectersToDestroy;
    public int ticksExisted = 0;



    public java.util.ArrayList<CapturedEntity> CapturedEntities = new ArrayList<CapturedEntity>();
    private boolean initialized;

    public void ShiftBlockPosition(BlockRecord Record) {
        Record.Shift(motionDirection);
    }

    public void ScheduleShiftedBlockUpdate(
            NBTTagCompound PendingBlockUpdateRecord) {
        worldObj.func_147446_b // scheduleBlockUpdateFromLoad
                (PendingBlockUpdateRecord.getInteger("X") + motionDirection.deltaX,
                        PendingBlockUpdateRecord.getInteger("Y")
                                + motionDirection.deltaY,
                        PendingBlockUpdateRecord.getInteger("Z")
                                + motionDirection.deltaZ,

                        Block.getBlockById(PendingBlockUpdateRecord.getInteger("Id")),

                        PendingBlockUpdateRecord.getInteger("Delay"),

                        PendingBlockUpdateRecord.getInteger("Priority"));
    }

    @Override
    public void updateEntity() {
        ticksExisted++;

        for (CapturedEntity Entity : CapturedEntities) {
            Entity.Update();
        }

        if (worldObj.isRemote) {
            return;
        }

        if (ticksExisted > 0
                && ticksExisted < RiMConfiguration.CarriageMotion.MotionDuration
                && ticksExisted % 20 == 0) {
            if (bodyHasCarriageDrive()) {
                ModRiM.plHelper.playSound(worldObj, xCoord, yCoord, zCoord,
                        CarriageMotion.SoundFile, CarriageMotion.volume, 1f);
            }
        }

        if (ticksExisted < RiMConfiguration.CarriageMotion.MotionDuration) {
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

        if (body != null && !body.isEmpty()) {
            for (BlockRecord record : body) {
                ShiftBlockPosition(record);
            }
            doRelease();
            body = new BlockRecordSet(); // clear list - prevents giga-dupe with
            // Gizmos temporal dislocator
            if(!spectersToDestroy.isEmpty())
                spectersToDestroy = new BlockRecordSet();
        }
    }
    
    public void doRelease() {

        if(!spectersToDestroy.isEmpty()){
            for (BlockRecord Record : spectersToDestroy) {
                SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y,
                        Record.Z, Blocks.air, 0);
            }
        }

        for (BlockRecord record : body) {
            SneakyWorldUtil.SetBlock(worldObj, record.X, record.Y, record.Z,
                    record.block, record.Meta);
        }
        
        RiMRegistry.blockMoveBus.post(new BlocksReplacedEvent(this));

        for (BlockRecord record : body) {

            int[] offset = getOffset();
            
            if (record.entityRecord != null) {
                constructTE(record);

            }

        }


        try {
            TileEntityCarriageDrive Drive = (TileEntityCarriageDrive) worldObj
                    .getTileEntity(driveRecord.X, driveRecord.Y, driveRecord.Z);

            if (!driveIsAnchored) {
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

        if(!spectersToDestroy.isEmpty()){
            for (BlockRecord Record : spectersToDestroy) {
                SneakyWorldUtil.RefreshBlock(worldObj, Record.X, Record.Y,
                        Record.Z, Blocks.air, Blocks.air);
            }
        }

        int PendingBlockUpdateCount = pendingBlockUpdates.tagCount();

        for (int Index = 0; Index < PendingBlockUpdateCount; Index++) {
            ScheduleShiftedBlockUpdate(pendingBlockUpdates
                    .getCompoundTagAt(Index));

        }

        for (BlockRecord record : body) {
            onMotionFinalized(record);
            record.block.onBlockAdded(worldObj,record.X,record.Y,record.Z);
            RiMRegistry.blockMoveBus.post(new MotionFinalizeEvent(record));
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
        return new int[]{motionDirection.deltaX, motionDirection.deltaY,
                motionDirection.deltaZ};
    }

    public int[] getOffset(BlockRecord record) {
        return getOffset();
    }
    
    public void constructTE(BlockRecord record) {
    	record.entityRecord.setInteger("x", record.X);
        record.entityRecord.setInteger("y", record.Y);
        record.entityRecord.setInteger("z", record.Z);

        RiMRegistry.blockMoveBus.post(new TEPreUnpackEvent(this, record));

        record.entity = TileEntity
        		.createAndLoadEntity(record.entityRecord);

        RiMRegistry.blockMoveBus.post(new TEPrePlaceEvent(this, record));

        if (record.entity != null) {
        	SneakyWorldUtil.SetTileEntity(worldObj, record.X, record.Y,
        			record.Z, record.entity);
        }

        RiMRegistry.blockMoveBus.post(new TEPostPlaceEvent(this, record));
    }

    public void onMotionFinalized(BlockRecord Record) {

    }
    
    @Override
    public void Finalize() {
        if (worldObj.isRemote) {
            CarriageRenderCache.Release(renderCacheKey);
        }
    }
    
    @Override
    public void WriteCommonRecord(NBTTagCompound TagCompound) {
        TagCompound.setInteger("motion", motionDirection.ordinal());

        TagCompound.setInteger("RenderCacheKeyX", renderCacheKey.X);
        TagCompound.setInteger("RenderCacheKeyY", renderCacheKey.Y);
        TagCompound.setInteger("RenderCacheKeyZ", renderCacheKey.Z);

        TagCompound.setInteger("RenderCacheKeyD", renderCacheKey.Dimension);
    }

    @Override
    public void ReadCommonRecord(NBTTagCompound TagCompound) {
        motionDirection = Directions.values()[TagCompound.getInteger("motion")];

        renderCacheKey = new BlockPosition(
                TagCompound.getInteger("RenderCacheKeyX"),
                TagCompound.getInteger("RenderCacheKeyY"),
                TagCompound.getInteger("RenderCacheKeyZ"),
                TagCompound.getInteger("RenderCacheKeyD"));
    }

    @Override
    public void WriteServerRecord(NBTTagCompound TagCompound) {
        if(driveRecord != null) {
            NBTTagCompound tag = new NBTTagCompound();
            driveRecord.writeToNBT(tag);
            TagCompound.setTag("driveRecord", tag);
        }

        TagCompound.setBoolean("driveIsAnchored", driveIsAnchored);

        TagCompound.setInteger("ticksExisted", ticksExisted);

        // Don't need to send this whole thing over network all the time
        TagCompound.setTag("pendingBlockUpdates", pendingBlockUpdates);

        NBTTagList BodyRecord = new NBTTagList();

        if(body != null && !body.isEmpty()) {
            for (BlockRecord Record : body) {
                NBTTagCompound BodyBlockRecord = new NBTTagCompound();

                Record.writeToNBT(BodyBlockRecord);

                BodyRecord.appendTag(BodyBlockRecord);

            }
        }
        TagCompound.setTag("Body", BodyRecord);

        BodyRecord = new NBTTagList();

        if(spectersToDestroy != null && !spectersToDestroy.isEmpty()) {
            for (BlockRecord Record : spectersToDestroy) {
                NBTTagCompound BodyBlockRecord = new NBTTagCompound();

                Record.writeToNBT(BodyBlockRecord);

                BodyRecord.appendTag(BodyBlockRecord);

            }

            TagCompound.setTag("spectersToDestroy", BodyRecord);
        }
    }

    @Override
    public void ReadServerRecord(NBTTagCompound TagCompound) {
        if(TagCompound.hasKey("DriveX")) {
            driveRecord = new BlockRecord(TagCompound.getInteger("DriveX"),
                    TagCompound.getInteger("DriveY"),
                    TagCompound.getInteger("DriveZ"));
        }

        if(TagCompound.hasKey("driveRecord")) {
            driveRecord = BlockRecord.createFromNBT(TagCompound.getCompoundTag("driveRecord"));
        }

        driveIsAnchored = TagCompound.getBoolean("driveIsAnchored");

        ticksExisted = TagCompound.getInteger("ticksExisted");

        // Don't need to send this whole thing over network all the time
        pendingBlockUpdates = TagCompound.getTagList("pendingBlockUpdates", 10);

        body = new BlockRecordSet();

        NBTTagList BodyRecord = TagCompound.getTagList("Body", 10);

        int BodyBlockCount = BodyRecord.tagCount();

        for (int Index = 0; Index < BodyBlockCount; Index++) {
            NBTTagCompound BodyBlockRecord = BodyRecord.getCompoundTagAt(Index);

            BlockRecord Record = BlockRecord.createFromNBT(BodyBlockRecord);
            body.add(Record);
        }

        spectersToDestroy = new BlockRecordSet();

        if(TagCompound.hasKey("spectersToDestroy")) {
            BodyRecord = TagCompound.getTagList("spectersToDestroy", 10);

            BodyBlockCount = BodyRecord.tagCount();

            for (int Index = 0; Index < BodyBlockCount; Index++) {
                NBTTagCompound BodyBlockRecord = BodyRecord.getCompoundTagAt(Index);

                BlockRecord Record = BlockRecord.createFromNBT(BodyBlockRecord);
                spectersToDestroy.add(Record);
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

            CapturedEntityRecord.setDouble("netMotionX", Entity.netMotionX);
            CapturedEntityRecord.setDouble("netMotionY", Entity.netMotionY);
            CapturedEntityRecord.setDouble("netMotionZ", Entity.netMotionZ);

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
                    EntityRecord.getDouble("InitialZ"), EntityRecord.getDouble("netMotionX"), EntityRecord.getDouble("netMotionY"), EntityRecord.getDouble("netMotionZ")));
        }
    }

    public void fixLagError(CapturedEntity capture, Entity entity) {
        double motionX = 0;
        double motionY = 0;
        double motionZ = 0;

        if(motionDirection.deltaX != 0 && !isTolerable(capture.netMotionX, (double) motionDirection.deltaX, 0.02D)){
            motionX = (double) motionDirection.deltaX - (capture.netMotionX);
        }
        if(motionDirection.deltaY != 0 && !isTolerable(capture.netMotionY, (double) motionDirection.deltaY, 0.02D)){
            motionY = (double) motionDirection.deltaY - capture.netMotionY;
        }
        if(motionDirection.deltaZ != 0 && !isTolerable(capture.netMotionZ, (double) motionDirection.deltaZ, 0.02D)){
            motionZ = (double) motionDirection.deltaZ - (capture.netMotionZ);
        }
        if(motionDirection.deltaY != 0){
            entity.onGround = capture.WasOnGround;
            entity.isAirBorne = capture.WasAirBorne;
            capture.SetPosition(motionX, 0, motionZ);
            // try to fix double precision errors
            // we only do positive as it's okay to be 0.025 up and fall
            capture.SetYPosition(capture.netMotionY + 0.025);
        } else {
            capture.SetPosition(motionX, motionY, motionZ);
        }

    }

    public void doPerSpectreUpdate(CapturedEntity capture, Entity entity) {
        entity.fallDistance = 0;

        if(capture.netMotionX == motionDirection.deltaX && capture.netMotionY == motionDirection.deltaY
                && capture.netMotionZ == motionDirection.deltaZ) {
            return;
        }

        double motionX = velocity * (double) motionDirection.deltaX;
        double motionY = velocity * (double) motionDirection.deltaY;
        double motionZ = velocity * (double) motionDirection.deltaZ;

        capture.netMotionX += motionX;
        capture.netMotionY += motionY;
        capture.netMotionZ += motionZ;

        if(motionDirection.deltaY != 0){
            entity.onGround = capture.WasOnGround;
            entity.isAirBorne = capture.WasAirBorne;
            capture.SetPosition(motionX, 0, motionZ);
            capture.SetYPosition(capture.netMotionY);
            //entity.motionY = 0;
        } else {
            capture.SetPosition(motionX, motionY, motionZ);
        }

        if (ticksExisted >= RiMConfiguration.CarriageMotion.MotionDuration) {
            fixLagError(capture, entity);
            //capture.stop();
            return;
        }
    }

    private boolean isTolerable(double value1, double value2, double tolerance) {
        return Math.abs(value1 - value2) <= tolerance;
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
        motionDirection = Package.MotionDirection;

        body = Package.Body;

        spectersToDestroy = Package.spectersToDestroy;

        renderCacheKey = Package.RenderCacheKey;

        pendingBlockUpdates = Package.PendingBlockUpdates;

        driveRecord = new BlockRecord(Package.driveRecord);

        if (!Package.DriveIsAnchored) {
            driveRecord.Shift(Package.MotionDirection);
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

    public class CapturedEntity {
        public Entity entity;

        public double InitialX;
        public double InitialY;
        public double InitialZ;

        public double netMotionX = 0;
        public double netMotionY = 0;
        public double netMotionZ = 0;

        boolean WasOnGround;

        boolean WasAirBorne;

        public CapturedEntity(Entity entity) {
            this(entity, entity.posX, entity.posY, entity.posZ, 0, 0, 0);
        }

        public CapturedEntity(Entity entity, double InitialX, double InitialY,
                              double InitialZ, double netMotionX, double netMotionY, double netMotionZ) {
            this.entity = entity;

            this.InitialX = InitialX;
            this.InitialY = InitialY;
            this.InitialZ = InitialZ;

            this.netMotionX = netMotionX;
            this.netMotionY = netMotionY;
            this.netMotionZ = netMotionZ;

            WasOnGround = entity.onGround;

            WasAirBorne = entity.isAirBorne;

            Update();
        }

        public void SetPosition(double OffsetX, double OffsetY, double OffsetZ) {
            entity.setPosition(entity.posX + OffsetX, entity.posY + OffsetY, entity.posZ + OffsetZ);
            //entity.moveEntity(OffsetX, OffsetY, OffsetZ);
        }

        public void SetYPosition(double OffsetY) {
            entity.setPosition(entity.posX, InitialY + entity.yOffset + OffsetY, entity.posZ);
        }

        public void Update() {
            doPerSpectreUpdate(this, entity);
        }

        public void stop() {
            entity.lastTickPosX = entity.prevPosX = entity.posX;
            entity.lastTickPosY = entity.prevPosY = entity.posY;
            entity.lastTickPosZ = entity.prevPosZ = entity.posZ;
        }
    }


}
