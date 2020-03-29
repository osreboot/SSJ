package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawLine;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.painter.HvlCamera2D;


class Portal {
	HvlCoord2D loc;
	public Portal(HvlCoord2D loc) {
		this.loc = loc;
	}
	
	public void draw() {
		hvlDrawQuadc(loc.x, loc.y, 300, 300, Color.cyan);
	}	
}

public class Game {

	public static final int END_DISTANCE = 1000;

	public static HvlCamera2D camera;

	public static ArrayList<PhysicsObject> physicsObjects;

	public static Player player;

	public static ArrayList<Projectile> projectiles;
	public static ArrayList<Particle> particles;

	public static float globalTimer = 0f;
	public static float messageTimer = 0f;

	static boolean debugCam = false;

	public static void reset() {
		camera = new HvlCamera2D(0, 0, 0, debugCam ? 0.2f : 1f, HvlCamera2D.ALIGNMENT_CENTER);

		physicsObjects = new ArrayList<>();

		player = new Player();
		projectiles = new ArrayList<>();
		particles = new ArrayList<>();

		EnvironmentManager.init();
	}

	
	public static boolean portalSpawned = false;
	public static Portal p;
	public static void story(int stage) {
		if (stage == 0) {
			if (globalTimer < 10f) {
				float alpha = 1f - (Math.abs(globalTimer / 5 - 0.5f));
				Main.font.drawWordc(
						"A signal from deep space has been trying to reach you,"
								+ "\n          but the dense asteroid field is interfering!"
								+ "\n                          Get to deep space!",
						Display.getWidth() / 2, Display.getHeight() / 2 + 150, new Color(1f, 1f, 1f, alpha), 0.18f);
			}
		}

		if (stage == 1) {
			if (player.progress < 1)
				messageTimer = globalTimer + 10;
			else {
				float alpha = 1f - (Math.abs((messageTimer - globalTimer) / 5 - 0.5f));
				Main.font.drawWordc(
				  "     Message Received!,"
				+ "\nLooks like some coordinates..."
				+ "\n      Let's check it out.",
						Display.getWidth() / 2, Display.getHeight() / 2 + 150, new Color(1f, 1f, 1f, alpha), 0.18f);
				
				if(messageTimer - globalTimer < 0f) {
					float angle = HvlMath.randomFloatBetween(-3.14f, 3.14f);
					float x = (float) (2000 * Math.cos(angle));
					float y = (float) (2000 * Math.sin(angle));
					
					if(!portalSpawned) {
						p = new Portal(new HvlCoord2D(x,y));
						portalSpawned = true;
					}
					
					stage = 2;
				}
			}
		}
	}

	public static void update(float delta) {
		// if(Mouse.isButtonDown(0)){
		// new ProjectileEnemyGunner(HvlCursor.getCursorPosition().addNew(camera.getX(),
		// camera.getY()).add(camera.getAlignment()), new HvlCoord2D(), 0);
		// }

		// Attach ships to the player if they collide
		for (ShipFriendly ship : EnvironmentManager.friendlyShips) {
			if (!ship.physicsObject.hasParent()) {
				if (player.isShipConnected(ship.physicsObject)) {
					player.disconnectShip(ship.physicsObject);
				}

				PhysicsObject collisionObject = player.collidesWith(ship.physicsObject);
				if (collisionObject != null) {
					player.connectShip(ship.physicsObject, collisionObject);
				}
			}
		}

		// Dealing damage across all entities
		physicsObjects.removeIf(p -> p.isDead());
		for (PhysicsObject physicsObjectCollidee : physicsObjects) {
			if (physicsObjectCollidee.canReceiveDamage) {
				for (PhysicsObject physicsObjectCollider : physicsObjects) {
					if (physicsObjectCollider.canDealDamage) {
						if (physicsObjectCollidee != physicsObjectCollider
								&& physicsObjectCollidee.alliance != physicsObjectCollider.alliance
								&& physicsObjectCollidee.collidesWith(physicsObjectCollider)) {
							physicsObjectCollidee.onCollision(physicsObjectCollider);
							if (physicsObjectCollidee.isDead()) {
								physicsObjectCollidee.onDeath();
							}
						}
					}
				}
			}
		}
		physicsObjects.removeIf(p -> p.isDead());

		for (Particle particle : particles)
			particle.update(delta);
		particles.removeIf(p -> p.isDead());

		// TODO check if player dies

		projectiles.removeIf(p -> p.physicsObject.isDead());

		ArtManager.drawBackground(player.getBaseLocation().x, player.getBaseLocation().y);

		if (debugCam)
			Main.font.drawWord("Diff: " + EnvironmentManager.closestChunk.difficultyLevel, 10, 10, Color.green, 0.3f);
		camera.setPosition(player.getBaseLocation().x / (debugCam ? 5 : 1),
				player.getBaseLocation().y / (debugCam ? 5 : 1));
		camera.doTransform(new HvlAction0() {
			@Override
			public void run() {

				for (Particle particle : particles)
					particle.draw(delta);

				EnvironmentManager.update(delta);

				// Update and draw the player
				player.update(delta);
				player.draw(delta);

				for (Projectile projectile : projectiles) {
					projectile.update(delta);
					projectile.draw(delta);
				}
				
				if(portalSpawned) {
					p.draw();
				}
			}
		});
		player.drawHUD();
		
		if(globalTimer < 15f)
			story(0);
		else
			story(1);

		ArtManager.drawVignette();

		ArtManager.blurFrame.doCapture(new HvlAction0() {
			@Override
			public void run() {
				camera.doTransform(new HvlAction0() {
					@Override
					public void run() {

						for (Particle particle : particles)
							particle.drawEmissive(delta);
						
						EnvironmentManager.drawEmissive(delta);
	
						player.drawEmissive(delta);

						for (Projectile projectile : projectiles) {
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



