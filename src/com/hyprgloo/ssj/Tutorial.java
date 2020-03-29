package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawLine;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.painter2d.HvlPainter2D;

public abstract class Tutorial {

	public static final float DURATION_TUTORIAL = 7.5f;
	
	public static final Color COLOR_TUTORIAL_BACKGROUND = new Color(0f, 0f, 0.5f);
	
	private static ArrayList<Tutorial> tutorials;
	private static Tutorial current;

	public static boolean isDisplaying(){
		return current != null;
	}

	public static void init(){
		tutorials = new ArrayList<>();
		tutorials.add(new Tutorial(){
			@Override
			public boolean shouldSpawn(){
				return true;
			}

			@Override
			public void display(float delta){
				point(400, 400, 50, 50, 100, 50);
				displayText("TUTORIAL YESSSSSS", 400, 400);
				if(timer <= 0) complete = true;
			}
		});
	}
	
	public static void gameReset(){
		current = null;
	}

	public static void update(float delta){
		if(Options.tutorials){
			if(current == null){
				for(Tutorial t : tutorials){
					if(!t.complete && t.shouldSpawn()){
						current = t;
						current.timer = DURATION_TUTORIAL;
						break;
					}
				}
			}else{
				current.draw(delta);
				if(current.timer <= 0 || current.complete){
					current = null;
				}
			}
		}
	}

	public boolean complete;

	public float timer = 0f;

	public Tutorial(){
		timer = DURATION_TUTORIAL;
	}

	public void draw(float delta){
		timer = HvlMath.stepTowards(timer, delta, 0f);
		display(delta);
	}
	
	@SuppressWarnings("deprecation")
	public void displayText(String text, float x, float y){
		float width = Main.font.getLineWidth(text) * 0.25f;
		float height = Main.font.getLineHeight(text) * 0.25f;
		HvlPainter2D.hvlForceRefresh();
		hvlDrawQuadc(x - 2f, y - 2f, width + 4f, height + 4f, COLOR_TUTORIAL_BACKGROUND);
		Main.font.drawWordc(text, x - 1f, y - 1f, Color.gray, 0.25f);
		Main.font.drawWordc(text, x + 1f, y + 1f, Color.white, 0.25f);
	}
	
	public void point(float fromX, float fromY, float toX, float toY, float toWidth, float toHeight){
		hvlDrawLine(fromX, fromY, toX, toY, Color.blue, 8f);
		hvlDrawQuadc(toX, toY, toWidth, toHeight, Color.blue);
	}
	
	public abstract boolean shouldSpawn();
	public abstract void display(float delta);

}
