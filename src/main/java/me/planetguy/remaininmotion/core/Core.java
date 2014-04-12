package me.planetguy.remaininmotion.core ;

import me.planetguy.remaininmotion.CarriageItem;
import me.planetguy.remaininmotion.CarriagePackageBlacklist;
import me.planetguy.remaininmotion.CarriageTranslocatorEntity;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.Items;

public abstract class Core
{
	public static void HandlePreInit ( )
	{
	}

	public static void HandleInit ( )
	{
		ModInteraction . Establish ( ) ;

		CreativeTab . Prepare ( ) ;

		Blocks . Initialize ( ) ;

		Items . Initialize ( ) ;

		CreativeTab . Initialize ( CarriageItem.instance ) ;
	}

	public static void HandlePostInit ( )
	{
		Recipes . Register ( ) ;

		CarriagePackageBlacklist . Initialize ( ) ;
	}

	public static void HandleServerStopping ( )
	{
		CarriageTranslocatorEntity . ActiveTranslocatorSets . clear ( ) ;
	}

	/*
	public static void HandlePacket ( int Type , net . minecraft . nbt . NBTTagCompound Body , cpw . mods . fml . common . network . Player Player )
	{
		switch ( PacketTypes . values ( ) [ Type ] )
		{
			case Render :

				RenderPacket . Handle ( Body , ( ( net . minecraft . entity . player . EntityPlayer ) Player ) . worldObj ) ;

				break ;

			case MultipartPropagation :

				MultipartPropagationPacket . Handle ( Body , ( ( net . minecraft . entity . player . EntityPlayer ) Player ) . worldObj ) ;

				break ;
		}
	}
	*/
}
