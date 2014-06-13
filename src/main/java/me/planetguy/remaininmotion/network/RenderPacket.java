package me.planetguy.remaininmotion.network ;

import net.minecraft.nbt.NBTTagList;
import me.planetguy.remaininmotion.BlockPosition;
import me.planetguy.remaininmotion.BlockRecord;
import me.planetguy.remaininmotion.BlockRecordSet;
import me.planetguy.remaininmotion.CarriagePackage;
import me.planetguy.remaininmotion.CarriageRenderCache;
import me.planetguy.remaininmotion.PacketTypes;
import me.planetguy.remaininmotion.drive.CarriageDriveEntity;
import me.planetguy.remaininmotion.util.Debug;

public abstract class RenderPacket
{
	public static void Dispatch ( CarriagePackage Package )
	{
		Debug.dbg("Dispatching render packet");
		net . minecraft . nbt . NBTTagCompound Packet = new net . minecraft . nbt . NBTTagCompound ( ) ;

		Packet . setInteger ( "DriveX" , Package . DriveRecord . X ) ;
		Packet . setInteger ( "DriveY" , Package . DriveRecord . Y ) ;
		Packet . setInteger ( "DriveZ" , Package . DriveRecord . Z ) ;

		Packet . setBoolean ( "Anchored" , Package . DriveIsAnchored ) ;

		Packet . setInteger ( "Dimension" , Package . RenderCacheKey . Dimension ) ;

		net . minecraft . nbt . NBTTagList Body = new net . minecraft . nbt . NBTTagList ( ) ;

		for ( BlockRecord Record : Package . Body )
		{
			net . minecraft . nbt . NBTTagCompound Tag = new net . minecraft . nbt . NBTTagCompound ( ) ;

			Tag . setInteger ( "X" , Record . X ) ;
			Tag . setInteger ( "Y" , Record . Y ) ;
			Tag . setInteger ( "Z" , Record . Z ) ;

			Body . appendTag ( Tag ) ;
		}

		Packet.setTag ( "Body" , Body ) ;
		
		Debug.dbg("RemIM tags:"+((NBTTagList) Packet.getTag("Body")).tagCount());

		if ( Package . MotionDirection == null )
		{
			PacketManager . BroadcastPacketFromBlock ( Package . AnchorRecord . X , Package . AnchorRecord . Y , Package . AnchorRecord . Z , Package . World , PacketTypes . Render , Packet ) ;

			PacketManager . BroadcastPacketFromBlock
			(
				Package . AnchorRecord . X - Package . DriveRecord . X + Package . Translocator . xCoord ,
				Package . AnchorRecord . Y - Package . DriveRecord . Y + Package . Translocator . yCoord ,
				Package . AnchorRecord . Z - Package . DriveRecord . Z + Package . Translocator . zCoord ,
				Package . Translocator.getWorldObj() ,
				PacketTypes . Render ,
				Packet
			) ;
		}
		else
		{
			PacketManager . BroadcastPacketFromBlock
			(
				Package . AnchorRecord . X + Package . MotionDirection . DeltaX ,
				Package . AnchorRecord . Y + Package . MotionDirection . DeltaY ,
				Package . AnchorRecord . Z + Package . MotionDirection . DeltaZ ,
				Package . World ,
				PacketTypes . Render ,
				Packet
			) ;
		}
	}

	public static void Handle ( net . minecraft . nbt . NBTTagCompound Packet , net . minecraft . world . World World )
	{
		Debug.dbg("RenderPacket arrived!");
		
		int DriveX = Packet . getInteger ( "DriveX" ) ;
		int DriveY = Packet . getInteger ( "DriveY" ) ;
		int DriveZ = Packet . getInteger ( "DriveZ" ) ;

		boolean DriveIsAnchored = Packet . getBoolean ( "Anchored" ) ;

		int Dimension = Packet . getInteger ( "Dimension" ) ;

		net . minecraft . nbt . NBTTagList Body = (NBTTagList) Packet.getTag("Body") ;

		Debug.dbg("<Body= ("+Body.tagCount()+")");
		
		BlockRecordSet Blocks = new BlockRecordSet ( ) ;

		BlockRecordSet TileEntities = new BlockRecordSet ( ) ;

		for ( int Index = 0 ; Index < Body . tagCount ( ) ; Index ++ )
		{
			net . minecraft . nbt . NBTTagCompound Tag = Body.getCompoundTagAt( Index ) ;

			BlockRecord Record = new BlockRecord ( Tag . getInteger ( "X" ) , Tag . getInteger ( "Y" ) , Tag . getInteger ( "Z" ) ) ;

			Record . Identify ( World ) ;

			Blocks . add ( Record ) ;

			if ( Record . Entity != null )
			{
				TileEntities . add ( Record ) ;
			}

			if ( ! DriveIsAnchored )
			{
				if ( Record . X == DriveX )
				{
					if ( Record . Y == DriveY )
					{
						if ( Record . Z == DriveZ )
						{
							try
							{
								( ( CarriageDriveEntity ) Record . Entity ) . Active = true ;
							}
							catch ( Throwable Throwable )
							{
								Throwable . printStackTrace ( ) ;
							}
						}
					}
				}
			}
		}

		try
		{
			CarriageRenderCache . Assemble ( Blocks , TileEntities , World , new BlockPosition ( DriveX , DriveY , DriveZ , Dimension ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}
}
