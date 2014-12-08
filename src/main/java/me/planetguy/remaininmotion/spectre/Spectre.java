package me.planetguy.remaininmotion.spectre ;

import me.planetguy.remaininmotion.ToolItemSet;
import me.planetguy.remaininmotion.base.RIMBlock;
import me.planetguy.remaininmotion.core.Configuration;
import me.planetguy.remaininmotion.util.WorldUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class Spectre extends RIMBlock
{
	public Spectre ( )
	{
		super (Blocks.bedrock , SpectreItem . class , MotiveSpectreEntity . class , null , TeleportativeSpectreEntity . class, TransduplicativeSpectreEntity.class, RotativeSpectreEntity.class ) ;
		RenderId = -1 ;
	}

	public enum Types
	{
		Motive ,
		Supportive ,
		Teleportative,
		Transduplicative,
		Rotative;
	}

	@Override
	public boolean onBlockActivated ( net . minecraft . world . World World , int X , int Y , int Z , net . minecraft . entity . player . EntityPlayer Player , int Side , float HitX , float HitY , float HitZ )
	{
		if ( World . isRemote )
		{
			return ( false ) ;
		}

		if ( World . getBlockMetadata ( X , Y , Z ) != Types . Supportive . ordinal ( ) )
		{
			return ( false ) ;
		}

		if ( ! ToolItemSet . IsScrewdriverOrEquivalent ( Player . inventory . getCurrentItem ( ) ) )
		{
			return ( false ) ;
		}

		WorldUtil . ClearBlock ( World , X , Y , Z ) ;

		return ( true ) ;
	}
	
	public IIcon getIcon(int a, int b){
		return Blocks.planks.getIcon(0, 0);
	}

	@Override
	public boolean isOpaqueCube ( )
	{
		//System.out.println("Render fallback (IOC): "+Configuration.Cosmetic.renderFallback);
		return ( Configuration.Cosmetic.renderFallback ) ;
	}

	@Override
	public boolean renderAsNormalBlock ( )
	{
		//System.out.println("Render fallback (RANB): "+Configuration.Cosmetic.renderFallback);
		return ( Configuration.Cosmetic.renderFallback ) ;
	}
}
