package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawLine;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public abstract class Tutorial {

	public static final float 
	DURATION_TUTORIAL = 10f,
	DURATION_TASK_TIMEOUT = 2f;

	public static final Color 
	COLOR_TUTORIAL_BACKGROUND = new Color(0f, 0f, 0.5f),
	COLOR_FONT = Color.white,
	COLOR_FONT_COMPLETE = Color.green,
	COLOR_FONT_BG = Color.gray,
	COLOR_FONT_BG_COMPLETE = Color.darkGray,
	COLOR_ARROW_HINT = new Color(1f, 1f, 1f, 0.3f),
	COLOR_ARROW_HINT_COMPLETE = new Color(0.5f, 1f, 0.5f, 0.3f);

	private static ArrayList<Tutorial> tutorials;
	private static Tutorial current;

	public static boolean isDisplaying(){
		return current != null;
	}

	public static void init(){
		tutorials = new ArrayList<>();

		Tutorial tutorialWASD = new TutorialTask(){
			boolean hasPressedW = false;
			boolean hasPressedA = false;
			boolean hasPressedS = false;
			boolean hasPressedD = false;
			@Override
			public boolean shouldSpawn(){
				return Game.globalTimer > 1f;
			}
			@Override
			protected void displayPre(float delta){
				if(Keyboard.isKeyDown(Keyboard.KEY_W)) hasPressedW = true;
				if(Keyboard.isKeyDown(Keyboard.KEY_A)) hasPressedA = true;
				if(Keyboard.isKeyDown(Keyboard.KEY_S)) hasPressedS = true;
				if(Keyboard.isKeyDown(Keyboard.KEY_D)) hasPressedD = true;

				if(hasPressedW && hasPressedA && hasPressedS && hasPressedD)
					taskComplete = true;
			}
			@Override
			protected void displayPost(float delta){
				displayText("Welcome to NAME!", Display.getWidth()/2, 64, taskComplete); // TODO NAME HERE
				displayText("Use WASD to move.", Display.getWidth()/2, 128, taskComplete);
				drawTaskCheckMark(Display.getWidth()/2 + 128f + 128f, 128);
				
				displayKeyboardHint("W", Display.getWidth()/2, Display.getHeight()/2 - 96f, hasPressedW);
				displayKeyboardHint("S", Display.getWidth()/2, Display.getHeight()/2 + 96f, hasPressedS);
				displayKeyboardHint("A", Display.getWidth()/2 - 96f, Display.getHeight()/2, hasPressedA);
				displayKeyboardHint("D", Display.getWidth()/2 + 96f, Display.getHeight()/2, hasPressedD);
				
				hvlDrawQuadc(Display.getWidth()/2, Display.getHeight()/2 - 64f, 24f, 24f, Main.getTexture(Main.INDEX_ARROW_UP), hasPressedW ? COLOR_ARROW_HINT_COMPLETE : COLOR_ARROW_HINT);
				hvlDrawQuadc(Display.getWidth()/2, Display.getHeight()/2 + 64f, 24f, -24f, Main.getTexture(Main.INDEX_ARROW_UP), hasPressedS ? COLOR_ARROW_HINT_COMPLETE : COLOR_ARROW_HINT);
				
				hvlRotate(Display.getWidth()/2, Display.getHeight()/2, 90f);
				hvlDrawQuadc(Display.getWidth()/2, Display.getHeight()/2 - 64f, 24f, 24f, Main.getTexture(Main.INDEX_ARROW_UP), hasPressedD ? COLOR_ARROW_HINT_COMPLETE : COLOR_ARROW_HINT);
				hvlDrawQuadc(Display.getWidth()/2, Display.getHeight()/2 + 64f, 24f, -24f, Main.getTexture(Main.INDEX_ARROW_UP), hasPressedA ? COLOR_ARROW_HINT_COMPLETE : COLOR_ARROW_HINT);
				hvlResetRotation();
			}
		};
		tutorials.add(tutorialWASD);
		
		Tutorial tutorialQE = new TutorialTask(){
			boolean hasPressedQ = false;
			boolean hasPressedE = false;
			@Override
			public boolean shouldSpawn(){
				return tutorialWASD.complete;
			}
			@Override
			protected void displayPre(float delta){
				if(Keyboard.isKeyDown(Keyboard.KEY_Q)) hasPressedQ = true;
				if(Keyboard.isKeyDown(Keyboard.KEY_E)) hasPressedE = true;

				if(hasPressedQ && hasPressedE)
					taskComplete = true;
			}
			@Override
			protected void displayPost(float delta){
				displayText("Use Q and E to rotate.", Display.getWidth()/2, 96f, taskComplete);
				drawTaskCheckMark(Display.getWidth()/2 + 128f + 128f + 32f, 96f);
				
				displayKeyboardHint("Q", Display.getWidth()/2 - 96f, Display.getHeight()/2 - 96f, hasPressedQ);
				displayKeyboardHint("E", Display.getWidth()/2 + 96f, Display.getHeight()/2 - 96f, hasPressedE);
				
				hvlDrawQuadc(Display.getWidth()/2 - 64f, Display.getHeight()/2 - 64f, 32f, 32f, Main.getTexture(Main.INDEX_ARROW_LEFT), hasPressedQ ? COLOR_ARROW_HINT_COMPLETE : COLOR_ARROW_HINT);
				hvlDrawQuadc(Display.getWidth()/2 + 64f, Display.getHeight()/2 - 64f, -32f, 32f, Main.getTexture(Main.INDEX_ARROW_LEFT), hasPressedE ? COLOR_ARROW_HINT_COMPLETE : COLOR_ARROW_HINT);
			}
		};
		tutorials.add(tutorialQE);
		
		Tutorial tutorialShoot = new TutorialTask(){
			@Override
			public boolean shouldSpawn(){
				return tutorialWASD.complete;
			}
			@Override
			protected void displayPre(float delta){
				if(Mouse.isButtonDown(0)) taskComplete = true;

				pointShip(Display.getWidth()/2, 96f, Display.getWidth()/2, Display.getHeight()/2);
			}
			@Override
			protected void displayPost(float delta){
				displayText("Use LMOUSE to shoot.", Display.getWidth()/2, 96f, taskComplete);
				drawTaskCheckMark(Display.getWidth()/2 + 128f + 128f + 32f, 96f);
			}
		};
		tutorials.add(tutorialShoot);
	}

	public static void gameReset(){
		current = null;
	}

	public static void tickPre(float delta){
		if(Options.tutorials){
			if(current == null){
				for(Tutorial t : tutorials){
					if(!t.complete && t.shouldSpawn()){
						current = t;
						current.reset();
						break;
					}
				}
			}else{
				current.updatePre(delta);
				if(current.timer <= 0 || current.complete){
					current = null;
				}
			}
		}
	}
	
	public static void tickPost(float delta){
		if(Options.tutorials){
			if(current != null)
				current.updatePost(delta);
		}
	}

	public boolean complete;

	public float timer = 0f;

	public Tutorial(){
		timer = DURATION_TUTORIAL;
	}

	public void reset(){
		timer = DURATION_TUTORIAL;
	}
	
	public void updatePre(float delta){
		timer = HvlMath.stepTowards(timer, delta, 0f);
		displayPre(delta);
	}
	
	public void updatePost(float delta){
		displayPost(delta);
	}

	@SuppressWarnings("deprecation")
	public void displayText(String text, float x, float y, boolean completed){
		float width = Main.font.getLineWidth(text) * 0.25f;
		float height = Main.font.getLineHeight(text) * 0.25f;
		HvlPainter2D.hvlForceRefresh();
		hvlDrawQuadc(x - 2f, y - 2f, width + 4f, height + 4f, COLOR_TUTORIAL_BACKGROUND);
		Main.font.drawWordc(text, x - 1f, y - 1f, completed ? COLOR_FONT_BG_COMPLETE : COLOR_FONT_BG, 0.25f);
		Main.font.drawWordc(text, x + 1f, y + 1f, completed ? COLOR_FONT_COMPLETE : COLOR_FONT, 0.25f);
	}

	@SuppressWarnings("deprecation")
	public void displayKeyboardHint(String text, float x, float y, boolean completed){
		float width = Main.font.getLineWidth(text) * 0.125f;
		float height = Main.font.getLineHeight(text) * 0.125f;
		HvlPainter2D.hvlForceRefresh();
		hvlDrawQuadc(x - 2f, y - 2f, width + 4f, height + 4f, COLOR_TUTORIAL_BACKGROUND);
		Main.font.drawWordc(text, x - 1f, y - 1f, completed ? COLOR_FONT_BG_COMPLETE : COLOR_FONT_BG, 0.125f);
		Main.font.drawWordc(text, x + 1f, y + 1f, completed ? COLOR_FONT_COMPLETE : COLOR_FONT, 0.125f);
	}

	public void point(float fromX, float fromY, float toX, float toY, float toWidth, float toHeight){
		hvlDrawLine(fromX, fromY, toX, toY, Color.blue, 4f);
		hvlDrawQuadc(toX, toY, toWidth, toHeight, COLOR_TUTORIAL_BACKGROUND);
	}

	public void point(float fromX, float fromY, float toX, float toY){
		hvlDrawLine(fromX, fromY, toX, toY, Color.blue, 4f);
		hvlDrawQuadc(toX, toY, 8f, 8f, Main.getTexture(Main.INDEX_CIRCLE), COLOR_TUTORIAL_BACKGROUND);
	}
	
	public void pointShip(float fromX, float fromY, float toX, float toY){
		hvlDrawLine(fromX, fromY, toX, toY, Color.blue, 4f);
		hvlDrawQuadc(toX, toY, 48f, 48f, Main.getTexture(Main.INDEX_CIRCLE), COLOR_TUTORIAL_BACKGROUND);
	}

	public abstract boolean shouldSpawn();
	protected abstract void displayPre(float delta);
	protected abstract void displayPost(float delta);

	public abstract static class TutorialTask extends Tutorial{

		public boolean taskComplete = false;
		public float taskCompleteTimer = DURATION_TASK_TIMEOUT;

		@Override
		public void reset(){
			super.reset();
			taskCompleteTimer = DURATION_TASK_TIMEOUT;
		}
		
		@Override
		public void updatePre(float delta){
			if(taskComplete){
				taskCompleteTimer = HvlMath.stepTowards(taskCompleteTimer, delta, 0f);
				if(taskCompleteTimer <= 0f)
					complete = true;
			}
			displayPre(delta);
		}

		public void drawTaskCheckMark(float x, float y){
			float width = 64f;
			float height = 64f;
			if(taskComplete){
				hvlDrawQuadc(x - 2f, y - 2f, width + 4f, height + 4f, COLOR_TUTORIAL_BACKGROUND);
				hvlDrawQuadc(x + 1f, y + 1f, width, height, Main.getTexture(Main.INDEX_CHECK), COLOR_FONT_BG_COMPLETE);
				hvlDrawQuadc(x - 1f, y - 1f, width, height, Main.getTexture(Main.INDEX_CHECK), COLOR_FONT_COMPLETE);
			}
		}

	}

}
