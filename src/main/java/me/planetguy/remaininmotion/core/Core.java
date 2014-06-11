package me.planetguy.remaininmotion.core ;

import java.util.Arrays;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import me.planetguy.remaininmotion.CarriagePackageBlacklist;
import me.planetguy.remaininmotion.CreativeTab;
import me.planetguy.remaininmotion.DefaultCarriageMatcher;
import me.planetguy.remaininmotion.PacketTypes;
import me.planetguy.remaininmotion.api.RiMRegistry;
import me.planetguy.remaininmotion.crafting.Recipes;
import me.planetguy.remaininmotion.drive.CarriageTranslocatorEntity;
import me.planetguy.remaininmotion.network.MultipartPropagationPacket;
import me.planetguy.remaininmotion.network.PacketManager;
import me.planetguy.remaininmotion.network.RenderPacket;
import me.planetguy.remaininmotion.util.Reflection;

public abstract class Core
{
	public static void HandlePreInit ( )
	{
	}

	public static void HandleInit ( )
	{
		Reflection.init();
		
		ModInteraction . Establish ( ) ;

		CreativeTab . Prepare ( ) ;

		RIMBlocks . Initialize ( ) ;

		Items . Initialize ( ) ;

		CreativeTab . Initialize ( Item.getItemFromBlock(RIMBlocks . Carriage ) );
		
		RiMRegistry.registerMatcher(new DefaultCarriageMatcher());
		
		PacketManager.init();
	}

	public static void HandlePostInit ( )
	{
		Recipes . Register ( ) ;

		CarriagePackageBlacklist . Initialize ( ) ;
	}

	public static void HandleServerStopping ( )
	{
		try{
			CarriageTranslocatorEntity . ActiveTranslocatorSets . clear ( ) ;
		}catch(Error e){
			//e.printStackTrace();
		}
	}

	public static void HandlePacket ( int Type , net . minecraft . nbt . NBTTagCompound Body, EntityPlayer player)
	{
		switch ( PacketTypes . values ( ) [ Type ] )
		{
		case Render :

			RenderPacket . Handle ( Body , player . worldObj ) ;

			break ;

		case MultipartPropagation :

			MultipartPropagationPacket . Handle ( Body ,  player . worldObj ) ;

			break ;
		}
	}
}
