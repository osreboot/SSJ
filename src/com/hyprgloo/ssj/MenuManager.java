package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction1;
import com.osreboot.ridhvl.action.HvlAction2;
import com.osreboot.ridhvl.menu.HvlButtonMenuLink;
import com.osreboot.ridhvl.menu.HvlComponent;
import com.osreboot.ridhvl.menu.HvlComponentDefault;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox;
import com.osreboot.ridhvl.menu.component.HvlButton;
import com.osreboot.ridhvl.menu.component.HvlComponentDrawable;
import com.osreboot.ridhvl.menu.component.HvlSpacer;
import com.osreboot.ridhvl.menu.component.HvlArrangerBox.ArrangementStyle;
import com.osreboot.ridhvl.menu.component.collection.HvlLabeledButton;
import com.osreboot.ridhvl.painter.HvlRenderFrame;

public class MenuManager {

	private static final float BUTTON_SPACING = 32f;

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

		try {
			pauseFrame = new HvlRenderFrame(Display.getWidth(), Display.getHeight());
		} catch (Exception e) {
		}

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
						HvlMenu.setCurrent(main);
					}
				}).build());

		options.add(new HvlArrangerBox.Builder().setxAlign(0.03f).setyAlign(0.95f).build());
		options.getFirstArrangerBox().add(new HvlLabeledButton.Builder().setText("Back").setWidth(128).setHeight(128)
				.setClickedCommand(new HvlAction1<HvlButton>() {
					@Override
					public void run(HvlButton aArg) {
						HvlMenu.setCurrent(main);
					}
				}).build());

		HvlMenu.setCurrent(splash);
	}

	private static float splashProgress = 0f;
	static int i = HvlMath.randomInt(100);
	static String textC = "";

	public static void update(float delta) {

		HvlMenu.updateMenus(delta);

		if (HvlMenu.getCurrent() == splash) {

			// TODO: DELETE WHEN PUBLISHED IS READY
			HvlMenu.setCurrent(main);
			//////////////////////////////////////

			splashProgress += delta / 4f;
			if (splashProgress >= 1f || (splashProgress > 0.25f && Mouse.isButtonDown(0)))
				HvlMenu.setCurrent(main);
			float alpha = 1f - (Math.abs(splashProgress - 0.5f) * 2f);
			hvlDrawQuadc(Display.getWidth() / 2, Display.getHeight() / 2, 512, 512, Main.getTexture(Main.INDEX_SPLASH),
					new Color(1f, 1f, 1f, alpha));
		} else if (HvlMenu.getCurrent() == main) {
		} else if (HvlMenu.getCurrent() == game) {
			Game.update(delta);
		} else if (HvlMenu.getCurrent() == credits) {
			Main.font.drawWordc("CREDITS", (Display.getWidth() / 2) + 4, (Display.getHeight() / 8) + 4, Color.darkGray,
					0.5f);
			Main.font.drawWordc("CREDITS", Display.getWidth() / 2, Display.getHeight() / 8, Color.lightGray, 0.5f);
			Main.font.drawWordc("os_reboot", Display.getWidth() / 2, Display.getHeight() * 4 / 20 + 12, Color.lightGray,
					0.325f);
			Main.font.drawWordc("Twitter: os_reboot", Display.getWidth() / 2, Display.getHeight() * 5 / 20 + 24,
					Color.lightGray, 0.2f);

			Main.font.drawWordc("HaveANiceDay", Display.getWidth() / 2, Display.getHeight() * 8 / 20, Color.lightGray,
					0.325f);
			Main.font.drawWordc("github.com/haveaniceday33", Display.getWidth() / 2, Display.getHeight() * 9 / 20 + 12,
					Color.lightGray, 0.2f);

			Main.font.drawWordc("JKTransformers", Display.getWidth() / 2, Display.getHeight() * 12 / 20, Color.lightGray,
					0.325f);

			Main.font.drawWordc("Basset", Display.getWidth() / 2, Display.getHeight() * 15 / 20 - 12, Color.lightGray,
					0.325f);			
			
			textC = (i < 2) ? "Roblox : https://www.roblox.com/users/525422/profile" : "Twitter: xbassetx";
			
			Main.font.drawWordc(textC, Display.getWidth() / 2, Display.getHeight() * 16 / 20, Color.lightGray,
					0.2f);

			Main.font.drawWordc("Made in 48 hours for Stay Safe! Game Jam", Display.getWidth() / 2,
					Display.getHeight() * 18 / 20 + 16, Color.lightGray, 0.15f);
		}

	}
}
