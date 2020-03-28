package com.hyprgloo.ssj;
import java.util.ArrayList;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class AsteroidManager {
	
	private static ArrayList<Asteroid> asteroids;
	
	public static void initAsteroids() {
		asteroids = new ArrayList<>();
		double time1 = System.currentTimeMillis();
		float xPos = HvlMath.randomFloatBetween(-50000, 50000);
		float yPos = HvlMath.randomFloatBetween(-50000, 50000);
		for(int i = 0; i < 10000; i++) {
			xPos = HvlMath.randomFloatBetween(-50000, 50000);
			yPos = HvlMath.randomFloatBetween(-50000, 50000);
			
			HvlCoord2D asPos = new HvlCoord2D(xPos, yPos);

			Asteroid a = new Asteroid(asPos, false);
			asteroids.add(a);
		}
		double time2 = System.currentTimeMillis();
		
		System.out.println("Took "+(time2-time1)+" millis to generate");
	}
	
	public static void update() {
		for(Asteroid a : asteroids) {
			if(HvlMath.distance(Game.player.physicsObject.location, a.physicsObject.location) < 1200) {
				a.draw();
			}
		}
	}
}
