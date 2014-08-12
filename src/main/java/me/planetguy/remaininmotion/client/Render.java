package me.planetguy.remaininmotion.client ;

import me.planetguy.remaininmotion.Directions;

public abstract class Render
{
	public static void Begin ( )
	{
		net . minecraft . client . renderer . Tessellator . instance . startDrawingQuads ( ) ;
	}

	public static void End ( )
	{
		net . minecraft . client . renderer . Tessellator . instance . draw ( ) ;
	}

	public static void SetBrightness ( int Brightness )
	{
		net . minecraft . client . renderer . Tessellator . instance . setBrightness ( Brightness ) ;
	}

	public static void SetColorFactor ( double R , double G , double B )
	{
		net . minecraft . client . renderer . Tessellator . instance . setColorOpaque_F ( ( float ) R , ( float ) G , ( float ) B ) ;
	}

	public static void SetColorFactor ( double Factor )
	{
		SetColorFactor ( Factor , Factor , Factor ) ;
	}

	public static void SetNormal ( double I , double J , double K )
	{
		net . minecraft . client . renderer . Tessellator . instance . setNormal ( ( float ) I , ( float ) J , ( float ) K ) ;
	}

	public static void SetNormal ( Directions Direction )
	{
		SetNormal ( Direction . DeltaX , Direction . DeltaY , Direction . DeltaZ ) ;
	}

	public static void AddVertex ( double X , double Y , double Z , double U , double V )
	{
		net . minecraft . client . renderer . Tessellator . instance . addVertexWithUV ( X , Y , Z , U , V ) ;
	}

	public static void PushMatrix ( )
	{
		org . lwjgl . opengl . GL11 . glPushMatrix ( ) ;
	}

	public static void PopMatrix ( )
	{
		org . lwjgl . opengl . GL11 . glPopMatrix ( ) ;
	}

	public static void Translate ( double X , double Y , double Z )
	{
		org . lwjgl . opengl . GL11 . glTranslated ( X , Y , Z ) ;
	}

	public static void Rotate ( double Angle , double I , double J , double K )
	{
		org . lwjgl . opengl . GL11 . glRotatef ( ( float ) Angle , ( float ) I , ( float ) J , ( float ) K ) ;
	}

	public static void Scale ( double X , double Y , double Z )
	{
		org . lwjgl . opengl . GL11 . glScaled ( X , Y , Z ) ;
	}

	public static int InitializeDisplayList ( )
	{
		int DisplayList = org . lwjgl . opengl . GL11 . glGenLists ( 1 ) ;

		org . lwjgl . opengl . GL11 . glNewList ( DisplayList , org . lwjgl . opengl . GL11 . GL_COMPILE ) ;
		
		return ( DisplayList ) ;
	}

	public static void FinalizeDisplayList ( )
	{
		org . lwjgl . opengl . GL11 . glEndList ( ) ;
	}

	public static void ExecuteDisplayList ( int DisplayList )
	{
		org . lwjgl . opengl . GL11 . glCallList ( DisplayList ) ;
	}

	public static void ReleaseDisplayList ( int DisplayList )
	{
		if ( DisplayList == 0 )
		{
			return ;
		}

		org . lwjgl . opengl . GL11 . glDeleteLists ( DisplayList , 1 ) ;
	}

	public static net . minecraft . client . renderer . texture . TextureManager TextureManager ( )
	{
		return ( net . minecraft . client . Minecraft . getMinecraft ( ) . renderEngine ) ;
	}

	public static void ResetBoundTexture ( )
	{
	}

	public static void BindBlockTexture ( )
	{
		TextureManager ( ) . bindTexture ( TextureManager ( ) . getResourceLocation ( 0 ) ) ;
	}
}
