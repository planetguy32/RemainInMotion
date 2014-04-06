package me.planetguy.remaininmotion ;

public enum Directions
{
	NegY ( 0 ) ,
	PosY ( 1 ) ,
	NegZ ( 2 ) ,
	PosZ ( 3 ) ,
	NegX ( 4 ) ,
	PosX ( 5 ) ;

	public int DeltaX ;
	public int DeltaY ;
	public int DeltaZ ;

	public int Opposite ;
	
	private Directions ( int direction )
	{
		int[] dx={1,-1,0,0,0,0};
		int[] dy={0,0,1,-1,0,0};
		int[] dz={0,0,0,0,1,-1};
		int[] opposite={1,0,3,2,5,4};
		DeltaX = dx[direction] ;
		DeltaY = dy[direction];
		DeltaZ = dz[direction];

		Opposite =opposite[direction];
	}

	public Directions Opposite ( )
	{
		return ( values ( ) [ Opposite ] ) ;
	}
}
