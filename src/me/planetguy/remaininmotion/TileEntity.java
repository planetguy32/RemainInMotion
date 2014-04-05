package me.planetguy.remaininmotion ;

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
	public net . minecraft . network . packet . Packet132TileEntityData getDescriptionPacket ( )
	{
		net . minecraft . network . packet . Packet132TileEntityData Packet = new net . minecraft . network . packet . Packet132TileEntityData ( xCoord , yCoord , zCoord , 0 , new net . minecraft . nbt . NBTTagCompound ( ) ) ;

		WriteCommonRecord ( Packet . data ) ;

		WriteClientRecord ( Packet . data ) ;

		return ( Packet ) ;
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
	public void onDataPacket ( net . minecraft . network . INetworkManager NetworkManager , net . minecraft . network . packet . Packet132TileEntityData Packet )
	{
		ReadCommonRecord ( Packet . data ) ;

		ReadClientRecord ( Packet . data ) ;

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
		worldObj . markBlockForRenderUpdate ( xCoord , yCoord , zCoord ) ;
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

	public void EmitDrop ( Block Block , net . minecraft . item . ItemStack Drop )
	{
		Block . dropBlockAsItem_do ( worldObj , xCoord , yCoord , zCoord , Drop ) ;
	}
}
