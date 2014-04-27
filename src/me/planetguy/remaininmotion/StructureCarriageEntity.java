package me.planetguy.remaininmotion ;

public class StructureCarriageEntity extends CarriageEntity
{
	public enum EdgeTypes
	{
		X ( Directions . NegX , Directions . PosX ) ,
		Y ( Directions . NegY , Directions . PosY ) ,
		Z ( Directions . NegZ , Directions . PosZ ) ;

		public Directions Neg ;
		public Directions Pos ;

		private EdgeTypes ( Directions Neg , Directions Pos )
		{
			this . Neg = Neg ;
			this . Pos = Pos ;
		}
	}

	public EdgeTypes EdgeType ;

	public enum CornerTypes
	{
		NegX_NegY_NegZ ( Directions . PosX , Directions . PosY , Directions . PosZ ) ,
		PosX_NegY_NegZ ( Directions . NegX , Directions . PosY , Directions . PosZ ) ,
		NegX_PosY_NegZ ( Directions . PosX , Directions . NegY , Directions . PosZ ) ,
		PosX_PosY_NegZ ( Directions . NegX , Directions . NegY , Directions . PosZ ) ,
		NegX_NegY_PosZ ( Directions . PosX , Directions . PosY , Directions . NegZ ) ,
		PosX_NegY_PosZ ( Directions . NegX , Directions . PosY , Directions . NegZ ) ,
		NegX_PosY_PosZ ( Directions . PosX , Directions . NegY , Directions . NegZ ) ,
		PosX_PosY_PosZ ( Directions . NegX , Directions . NegY , Directions . NegZ ) ;

		public Directions ToNextAlongX ;
		public Directions ToNextAlongY ;
		public Directions ToNextAlongZ ;

		private CornerTypes ( Directions ToNextAlongX , Directions ToNextAlongY , Directions ToNextAlongZ )
		{
			this . ToNextAlongX = ToNextAlongX ;
			this . ToNextAlongY = ToNextAlongY ;
			this . ToNextAlongZ = ToNextAlongZ ;
		}

		public CornerTypes NextAlongAxisX ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x1 ] ) ;
		}

		public CornerTypes NextAlongAxisY ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x2 ] ) ;
		}

		public CornerTypes NextAlongAxisZ ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x4 ] ) ;
		}

		public CornerTypes NextAlongPlaneXY ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x3 ] ) ;
		}

		public CornerTypes NextAlongPlaneXZ ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x5 ] ) ;
		}

		public CornerTypes NextAlongPlaneYZ ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x6 ] ) ;
		}

		public CornerTypes Opposite ( )
		{
			return ( values ( ) [ ordinal ( ) ^ 0x7 ] ) ;
		}
	}

	public CornerTypes CornerType ;

	public void CheckSides ( )
	{
		EdgeType = null ;

		CornerType = null ;

		java . util . ArrayList < Directions > OpenDirections = new java . util . ArrayList < Directions > ( ) ;

		for ( Directions Direction : Directions . values ( ) )
		{
			if ( ! SideClosed [ Direction . ordinal ( ) ] )
			{
				OpenDirections . add ( Direction ) ;
			}
		}

		java . util . Collections . sort ( OpenDirections ) ;

		if ( OpenDirections . size ( ) == 2 )
		{
			Directions Neg = OpenDirections . get ( 0 ) ;
			Directions Pos = OpenDirections . get ( 1 ) ;

			for ( EdgeTypes EdgeType : EdgeTypes . values ( ) )
			{
				if ( EdgeType . Neg != Neg )
				{
					continue ;
				}

				if ( EdgeType . Pos != Pos )
				{
					continue ;
				}

				this . EdgeType = EdgeType ;

				return ;
			}
		}
		else if ( OpenDirections . size ( ) == 3 )
		{
			Directions Y = OpenDirections . get ( 0 ) ;
			Directions Z = OpenDirections . get ( 1 ) ;
			Directions X = OpenDirections . get ( 2 ) ;

			for ( CornerTypes CornerType : CornerTypes . values ( ) )
			{
				if ( CornerType . ToNextAlongX != X )
				{
					continue ;
				}

				if ( CornerType . ToNextAlongY != Y )
				{
					continue ;
				}

				if ( CornerType . ToNextAlongZ != Z )
				{
					continue ;
				}

				this . CornerType = CornerType ;

				return ;
			}
		}
	}

	@Override
	public void ReadServerRecord ( net . minecraft . nbt . NBTTagCompound TagCompound )
	{
		CheckSides ( ) ;
	}

	public boolean IsStructureCarriage ( BlockRecord Record )
	{
		Record . Identify ( worldObj ) ;

		if ( Record . Id == Blocks . Carriage . blockID )
		{
			if ( Record . Meta == Carriage . Types . Structure . ordinal ( ) )
			{
				return ( true ) ;
			}
		}

		return ( false ) ;
	}

	@Override
	public void ToggleSide ( int Side , boolean Sneaking )
	{
		Directions ActionDirection = Directions . values ( ) [ Side ] ;

		Directions PropagationDirection = ActionDirection . Opposite ( ) ;

		BlockRecord NextRecord = new BlockRecord ( xCoord , yCoord , zCoord ) ;

		NextRecord . Shift ( PropagationDirection ) ;

		if ( ! IsStructureCarriage ( NextRecord ) )
		{
			return ;
		}

		BlockRecord NextNextRecord = NextRecord . NextInDirection ( PropagationDirection ) ;

		while ( true )
		{
			StructureCarriageEntity NextCarriage = ( StructureCarriageEntity ) NextRecord . Entity ;

			if ( IsStructureCarriage ( NextNextRecord ) )
			{
				for ( Directions Target : Directions . values ( ) )
				{
					if ( ( Target == PropagationDirection ) || ( Target == ActionDirection ) )
					{
						NextCarriage . SideClosed [ Target . ordinal ( ) ] = false ;
					}
					else
					{
						NextCarriage . SideClosed [ Target . ordinal ( ) ] = true ;
					}
				}

				NextCarriage . CheckSides ( ) ;

				NextCarriage . Propagate ( ) ;

				NextRecord = NextNextRecord ;

				NextNextRecord = NextRecord . NextInDirection ( PropagationDirection ) ;

				continue ;
			}

			NextCarriage . SideClosed [ ActionDirection . ordinal ( ) ] = false ;

			NextCarriage . SideClosed [ PropagationDirection . ordinal ( ) ] = true ;

			NextCarriage . CheckSides ( ) ;

			NextCarriage . Propagate ( ) ;

			break ;
		}

		SideClosed [ ActionDirection . ordinal ( ) ] = true ;

		SideClosed [ PropagationDirection . ordinal ( ) ] = false ;

		CheckSides ( ) ;

		Propagate ( ) ;
	}

	public StructureCarriageEntity FollowEdgeToCorner ( EdgeTypes EdgeType , StructureCarriageEntity Origin , Directions Direction ) throws CarriageMotionException
	{
		BlockRecord Record = new BlockRecord ( Origin . xCoord , Origin . yCoord , Origin . zCoord ) ;

		Record . Shift ( Direction ) ;

		while ( IsStructureCarriage ( Record ) )
		{
			StructureCarriageEntity Carriage = ( StructureCarriageEntity ) Record . Entity ;

			if ( Carriage . CornerType != null )
			{
				return ( Carriage ) ;
			}

			if ( Carriage . EdgeType != EdgeType )
			{
				throw ( new CarriageMotionException ( "expected block at (" + Record . X + "," + Record . Y + "," + Record . Z + ") to be edge type " + EdgeType . name ( ) + " of structure carriage" ) ) ;
			}

			Record = Record . NextInDirection ( Direction ) ;
		}

		throw ( new CarriageMotionException ( "expected corner of structure carriage at (" + Record . X + "," + Record . Y + "," + Record . Z + ")" ) ) ;
	}

	public StructureCarriageEntity FollowEdgeToCorner ( EdgeTypes EdgeType , CornerTypes CornerType , StructureCarriageEntity Origin , Directions Direction ) throws CarriageMotionException
	{
		StructureCarriageEntity Carriage = FollowEdgeToCorner ( EdgeType , Origin , Direction ) ;

		if ( Carriage . CornerType == CornerType )
		{
			return ( Carriage ) ;
		}

		throw ( new CarriageMotionException ( "expected " + CornerType . name ( ) + " corner of structure carriage at (" + Carriage . xCoord + "," + Carriage . yCoord + "," + Carriage . zCoord + ")" ) ) ;
	}

	public StructureCarriageEntity AssertCoordsMatch ( StructureCarriageEntity A , StructureCarriageEntity B ) throws CarriageMotionException
	{
		if ( A . xCoord == B . xCoord )
		{
			if ( A . yCoord == B . yCoord )
			{
				if ( A . zCoord == B . zCoord )
				{
					return ( A ) ;
				}
			}
		}

		throw ( new CarriageMotionException ( "structure carriage not properly-configured cuboid" ) ) ;
	}

	public StructureCarriageEntity AssertCoordsMatch ( StructureCarriageEntity A , StructureCarriageEntity B , StructureCarriageEntity C ) throws CarriageMotionException
	{
		return ( AssertCoordsMatch ( AssertCoordsMatch ( A , B ) , C ) ) ;
	}

	@Override
	public void fillPackage ( CarriagePackage Package ) throws CarriageMotionException
	{
		if ( EdgeType != null )
		{
			FollowEdgeToCorner ( EdgeType , this , EdgeType . Neg ) . fillPackage ( Package ) ;

			return ;
		}

		if ( CornerType == null )
		{
			throw ( new CarriageMotionException ( "anchor of structure carriage is neither edge nor corner" ) ) ;
		}

		StructureCarriageEntity NextAlongX = FollowEdgeToCorner ( EdgeTypes . X , CornerType . NextAlongAxisX ( ) , this , CornerType . ToNextAlongX ) ;

		StructureCarriageEntity NextAlongY = FollowEdgeToCorner ( EdgeTypes . Y , CornerType . NextAlongAxisY ( ) , this , CornerType . ToNextAlongY ) ;

		StructureCarriageEntity NextAlongZ = FollowEdgeToCorner ( EdgeTypes . Z , CornerType . NextAlongAxisZ ( ) , this , CornerType . ToNextAlongZ ) ;

		int MinX ;
		int MinY ;
		int MinZ ;

		int MaxX ;
		int MaxY ;
		int MaxZ ;

		if ( CornerType . ToNextAlongX == Directions . PosX )
		{
			MinX = xCoord ;

			MaxX = NextAlongX . xCoord ;
		}
		else
		{
			MinX = NextAlongX . xCoord ;

			MaxX = xCoord ;
		}

		if ( CornerType . ToNextAlongY == Directions . PosY )
		{
			MinY = yCoord ;

			MaxY = NextAlongY . yCoord ;
		}
		else
		{
			MinY = NextAlongY . yCoord ;

			MaxY = yCoord ;
		}

		if ( CornerType . ToNextAlongZ == Directions . PosZ )
		{
			MinZ = zCoord ;

			MaxZ = NextAlongZ . zCoord ;
		}
		else
		{
			MinZ = NextAlongZ . zCoord ;

			MaxZ = zCoord ;
		}

		StructureCarriageEntity NextAlongXY = AssertCoordsMatch
		(
			FollowEdgeToCorner ( EdgeTypes . Y , CornerType . NextAlongPlaneXY ( ) , NextAlongX , CornerType . ToNextAlongY ) ,
			FollowEdgeToCorner ( EdgeTypes . X , CornerType . NextAlongPlaneXY ( ) , NextAlongY , CornerType . ToNextAlongX )
		) ;

		StructureCarriageEntity NextAlongXZ = AssertCoordsMatch
		(
			FollowEdgeToCorner ( EdgeTypes . Z , CornerType . NextAlongPlaneXZ ( ) , NextAlongX , CornerType . ToNextAlongZ ) ,
			FollowEdgeToCorner ( EdgeTypes . X , CornerType . NextAlongPlaneXZ ( ) , NextAlongZ , CornerType . ToNextAlongX )
		) ;

		StructureCarriageEntity NextAlongYZ = AssertCoordsMatch
		(
			FollowEdgeToCorner ( EdgeTypes . Y , CornerType . NextAlongPlaneYZ ( ) , NextAlongZ , CornerType . ToNextAlongY ) ,
			FollowEdgeToCorner ( EdgeTypes . Z , CornerType . NextAlongPlaneYZ ( ) , NextAlongY , CornerType . ToNextAlongZ )
		) ;

		StructureCarriageEntity Opposite = AssertCoordsMatch
		(
			FollowEdgeToCorner ( EdgeTypes . Z , CornerType . Opposite ( ) , NextAlongXY , CornerType . ToNextAlongZ ) ,
			FollowEdgeToCorner ( EdgeTypes . Y , CornerType . Opposite ( ) , NextAlongXZ , CornerType . ToNextAlongY ) ,
			FollowEdgeToCorner ( EdgeTypes . X , CornerType . Opposite ( ) , NextAlongYZ , CornerType . ToNextAlongX )
		) ;

		for ( int X = MinX ; X <= MaxX ; X ++ )
		{
			for ( int Y = MinY ; Y <= MaxY ; Y ++ )
			{
				for ( int Z = MinZ ; Z <= MaxZ ; Z ++ )
				{
					if ( worldObj . isAirBlock ( X , Y , Z ) )
					{
						continue ;
					}

					BlockRecord Record = new BlockRecord ( X , Y , Z ) ;

					Record . Identify ( worldObj ) ;

					Package . AddBlock ( Record ) ;

					if
					(
						( ( X == MinX ) && ( Package . MotionDirection == Directions . NegX ) ) ||
						( ( X == MaxX ) && ( Package . MotionDirection == Directions . PosX ) ) ||
						( ( Y == MinY ) && ( Package . MotionDirection == Directions . NegY ) ) ||
						( ( Y == MaxY ) && ( Package . MotionDirection == Directions . PosY ) ) ||
						( ( Z == MinZ ) && ( Package . MotionDirection == Directions . NegZ ) ) ||
						( ( Z == MaxZ ) && ( Package . MotionDirection == Directions . PosZ ) )
					)
					{
						Package . AddPotentialObstruction ( Record . NextInDirection ( Package . MotionDirection ) ) ;
					}
				}
			}
		}
	}
}
