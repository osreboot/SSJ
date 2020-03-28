package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class ArtManager {

	public static final float
	SCALE_PLASMA = 0.0001f;
	
	public static final Color
	COLOR_PLASMA = new Color(0.4f, 0.4f, 0.4f);
	
	public static void drawBackground(float xArg, float yArg){
		float uOffset = xArg * SCALE_PLASMA;
		float vOffset = yArg * SCALE_PLASMA;
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(),
				uOffset, vOffset, uOffset + (Display.getWidth() / 2048f), vOffset + (Display.getHeight() / 2048f),
				Main.getTexture(Main.INDEX_PLASMA), COLOR_PLASMA);
	}
	
}
