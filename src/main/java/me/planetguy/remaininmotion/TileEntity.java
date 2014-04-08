package me.planetguy.remaininmotion ;

import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public abstract class TileEntity extends net . minecraft . tileentity . TileEntity
{
	public void WriteCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
	}

	public void WriteServerRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
	}

	public void WriteClientRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
	}

	@Override
	public void writeToNBT ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		super . writeToNBT ( TagCompound ) ;

		WriteCommonRecord ( TagCompound ) ;

		WriteServerRecord ( TagCompound ) ;
	}

	@Override
	public Packet getDescriptionPacket ( )
	{
		S35PacketUpdateTileEntity packet = new S35PacketUpdateTileEntity ( xCoord , yCoord , zCoord , 0 , new net . minecraft . nbt . NBTTagCompound ( ) ) ;

		WriteCommonRecord ( packet.func_148857_g() ) ;

		WriteClientRecord ( packet.func_148857_g() ) ;

		return ( packet ) ;
	}

	public void ReadCommonRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
	}

	public void ReadServerRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
	}

	public void ReadClientRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
	}

	@Override
	public void readFromNBT ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		super . readFromNBT ( TagCompound ) ;

		ReadCommonRecord ( TagCompound ) ;

		ReadServerRecord ( TagCompound ) ;
	}

	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		ReadCommonRecord ( pkt.func_148857_g() ) ;

		ReadClientRecord ( pkt .func_148857_g() ) ;

		MarkRenderRecordDirty ( ) ;
	}

	public void MarkServerRecordDirty ( )
	{
		worldObj . getChunkFromBlockCoords ( xCoord , zCoord ) . setChunkModified ( ) ;
	}

	public void MarkClientRecordDirty ( )
	{
		worldObj . markBlockForUpdate ( xCoord , yCoord , zCoord ) ;
	}

	public void Propagate ( )
	{
		MarkServerRecordDirty ( ) ;

		MarkClientRecordDirty ( ) ;
	}

	public void MarkRenderRecordDirty ( )
	{
		worldObj . markBlockForUpdate ( xCoord , yCoord , zCoord ) ;
	}

	public void Initialize ( )
	{
	}

	@Override
	public void validate ( )
	{
		super . validate ( ) ;

		Initialize ( ) ;
	}

	public void Finalize ( )
	{
	}

	@Override
	public void invalidate ( )
	{
		Finalize ( ) ;

		super . invalidate ( ) ;
	}

	public void Setup ( net . minecraft . entity . player . EntityPlayer Player , net . minecraft . item . ItemStack Item )
	{
	}

	public void EmitDrops ( Block Block , int Meta )
	{
	}

	public void EmitDrop ( Block block , net . minecraft . item . ItemStack Drop )
	{
		block . dropBlockAsItem ( worldObj , xCoord , yCoord , zCoord , Drop ) ;
	}
}
