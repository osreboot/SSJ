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

	public static ArrayList<PhysicsObject> physicsObjects;

	public static Player player;

	public static ArrayList<Projectile> projectiles;

	public static float globalTimer = 0f;

	public static void reset(){
		camera = new HvlCamera2D(0, 0, 0, 1f, HvlCamera2D.ALIGNMENT_CENTER);

		physicsObjects = new ArrayList<>();

		player = new Player();
		projectiles = new ArrayList<>();

		
		EnvironmentManager.init();
	}

	public static void update(float delta){
		// Attach ships to the player if they collide
		ArrayList<ShipFriendly> connectedShips = new ArrayList<>();	
		for(ShipFriendly ship : EnvironmentManager.friendlyShips){
			if(player.collidesWith(ship.physicsObject)){
				player.connectShip(ship);
				connectedShips.add(ship);
			}
		}
		for(ShipFriendly ship : connectedShips)
			EnvironmentManager.friendlyShips.remove(ship);

		// Dealing damage across all entities
		physicsObjects.removeIf(p -> p.isDead());
		for(PhysicsObject physicsObjectCollidee : physicsObjects){
			if(physicsObjectCollidee.canReceiveDamage){
				for(PhysicsObject physicsObjectCollider : physicsObjects){
					if(physicsObjectCollider.canDealDamage){
						if(physicsObjectCollidee != physicsObjectCollider &&
								physicsObjectCollidee.alliance != physicsObjectCollider.alliance &&
								physicsObjectCollidee.collidesWith(physicsObjectCollider)){
							physicsObjectCollidee.onCollision(physicsObjectCollider);
							if(physicsObjectCollidee.isDead()){
								physicsObjectCollidee.onDeath();
							}
						}
					}
				}
			}
		}
		physicsObjects.removeIf(p -> p.isDead());

		// Removing all dead entities
		// TODO check if player dies
		EnvironmentManager.friendlyShips.removeIf(s -> s.physicsObject.isDead());
		EnvironmentManager.enemyShips.removeIf(s -> s.physicsObject.isDead());
		projectiles.removeIf(p -> p.physicsObject.isDead());
		EnvironmentManager.asteroids.removeIf(a -> a.physicsObject.isDead());

		camera.setPosition(player.getBaseLocation().x, player.getBaseLocation().y);
		camera.doTransform(new HvlAction0(){
			@Override
			public void run(){
				
				EnvironmentManager.update(delta);
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
