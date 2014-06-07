package me.planetguy.remaininmotion ;

import net.minecraft.util.Icon;

public class Spectre extends Block
{
	public Spectre ( )
	{
		super ( Configuration . BlockIds . Spectre , bedrock , SpectreItem . class , MotiveSpectreEntity . class , null , TeleportativeSpectreEntity . class, TransduplicativeSpectreEntity.class ) ;

		RenderId = -1 ;
	}

	public enum Types
	{
		Motive ,
		Supportive ,
		Teleportative,
		Transduplicative;
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
	
	public Icon getIcon(int a, int b){
		return Block.planks.getIcon(0, 0);
	}

	@Override
	public boolean isOpaqueCube ( )
	{
		System.out.println("Render fallback (IOC): "+Configuration.Cosmetic.renderFallback);
		return ( Configuration.Cosmetic.renderFallback ) ;
	}

	@Override
	public boolean renderAsNormalBlock ( )
	{
		System.out.println("Render fallback (RANB): "+Configuration.Cosmetic.renderFallback);
		return ( Configuration.Cosmetic.renderFallback ) ;
	}
}
