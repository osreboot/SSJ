package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.action.HvlAction2;
import com.osreboot.ridhvl.menu.HvlButtonMenuLink;
import com.osreboot.ridhvl.menu.HvlComponent;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
import com.osreboot.ridhvl.painter.HvlCursor;
import com.osreboot.ridhvl.painter.HvlRenderFrame;


public class MenuManager {

	private static final float BUTTON_SPACING = 32f;

	public static HvlMenu splash, main, credits, options, game, end, pause;
	public static HvlRenderFrame pauseFrame;

	public static boolean escapeHeld;
	
	public static boolean win;
	
	public static float backX, backY;
	
	public static String[] deaths;
	public static int death;

	public static HashMap<HvlLabeledButton, LabeledButtonAlias> buttonAliases;
	
	public static HvlCoord2D cursorFocus;

	public static void init() {
		main = new HvlMenu();
		game = new HvlMenu();
		pause = new HvlMenu();
		options = new HvlMenu();
		credits = new HvlMenu();
		end = new HvlMenu();
		splash = new HvlMenu();

		buttonAliases = new HashMap<>();
		
		deaths = new String[] {
				"Space, the final frontier. YOUR final frontier.",
				"YOU PERISHED IN DEEP SPACE",
				"Pressure vessel breach. You died.",
				"Rescue vessel pilot lost. Portal closed."
				};
		
		cursorFocus = new HvlCoord2D();

		try {
			pauseFrame = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		} catch (Exception e) {}

		HvlArrangerBox defaultArrangerBox = new HvlArrangerBox(Display.getWidth(), Display.getHeight(),
				ArrangementStyle.HORIZONTAL);
		defaultArrangerBox.setxAlign(0.5f);
		defaultArrangerBox.setyAlign(0.15f);
		HvlComponentDefault.setDefault(defaultArrangerBox);

		HvlLabeledButton defaultLabeledButton = new HvlLabeledButton(256, 256, new HvlComponentDrawable() {
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Main.getTexture(Main.INDEX_MENU_BUTT));
			}
		}, new HvlComponentDrawable() {
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Main.getTexture(Main.INDEX_MENU_BUTT), Color.gray);
			}
		}, new HvlComponentDrawable() {
			@Override
			public void draw(float deltaArg, float xArg, float yArg, float widthArg, float heightArg) {
				hvlDrawQuad(xArg, yArg, widthArg, heightArg, Main.getTexture(Main.INDEX_MENU_BUTT), Color.lightGray);
			}
		}, Main.font, "", Color.white);

		defaultLabeledButton.setTextScale(0.25f);
		defaultLabeledButton.setyAlign(0.5f);
		defaultLabeledButton.setxAlign(0.5f);
		defaultLabeledButton.setUpdateOverride(new HvlAction2<HvlComponent, Float>(){
			@Override
			public void run(HvlComponent a, Float delta){
				HvlLabeledButton button = (HvlLabeledButton)a;
				if(!buttonAliases.containsKey(button))
					buttonAliases.put(button, new LabeledButtonAlias());
				buttonAliases.get(button).update(delta, button.isHovering());
				button.update(delta);
			}
		});
		HvlComponentDefault.setDefault(defaultLabeledButton);

		main.add(new HvlArrangerBox.Builder().build());

		main.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Options").setWidth(200).setHeight(200)
				.setClickedCommand(new HvlButtonMenuLink(options)).build());
		main.getFirstArrangerBox().add(new HvlSpacer(BUTTON_SPACING, BUTTON_SPACING));

		main.getFirstArrangerBox()
		.add(new HvlLabeledButton.Builder().setText("Play").setClickedCommand(new HvlAction1<HvlButton>() {
			@Override
			public void run(HvlButton aArg) {
				HvlMenu.setCurrent(game);
				Game.reset();
			}
		}).build());
		main.getFirstArrangerBox().add(new HvlSpacer(BUTTON_SPACING, BUTTON_SPACING));
		main.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Credits").setWidth(200).setHeight(200)
				.setClickedCommand(new HvlButtonMenuLink(credits)).build());

		main.add(new HvlArrangerBox.Builder().setxAlign(0.03f).setyAlign(0.95f).build());
		main.getChildOfType(HvlArrangerBox.class, 1).add(new HvlLabeledButton.Builder().setText("Exit").setWidth(128)
				.setHeight(128).setClickedCommand(new HvlAction1<HvlButton>() {
					@Override
					public void run(HvlButton aArg) {
						System.exit(0);
					}
				}).build());

		credits.add(new HvlArrangerBox.Builder().setxAlign(0.03f).setyAlign(0.95f).build());
		credits.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Back").setWidth(128).setHeight(128)
				.setClickedCommand(new HvlAction1<HvlButton>() {
					@Override
					public void run(HvlButton aArg) {
						randomizeBackdrop();
						HvlMenu.setCurrent(main);
					}
				}).build());

		
		options.add(new HvlArrangerBox.Builder().build());
		options.add(new HvlArrangerBox.Builder().setxAlign(0.03f).setyAlign(0.95f).build());
		options.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("SFX: " + (Options.sound ? "ON" : "OFF")).setWidth(250).setHeight(250).setTextScale(0.21f)
				.setClickedCommand(new HvlAction1<HvlButton>(){
					@Override
					public void run(HvlButton a){
						Options.sound = !Options.sound;
						((HvlLabeledButton)a).setText("SFX: " + (Options.sound ? "ON" : "OFF"));
					}
				}).build());
		options.getFirstArrangerBox().add(new HvlSpacer(BUTTON_SPACING, BUTTON_SPACING));
		
		options.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Music: " + (Options.sound ? "ON" : "OFF")).setWidth(250).setHeight(250).setTextScale(0.21f)
				.setClickedCommand(new HvlAction1<HvlButton>(){
					@Override
					public void run(HvlButton a){
						Options.music = !Options.music;
						((HvlLabeledButton)a).setText("Music: " + (Options.music ? "ON" : "OFF"));
					}
				}).build());
		options.getFirstArrangerBox().add(new HvlSpacer(BUTTON_SPACING, BUTTON_SPACING));

		options.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Tutorials: " + (Options.tutorials ? "ON" : "OFF")).setWidth(250).setHeight(250).setTextScale(0.18f)
				.setClickedCommand(new HvlAction1<HvlButton>(){
					@Override
					public void run(HvlButton a){
						Options.tutorials = !Options.tutorials;
						((HvlLabeledButton)a).setText("Tutorials:" + (Options.tutorials ? "ON" : "OFF"));
					}
				}).build());
		options.getChildOfType(HvlArrangerBox.class, 1).add(new HvlLabeledButton.Builder().setText("Back").setWidth(128).setHeight(128)
				.setClickedCommand(new HvlAction1<HvlButton>() {
					@Override
					public void run(HvlButton aArg) {
						randomizeBackdrop();
						HvlMenu.setCurrent(main);
					}
				}).build());
		
		pause.add(new HvlArrangerBox.Builder().setxAlign(0.5f).setyAlign(0.5f).build());
		pause.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Resume").setWidth(275).setHeight(275)
				.setClickedCommand(new HvlAction1<HvlButton>() {
					@Override
					public void run(HvlButton aArg) {
						HvlMenu.setCurrent(game);
					}
				}).build());
		pause.getFirstArrangerBox().add(new HvlSpacer(50, 75));
		pause.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Main Menu").setWidth(275).setHeight(275)
				.setClickedCommand(new HvlAction1<HvlButton>() {
					@Override
					public void run(HvlButton aArg) {
						randomizeBackdrop();
						HvlMenu.setCurrent(main);
					}
				}).build());
		
		end.add(new HvlArrangerBox.Builder().setyAlign(0.7f).build());
		end.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Return").setWidth(250).setHeight(250).setTextScale(0.21f)
				.setClickedCommand(new HvlButtonMenuLink(main)).build());
		
		randomizeBackdrop();
				
		HvlMenu.setCurrent(splash);
	}

	private static float splashProgress = 0f;
	static int i = HvlMath.randomInt(100);
	static String textC = "";

	public static void update(float delta) {
		
		cursorFocus.x = ((float)HvlCursor.getCursorX() - (Display.getWidth() / 2)) / (Display.getWidth() / 2);
		cursorFocus.y = ((float)HvlCursor.getCursorY() - (Display.getHeight() / 2)) / (Display.getHeight() / 2);
		
		if(HvlMenu.getCurrent() != game && HvlMenu.getCurrent() != pause)
			ArtManager.drawBackground(cursorFocus.x * 100f, (cursorFocus.y * 100f) - (Main.getNewestInstance().getTimer().getTotalTime() * 100f));
		
		if (HvlMenu.getCurrent() == pause) {
			hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), pauseFrame);
			hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), new Color(0f, 0f, 0f, 0.4f));
		}

		HvlMenu.updateMenus(delta);

		if (HvlMenu.getCurrent() == splash) {
			splashProgress += delta / 4f;
			if (splashProgress >= 1f || (splashProgress > 0.25f && Mouse.isButtonDown(0)))
				HvlMenu.setCurrent(main);
			float alpha = 1f - (Math.abs(splashProgress - 0.5f) * 2f);
			hvlDrawQuadc(Display.getWidth() / 2, Display.getHeight() / 2, 512, 512, Main.getTexture(Main.INDEX_SPLASH),
					new Color(1f, 1f, 1f, alpha));
		} else if (HvlMenu.getCurrent() == main) {
			Main.font.drawWordc("The Cosmic Collective", Display.getWidth()/2+4 + (cursorFocus.x * 10f), Display.getHeight()/2+114 + (cursorFocus.y * 10f), Color.gray, 0.7f);
			Main.font.drawWordc("The Cosmic Collective", Display.getWidth()/2 + (cursorFocus.x * 10f), Display.getHeight()/2+110 + (cursorFocus.y * 10f), Color.white, 0.7f);
		} else if (HvlMenu.getCurrent() == game) {
			Game.update(delta);
		
			
			if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				escapeHeld = false;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !escapeHeld){
				escapeHeld = true;
				if(HvlMenu.getCurrent()==MenuManager.game) {
					HvlMenu.setCurrent(MenuManager.pause);
				} else if(HvlMenu.getCurrent()==MenuManager.pause) {
					HvlMenu.setCurrent(MenuManager.game);
				}	
			}
		} else if (HvlMenu.getCurrent() == credits) {
			Main.font.drawWordc("CREDITS", (Display.getWidth() / 2) + 4, (Display.getHeight() / 8) + 4, Color.darkGray,
					0.5f);
			Main.font.drawWordc("CREDITS", Display.getWidth() / 2, Display.getHeight() / 8, Color.lightGray, 0.5f);
			Main.font.drawWordc("os_reboot", Display.getWidth() / 2, Display.getHeight() * 4 / 20 + 12, Color.lightGray,
					0.325f);
			Main.font.drawWordc("github.com/osreboot", Display.getWidth() / 2, Display.getHeight() * 5 / 20 + 24,
					Color.lightGray, 0.2f);

			Main.font.drawWordc("HaveANiceDay", Display.getWidth() / 2, Display.getHeight() * 8 / 20, Color.lightGray,
					0.325f);
			Main.font.drawWordc("github.com/haveaniceday33", Display.getWidth() / 2, Display.getHeight() * 9 / 20 + 12,
					Color.lightGray, 0.2f);

			Main.font.drawWordc("JKTransformers", Display.getWidth() / 2, Display.getHeight() * 12 / 20, Color.lightGray,
					0.325f);

			Main.font.drawWordc("Basset", Display.getWidth() / 2, Display.getHeight() * 15 / 20 - 12, Color.lightGray,
					0.325f);			

			textC = (i < 2) ? "Roblox : https://www.roblox.com/users/525422/profile" : "github.com/basset10";

			Main.font.drawWordc(textC, Display.getWidth() / 2, Display.getHeight() * 16 / 20, Color.lightGray,
					0.2f);

			Main.font.drawWordc("Made in 48 hours for Stay Safe! Game Jam", Display.getWidth() / 2,
					Display.getHeight() * 18 / 20 + 16, Color.lightGray, 0.15f);
		} else if (HvlMenu.getCurrent() == pause) {
			
			if(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
				escapeHeld = false;
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !escapeHeld){
				escapeHeld = true;
				HvlMenu.setCurrent(MenuManager.game);
			}

			Main.font.drawWordc("Pause", Display.getWidth() / 2, Display.getHeight() / 8, Color.lightGray, 0.5f);

		} else if (HvlMenu.getCurrent() == end) {
			Game.whiteFade = HvlMath.stepTowards(Game.whiteFade, delta*1.2f, 0f);
			hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), new Color(1f, 1f, 1f, Game.whiteFade));
			String s = win ? "Congratulations! You saved " + Game.player.connectedShips.size() + " ship"+ (Game.player.connectedShips.size() == 1 ? "" : "s") + "!" : deaths[death];
			Main.font.drawWordc(s, Display.getWidth()/2, 250, Color.white, 0.3f);
		}

		if(HvlMenu.getCurrent() != game){

			Color emissiveColor = new Color(0f, 0f, 0.6f, 1f);

			ArrayList<HvlLabeledButton> buttons = getAllButtons(HvlMenu.getCurrent());
			for(HvlLabeledButton button : buttons){
				if(buttonAliases.containsKey(button)){
					float growAmount = buttonAliases.get(button).hoverAmount * button.getWidth() * 0.1f;
					hvlRotate(button.getX() + (button.getWidth() / 2),  button.getY() + (button.getHeight() / 2), buttonAliases.get(button).lightsAngle);
					hvlDrawQuadc(button.getX() + (button.getWidth() / 2),  button.getY() + (button.getHeight() / 2),
							button.getWidth() + growAmount, button.getHeight() + growAmount, Main.getTexture(Main.INDEX_MENU_BUTT_EMISSIVE), emissiveColor);
					hvlResetRotation();
				}
			}

			ArtManager.drawVignette();

			ArtManager.blurFrame.doCapture(new HvlAction0() {
				@Override
				public void run() {
					for(HvlLabeledButton button : buttons){
						if(buttonAliases.containsKey(button)){
							float growAmount = buttonAliases.get(button).hoverAmount * button.getWidth() * 0.1f;
							hvlRotate(button.getX() + (button.getWidth() / 2),  button.getY() + (button.getHeight() / 2), buttonAliases.get(button).lightsAngle);
							hvlDrawQuadc(button.getX() + (button.getWidth() / 2),  button.getY() + (button.getHeight() / 2),
									button.getWidth() + growAmount, button.getHeight() + growAmount, Main.getTexture(Main.INDEX_MENU_BUTT_EMISSIVE), emissiveColor);
							hvlResetRotation();
						}
					}
				}
			});

			ArtManager.drawEmissive();
		}

	}

	private static ArrayList<HvlLabeledButton> getAllButtons(HvlMenu menu){
		ArrayList<HvlLabeledButton> buttons = new ArrayList<>();
		for(int i = 0; i < menu.getChildCount(); i++){
			if(menu.get(i) instanceof HvlArrangerBox)
				buttons.addAll(getAllButtons(menu.<HvlArrangerBox>get(i)));
			if(menu.get(i) instanceof HvlLabeledButton)
				buttons.add(menu.get(i));
		}
		return buttons;
	}

	private static ArrayList<HvlLabeledButton> getAllButtons(HvlArrangerBox arranger){
		ArrayList<HvlLabeledButton> output = new ArrayList<>();
		if(arranger != null){
			for(HvlComponent c : arranger.getChildren()){
				if(c instanceof HvlArrangerBox)
					output.addAll(getAllButtons((HvlArrangerBox)c));
				if(c instanceof HvlLabeledButton)
					output.add((HvlLabeledButton)c);
			}
		}
		return output;
	}
	
	private static void randomizeBackdrop() {
		backX = HvlMath.randomIntBetween(-10000, 10000);
		backY = HvlMath.randomIntBetween(-10000, 10000);
	}

	public static class LabeledButtonAlias{

		float hoverAmount = 0f;
		float lightsAngle = 0f;
		boolean beeped = false;

		public LabeledButtonAlias(){}

		public void update(float delta, boolean hover){
			hoverAmount = HvlMath.stepTowards(hoverAmount, delta * 5f, hover ? 1f : 0f);
			lightsAngle += delta * (hover ? 50f : 20f);
			if(hover && !beeped){
				if(Options.sound) Main.getSound(Main.INDEX_MENU_ROLLY_2).playAsSoundEffect(HvlMath.randomFloatBetween(0.45f, 0.5f), 0.025f, false);
				beeped = true;
			}
			if(!hover) beeped = false;
		}

	}
}
