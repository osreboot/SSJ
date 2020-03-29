package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.merchant.ShipFriendlyGrenadier;
import com.hyprgloo.ssj.merchant.ShipFriendlyGunner;
import com.hyprgloo.ssj.merchant.ShipFriendlyTrader;
import com.hyprgloo.ssj.particle.ParticleSpark;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Player {

	public static final float ACCELERATION = 400f;
	public static final float ROTATION_ACCE = 300f;
	private static final float MAX_TRANSLATE = 250;

	private float xsInput, ysInput;
	private boolean done;
	public PhysicsObject physicsObject;
	public ArrayList<PhysicsObject> connectedShips;
	
	private float iconShieldUpdate, iconTurretUpdate, iconMissileUpdate;
	private int shieldCount, turretCount, missileCount;

	public Player() {
		physicsObject = new PhysicsObject(0f, 0f, 0f, 16f);
		physicsObject.alliance = Alliance.FRIENDLY;
		physicsObject.damage = 100f;
		physicsObject.health = 400f;
		xsInput = 0;
		ysInput = 0;
		connectedShips = new ArrayList<>();
		
		iconShieldUpdate = 0;
		iconTurretUpdate = 0;
		iconMissileUpdate = 0;
		
		shieldCount = 0;
		turretCount = 0;
		missileCount = 0;
		
		done = false;
	}

	public float progress;

	public void drawHUD() {
		
		if(!done)
			progress = HvlMath.distance(physicsObject.location.x, physicsObject.location.y, 0, 0) / Game.END_DISTANCE;

		if (progress >= 1) {
			progress = 1;
			done = true;
		}
		
		if (!Game.portalSpawned) {
		
			hvlDrawQuadc(140, 30, 240, 30, Main.getTexture(Main.INDEX_PROG_BAR));
			hvlDrawQuad(25, 20, 230 * progress, 20, Color.cyan);
			Main.font.drawWordc("Distance to Deep Space", 140, 30, Color.white, 0.1f);

		}
		
		if(Game.portalSpawned) {
			Main.font.drawWordc("Compass", 100, 30, Color.cyan, 0.12f);
			hvlDrawQuadc(100, 100, 105, 105, Main.getTexture(Main.INDEX_MENU_BUTT));
			hvlRotate(100, 100, (float) (180/Math.PI) * HvlMath.fullRadians(physicsObject.location, Game.p.loc));
			hvlDrawQuadc(50, 100, 40, 8, Main.getTexture(Main.INDEX_ARROW));
			hvlDrawQuadc(100, 100, 80, 3, Color.cyan);
			hvlResetRotation();
		}
		
		hvlDrawQuadc(140, Display.getHeight()-30, 240, 30, Main.getTexture(Main.INDEX_PROG_BAR));
		hvlDrawQuad(25, Display.getHeight()-40, physicsObject.health * 0.575f, 20, Color.red);
		if(physicsObject.health <= 0) physicsObject.health = 0;
		Main.font.drawWordc("Health", 140, Display.getHeight()-30, Color.white, 0.1f);
		
		hvlDrawQuadc(Display.getWidth() - 192f, Display.getHeight() - 40, 64 + (iconShieldUpdate * 32f), 64 + (iconShieldUpdate * 32f),
				Main.getTexture(Main.INDEX_ICON_SHIELD), new Color(1f, 1f, 1f, (iconShieldUpdate / 2f) + 0.5f));
		Main.font.drawWord("x" + shieldCount, Display.getWidth() - 192f, Display.getHeight() - 60, Color.white, 0.1f);
		hvlDrawQuadc(Display.getWidth() - 128f, Display.getHeight() - 40, 64 + (iconTurretUpdate * 32f), 64 + (iconTurretUpdate * 32f),
				Main.getTexture(Main.INDEX_ICON_TURRET), new Color(1f, 1f, 1f, (iconTurretUpdate / 2f) + 0.5f));
		Main.font.drawWord("x" + turretCount, Display.getWidth() - 128f, Display.getHeight() - 60, Color.white, 0.1f);
		hvlDrawQuadc(Display.getWidth() - 64f, Display.getHeight() - 40, 64 + (iconMissileUpdate * 32f), 64 + (iconMissileUpdate * 32f),
				Main.getTexture(Main.INDEX_ICON_MISSILE), new Color(1f, 1f, 1f, (iconMissileUpdate / 2f) + 0.5f));
		Main.font.drawWord("x" + missileCount, Display.getWidth() - 64f, Display.getHeight() - 60, Color.white, 0.1f);

	}

	public void update(float delta) {
		iconShieldUpdate = HvlMath.stepTowards(iconShieldUpdate, delta, 0f);
		iconTurretUpdate = HvlMath.stepTowards(iconTurretUpdate, delta, 0f);
		iconMissileUpdate = HvlMath.stepTowards(iconMissileUpdate, delta, 0f);
		
		int tickShieldCount = 0;
		int tickTurretCount = 0;
		int tickMissileCount = 0;
		for(ShipFriendly ship : EnvironmentManager.friendlyShips){
			if(ship.physicsObject.hasParent() && connectedShips.contains(ship.physicsObject)){
				if(ship instanceof ShipFriendlyTrader) tickShieldCount++;
				if(ship instanceof ShipFriendlyGunner) tickTurretCount++;
				if(ship instanceof ShipFriendlyGrenadier) tickMissileCount++;
			}
		}
		if(shieldCount != tickShieldCount){
			shieldCount = tickShieldCount;
			iconShieldUpdate = 1f;
		}
		if(turretCount != tickTurretCount){
			turretCount = tickTurretCount;
			iconTurretUpdate = 1f;
		}
		if(missileCount != tickMissileCount){
			missileCount = tickMissileCount;
			iconMissileUpdate = 1f;
		}
		
		xsInput = (Keyboard.isKeyDown(Keyboard.KEY_A) ? -ACCELERATION : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_D) ? ACCELERATION : 0);
		ysInput = (Keyboard.isKeyDown(Keyboard.KEY_W) ? -ACCELERATION : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_S) ? ACCELERATION : 0);

		if (physicsObject.speed.x >= MAX_TRANSLATE)
			physicsObject.speed.x = MAX_TRANSLATE;
		else if (physicsObject.speed.x <= -MAX_TRANSLATE)
			physicsObject.speed.x = -MAX_TRANSLATE;

		if (physicsObject.speed.y >= MAX_TRANSLATE)
			physicsObject.speed.y = MAX_TRANSLATE;
		else if (physicsObject.speed.y <= -MAX_TRANSLATE)
			physicsObject.speed.y = -MAX_TRANSLATE;

		if (!Keyboard.isKeyDown(Keyboard.KEY_A) || !Keyboard.isKeyDown(Keyboard.KEY_D))
			physicsObject.speed.x = HvlMath.stepTowards(physicsObject.speed.x, delta * ACCELERATION / 2, 0);
		if (!Keyboard.isKeyDown(Keyboard.KEY_W) || !Keyboard.isKeyDown(Keyboard.KEY_S))
			physicsObject.speed.y = HvlMath.stepTowards(physicsObject.speed.y, delta * ACCELERATION / 2, 0);

		physicsObject.speed.add(xsInput * delta, ysInput * delta);

		float angleInput = (Keyboard.isKeyDown(Keyboard.KEY_Q) ? -ROTATION_ACCE : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_E) ? ROTATION_ACCE : 0);

		if (!Keyboard.isKeyDown(Keyboard.KEY_Q) || !Keyboard.isKeyDown(Keyboard.KEY_E))
			physicsObject.angleSpeed = HvlMath.stepTowards(physicsObject.angleSpeed, delta * ACCELERATION / 5, 0);

		physicsObject.angleSpeed += angleInput * delta;

		physicsObject.update(delta);

		connectedShips.removeIf(p -> p.isDead());

	}

	public void draw(float delta) {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f,
				physicsObject.radius * 2f, Main.getTexture(Main.INDEX_PLAYER_SHIP),
				physicsObject.isDead() ? Color.darkGray : Color.white);
		hvlResetRotation();
	}

	public void drawEmissive(float delta) {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f,
				physicsObject.radius * 2f, Main.getTexture(Main.INDEX_PLAYER_SHIP_EMISSIVE));
		hvlResetRotation();
	}

	public void connectShip(PhysicsObject shipPhysicsObjectArg, PhysicsObject collisionObjectArg) {
		connectedShips.add(shipPhysicsObjectArg);
		shipPhysicsObjectArg.connectToParent(collisionObjectArg);
		if(Options.sound && !physicsObject.isDead()) Main.getSound(Main.INDEX_CLICK).playAsSoundEffect(HvlMath.randomFloatBetween(0.85f, 1f), 0.25f, false);
	}

	public void disconnectShip(PhysicsObject shipPhysicsObjectArg) {
		connectedShips.remove(shipPhysicsObjectArg);
		ArrayList<PhysicsObject> childrenCopy = new ArrayList<>(shipPhysicsObjectArg.getChildren());
		for (PhysicsObject child : childrenCopy) {
			disconnectShip(child);
		}
		shipPhysicsObjectArg.disconnectFromParent();
	}

	public boolean isShipConnected(PhysicsObject shipArg) {
		return connectedShips.contains(shipArg);
	}

	public PhysicsObject collidesWith(PhysicsObject physicsObjectArg) {
		if (physicsObject.collidesWith(physicsObjectArg))
			return physicsObject;
		for (PhysicsObject ship : connectedShips) {
			if (ship.collidesWith(physicsObjectArg))
				return ship;
		}
		return null;
	}

	public float distance(float xArg, float yArg) {
		float minimumDistance = HvlMath.distance(physicsObject.location.x, physicsObject.location.y, xArg, yArg)
				- physicsObject.radius;
		for (PhysicsObject ship : connectedShips) {
			float shipDistance = HvlMath.distance(ship.location.x, ship.location.y, xArg, yArg) - ship.radius;
			minimumDistance = Math.min(minimumDistance, shipDistance);
		}
		return minimumDistance;
	}

	public HvlCoord2D getBaseLocation() {
		return physicsObject.location;
	}

}
