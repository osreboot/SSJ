package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;

import com.hyprgloo.ssj.merchant.ShipFriendlyGunner;
import com.hyprgloo.ssj.projectile.ProjectileEnemyGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlCamera2D;
import com.osreboot.ridhvl.painter.HvlCursor;

public class Game {

	public static HvlCamera2D camera;

	public static ArrayList<PhysicsObject> physicsObjects;

	public static Player player;

	public static ArrayList<ShipFriendly> friendlyShips;
	public static ArrayList<ShipEnemy> enemyShips;
	public static ArrayList<Projectile> projectiles;

	public static float globalTimer = 0f;

	public static void reset(){
		camera = new HvlCamera2D(0, 0, 0, 1f, HvlCamera2D.ALIGNMENT_CENTER);

		physicsObjects = new ArrayList<>();

		player = new Player();
		friendlyShips = new ArrayList<>();
		enemyShips = new ArrayList<>();
		projectiles = new ArrayList<>();

		// Spawn idle ships
		friendlyShips.add(new ShipFriendlyGunner(500f, 500f, 0f));
		friendlyShips.add(new ShipFriendlyGunner(300f, 500f, 0f));
		friendlyShips.add(new ShipFriendlyGunner(500f, 300f, 0f));
		friendlyShips.add(new ShipFriendlyGunner(400f, 400f, 0f));

		// Spawn enemy ships
//		enemyShips.add(new ShipEnemyGunner(100f, 100f, 0f));

		EnvironmentManager.initAsteroids();
	}

	public static void update(float delta){
		if(Mouse.isButtonDown(0)){
			new ProjectileEnemyGunner(HvlCursor.getCursorPosition().addNew(camera.getX(), camera.getY()).add(camera.getAlignment()), new HvlCoord2D(), 0);
		}
		
		// Attach ships to the player if they collide
		for(ShipFriendly ship : friendlyShips){
			if(!ship.physicsObject.hasParent()){
				if(player.connectedShips.contains(ship))
					player.connectedShips.remove(ship);
				
				PhysicsObject collisionObject = player.collidesWith(ship.physicsObject);
				if(collisionObject != null){
					player.connectedShips.add(ship);
					ship.physicsObject.connectToParent(collisionObject);
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
		friendlyShips.removeIf(s -> s.physicsObject.isDead());
		enemyShips.removeIf(s -> s.physicsObject.isDead());
		projectiles.removeIf(p -> p.physicsObject.isDead());
		EnvironmentManager.asteroids.removeIf(a -> a.physicsObject.isDead());

		camera.setPosition(player.getBaseLocation().x, player.getBaseLocation().y);
		camera.doTransform(new HvlAction0(){
			@Override
			public void run(){

				// TODO remove this when real scenery is added
				hvlDrawQuadc(0, 0, 500f, 500f, Color.darkGray);

				// Update and draw all idle ships
				for(ShipFriendly ship : friendlyShips){
					ship.update(delta, player);
					ship.draw(delta);
				}

				EnvironmentManager.update(delta);

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
