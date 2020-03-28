package com.hyprgloo.ssj;
import java.util.ArrayList;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class AsteroidManager {
	
	private static ArrayList<Asteroid> asteroids;
	
	public static void initAsteroids() {
		asteroids = new ArrayList<>();
	
		for(int i = 0; i < 20000; i++) {
			
			HvlCoord2D asPos = new HvlCoord2D();
			
			asPos.x = HvlMath.randomFloatBetween(-50000, 50000);
			asPos.y = HvlMath.randomFloatBetween(-50000, 50000);
			
			Asteroid a = new Asteroid(asPos, false);
			asteroids.add(a);
		}
		
	}
	
	public static void update(float delta) {
		for(Asteroid a : asteroids) {
			a.assignType();
			if(HvlMath.distance(Game.player.physicsObject.location, a.physicsObject.location) < 1200) {
				a.update(delta);
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
