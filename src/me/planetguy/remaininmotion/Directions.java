package me.planetguy.remaininmotion ;

public enum Directions
{
	NegY ( net . minecraftforge . common . ForgeDirection . DOWN ) ,
	PosY ( net . minecraftforge . common . ForgeDirection . UP ) ,
	NegZ ( net . minecraftforge . common . ForgeDirection . NORTH ) ,
	PosZ ( net . minecraftforge . common . ForgeDirection . SOUTH ) ,
	NegX ( net . minecraftforge . common . ForgeDirection . WEST ) ,
	PosX ( net . minecraftforge . common . ForgeDirection . EAST ) ;

	public int DeltaX ;
	public int DeltaY ;
	public int DeltaZ ;

	public int Opposite ;

	private Directions ( net . minecraftforge . common . ForgeDirection Direction )
	{
		DeltaX = Direction . offsetX ;
		DeltaY = Direction . offsetY ;
		DeltaZ = Direction . offsetZ ;

		Opposite = Direction . getOpposite ( ) . ordinal ( ) ;
	}

	public Directions Opposite ( )
	{
		return ( values ( ) [ Opposite ] ) ;
	}
}
