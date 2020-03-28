package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.action.HvlAction2;
import com.osreboot.ridhvl.menu.HvlButtonMenuLink;
import com.osreboot.ridhvl.menu.HvlComponent;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
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
		
		try{
			pauseFrame = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		}catch(Exception e){}
		
		HvlArrangerBox defaultArrangerBox = new HvlArrangerBox(Display.getWidth(), Display.getHeight(), ArrangementStyle.VERTICAL);
		defaultArrangerBox.setxAlign(0.05f);
		HvlComponentDefault.setDefault(defaultArrangerBox);

		HvlLabeledButton defaultLabeledButton = new HvlLabeledButton(256, 64, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg){
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.darkGray);
			}
		}, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg){
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.gray);
			}
		}, new HvlComponentDrawable(){
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg){
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Color.lightGray);
			}
		}, Main.font, "", Color.blue);
		
		defaultLabeledButton.setTextScale(0.25f);
		defaultLabeledButton.setyAlign(0.7f);
		defaultLabeledButton.setxAlign(0.5f);
		HvlComponentDefault.setDefault(defaultLabeledButton);
		
		main.add(new HvlArrangerBox.Builder().build());
		main.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Play!").setClickedCommand(new HvlButtonMenuLink(game)).build());
		
		HvlMenu.setCurrent(splash);
	}
	
	private static float splashProgress = 0f;

	public static void update(float delta) {
		
		HvlMenu.updateMenus(delta);
		
		if (HvlMenu.getCurrent() == splash) {
			
			//TODO: DELETE WHEN PUBLISHED IS READY
			HvlMenu.setCurrent(main);
			//////////////////////////////////////
			
			splashProgress += delta / 4f;
			if (splashProgress >= 1f || (splashProgress > 0.25f && Mouse.isButtonDown(0)))
				HvlMenu.setCurrent(main);
			float alpha = 1f - (Math.abs(splashProgress - 0.5f) * 2f);
			hvlDrawQuadc(Display.getWidth() / 2, Display.getHeight() / 2, 512, 512,
					Main.getTexture(Main.INDEX_SPLASH), new Color(1f, 1f, 1f, alpha));
		} else if (HvlMenu.getCurrent() == main) {
			
			Game.reset();
			HvlMenu.setCurrent(game);
		} else if (HvlMenu.getCurrent() == game) {
			Game.update(delta);
		}
		
	}
}
