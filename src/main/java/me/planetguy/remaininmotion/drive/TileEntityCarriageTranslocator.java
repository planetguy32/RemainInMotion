package me.planetguy.remaininmotion.drive;

import java.util.HashMap;
import java.util.LinkedList;

import me.planetguy.remaininmotion.motion.CarriageMotionException;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.position.BlockPosition;
import me.planetguy.remaininmotion.util.position.BlockRecord;
import me.planetguy.remaininmotion.util.transformations.Directions;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.base.BlockRiM;
import me.planetguy.remaininmotion.core.RIMBlocks;
import me.planetguy.remaininmotion.core.RiMConfiguration;
import me.planetguy.remaininmotion.drive.gui.Buttons;
import me.planetguy.remaininmotion.spectre.BlockSpectre;
import me.planetguy.remaininmotion.spectre.TileEntityTeleportativeSpectre;
import me.planetguy.remaininmotion.util.MultiTypeCarriageUtil;
import me.planetguy.remaininmotion.util.SneakyWorldUtil;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCarriageTranslocator extends TileEntityCarriageDrive {
	
	static {
		RiMRegistry.registerEventHandler(new TranslocatorMoveListener());
	}
	
	public String																Player;

	public int																	Label;
	
	static {
		
	}

	public static HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>	ActiveTranslocatorSets	= new HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>();

	public HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>> getRegistry(){
		return ActiveTranslocatorSets;
	}
	
	protected void registerLabel() {
		HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>	ActiveTranslocatorSets=getRegistry();

		HashMap<Integer, LinkedList<BlockPosition>> ActiveTranslocatorSet = ActiveTranslocatorSets.get(Player);
		
		if (ActiveTranslocatorSet == null) {
			ActiveTranslocatorSet = new HashMap<Integer, LinkedList<BlockPosition>>();

			ActiveTranslocatorSets.put(Player, ActiveTranslocatorSet);
		}

		LinkedList<BlockPosition> ActiveTranslocators = ActiveTranslocatorSet.get(Label);

		if (ActiveTranslocators == null) {
			ActiveTranslocators = new LinkedList<BlockPosition>();

			ActiveTranslocatorSet.put(Label, ActiveTranslocators);
		}

		ActiveTranslocators.add(GeneratePositionObject());
	}

	public void unregisterLabel() {
		HashMap<String, HashMap<Integer, LinkedList<BlockPosition>>>	ActiveTranslocatorSets=getRegistry();
		try {
			ActiveTranslocatorSets.get(Player).get(Label).remove(GeneratePositionObject());
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();
		}
	}

	@Override
	public void Setup(EntityPlayer Player, ItemStack Item) {
		super.Setup(Player, Item);

		this.Player = ItemCarriageDrive.GetPrivateFlag(Item) ? Player.getDisplayName() : "";

		Label = ItemCarriageDrive.GetLabel(Item);

		if (!worldObj.isRemote) {

			registerLabel();

			/* dirty hack needed for unknown reason */
			{
				unregisterLabel();

				registerLabel();
			}
		}
	}

	@Override
	public void EmitDrops(BlockRiM Block, int Meta) {
		EmitDrop(Block, ItemCarriageDrive.Stack(Meta, Tier, !Player.equals(""), Label));
	}

	@Override
	public void Initialize() {
		super.Initialize();

		if (!worldObj.isRemote) {
			if (Player != null) {
				registerLabel();
			}
		}
	}

	@Override
	public void Finalize() {
		if (!worldObj.isRemote) {
			unregisterLabel();
		}
	}

	@Override
	public void ReadCommonRecord(NBTTagCompound TagCompound) {
		super.ReadCommonRecord(TagCompound);

		Player = TagCompound.getString("Player");

		Label = TagCompound.getInteger("Label");
	}

	@Override
	public void WriteCommonRecord(NBTTagCompound TagCompound) {
		super.WriteCommonRecord(TagCompound);

		TagCompound.setString("Player", Player);

		TagCompound.setInteger("Label", Label);
	}

	@Override
	public boolean Anchored() {
		return (true);
	}

	@Override
	public CarriagePackage PreparePackage(Directions MotionDirection) throws CarriageMotionException {
		CarriagePackage Package = super.PreparePackage(null);

		TileEntityCarriageTranslocator target = null;

		java.util.LinkedList<BlockPosition> activeTranslocators;

		try {
			activeTranslocators = getRegistry().get(Player).get(Label);
		} catch (Throwable Throwable) {
			Throwable.printStackTrace();

			throw (new CarriageMotionException("translocator array is corrupt"));
		}

		for (int index = 0; index < activeTranslocators.size(); index++) {
			BlockPosition position = activeTranslocators.get(index);

			try {
				TileEntityCarriageTranslocator translocator = (TileEntityCarriageTranslocator) WorldUtil.GetWorld(
						position.Dimension).getTileEntity(position.X, position.Y, position.Z);

				if (translocator == this) {
					continue;
				}

				boolean targetValid = true;

				for (BlockRecord record : Package.NewPositions) {
					if (targetBlockReplaceable(translocator, record) != 0) {
						targetValid = false;

						break;
					}
				}

				if (targetValid) {
					target = translocator;

					break;
				}
			} catch (Throwable Throwable) {
				Throwable.printStackTrace();
			}
		}

		if (target == null) { throw (new CarriageMotionException(
				"no other matching translocators available with space to receive carriage assembly")); }

		Package.Translocator = target;
        if(RiMConfiguration.HardMode.distanceAffectsEnergy) {
            double distance = Math.sqrt((target.xCoord - xCoord) * (target.xCoord - xCoord) + (target.yCoord - yCoord) * (target.yCoord - yCoord) + (target.zCoord - zCoord) * (target.zCoord - zCoord));
            distance = Math.min(distance, RiMConfiguration.HardMode.peakDistance) / RiMConfiguration.HardMode.peakDistance * RiMConfiguration.HardMode.maxDistanceMultiplier;
            if (target.worldObj.getWorldInfo().getVanillaDimension() != worldObj.getWorldInfo().getVanillaDimension()) {
                distance *= RiMConfiguration.HardMode.otherDimensionMultiplier;
            }

            extraEnergy = Math.max(distance, 1.0D);
        }

        Package.Finalize();

		return (Package);
	}

	@Override
	public CarriagePackage GeneratePackage(TileEntity carriage, Directions CarriageDirection, Directions MotionDirection)
			throws CarriageMotionException {
		CarriagePackage Package = new CarriagePackage(this, carriage, null);

		MultiTypeCarriageUtil.fillPackage(Package, carriage);

		if (Package.Body.contains(Package.driveRecord)) { throw (new CarriageMotionException(
				"carriage is attempting to grab translocator")); }

		return (Package);
	}

	@Override
	public void InitiateMotion(CarriagePackage Package) {
		Package.Translocator.ToggleActivity();

		super.InitiateMotion(Package);
	}

	@Override
	public void EstablishPlaceholders(CarriagePackage Package) {
		for (BlockRecord Record : Package.NewPositions) {
			SneakyWorldUtil.SetBlock(worldObj, Record.X + xCoord, Record.Y + yCoord, Record.Z + zCoord,
					RIMBlocks.Spectre, BlockSpectre.Types.Supportive.ordinal());

			SneakyWorldUtil.SetBlock(Package.Translocator.worldObj, Record.X + Package.Translocator.xCoord, Record.Y
					+ Package.Translocator.yCoord, Record.Z + Package.Translocator.zCoord, RIMBlocks.Spectre,
					BlockSpectre.Types.Supportive.ordinal());
		}
	}

	@Override
	public void EstablishSpectre(CarriagePackage Package) {
		WorldUtil.SetBlock(worldObj, Package.AnchorRecord.X, Package.AnchorRecord.Y, Package.AnchorRecord.Z,
				RIMBlocks.Spectre, BlockSpectre.Types.Teleportative.ordinal());

		((TileEntityTeleportativeSpectre) worldObj.getTileEntity(Package.AnchorRecord.X, Package.AnchorRecord.Y,
				Package.AnchorRecord.Z)).AbsorbSource(Package);

		int NewX = Package.AnchorRecord.X - xCoord + Package.Translocator.xCoord;
		int NewY = Package.AnchorRecord.Y - yCoord + Package.Translocator.yCoord;
		int NewZ = Package.AnchorRecord.Z - zCoord + Package.Translocator.zCoord;

		WorldUtil.SetBlock(Package.Translocator.worldObj, NewX, NewY, NewZ, RIMBlocks.Spectre,
				BlockSpectre.Types.Teleportative.ordinal());

		((TileEntityTeleportativeSpectre) Package.Translocator.worldObj.getTileEntity(NewX, NewY, NewZ))
				.AbsorbSink(Package);
	}
	
    public void setConfiguration(long flags, EntityPlayerMP changer){
    	super.setConfiguration(flags, changer);
    	flags=flags>>3;
    	//take 16 bits
    	Label=(int) (flags&0xFFFFl);
    	if((flags & 1<<Buttons.CONTINUOUS_MODE.ordinal()) == 1){
    		Player=changer.getDisplayName();
    	}else{
    		Player=null;
    	}
    }
}
