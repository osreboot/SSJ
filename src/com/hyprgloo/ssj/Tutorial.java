package com.hyprgloo.ssj;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0r;
import com.osreboot.ridhvl.action.HvlAction2;

public class Tutorial {

	public static final float DURATION_TUTORIAL = 10f;
	
	private static ArrayList<Tutorial> tutorials;
	private static Tutorial current;

	public static boolean isDisplaying(){
		return current != null;
	}

	public static void init(){
		tutorials = new ArrayList<>();
		tutorials.add(new Tutorial(new HvlAction0r<Boolean>(){
			@Override
			public Boolean run(){
				return true;
			}
		}, new HvlAction2<Tutorial, Float>(){
			@Override
			public void run(Tutorial t, Float delta){
				Main.font.drawWord("TUTORIAL", 0, 0, Color.red);
				if(t.timer <= 0) t.complete = true;
			}
		}));
	}
	
	public static void gameReset(){
		current = null;
	}

	public static void update(float delta){
		if(Options.tutorials){
			if(current == null){
				for(Tutorial t : tutorials){
					if(!t.complete && t.spawn.run()){
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
	public HvlAction0r<Boolean> spawn;
	public HvlAction2<Tutorial, Float> draw;

	private float timer = 0f;

	public Tutorial(HvlAction0r<Boolean> spawnArg, HvlAction2<Tutorial, Float> drawArg){
		timer = DURATION_TUTORIAL;
		spawn = spawnArg;
		draw = drawArg;
	}

	public void draw(float delta){
		timer = HvlMath.stepTowards(timer, delta, 0f);
		draw.run(this, delta);
	}

}
