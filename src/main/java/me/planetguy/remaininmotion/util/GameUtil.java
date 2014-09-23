package me.planetguy.remaininmotion.util ;

public abstract class GameUtil
{
	public static net . minecraft . server . MinecraftServer GetServer ( )
	{
		return ( cpw . mods . fml . common . FMLCommonHandler . instance ( ) . getMinecraftServerInstance ( ) ) ;
	}
}
