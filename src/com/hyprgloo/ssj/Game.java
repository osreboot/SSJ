package com.hyprgloo.ssj;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;

import com.hyprgloo.ssj.projectile.ProjectileEnemyGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlCamera2D;
import com.osreboot.ridhvl.painter.HvlCursor;

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
		if(Mouse.isButtonDown(0)){
			new ProjectileEnemyGunner(HvlCursor.getCursorPosition().addNew(camera.getX(), camera.getY()).add(camera.getAlignment()), new HvlCoord2D(), 0);
		}
		
		// Attach ships to the player if they collide
		for(ShipFriendly ship : EnvironmentManager.friendlyShips){
			if(!ship.physicsObject.hasParent()){
				if(player.isShipConnected(ship.physicsObject)){
					player.disconnectShip(ship.physicsObject);
				}
				
				PhysicsObject collisionObject = player.collidesWith(ship.physicsObject);
				if(collisionObject != null){
					player.connectShip(ship.physicsObject, collisionObject);
				}
			}
		}
		
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
