package me.planetguy.remaininmotion.render;

import me.planetguy.remaininmotion.util.transformations.Directions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;

import org.lwjgl.opengl.GL11;

public abstract class Render {
	public static void Begin() {
		Tessellator.instance.startDrawingQuads();
	}

	public static void End() {
		Tessellator.instance.draw();
	}

	public static void SetBrightness(int Brightness) {
		Tessellator.instance.setBrightness(Brightness);
	}

	public static void SetColorFactor(double R, double G, double B) {
		Tessellator.instance.setColorOpaque_F((float) R, (float) G, (float) B);
	}

	public static void SetColorFactor(double Factor) {
		SetColorFactor(Factor, Factor, Factor);
	}

	public static void SetNormal(double I, double J, double K) {
		Tessellator.instance.setNormal((float) I, (float) J, (float) K);
	}

	public static void SetNormal(Directions Direction) {
		SetNormal(Direction.deltaX, Direction.deltaY, Direction.deltaZ);
	}

	public static void AddVertex(double X, double Y, double Z, double U, double V) {
		Tessellator.instance.addVertexWithUV(X, Y, Z, U, V);
	}

	public static void PushMatrix() {
		GL11.glPushMatrix();
	}

	public static void PopMatrix() {
		GL11.glPopMatrix();
	}

	public static void Translate(double X, double Y, double Z) {
		GL11.glTranslated(X, Y, Z);
	}

	public static void Rotate(double Angle, double I, double J, double K) {
		GL11.glRotatef((float) Angle, (float) I, (float) J, (float) K);
	}

	public static void Scale(double X, double Y, double Z) {
		GL11.glScaled(X, Y, Z);
	}

	public static int InitializeDisplayList() {
		int DisplayList = GL11.glGenLists(1);

		GL11.glNewList(DisplayList, GL11.GL_COMPILE);

		return (DisplayList);
	}

	public static void FinalizeDisplayList() {
		GL11.glEndList();
	}

	public static void ExecuteDisplayList(int DisplayList) {
		GL11.glCallList(DisplayList);
	}

	public static void ReleaseDisplayList(int DisplayList) {
		if (DisplayList == 0) { return; }

		GL11.glDeleteLists(DisplayList, 1);
	}

	public static TextureManager TextureManager() {
		return (Minecraft.getMinecraft().renderEngine);
	}

	public static void ResetBoundTexture() {}

	public static void BindBlockTexture() {
		TextureManager().bindTexture(TextureManager().getResourceLocation(0));
	}
}
