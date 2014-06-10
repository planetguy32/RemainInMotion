package me.planetguy.remaininmotion.network ;

import net.minecraft.block.Block;
import me.planetguy.remaininmotion.MotiveSpectreEntity;
import me.planetguy.remaininmotion.PacketTypes;

public abstract class MultipartPropagationPacket
{
	public static void Dispatch ( net . minecraft . entity . player . EntityPlayerMP Player , java . util . Collection < net . minecraft . tileentity . TileEntity > Tiles )
	{
		net . minecraft . nbt . NBTTagCompound Packet = new net . minecraft . nbt . NBTTagCompound ( ) ;

		Packet . setInteger ( "Id" , Block.getIdFromBlock(MotiveSpectreEntity . MultipartContainerBlockId ) );

		net . minecraft . nbt . NBTTagList Body = new net . minecraft . nbt . NBTTagList ( ) ;

		for ( net . minecraft . tileentity . TileEntity Tile : Tiles )
		{
			net . minecraft . nbt . NBTTagCompound Tag = new net . minecraft . nbt . NBTTagCompound ( ) ;

			Tag . setInteger ( "X" , Tile . xCoord ) ;
			Tag . setInteger ( "Y" , Tile . yCoord ) ;
			Tag . setInteger ( "Z" , Tile . zCoord ) ;

			Body . appendTag ( Tag ) ;
		}

		Packet . setTag ( "Body" , Body ) ;

		PacketManager . SendPacketToPlayer ( Player , PacketTypes . MultipartPropagation , Packet ) ;
	}

	public static void Handle ( net . minecraft . nbt . NBTTagCompound Packet , net . minecraft . world . World World )
	{
		Block Id = Block.getBlockById(Packet . getInteger ( "Id" )) ;

		net . minecraft . nbt . NBTTagList Body = Packet . getTagList ( "Body", 11 ) ;

		for ( int Index = 0 ; Index < Body . tagCount ( ) ; Index ++ )
		{
			net . minecraft . nbt . NBTTagCompound Tag = ( net . minecraft . nbt . NBTTagCompound ) Body.getCompoundTagAt ( Index ) ;

			World . setBlock ( Tag . getInteger ( "X" ) , Tag . getInteger ( "Y" ) , Tag . getInteger ( "Z" ) , Id , 0 , 0 ) ;
		}
	}
}
