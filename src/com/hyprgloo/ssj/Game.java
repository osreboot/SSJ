package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.ssj.physics.PhysicsObjectShip;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.action.HvlAction0;
import com.osreboot.ridhvl.menu.HvlMenu;
import com.osreboot.ridhvl.painter.HvlCamera2D;


class Portal {
	HvlCoord2D loc;
	float rad = 0f;
	public Portal(HvlCoord2D loc) {
		this.loc = loc;
	}
	
	public void draw(float delta) {
		for(PhysicsObject p : Game.physicsObjects) {
			if(HvlMath.distance(p.location, this.loc) < 10000 && p != Game.player.physicsObject && !(p instanceof PhysicsObjectShip && p.hasParent())) {
				p.location.x = HvlMath.stepTowards(p.location.x, (200000/HvlMath.distance(p.location, this.loc))*delta, this.loc.x);
				p.location.y = HvlMath.stepTowards(p.location.y, (200000/HvlMath.distance(p.location, this.loc))*delta, this.loc.y);
				if(HvlMath.distance(p.location, this.loc) < 200)
					p.health = 0;
			} else if (p == Game.player.physicsObject && HvlMath.distance(p.location, this.loc) < 1000) {
				p.location.x = HvlMath.stepTowards(p.location.x, (200000/HvlMath.distance(p.location, this.loc))*delta, this.loc.x);
				p.location.y = HvlMath.stepTowards(p.location.y, (200000/HvlMath.distance(p.location, this.loc))*delta, this.loc.y);
				for(PhysicsObject f : Game.physicsObjects) {
					f.canDealDamage = false;
					f.canReceiveDamage = false;
				}
				if(HvlMath.distance(p.location, this.loc) < 50) {
					MenuManager.win = true;
					if(Options.sound)Main.getSound(Main.INDEX_ESCAPE).playAsSoundEffect(0.9f, 0.2f, false);
					HvlMenu.setCurrent(MenuManager.end);
				}
			}
		}
		rad += 500 * delta;
		hvlRotate(this.loc, rad);
		hvlDrawQuadc(loc.x, loc.y, 300, 300, Color.cyan);
		hvlResetRotation();
	}	
}

public class Game {

	public static final int END_DISTANCE = 25000;

	public static HvlCamera2D camera;

	public static ArrayList<PhysicsObject> physicsObjects;

	public static Player player;

	public static ArrayList<Projectile> projectiles;
	public static ArrayList<Particle> particles;
	
	public static boolean playedFriendlyGunnerSound;

	public static float globalTimer = 0f;
	public static float messageTimer = 0f;

	static boolean debugCam = false;
	
	public static float whiteFade = 1f;

	public static void reset() {
		camera = new HvlCamera2D(0, 0, 0, debugCam ? 0.2f : 1f, HvlCamera2D.ALIGNMENT_CENTER);

		physicsObjects = new ArrayList<>();

		player = new Player();
		projectiles = new ArrayList<>();
		particles = new ArrayList<>();

		playedFriendlyGunnerSound = false;
		
		EnvironmentManager.init();
		
		p = null;
		portalSpawned = false;
		
		globalTimer = 0f;
		portalCount = 0f;
		
		Tutorial.gameReset();
	}

	
	public static boolean portalSpawned = false;
	public static Portal p;
	public static float portalCount = 0f;
	public static void spawnPortal(float delta) {
		
		float angle = HvlMath.randomFloatBetween(-3.14f, 3.14f);
		float x = (float) (6000 * Math.cos(angle));
		float y = (float) (6000 * Math.sin(angle));
					
		portalCount += delta;
		
		if (!portalSpawned && portalCount > 10f) {
			p = new Portal(new HvlCoord2D(x, y));
			portalSpawned = true;
		}
	}

	public static void update(float delta) {
		// if(Mouse.isButtonDown(0)){
		// new ProjectileEnemyGunner(HvlCursor.getCursorPosition().addNew(camera.getX(),
		// camera.getY()).add(camera.getAlignment()), new HvlCoord2D(), 0);
		// }
		playedFriendlyGunnerSound = false;

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

		if(player.physicsObject.health <= 0) {
			MenuManager.win = false;
			whiteFade = 1f;			
			HvlMenu.setCurrent(MenuManager.end);
		}
		projectiles.removeIf(p -> p.physicsObject.isDead());

		MenuManager.pauseFrame.doCapture(true, new HvlAction0(){
			@Override
			public void run(){
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
							p.draw(delta);
						}
					}
				});
				player.drawHUD(delta);
			}
		});
		hvlDrawQuad(0, 0, Display.getWidth(), Display.getHeight(), MenuManager.pauseFrame);

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

		Tutorial.update(delta);
		
		globalTimer += delta;
	}
}



