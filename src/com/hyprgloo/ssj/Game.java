package com.hyprgloo.ssj;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlCamera2D;

public class Game {

	public static HvlCamera2D camera;

	public static ArrayList<PhysicsObject> physicsObjects;

	public static Player player;

	public static ArrayList<Projectile> projectiles;
	public static ArrayList<Particle> particles;

	public static float globalTimer = 0f;
	
	static boolean debugCam = false;

	public static void reset(){
		camera = new HvlCamera2D(0, 0, 0, debugCam ? 0.2f : 1f, HvlCamera2D.ALIGNMENT_CENTER);

		physicsObjects = new ArrayList<>();

		player = new Player();
		projectiles = new ArrayList<>();
		particles = new ArrayList<>();
		
		EnvironmentManager.init();
	}

	public static void update(float delta){
//		if(Mouse.isButtonDown(0)){
//			new ProjectileEnemyGunner(HvlCursor.getCursorPosition().addNew(camera.getX(), camera.getY()).add(camera.getAlignment()), new HvlCoord2D(), 0);
//		}
		
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

		for(Particle particle : particles)
			particle.update(delta);
		particles.removeIf(p -> p.isDead());
		
		// TODO check if player dies
	
		projectiles.removeIf(p -> p.physicsObject.isDead());

		ArtManager.drawBackground(player.getBaseLocation().x, player.getBaseLocation().y);
		
		if(debugCam) Main.font.drawWord("Diff: "+EnvironmentManager.closestChunk.difficultyLevel, 10, 10, Color.green, 0.3f);
		camera.setPosition(player.getBaseLocation().x / (debugCam ? 5 : 1), player.getBaseLocation().y / (debugCam ? 5 : 1));
		camera.doTransform(new HvlAction0(){
			@Override
			public void run(){
				
				for(Particle particle : particles)
					particle.draw(delta);
				
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
		
		ArtManager.drawVignette();
		
		ArtManager.blurFrame.doCapture(new HvlAction0(){
			@Override
			public void run(){
				camera.doTransform(new HvlAction0(){
					@Override
					public void run(){
						
						for(Particle particle : particles)
							particle.drawEmissive(delta);
						
						player.drawEmissive(delta);
						
						for(Projectile projectile : projectiles){
							projectile.drawEmissive(delta);
						}
						
						// TODO others
						
					}
				});
			}
		});
		
		ArtManager.drawEmissive();
		
		globalTimer += delta;
	}
}
