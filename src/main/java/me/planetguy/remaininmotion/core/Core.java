package me.planetguy.remaininmotion.core ;

import net.minecraft.entity.player.EntityPlayer;
import me.planetguy.remaininmotion.CarriageItem;
import me.planetguy.remaininmotion.CarriagePackageBlacklist;
import me.planetguy.remaininmotion.CarriageTranslocatorEntity;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.Items;
import me.planetguy.remaininmotion.network.MultipartPropagationPacket;
import me.planetguy.remaininmotion.network.RenderPacket;

public abstract class Core
{
	public static void HandlePreInit ( )
	{
		
		ModInteraction . Establish ( ) ;

		CreativeTab . Prepare ( ) ;

		Blocks . Initialize ( ) ;

		Items . Initialize ( ) ;
		
	}

	public static void HandleInit ( )
	{


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

	
	public static void HandlePacket ( int Type , net . minecraft . nbt . NBTTagCompound Body , EntityPlayer Player )
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
	
	public enum PacketTypes{
		Render,
		MultipartPropagation
	}
	
}
