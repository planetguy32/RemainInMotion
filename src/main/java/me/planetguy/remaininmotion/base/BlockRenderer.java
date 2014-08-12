package me.planetguy.remaininmotion.base ;

import net.minecraft.block.Block;
import net.minecraft.util.IIcon;
import me.planetguy.remaininmotion.Directions;
import me.planetguy.remaininmotion.client.Render;

public abstract class BlockRenderer implements cpw . mods . fml . client . registry . ISimpleBlockRenderingHandler , net . minecraftforge . client . IItemRenderer
{
	public int RenderId ;

	@Override
	public int getRenderId ( )
	{
		return ( RenderId ) ;
	}

	public int Initialize ( Block block )
	{
		RenderId = cpw . mods . fml . client . registry . RenderingRegistry . getNextAvailableRenderId ( ) ;

		cpw . mods . fml . client . registry . RenderingRegistry . registerBlockHandler ( this ) ;

		net . minecraftforge . client . MinecraftForgeClient.registerItemRenderer( Item.getItemFromBlock(block) , this ) ;

		return ( RenderId ) ;
	}

	public void Render ( net . minecraft . tileentity . TileEntity TileEntity )
	{
	}

	public int X ;
	public int Y ;
	public int Z ;

	public int Brightness ;

	@Override
	public boolean renderWorldBlock ( net . minecraft . world . IBlockAccess World , int X , int Y , int Z , net . minecraft . block . Block Block , int RenderId , net . minecraft . client . renderer . RenderBlocks RenderBlocks )
	{
		this . X = X ;
		this . Y = Y ;
		this . Z = Z ;

		Brightness = Block . getMixedBrightnessForBlock ( World , X , Y , Z ) ;

		RenderBlocks . setRenderBounds ( 0 , 0 , 0 , 1 , 1 , 1 ) ;

		RenderBlocks . renderStandardBlock ( Block , X , Y , Z ) ;

		Render ( World . getTileEntity ( X , Y , Z ) ) ;

		return ( true ) ;
	}

	@Override
	public boolean shouldRender3DInInventory ( int modelID )
	{
		return ( true ) ;
	}

	@Override
	public void renderInventoryBlock ( net . minecraft . block . Block Block , int Meta , int RenderId , net . minecraft . client . renderer . RenderBlocks RenderBlocks )
	{
	}

	@Override
	public boolean handleRenderType ( net . minecraft . item . ItemStack Item , ItemRenderType Type )
	{
		switch ( Type )
		{
			case ENTITY :
			case EQUIPPED :
			case EQUIPPED_FIRST_PERSON :
			case INVENTORY :

				return ( true ) ;
		default:
			return false;
		}

	}

	@Override
	public boolean shouldUseRenderHelper ( ItemRenderType Type , net . minecraft . item . ItemStack Item , ItemRendererHelper Helper )
	{
		return ( true ) ;
	}

	public abstract IIcon GetIcon ( net . minecraft . item . ItemStack Item , Directions Side ) ;

	@Override
	public void renderItem ( ItemRenderType Type , net . minecraft . item . ItemStack Item , Object ... Arguments )
	{
		net . minecraft . client . renderer . RenderBlocks RenderBlocks = ( net . minecraft . client . renderer . RenderBlocks ) Arguments [ 0 ] ;

		RenderBlocks . setRenderBounds ( 0 , 0 , 0 , 1 , 1 , 1 ) ;

		if ( Type == ItemRenderType . ENTITY )
		{
			Render . Translate ( -0.5 , -0.5 , -0.5 ) ;
		}

		Render . Begin ( ) ;

		Render . SetNormal ( Directions . NegY ) ;
		RenderBlocks . renderFaceYNeg ( null , 0 , 0 , 0 , GetIcon ( Item , Directions . NegY ) ) ;

		Render . SetNormal ( Directions . PosY ) ;
		RenderBlocks . renderFaceYPos ( null , 0 , 0 , 0 , GetIcon ( Item , Directions . PosY ) ) ;

		Render . SetNormal ( Directions . NegZ ) ;
		RenderBlocks . renderFaceZNeg ( null , 0 , 0 , 0 , GetIcon ( Item , Directions . NegZ ) ) ;

		Render . SetNormal ( Directions . PosZ ) ;
		RenderBlocks . renderFaceZPos ( null , 0 , 0 , 0 , GetIcon ( Item , Directions . PosZ ) ) ;

		Render . SetNormal ( Directions . NegX ) ;
		RenderBlocks . renderFaceXNeg ( null , 0 , 0 , 0 , GetIcon ( Item , Directions . NegX ) ) ;

		Render . SetNormal ( Directions . PosX ) ;
		RenderBlocks . renderFaceXPos ( null , 0 , 0 , 0 , GetIcon ( Item , Directions . PosX ) ) ;

		Render . End ( ) ;
	}

	public double SideMinH ;
	public double SideMinV ;

	public double SideMaxH ;
	public double SideMaxV ;

	public void SetSideSpan ( double SideMinH , double SideMinV , double SideMaxH , double SideMaxV )
	{
		this . SideMinH = SideMinH ;
		this . SideMinV = SideMinV ;

		this . SideMaxH = SideMaxH ;
		this . SideMaxV = SideMaxV ;
	}

	public void SubsectSideSpan ( double SubsectMinH , double SubsectMinV , double SubsectMaxH , double SubsectMaxV )
	{
		double SideSpanH = SideMaxH - SideMinH ;
		double SideSpanV = SideMaxV - SideMinV ;

		SideMinH = SideMinH + SideSpanH * SubsectMinH ;
		SideMinV = SideMinV + SideSpanV * SubsectMinV ;

		SideMaxH = SideMaxH - SideSpanH * ( 1 - SubsectMaxH ) ;
		SideMaxV = SideMaxV - SideSpanV * ( 1 - SubsectMaxV ) ;
	}

	public double TextureMinU ;
	public double TextureMinV ;
	public double TextureMaxU ;
	public double TextureMaxV ;

	public void SetTextureSpan ( IIcon Texture )
	{
		TextureMinU = Texture . getInterpolatedU ( 0 ) ;
		TextureMinV = Texture . getInterpolatedV ( 0 ) ;

		TextureMaxU = Texture . getInterpolatedU ( 16 ) ;
		TextureMaxV = Texture . getInterpolatedV ( 16 ) ;
	}

	public void SubsectTextureSpan ( double SubsectMinU , double SubsectMinV , double SubsectMaxU , double SubsectMaxV )
	{
		double TextureSpanU = TextureMaxU - TextureMinU ;
		double TextureSpanV = TextureMaxV - TextureMinV ;

		TextureMinU = TextureMinU + TextureSpanU * SubsectMinU ;
		TextureMinV = TextureMinV + TextureSpanV * SubsectMinV ;

		TextureMaxU = TextureMaxU - TextureSpanU * ( 1 - SubsectMaxU ) ;
		TextureMaxV = TextureMaxV - TextureSpanV * ( 1 - SubsectMaxV ) ;
	}

	public void SetTint ( double Tint )
	{
		Render . SetBrightness ( Brightness ) ;

		Render . SetColorFactor ( Tint ) ;
	}

	public void RenderGhost ( int OffsetX , int OffsetY , int OffsetZ )
	{
		RenderBlock ( X + OffsetX , Y + OffsetY , Z + OffsetZ , - 0.001 , null , true ) ;
	}

	public void RenderOverlay ( double Outset , boolean [ ] SidesToRender , boolean RenderSideFlag )
	{
		RenderBlock ( X , Y , Z , Outset , SidesToRender , RenderSideFlag ) ;
	}

	public void RenderBlock ( int X , int Y , int Z , double Outset , boolean [ ] SidesToRender , boolean RenderSideFlag )
	{
		if ( ( SidesToRender == null ) || ( SidesToRender [ Directions . NegY . ordinal ( ) ] == RenderSideFlag ) )
		{
			SetTint ( 0.5 ) ;

			Render . AddVertex ( X + 1 - SideMinH , Y - Outset , Z + 1 - SideMinV , TextureMinU , TextureMaxV ) ;
			Render . AddVertex ( X + 1 - SideMaxH , Y - Outset , Z + 1 - SideMinV , TextureMaxU , TextureMaxV ) ;
			Render . AddVertex ( X + 1 - SideMaxH , Y - Outset , Z + 1 - SideMaxV , TextureMaxU , TextureMinV ) ;
			Render . AddVertex ( X + 1 - SideMinH , Y - Outset , Z + 1 - SideMaxV , TextureMinU , TextureMinV ) ;
		}

		if ( ( SidesToRender == null ) || ( SidesToRender [ Directions . PosY . ordinal ( ) ] == RenderSideFlag ) )
		{
			SetTint ( 1 ) ;

			Render . AddVertex ( X + SideMinH , Y + 1 + Outset , Z + 1 - SideMinV , TextureMinU , TextureMaxV ) ;
			Render . AddVertex ( X + SideMaxH , Y + 1 + Outset , Z + 1 - SideMinV , TextureMaxU , TextureMaxV ) ;
			Render . AddVertex ( X + SideMaxH , Y + 1 + Outset , Z + 1 - SideMaxV , TextureMaxU , TextureMinV ) ;
			Render . AddVertex ( X + SideMinH , Y + 1 + Outset , Z + 1 - SideMaxV , TextureMinU , TextureMinV ) ;
		}

		if ( ( SidesToRender == null ) || ( SidesToRender [ Directions . NegZ . ordinal ( ) ] == RenderSideFlag ) )
		{
			SetTint ( 0.8 ) ;

			Render . AddVertex ( X + 1 - SideMaxH , Y + SideMaxV , Z - Outset , TextureMinU , TextureMinV ) ;
			Render . AddVertex ( X + 1 - SideMinH , Y + SideMaxV , Z - Outset , TextureMaxU , TextureMinV ) ;
			Render . AddVertex ( X + 1 - SideMinH , Y + SideMinV , Z - Outset , TextureMaxU , TextureMaxV ) ;
			Render . AddVertex ( X + 1 - SideMaxH , Y + SideMinV , Z - Outset , TextureMinU , TextureMaxV ) ;
		}

		if ( ( SidesToRender == null ) || ( SidesToRender [ Directions . PosZ . ordinal ( ) ] == RenderSideFlag ) )
		{
			SetTint ( 0.8 ) ;

			Render . AddVertex ( X + SideMinH , Y + SideMinV , Z + 1 + Outset , TextureMinU , TextureMaxV ) ;
			Render . AddVertex ( X + SideMaxH , Y + SideMinV , Z + 1 + Outset , TextureMaxU , TextureMaxV ) ;
			Render . AddVertex ( X + SideMaxH , Y + SideMaxV , Z + 1 + Outset , TextureMaxU , TextureMinV ) ;
			Render . AddVertex ( X + SideMinH , Y + SideMaxV , Z + 1 + Outset , TextureMinU , TextureMinV ) ;
		}

		if ( ( SidesToRender == null ) || ( SidesToRender [ Directions . NegX . ordinal ( ) ] == RenderSideFlag ) )
		{
			SetTint ( 0.6 ) ;

			Render . AddVertex ( X - Outset , Y + SideMinV , Z + SideMinH , TextureMinU , TextureMaxV ) ;
			Render . AddVertex ( X - Outset , Y + SideMinV , Z + SideMaxH , TextureMaxU , TextureMaxV ) ;
			Render . AddVertex ( X - Outset , Y + SideMaxV , Z + SideMaxH , TextureMaxU , TextureMinV ) ;
			Render . AddVertex ( X - Outset , Y + SideMaxV , Z + SideMinH , TextureMinU , TextureMinV ) ;
		}

		if ( ( SidesToRender == null ) || ( SidesToRender [ Directions . PosX . ordinal ( ) ] == RenderSideFlag ) )
		{
			SetTint ( 0.6 ) ;

			Render . AddVertex ( X + 1 + Outset , Y + SideMaxV , Z + 1 - SideMaxH , TextureMinU , TextureMinV ) ;
			Render . AddVertex ( X + 1 + Outset , Y + SideMaxV , Z + 1 - SideMinH , TextureMaxU , TextureMinV ) ;
			Render . AddVertex ( X + 1 + Outset , Y + SideMinV , Z + 1 - SideMinH , TextureMaxU , TextureMaxV ) ;
			Render . AddVertex ( X + 1 + Outset , Y + SideMinV , Z + 1 - SideMaxH , TextureMinU , TextureMaxV ) ;
		}
	}

}
