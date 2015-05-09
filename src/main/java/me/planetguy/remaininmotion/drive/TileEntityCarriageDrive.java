package me.planetguy.remaininmotion.drive;

//import codechicken.chunkloader.TileChunkLoaderBase;
import cpw.mods.fml.common.Optional;
import me.planetguy.lib.util.Debug;
import me.planetguy.remaininmotion.motion.CarriageMatchers;
import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriageObstructionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.api.Moveable;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.base.BlockCamouflageable;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.base.TileEntityCamouflageable;
import me.planetguy.remaininmotion.core.ModRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.core.RiMConfiguration.CarriageMotion;
import me.planetguy.remaininmotion.drive.BlockCarriageDrive.Types;
import me.planetguy.remaininmotion.drive.gui.Buttons;
import me.planetguy.remaininmotion.network.PacketRenderData;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityMotiveSpectre;
import me.planetguy.remaininmotion.spectre.TileEntitySupportiveSpectre;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import me.planetguy.remaininmotion.util.position.AABBUtil;
import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.position.BlockRecordSet;
import me.planetguy.remaininmotion.util.transformations.ArrayRotator;
import me.planetguy.remaininmotion.util.transformations.Directions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import cofh.api.energy.IEnergyHandler;

import java.util.UUID;

@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")
public abstract class TileEntityCarriageDrive extends TileEntityCamouflageable implements IEnergyHandler {
    public boolean Continuous;

    public boolean[] SideClosed = new boolean[Directions.values().length];

    public boolean Signalled;

    public int CooldownRemaining;

    public boolean Active;

    public int Tier;

    public int energyStored = 0;

    public EntityPlayer lastUsingPlayer;

    public Directions CarriageDirection;

	protected Directions SignalDirection;
    protected double extraEnergy = 1;
    private int ticksExisted = 0;

    public boolean isCreative = false;
    
    public boolean requiresScrewdriverToOpen=false;

    @Override
    public void WriteCommonRecord(NBTTagCompound TagCompound) {
        TagCompound.setBoolean("Continuous", Continuous);

        for (Directions Direction : Directions.values()) {
            TagCompound.setBoolean("SideClosed" + Direction.ordinal(), SideClosed[Direction.ordinal()]);
        }

        TagCompound.setBoolean("Active", Active);

        TagCompound.setInteger("Tier", Tier);
        TagCompound.setInteger("energyStored", energyStored);
        
        TagCompound.setBoolean("screwdriver", requiresScrewdriverToOpen);
    }

    @Override
    public void WriteServerRecord(NBTTagCompound TagCompound) {
        TagCompound.setBoolean("Signalled", Signalled);

        TagCompound.setInteger("CooldownRemaining", CooldownRemaining);

        if(lastUsingPlayer != null) TagCompound.setString("player", lastUsingPlayer.getCommandSenderName());

        TagCompound.setBoolean("creative", isCreative);
    }

    @Override
    public void ReadCommonRecord(NBTTagCompound TagCompound) {
        Continuous = TagCompound.getBoolean("Continuous");

        for (Directions Direction : Directions.values()) {
            SideClosed[Direction.ordinal()] = TagCompound.getBoolean("SideClosed" + Direction.ordinal());
        }

        Active = TagCompound.getBoolean("Active");

        Tier = TagCompound.getInteger("Tier");

        energyStored = TagCompound.getInteger("energyStored");
        
        requiresScrewdriverToOpen=TagCompound.getBoolean("screwdriver");
    }

    @Override
    public void ReadServerRecord(NBTTagCompound TagCompound) {
        Signalled = TagCompound.getBoolean("Signalled");

        CooldownRemaining = TagCompound.getInteger("CooldownRemaining");

        try {
            if (TagCompound.hasKey("player"))
                lastUsingPlayer = worldObj.getPlayerEntityByName(TagCompound.getString("player"));
        } catch(Exception e) {}

        isCreative = TagCompound.getBoolean("creative");
    }

    @Override
    public void EmitDrops(BlockRiM Block, int Meta) {
        EmitDrop(Block, ItemCarriageDrive.Stack(Meta, Tier));
    }

    @Override
    public void Setup(EntityPlayer Player, ItemStack Item) {
        super.Setup(Player, Item);
        lastUsingPlayer = Player;
        Tier = ItemCarriageDrive.GetTier(Item);
        isCreative = Player != null ? Player.capabilities.isCreativeMode : false;
    }

    public void HandleToolUsage(int Side, boolean Sneaking) {
        if (Sneaking) {
            SideClosed[Side] = !SideClosed[Side];
        } else {
            Continuous = !Continuous;
        }

        Propagate();
    }

    public void ToggleActivity() {
        if (Active && Continuous) {
            CooldownRemaining = RiMConfiguration.CarriageDrive.ContinuousCooldown;
        }else if(Active){
        	Signalled=true;
        }
        Active = !Active;

        Propagate();
    }

    public boolean Stale = true;

    @Override
    public void Initialize() {
        Stale = true;
    }

    public void HandleNeighbourBlockChange() {
        Stale = false;

        CarriageDirection = null;

        boolean CarriageDirectionValid = true;

        setSignalDirection(null);

        boolean SignalDirectionValid = true;

        for (Directions Direction : Directions.values()) {
            int X = xCoord + Direction.deltaX;
            int Y = yCoord + Direction.deltaY;
            int Z = zCoord + Direction.deltaZ;

            if (worldObj.isAirBlock(X, Y, Z)) {
                continue;
            }

            if (isSideClosed(Direction.ordinal())) {
                continue;
            }

            net.minecraft.block.Block Id = worldObj.getBlock(X, Y, Z);
            net.minecraft.tileentity.TileEntity te = worldObj.getTileEntity(X, Y, Z);

            Moveable m = CarriageMatchers.getMover(Id, worldObj.getBlockMetadata(X, Y, Z), te);

            if (m != null) {
                if (CarriageDirection != null) {
                    CarriageDirectionValid = false;
                } else {
                    CarriageDirection = Direction;
                }
            }
            if (Id.isProvidingWeakPower(worldObj, X, Y, Z, Direction.ordinal()) > 0) {
                if (getSignalDirection() != null) {
                    SignalDirectionValid = false;
                } else {
                    setSignalDirection(Direction);
                }
            }
        }

        if (!CarriageDirectionValid) {
            CarriageDirection = null;
        }

        if (!SignalDirectionValid) {
            setSignalDirection(null);
        }
    }

    @Override
    public void updateEntity() {
        worldObj.theProfiler.startSection("RiMCarriageDrive");
        if (worldObj.isRemote) {
            worldObj.theProfiler.endSection();
            return;
        }

        if(Active) {
            if (ticksExisted > 0
                    && ticksExisted < CarriageMotion.MotionDuration
                    && ticksExisted % 20 == 0) {
                if (Anchored()) {
                    ModRiM.plHelper.playSound(worldObj, xCoord, yCoord, zCoord,
                            CarriageMotion.SoundFile, CarriageMotion.volume, 1f);
                }
            }
            ticksExisted++;
            if (ticksExisted >= CarriageMotion.MotionDuration) ticksExisted = 0;
        } else {
            if(ticksExisted != 0) ticksExisted = 0;
        }

        if (Stale) {
            HandleNeighbourBlockChange();
        }

        if (CooldownRemaining > 0) {
            CooldownRemaining--;

            MarkServerRecordDirty();

            worldObj.theProfiler.endSection();
            return;
        }

        if (Active) {

            worldObj.theProfiler.endSection();
            return;
        }

		if (SignalDirection == null) {
			if (Signalled) {
				Signalled = false;

                MarkServerRecordDirty();
            }

            worldObj.theProfiler.endSection();
            return;
        }

        if (CarriageDirection == null) {
            worldObj.theProfiler.endSection();
            return;
        }

        if (Signalled) {
            if (!Continuous) {
                worldObj.theProfiler.endSection();
                return;
            }
        } else {
            Signalled = true;

            MarkServerRecordDirty();
        }
        worldObj.theProfiler.startSection("InitiateMotion");
        try {
            InitiateMotion(PreparePackage(getSignalDirection().opposite()));

            ModRiM.plHelper.playSound(worldObj, xCoord, yCoord, zCoord, CarriageMotion.SoundFile, CarriageMotion.volume, 1f);
        } catch (CarriageMotionException Exception) {

            String Message = "Drive at (" + xCoord + "," + yCoord + "," + zCoord + ") in dimension "
                    + worldObj.provider.dimensionId + " failed to move carriage: " + Exception.getMessage();

            if (Exception instanceof CarriageObstructionException) {
                CarriageObstructionException ObstructionException = (CarriageObstructionException) Exception;

                Message += " - (" + ObstructionException.X + "," + ObstructionException.Y + ","
                        + ObstructionException.Z + ")";
            }

            if (RiMConfiguration.Debug.LogMotionExceptions) {
                Debug.dbg(Message);
            }

            if (lastUsingPlayer != null) {
                lastUsingPlayer.addChatComponentMessage(new ChatComponentText(Message));
            }
        }
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.endSection();
        
    }

    public CarriagePackage PreparePackage(Directions dir) throws CarriageMotionException {
        return prepareDefaultPackage(dir);
    }

    /**
     * @return 0 -> did not fail, 1 -> fragile, 2 -> liquid, 3 -> block
     */
    public static int targetBlockReplaceable(TileEntity translocator, BlockRecord record) {
        return targetBlockReplaceableNoTranslate(translocator, new BlockRecord(record.X + translocator.xCoord,
                record.Y + translocator.yCoord, record.Z + translocator.zCoord));
    }

    /**
     * @return 1 -> fragile, 2 -> liquid, 3 -> block
     */
    public static int targetBlockReplaceableNoTranslate(TileEntity translocator, BlockRecord record) {
        if (translocator.getWorldObj().isAirBlock(record.X, record.Y, record.Z)) {
            return 0;
        }

        Block block = translocator.getWorldObj().getBlock(record.X, record.Y, record.Z);
        if (block != null) {
            if (!CarriagePackage.ObstructedByLiquids && (FluidRegistry.lookupFluidForBlock(block) != null)) {
                return 0;
            } else if (CarriagePackage.ObstructedByLiquids && (FluidRegistry.lookupFluidForBlock(block) != null)) {
                return 2;
            }
            if (!CarriagePackage.ObstructedByFragileBlocks && block.getMaterial().isReplaceable()) {
                return 0;
            } else if (CarriagePackage.ObstructedByFragileBlocks && block.getMaterial().isReplaceable()) {
                return 1;
            }
            return 3;
        }
        return 0;
    }

    public CarriagePackage prepareDefaultPackage(Directions MotionDirection) throws CarriageMotionException {

        Moveable mv = CarriageMatchers.getMover(
                worldObj.getBlock(xCoord + CarriageDirection.deltaX, yCoord + CarriageDirection.deltaY, zCoord
                        + CarriageDirection.deltaZ),
                worldObj.getBlockMetadata(xCoord + CarriageDirection.deltaX, yCoord + CarriageDirection.deltaY, zCoord
                        + CarriageDirection.deltaZ),
                worldObj.getTileEntity(xCoord + CarriageDirection.deltaX, yCoord + CarriageDirection.deltaY, zCoord
                        + CarriageDirection.deltaZ));

        CarriagePackage _package = GeneratePackage(
                worldObj.getTileEntity(xCoord + CarriageDirection.deltaX, yCoord + CarriageDirection.deltaY, zCoord
                        + CarriageDirection.deltaZ), CarriageDirection, MotionDirection);

        return (_package);
    }

    public void removeUsedEnergy(CarriagePackage _package) throws CarriageMotionException {

        if (RiMConfiguration.HardMode.HardmodeActive && ! isCreative) {
            int Type = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);

            double EnergyRequired = _package.getMass() * BlockCarriageDrive.Types.values()[Type].EnergyConsumption * extraEnergy;

            int powerConsumed = (int) Math.ceil(EnergyRequired * RiMConfiguration.HardMode.PowerConsumptionFactor);

            // System.out.println("Moving carriage from "+Package.AnchorRecord.toString()+" containing "+Package.Mass+" blocks, using "+powerConsumed+" energy");

            if (powerConsumed > energyStored) {
                throw (new CarriageMotionException("(HARDMODE) not enough power to move carriage (have " + energyStored
                        + ", need " + powerConsumed));
            } else {
                energyStored -= powerConsumed;
            }

        }
    }

    public BlockPosition GeneratePositionObject() {
        return (new BlockPosition(xCoord, yCoord, zCoord, worldObj.provider.dimensionId));
    }

    public void InitiateMotion(CarriagePackage Package) {

        ToggleActivity();

        Package.RenderCacheKey = GeneratePositionObject();

        worldObj.theProfiler.startSection("SendRenderPacket");
        PacketRenderData.send(Package);

        worldObj.theProfiler.endStartSection("PreMovementModInteraction");
        doPreMovementModInteraction(Package);

        worldObj.theProfiler.endStartSection("Placeholders");
        EstablishPlaceholders(Package);

        worldObj.theProfiler.endStartSection("UpdateBlocks");
        RefreshWorld(Package);

        worldObj.theProfiler.endStartSection("MotiveSpecter");
        EstablishSpectre(Package);
        worldObj.theProfiler.endSection();

    }

    public void doPreMovementModInteraction(CarriagePackage carriagePackage)
    {
        for(BlockRecord record : carriagePackage.Body)
        {
        	RiMRegistry.blockMoveBus.post(new BlockPreMoveEvent(
                    record,
                    carriagePackage.MotionDirection != null
                            ? record.NextInDirection(carriagePackage.MotionDirection)
                            : record));
        }

    }

    public void EstablishPlaceholders(CarriagePackage Package) {
        BlockRecordSet temp = new BlockRecordSet();

        byte[] lightValues = new byte[Package.Body.size()];
        byte[] lightOpacities = new byte[Package.Body.size()];

        int i = 0;
        if(Package.MotionDirection != null) {
            for (BlockRecord Record : Package.Body) {

                try {
                    lightValues[i] = (byte) Record.block.getLightValue(worldObj, Record.X, Record.Y, Record.Z);
                    lightOpacities[i] = (byte) Record.block.getLightOpacity(worldObj, Record.X, Record.Y, Record.Z);
                } catch (Exception e) {
                    lightValues[i] = (byte) Record.block.getLightValue();
                    lightOpacities[i] = (byte) Record.block.getLightOpacity();
                }

                BlockRecord temp2 = Record.NextInDirection(Package.MotionDirection);
                temp2.block = Record.block;
                temp.add(temp2);
                i++;
            }
        }else {
            temp = Package.Body;
        }

        i = 0;

        for (BlockRecord Record : temp) {
            if(Package.MotionDirection != null) {
                //NBTTagCompound nbt = new NBTTagCompound();
                // Specters get in our way on elevators
                if(Package.MotionDirection.deltaY == 0) {
                    Block block = Record.block;
                    //Block block1 = worldObj.getBlock(Record.X, Record.Y, Record.Z);
                    //Block block2 = worldObj.getBlock(Record.X + Package.MotionDirection.deltaX, Record.Y, Record.Z + Package.MotionDirection.deltaZ);
                    if(block.isOpaqueCube()) {
                        SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                BlockSpectre.Types.Supportive.ordinal());
                    } else if(block.getCollisionBoundingBoxFromPool(worldObj, Record.X - Package.MotionDirection.deltaX, Record.Y, Record.Z - Package.MotionDirection.deltaZ) == null) {
                        SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                BlockSpectre.Types.SupportiveNoCollide.ordinal());
                    } else {
                        //AABBUtil.writeCollisionBoundingBoxesToNBT(worldObj, Record.X, Record.Y, Record.Z, nbt);
                        SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                BlockSpectre.Types.Supportive.ordinal());
                    }
                } else {
                    // we do want things like walls to get in our way, though
                    if(!Package.Body.contains(Record)) {
                        //if(worldObj.getBlock(Record.X, Record.Y, Record.Z).isBlockNormalCube() || worldObj.getBlock(Record.X, Record.Y, Record.Z).getCollisionBoundingBoxFromPool(worldObj, Record.X, Record.Y, Record.Z) == null) {
                            SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                    BlockSpectre.Types.SupportiveNoCollide.ordinal());
                        //} else {
                        //    AABBUtil.writeCollisionBoundingBoxesToNBT(worldObj, Record.X, Record.Y, Record.Z, nbt);
                        //    SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                        //            BlockSpectre.Types.SupportiveNoCollide.ordinal());
                        //}
                    } else {
                        //AABBUtil.writeCollisionBoundingBoxesToNBT(worldObj, Record.X, Record.Y, Record.Z, nbt);
                        SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                BlockSpectre.Types.Supportive.ordinal());
                    }
                }

                // only set Light if we're moving
                if (Package.MotionDirection.ordinal() != ForgeDirection.UNKNOWN.ordinal()) {
                    worldObj.setTileEntity(Record.X, Record.Y, Record.Z, new TileEntitySupportiveSpectre());
                    // handle camo blocks
                    TileEntitySupportiveSpectre tile = ((TileEntitySupportiveSpectre) worldObj.getTileEntity(Record.X, Record.Y, Record.Z));

                    tile.setLight(lightValues[i], lightOpacities[i]);

                    //tile.setBoundingBox(nbt);
                }
            }
            i++;
        }

        for(BlockRecord Record : Package.Body)
        {
            if(temp.contains(Record)) continue;
            if(Package.MotionDirection != null) {
                // Specters get in our way on elevators
                if(Package.MotionDirection.deltaY == 0) {
                    if(Record.block.getCollisionBoundingBoxFromPool(worldObj, Record.X,Record.Y,Record.Z) == null) {
                        SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                BlockSpectre.Types.SupportiveNoCollide.ordinal());
                    } else {
                        SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                                BlockSpectre.Types.Supportive.ordinal());
                    }
                } else {
                    SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.Spectre,
                            BlockSpectre.Types.SupportiveNoCollide.ordinal());
                }
                if (Package.MotionDirection.ordinal() != ForgeDirection.UNKNOWN.ordinal()) {
                    worldObj.setTileEntity(Record.X, Record.Y, Record.Z, new TileEntitySupportiveSpectre());
                    ((TileEntitySupportiveSpectre) worldObj.getTileEntity(Record.X, Record.Y, Record.Z)).setLight((byte)0,(byte)0);
                }
                Package.spectersToDestroy.add(new BlockRecord(Record));
            }

            // SneakyWorldUtil.SetBlock(worldObj, Record.X, Record.Y, Record.Z, RIMBlocks.air, 0);
        }
    }


	public void RefreshWorld(CarriagePackage Package) {
		for (BlockRecord Record : Package.Body) {
			SneakyWorldUtil.RefreshBlock(worldObj, Record.X, Record.Y, Record.Z, Record.block, Blocks.air);
		}
	}

    public void EstablishSpectre(CarriagePackage Package) {
        int CarriageX = Package.AnchorRecord.X + Package.MotionDirection.deltaX;
        int CarriageY = Package.AnchorRecord.Y + Package.MotionDirection.deltaY;
        int CarriageZ = Package.AnchorRecord.Z + Package.MotionDirection.deltaZ;

        WorldUtil.SetBlock(worldObj, CarriageX, CarriageY, CarriageZ, RIMBlocks.Spectre,
                BlockSpectre.Types.Motive.ordinal());

        worldObj.setTileEntity(CarriageX, CarriageY, CarriageZ, new TileEntityMotiveSpectre());

        ((TileEntityMotiveSpectre) worldObj.getTileEntity(CarriageX, CarriageY, CarriageZ)).Absorb(Package);
    }

    public abstract CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection,
                                                    Directions MotionDirection) throws CarriageMotionException;

    public abstract boolean Anchored();

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        int toRecieve = Math.min(RiMConfiguration.HardMode.powerCapacity - energyStored, maxReceive);
        if (!simulate) {
            energyStored += toRecieve;
        }
        return toRecieve;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        if(isCreative) return false;
        return RiMConfiguration.HardMode.HardmodeActive;
    }

    @Override
    public int getEnergyStored(ForgeDirection from) {
        return energyStored;
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from) {
        return RiMConfiguration.HardMode.powerCapacity;
    }

    public IIcon getIcon(int Side, int meta) {
        try {
            if (SideClosed[Side]) {
                if (getDecoration() != null) {
                    return getDecoration().getIcon(Side, DecorationMeta);
                } else {
                    return (BlockCarriageDrive.InactiveIcon);
                }
            }

            Types Type = Types.values()[meta];

            if (Continuous) {
                return (Active ? Type.ContinuousActiveIcon : Type.ContinuousIcon);
            }

            return (Active ? Type.NormalActiveIcon : Type.NormalIcon);
        } catch (Throwable Throwable) {
            // Throwable . printStackTrace ( ) ;

            return (Blocks.iron_block.getIcon(0, 0));
        }
    }

    public boolean isSideClosed(int side) {
        return SideClosed[side];
    }

    public void setSideClosed(int side, boolean closed) {
        SideClosed[side] = closed;
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    @Override
    public void rotateSpecial(ForgeDirection axis) {
        ArrayRotator.rotate(SideClosed, axis);
        Propagate();
    }

    public Directions getSignalDirection() {
        return SignalDirection;
    }

    public void setSignalDirection(Directions signalDirection) {
        SignalDirection = signalDirection;
    }

    public boolean onRightClicked(int side, EntityPlayer player) {
        return false;
    }
    
    public void setConfiguration(long flags, EntityPlayerMP changer){
    	flags=flags >> 3; //save space for heading - it's special-cased.
    	SideClosed[0]=(flags & 1<<Buttons.DOWN.ordinal()) == 1;
    	SideClosed[1]=(flags & 1<<Buttons.UP.ordinal()) == 1;
    	SideClosed[2]=(flags & 1<<Buttons.NORTH.ordinal()) == 1;
    	SideClosed[3]=(flags & 1<<Buttons.SOUTH.ordinal()) == 1;
    	SideClosed[4]=(flags & 1<<Buttons.WEST.ordinal()) == 1;
    	SideClosed[5]=(flags & 1<<Buttons.EAST.ordinal()) == 1;
    	requiresScrewdriverToOpen=(flags & 1<<Buttons.SCREWDRIVER_MODE.ordinal()) == 1;
    	Continuous=(flags & 1<<Buttons.CONTINUOUS_MODE.ordinal()) == 1;
    	
    	//flush changes to clients and persistence
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }
    
}
