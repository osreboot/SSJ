package com.hyprgloo.ssj;

import java.util.ArrayList;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class AsteroidManager {

	public static ArrayList<Asteroid> asteroids;

	public static void initAsteroids() {
		asteroids = new ArrayList<>();

		for (int i = 0; i < 20000; i++) {

			HvlCoord2D asPos = new HvlCoord2D();

			asPos.x = HvlMath.randomFloatBetween(-50000, 50000);
			asPos.y = HvlMath.randomFloatBetween(-50000, 50000);

			while (HvlMath.distance(asPos.x, asPos.y, 0, 0) < 750) {
				asPos.x = HvlMath.randomFloatBetween(-50000, 50000);
				asPos.y = HvlMath.randomFloatBetween(-50000, 50000);
			}

			new Asteroid(asPos, false, 0);
		}

	}

	public static void update(float delta) {
		for (Asteroid a : asteroids) {
			a.update(delta);
			if (HvlMath.distance(Game.player.physicsObject.location, a.physicsObject.location) < 1200) {
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
