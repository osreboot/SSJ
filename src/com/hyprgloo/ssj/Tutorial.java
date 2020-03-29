package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;

public abstract class Tutorial {

	public static final float DURATION_TUTORIAL = 10f;
	
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
				displayText("TUTORIAL YESSSSSS", 500, 500);
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
	
	public void displayText(String text, float x, float y){
		float width = Main.font.getLineWidth(text) * 0.25f;
		float height = Main.font.getLineHeight(text) * 0.25f;
		hvlDrawQuadc(x, y, width, height, Color.darkGray);
		Main.font.drawWordc(text, x, y, Color.white, 0.25f);
	}
	
	public abstract boolean shouldSpawn();
	public abstract void display(float delta);

}
