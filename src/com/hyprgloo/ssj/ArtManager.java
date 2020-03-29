package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadac;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlRenderFrame;
import com.osreboot.ridhvl.painter.HvlRenderFrame.FBOUnsupportedException;
import com.osreboot.ridhvl.painter.HvlShader;

public class ArtManager {

	public static final float
	SCALE_PLASMA = 0.0001f;

	public static final Color
	COLOR_PLASMA = new Color(0.4f, 0.4f, 0.4f),
	COLOR_PLASMA2 = new Color(0.4f, 0.4f, 0.4f, 0.5f),
	COLOR_PLASMA3 = new Color(0.4f, 0.4f, 0.4f, 0.7f);;

	public static HvlShader blurShader, emissiveShader;
	public static HvlRenderFrame blurFrame, emissiveFrame;

	public static ArrayList<Star> stars;

	public static void init(){
		blurShader = new HvlShader("shader/Blur.frag");
		System.out.println(blurShader.getFragLog());
		emissiveShader = new HvlShader("shader/Emissive.frag");
		System.out.println(emissiveShader.getFragLog());
		try{
			blurFrame = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
			emissiveFrame = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		}catch(FBOUnsupportedException e){
			e.printStackTrace();
		}

		stars = new ArrayList<>();
		for(int i = 0; i < 200; i++){
			stars.add(new Star());
		}
	}

	public static void drawBackground(float xArg, float yArg){
		float uOffset = xArg * SCALE_PLASMA;
		float vOffset = yArg * SCALE_PLASMA;
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(),
				uOffset, vOffset, uOffset + (Display.getWidth() / 2048f), vOffset + (Display.getHeight() / 2048f),
				Main.getTexture(Main.INDEX_PLASMA), COLOR_PLASMA);
		float plasma2Scale = 1.25f;
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(),
				(uOffset * plasma2Scale), (vOffset * plasma2Scale), (uOffset * plasma2Scale) + (Display.getWidth() / 2048f), (vOffset * plasma2Scale) + (Display.getHeight() / 2048f),
				Main.getTexture(Main.INDEX_PLASMA_2), COLOR_PLASMA2);
		float plasma3Scale = 1.5f;
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(),
				(uOffset * plasma3Scale), (vOffset * plasma3Scale), (uOffset * plasma3Scale) + (Display.getWidth() / 2048f), (vOffset * plasma3Scale) + (Display.getHeight() / 2048f),
				Main.getTexture(Main.INDEX_PLASMA_3), COLOR_PLASMA3);
		for(Star s : stars)
			s.draw(-xArg / 10000f, -yArg / 10000f);
	}

	public static void drawVignette(){
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), Main.getTexture(Main.INDEX_VIGNETTE), new Color(1f, 1f, 1f, 0.5f));
		//		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), Color.black);
	}

	public static void drawEmissive(){
		emissiveFrame.doCapture(new HvlAction0(){
			@Override
			public void run(){
				blurShader.doShade(new HvlAction0(){
					@Override
					public void run(){
						hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), blurFrame);
					}
				});
			}
		});
		emissiveShader.doShade(new HvlAction0(){
			@Override
			public void run(){
				hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), emissiveFrame);
			}
		});
	}

	public static class Star{

		public static final float DISTRIBUTION = 2048;
		
		public HvlCoord2D location;
		public float depth, rotation, scale;
		public Color color;

		public Star(){
			location = new HvlCoord2D(HvlMath.randomFloatBetween(0f, 1f), HvlMath.randomFloatBetween(0f, 1f));
			depth = HvlMath.randomFloatBetween(0f, 1f);
			rotation = HvlMath.randomFloatBetween(0f, 360f);
			color = new Color(0.1f, 0.1f, depth / 2f + 0.5f, depth);
			scale = HvlMath.map(depth, 0f, 1f, 2f, 8f);
		}
		
		public void draw(float offsetX, float offsetY){
			float speedDepth = HvlMath.map(depth, 0f, 1f, 0.5f, 1.5f);
			float x = (((offsetX * speedDepth + location.x) * DISTRIBUTION) % DISTRIBUTION);
			while(x < 0) x += DISTRIBUTION;
			while(x > DISTRIBUTION) x -= DISTRIBUTION;
			float y = (((offsetY * speedDepth + location.y) * DISTRIBUTION) % DISTRIBUTION);
			while(y < 0) y += DISTRIBUTION;
			while(y > DISTRIBUTION) y -= DISTRIBUTION;
			hvlRotate(x - (DISTRIBUTION / 2) + (Display.getWidth()/2), y - (DISTRIBUTION / 2) + (Display.getHeight() / 2), rotation);
			hvlDrawQuadac(x - (DISTRIBUTION / 2), y - (DISTRIBUTION / 2), scale, scale, Main.getTexture(Main.INDEX_STAR), color);
			hvlResetRotation();
		}

	}

}
