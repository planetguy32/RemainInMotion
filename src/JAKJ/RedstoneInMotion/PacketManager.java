package JAKJ . RedstoneInMotion ;

public class PacketManager implements cpw . mods . fml . common . network . IPacketHandler
{
	@Override
	public void onPacketData ( net . minecraft . network . INetworkManager NetworkManager , net . minecraft . network . packet . Packet250CustomPayload Packet , cpw . mods . fml . common . network . Player Player )
	{
		try
		{
			java . io . DataInputStream Data = new java . io . DataInputStream ( new java . io . ByteArrayInputStream ( Packet . data ) ) ;

			Core . HandlePacket ( Data . readInt ( ) , net . minecraft . network . packet . Packet . readNBTTagCompound ( Data ) , Player ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;
		}
	}

	public static net . minecraft . network . packet . Packet250CustomPayload GeneratePacket ( Enum Type , net . minecraft . nbt . NBTTagCompound Body )
	{
		try
		{
			java . io . ByteArrayOutputStream DataStore = new java . io . ByteArrayOutputStream ( ) ;

			java . io . DataOutputStream Data = new java . io . DataOutputStream ( DataStore ) ;

			Data . writeInt ( Type . ordinal ( ) ) ;

			net . minecraft . network . packet . Packet . writeNBTTagCompound ( Body , Data ) ;

			return ( cpw . mods . fml . common . network . PacketDispatcher . getPacket ( Mod . Channel , DataStore . toByteArray ( ) ) ) ;
		}
		catch ( Throwable Throwable )
		{
			Throwable . printStackTrace ( ) ;

			return ( null ) ;
		}
	}

	public static void BroadcastPacketFromBlock ( int X , int Y , int Z , int Dimension , Enum Type , net . minecraft . nbt . NBTTagCompound Body )
	{
		cpw . mods . fml . common . network . PacketDispatcher . sendPacketToAllAround ( X + 0.5 , Y + 0.5 , Z + 0.5 , 64 , Dimension , GeneratePacket ( Type , Body ) ) ;
	}

	public static void SendPacketToPlayer ( net . minecraft . entity . player . EntityPlayerMP Player , Enum Type , net . minecraft . nbt . NBTTagCompound Body )
	{
		cpw . mods . fml . common . network . PacketDispatcher . sendPacketToPlayer ( GeneratePacket ( Type , Body ) , ( cpw . mods . fml . common . network . Player ) Player ) ;
	}
}
