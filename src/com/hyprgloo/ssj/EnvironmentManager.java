package com.hyprgloo.ssj;

import java.util.ArrayList;

import com.hyprgloo.ssj.merchant.ShipEnemyConvoy;
import com.hyprgloo.ssj.merchant.ShipEnemyGunner;
import com.hyprgloo.ssj.merchant.ShipFriendlyGunner;
import com.hyprgloo.ssj.merchant.ShipFriendlyTrader;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class EnvironmentManager {
	
	private static final int NUM_ASTEROIDS = 2000;
	private static final int NUM_FRIENDLIES = 500;
	private static final int NUM_ENEMIES = 200;
	private static final int WORLD_SIZE = 10000;

	public static ArrayList<Asteroid> asteroids;
	public static ArrayList<ShipFriendly> friendlyShips;
	public static ArrayList<ShipEnemy> enemyShips;

	public static void init() {
		asteroids = new ArrayList<>();
		friendlyShips = new ArrayList<>();
		enemyShips = new ArrayList<>();

		for (int i = 0; i < NUM_ASTEROIDS; i++) {

			HvlCoord2D asPos = new HvlCoord2D();

			asPos.x = HvlMath.randomFloatBetween(-WORLD_SIZE, WORLD_SIZE);
			asPos.y = HvlMath.randomFloatBetween(-WORLD_SIZE, WORLD_SIZE);

			while (HvlMath.distance(asPos.x, asPos.y, 0, 0) < 750) {
				asPos.x = HvlMath.randomFloatBetween(-WORLD_SIZE, WORLD_SIZE);
				asPos.y = HvlMath.randomFloatBetween(-WORLD_SIZE, WORLD_SIZE);
			}

			new Asteroid(asPos, false, 0);
		}
		
		int shipType = 0;
		for (int i = 0; i < NUM_FRIENDLIES; i++) {
			shipType = HvlMath.randomInt(1000);
			HvlCoord2D spawnLoc = new HvlCoord2D();
			Asteroid randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));
			while(randomAsteroid.hasJr)
				randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));
			
			spawnLoc.x = randomAsteroid.physicsObject.location.x + 
					(HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100 : 
						+randomAsteroid.physicsObject.radius + 100);
			spawnLoc.y = randomAsteroid.physicsObject.location.y + 
					(HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100 : 
						+randomAsteroid.physicsObject.radius + 100);
			
			if(shipType >= 333)
				friendlyShips.add(new ShipFriendlyTrader(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f)));
			else if(shipType < 333)
				friendlyShips.add(new ShipFriendlyGunner(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f)));
		}
		
		for (int i = 0; i < NUM_ENEMIES; i++) {
			shipType = HvlMath.randomInt(1000);
			HvlCoord2D spawnLoc = new HvlCoord2D();
			Asteroid randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));
			while(randomAsteroid.hasJr)
				randomAsteroid = asteroids.get(HvlMath.randomInt(NUM_ASTEROIDS));
			
			spawnLoc.x = randomAsteroid.physicsObject.location.x + 
					(HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100 : 
						+randomAsteroid.physicsObject.radius + 100);
			spawnLoc.y = randomAsteroid.physicsObject.location.y + 
					(HvlMath.randomInt(2) == 0 ? -randomAsteroid.physicsObject.radius - 100 : 
						+randomAsteroid.physicsObject.radius + 100);
			

		if(shipType >= 333)
			enemyShips.add(new ShipEnemyConvoy(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f), false));
		else if(shipType < 333)
			enemyShips.add(new ShipEnemyGunner(spawnLoc.x, spawnLoc.y, HvlMath.randomFloatBetween(0, 3.14f), false));

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
		
		for(ShipFriendly ship : friendlyShips){
			if (HvlMath.distance(Game.player.physicsObject.location, ship.physicsObject.location) < 1200) {
				ship.update(delta, Game.player);
				ship.draw(delta);
				ship.physicsObject.canDealDamage = true;
				ship.physicsObject.canReceiveDamage = true;
			} else {
				ship.physicsObject.canDealDamage = false;
				ship.physicsObject.canReceiveDamage = false;
			}
		}
		
		for(ShipEnemy ship : enemyShips){
			if (HvlMath.distance(Game.player.physicsObject.location, ship.physicsObject.location) < 1200) {
				ship.update(delta, Game.player);
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
