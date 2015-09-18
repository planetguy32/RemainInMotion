package me.planetguy.remaininmotion.core.interop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.api.event.BlockPreMoveEvent;
import me.planetguy.remaininmotion.api.event.BlockRotateEvent;
import me.planetguy.remaininmotion.api.event.BlockSelectForMoveEvent;
import me.planetguy.remaininmotion.api.event.BlockSelectForRotateEvent;
import me.planetguy.remaininmotion.api.event.BlocksReplacedEvent;
import me.planetguy.remaininmotion.api.event.CancelableOnBlockAddedEvent;
import me.planetguy.remaininmotion.api.event.IBlockPos;
import me.planetguy.remaininmotion.api.event.MotionFinalizeEvent;
import me.planetguy.remaininmotion.api.event.PostRenderDuringMovementEvent;
import me.planetguy.remaininmotion.api.event.PreRenderDuringMovementEvent;
import me.planetguy.remaininmotion.api.event.RotatingTEPreUnpackEvent;
import me.planetguy.remaininmotion.api.event.TEPostPlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPrePlaceEvent;
import me.planetguy.remaininmotion.api.event.TEPreUnpackEvent;
import me.planetguy.remaininmotion.motion.CarriagePackage;
import me.planetguy.remaininmotion.util.position.BlockRecord;

public class EventPool {
	
	private static BlockPreMoveEvent theBlockPreMoveEvent=new BlockPreMoveEvent();
	private static BlockSelectForMoveEvent theBlockSelectForMoveEvent=new BlockSelectForMoveEvent();
	private static BlockSelectForRotateEvent theBlockSelectForRotateEvent=new BlockSelectForRotateEvent();
	private static PreRenderDuringMovementEvent thePreRenderDuringMovementEvent=new PreRenderDuringMovementEvent();
	private static PostRenderDuringMovementEvent thePostRenderDuringMovementEvent=new PostRenderDuringMovementEvent();
	private static CancelableOnBlockAddedEvent theCancelableOnBlockAddedEvent=new CancelableOnBlockAddedEvent();
	private static MotionFinalizeEvent theMotionFinalizeEvent=new MotionFinalizeEvent();
	private static BlocksReplacedEvent theBlocksReplacedEvent=new BlocksReplacedEvent();
	private static TEPreUnpackEvent theTEPreUnpackEvent=new TEPreUnpackEvent();
	private static TEPrePlaceEvent theTEPrePlaceEvent=new TEPrePlaceEvent();
	private static TEPostPlaceEvent theTEPostPlaceEvent=new TEPostPlaceEvent();
	private static RotatingTEPreUnpackEvent theRotatingTEPreUnpackEvent=new RotatingTEPreUnpackEvent();
	private static BlockRotateEvent theBlockRotateEvent=new BlockRotateEvent();
	
	private static Field eventPhase;
	
	static {
		try {
			eventPhase=Event.class.getDeclaredField("phase");
			eventPhase.setAccessible(true);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public static void postBlockPreMoveEvent(IBlockPos old, IBlockPos blockRecord, Set body) {
		theBlockPreMoveEvent.location=old;
		theBlockPreMoveEvent.newLoc=blockRecord;
		theBlockPreMoveEvent.blocks=body;
		postEvent(theBlockPreMoveEvent);
	}
	
	public static String postBlockSelectForMoveEvent(CarriagePackage poster, boolean blacklistByRotation, BlockRecord record, int axis) {
		BlockSelectForMoveEvent e;
		if(blacklistByRotation) {
			e=theBlockSelectForRotateEvent;
			theBlockSelectForRotateEvent.axis=axis;
		}else {
			e=theBlockSelectForMoveEvent;
		}
		e.location=record;
		//This is a hack to publish the active carriage package without introducing a
		//  dependency on CarriagePackage in the event API. Passed to ISpecialMoveBehaviors. 
		synchronized(CarriagePackage.class) {
			CarriagePackage.activePackage=poster;
			postEvent(e);
			CarriagePackage.activePackage=null;
		}
		if(e.isExcluded()) {
			return "<skipme>";
		}else if(e.isCanceled() || e.getCancelMessag() != null) {
			return e.getCancelMessag() == null ? "unspecified" : e.getCancelMessag();
		} else {
			return null;
		}
	}
	
	public static boolean postPreRenderDuringMovementEvent(RenderBlocks blockRenderer, int x, int y, int z, TileEntity entity, int pass) {
		thePreRenderDuringMovementEvent.renderBlocks=blockRenderer;
		thePreRenderDuringMovementEvent.x=x;
		thePreRenderDuringMovementEvent.y=y;
		thePreRenderDuringMovementEvent.z=z;
		thePreRenderDuringMovementEvent.tile=entity;
		thePreRenderDuringMovementEvent.pass=pass;
		postEvent(thePreRenderDuringMovementEvent);
		return thePreRenderDuringMovementEvent.isCanceled();
	}
	
	public static void postPostRenderDuringMovementEvent(RenderBlocks blockRenderer, int x, int y, int z, TileEntity entity, int pass) {
		thePostRenderDuringMovementEvent.renderBlocks=blockRenderer;
		thePostRenderDuringMovementEvent.x=x;
		thePostRenderDuringMovementEvent.y=y;
		thePostRenderDuringMovementEvent.z=z;
		thePostRenderDuringMovementEvent.tile=entity;
		thePostRenderDuringMovementEvent.pass=pass;
		postEvent(thePostRenderDuringMovementEvent);
	}
	
	public static boolean postCancelableOnBlockAddedEvent(World worldObj, int x, int y, int z) {
		theCancelableOnBlockAddedEvent.worldObj=worldObj;
		theCancelableOnBlockAddedEvent.xCoord=x;
		theCancelableOnBlockAddedEvent.yCoord=y;
		theCancelableOnBlockAddedEvent.zCoord=z;
		postEvent(theCancelableOnBlockAddedEvent);
		return theCancelableOnBlockAddedEvent.isCanceled();
	}

	public static void postMotionFinalizeEvent(BlockRecord record) {
		theMotionFinalizeEvent.location=record;
		postEvent(theMotionFinalizeEvent);
	}

	public static void postBlocksReplacedEvent(TileEntity te) {
		theBlocksReplacedEvent.unpackingEntity=te;
		postEvent(theBlocksReplacedEvent);
	}

	public static void postTEPreUnpackEvent(TileEntity unpacker, BlockRecord record) {
		theTEPreUnpackEvent.location=record;
		theTEPreUnpackEvent.spectre=unpacker;
		postEvent(theTEPreUnpackEvent);
	}

	public static void postTEPrePlaceEvent(TileEntity te, BlockRecord record) {
		theTEPrePlaceEvent.location=record;
		theTEPrePlaceEvent.spectre=te;
		postEvent(theTEPrePlaceEvent);
	}

	public static void postTEPostPlaceEvent(TileEntity te, BlockRecord record) {
		theTEPostPlaceEvent.location=record;
		theTEPostPlaceEvent.spectre=te;
		postEvent(theTEPostPlaceEvent);
	}

	public static void postRotatingTEPreUnpackEvent(TileEntity te, BlockRecord record, ForgeDirection dir) {
		theRotatingTEPreUnpackEvent.axis=dir;
		theRotatingTEPreUnpackEvent.location=record;
		theRotatingTEPreUnpackEvent.spectre=te;
		postEvent(theRotatingTEPreUnpackEvent);
	}

	public static void postBlockRotateEvent(BlockRecord record,	ForgeDirection forgeDirection) {
		theBlockRotateEvent.axis=forgeDirection;
		theBlockRotateEvent.location=record;
		postEvent(theBlockRotateEvent);
	}

	private static void postEvent(Event e) {
		try {
			eventPhase.set(e, null);
		} catch (IllegalArgumentException e1) {
			throw(new RuntimeException(e1));
		} catch (IllegalAccessException e1) {
			throw(new RuntimeException(e1));
		}
		RiMRegistry.blockMoveBus.post(e);
	}
	
	

}
