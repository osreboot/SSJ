package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;

import org.lwjgl.opengl.Display;

public class ArtManager {

	public static void drawBackground(float xArg, float yArg){
		float uOffset = xArg * 0.001f;
		float vOffset = yArg * 0.001f;
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(),
				uOffset, vOffset, uOffset + (Display.getWidth() / 2048f), vOffset + (Display.getHeight() / 2048f),
				Main.getTexture(Main.INDEX_PLASMA));
	}
	
}
