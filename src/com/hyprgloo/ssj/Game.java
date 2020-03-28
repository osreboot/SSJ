package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.merchant.ShipEnemyGunner;
import com.hyprgloo.ssj.merchant.ShipFriendlyGunner;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlCamera2D;

public class Game {
	
	public static HvlCamera2D camera;
	
	public static Player player;
	
	public static ArrayList<ShipFriendly> idleShips;
	public static ArrayList<ShipEnemy> enemyShips;
	public static ArrayList<Projectile> projectiles;
	public static ArrayList<Asteroid> asteroids;
	
	public static float globalTimer = 0f;
	
	public static void reset(){
		camera = new HvlCamera2D(0, 0, 0, 1f, HvlCamera2D.ALIGNMENT_CENTER);
		
		player = new Player();
		idleShips = new ArrayList<>();
		enemyShips = new ArrayList<>();
		projectiles = new ArrayList<>();
		asteroids = new ArrayList<>();
		
		// Spawn idle ships
		idleShips.add(new ShipFriendlyGunner(500f, 500f, 0f));
		idleShips.add(new ShipFriendlyGunner(300f, 500f, 0f));
		idleShips.add(new ShipFriendlyGunner(500f, 300f, 0f));
		idleShips.add(new ShipFriendlyGunner(400f, 400f, 0f));
		
		// Spawn enemy ships
		enemyShips.add(new ShipEnemyGunner(100f, 100f, 0f));
		
		//Spawn asteroids
		asteroids.add(new Asteroid(100f, 100f, 0f, 0f, 32f, 10f, 100f));
	}
	
	public static void update(float delta){
		// Attach ships to the player if they collide
		ArrayList<ShipFriendly> connectedShips = new ArrayList<>();	
		for(ShipFriendly ship : idleShips){
			if(player.collidesWith(ship.physicsObject)){
				player.connectShip(ship);
				connectedShips.add(ship);
			}
		}
		for(ShipFriendly ship : connectedShips)
			idleShips.remove(ship);
		
		// TODO check if player dies
		
		idleShips.removeIf(s -> s.physicsObject.isDead());
		enemyShips.removeIf(s -> s.physicsObject.isDead());
		projectiles.removeIf(p -> p.physicsObject.isDead());
//		asteroids.removeIf(a -> a.)
		
		camera.setPosition(player.getBaseLocation().x, player.getBaseLocation().y);
		camera.doTransform(new HvlAction0(){
			@Override
			public void run(){
				
				// TODO remove this when real scenery is added
				hvlDrawQuadc(0, 0, 500f, 500f, Color.darkGray);
				
				// Update and draw all idle ships
				for(ShipFriendly ship : idleShips){
					ship.update(delta, player);
					ship.draw(delta);
				}


			//Update and draw asteroids
				for(Asteroid a : asteroids){
					a.update(delta);
					a.draw();
				}
				// Update and draw all enemy ships
				for(ShipEnemy ship : enemyShips){
					ship.update(delta);
					ship.draw(delta);
				}
				
				// Update and draw the player
				player.update(delta);
				player.draw(delta);
				
				for(Projectile projectile : projectiles){
					projectile.update(delta);
					projectile.draw(delta);
				}
				
			}
		});
		globalTimer += delta;
	}
}
