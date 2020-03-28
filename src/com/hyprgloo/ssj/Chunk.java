package com.hyprgloo.ssj;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;

import com.hyprgloo.ssj.merchant.ShipEnemyConvoy;
import com.hyprgloo.ssj.merchant.ShipEnemyGunner;
import com.hyprgloo.ssj.merchant.ShipFriendlyGunner;
import com.hyprgloo.ssj.merchant.ShipFriendlyGrenadier;
import com.hyprgloo.ssj.merchant.ShipFriendlyTrader;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Chunk {

	private static final int NUM_ASTEROIDS = 40;
	private static final int NUM_FRIENDLIES = 30;
	private static final int NUM_ENEMIES = 5;

	public ArrayList<Asteroid> asteroids;
	
	public ArrayList<ShipEnemy> enemyShips;
	
	float difficultyLevel;

	public static final float CHUNK_SIZE = 2000f;
	HvlCoord2D loc;

	public Chunk(HvlCoord2D locA, boolean start) {
		loc = locA;
		asteroids = new ArrayList<>();
		
		difficultyLevel = HvlMath.distance(loc.x, loc.y, 0, 0)/CHUNK_SIZE;
		
		enemyShips = new ArrayList<>();

		for (int i = 0; i < NUM_ASTEROIDS + difficultyLevel; i++) {

			HvlCoord2D asPos = new HvlCoord2D();

			asPos.x = HvlMath.randomFloatBetween(-CHUNK_SIZE/2, CHUNK_SIZE/2) + loc.x;
			asPos.y = HvlMath.randomFloatBetween(-CHUNK_SIZE/2, CHUNK_SIZE/2) + loc.y;

			if (start) {
				while (HvlMath.distance(asPos.x, asPos.y, 0, 0) < 750) {
					asPos.x = HvlMath.randomFloatBetween(-CHUNK_SIZE/2, CHUNK_SIZE/2);
					asPos.y = HvlMath.randomFloatBetween(-CHUNK_SIZE/2, CHUNK_SIZE/2);
				}
			}

			new Asteroid(asPos, false, 0, asteroids);
		}

		int shipType = 0;
		for (int i = 0; i < (NUM_FRIENDLIES/(difficultyLevel+2))+20; i++) {
			shipType = HvlMath.randomInt(1000);
			HvlCoord2D spawnLoc = new HvlCoord2D();
			Asteroid randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));
			while (randomAsteroid.hasJr)
				randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));

			spawnLoc.x = randomAsteroid.physicsObject.location.x
					+ (HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100
							: +randomAsteroid.physicsObject.radius + 100);
			spawnLoc.y = randomAsteroid.physicsObject.location.y
					+ (HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100
							: +randomAsteroid.physicsObject.radius + 100);

			if (shipType >= 333 && shipType <= 800)
				EnvironmentManager.friendlyShips.add(new ShipFriendlyTrader(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f)));
			else if (shipType > 800)
				EnvironmentManager.friendlyShips.add(new ShipFriendlyGrenadier(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f)));
			else if (shipType < 333)
				EnvironmentManager.friendlyShips.add(new ShipFriendlyGunner(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f)));
		}

		for (int i = 0; i < NUM_ENEMIES + 2*Math.log(difficultyLevel); i++) {
			shipType = HvlMath.randomInt(1000);
			HvlCoord2D spawnLoc = new HvlCoord2D();
			Asteroid randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));
			while (randomAsteroid.hasJr)
				randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));

			spawnLoc.x = randomAsteroid.physicsObject.location.x
					+ (HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100
							: +randomAsteroid.physicsObject.radius + 100);
			spawnLoc.y = randomAsteroid.physicsObject.location.y
					+ (HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100
							: +randomAsteroid.physicsObject.radius + 100);
			if(shipType < 500)
				enemyShips.add(new ShipEnemyGunner(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f), false));
			else if(shipType >= 500)
				enemyShips.add(new ShipEnemyConvoy(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f), false));


		}

	}

	public void update(float delta) {
		
		asteroids.removeIf(a -> a.physicsObject.isDead());
		enemyShips.removeIf(s -> s.physicsObject.isDead());
		
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

		for (ShipEnemy ship : enemyShips) {
			
			if (HvlMath.distance(Game.player.physicsObject.location, ship.physicsObject.location) < CHUNK_SIZE * 1.5)
				ship.update(delta, Game.player);
			
			if (HvlMath.distance(Game.player.physicsObject.location, ship.physicsObject.location) < CHUNK_SIZE/2 + Display.getWidth()/2) {
				ship.draw(delta);
				ship.physicsObject.canDealDamage = true;
				ship.physicsObject.canReceiveDamage = true;
			} else {
				ship.physicsObject.canDealDamage = false;
				ship.physicsObject.canReceiveDamage = false;
			}
		}
	}

}
