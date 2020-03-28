package com.hyprgloo.ssj;
import java.util.ArrayList;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class AsteroidManager {
	
	private static ArrayList<Asteroid> asteroids;
	
	public static void initAsteroids() {
		asteroids = new ArrayList<>();
		double time1 = System.currentTimeMillis();
		float initSize;
		for(int i = 0; i < 20000; i++) {
			
			HvlCoord2D asPos = new HvlCoord2D();
			
			initSize = HvlMath.randomFloatBetween(50, 500);
			
			asPos.x = HvlMath.randomFloatBetween(-50000, 50000);
			asPos.y = HvlMath.randomFloatBetween(-50000, 50000);

			for(Asteroid a : asteroids) {
				if(HvlMath.distance(asPos, a.physicsObject.location) < a.physicsObject.radius + initSize + 100) {
					asPos.x = HvlMath.randomFloatBetween(-50000, 50000);
					asPos.y = HvlMath.randomFloatBetween(-50000, 50000);
				}
			}
			
			Asteroid a = new Asteroid(asPos, false);
			a.physicsObject.radius = initSize;
			asteroids.add(a);
		}
		double time2 = System.currentTimeMillis();
		
		System.out.println("Took "+(time2-time1)+" millis to generate");
	}
	
	public static void update() {
		for(Asteroid a : asteroids) {
			if(HvlMath.distance(Game.player.physicsObject.location, a.physicsObject.location) < 1200) {
				a.draw();
				a.physicsObject.canDealDamage = true;
				a.physicsObject.canReceiveDamage = true;
			} else {
				a.physicsObject.canDealDamage = false;
				a.physicsObject.canReceiveDamage = false;
			}
		}
	}
}
