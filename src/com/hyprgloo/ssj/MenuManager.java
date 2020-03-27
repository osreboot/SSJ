package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlRenderFrame;

public class MenuManager {

	private static HvlMenu splash, main, credits, options, game, end, pause;
	private static HvlRenderFrame pauseFrame;	

	public static void init() {
		main = new HvlMenu();
		game = new HvlMenu();
		pause = new HvlMenu();
		options = new HvlMenu();
		credits = new HvlMenu();
		end = new HvlMenu();
		splash = new HvlMenu();
		
		//HvlMenu.setCurrent(splash);
	}
	
	private static float splashProgress = 0f;

	public static void update(float delta) {
		if (HvlMenu.getCurrent() == splash) {
			splashProgress += delta / 4f;
			if (splashProgress >= 1f || (splashProgress > 0.25f && Mouse.isButtonDown(0)))
				HvlMenu.setCurrent(main);
			float alpha = 1f - (Math.abs(splashProgress - 0.5f) * 2f);
			hvlDrawQuadc(Display.getWidth() / 2, Display.getHeight() / 2, 512, 512,
					Main.getTexture(Main.INDEX_SPLASH), new Color(1f, 1f, 1f, alpha));
		} else if (HvlMenu.getCurrent() == main) {
			
			
			
		//Game.reset(); UNCOMMENT WHEN MENUS ARE SET
		} else if (HvlMenu.getCurrent() == game) {
			Game.update(delta);
		}
		
	}
}
