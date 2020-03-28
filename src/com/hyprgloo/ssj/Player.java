package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Player {

	public static final float ACCELERATION = 400f;
	public static final float ROTATION_ACCE = 300f;
	private static final float MAX_TRANSLATE = 250;
	
	private float xsInput, ysInput;
	public PhysicsObject physicsObject;

	public ArrayList<ShipFriendly> connectedShips;

	public Player() {
		physicsObject = new PhysicsObject(0f, 0f, 0f, 16f);
		physicsObject.alliance = Alliance.FRIENDLY;
		physicsObject.damage = 100f;
		xsInput = 0;
		ysInput = 0;
		connectedShips = new ArrayList<>();
	}

	public void update(float delta) {
		xsInput = (Keyboard.isKeyDown(Keyboard.KEY_A) ? -ACCELERATION/getConnectedShipsWeight() : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_D) ? ACCELERATION/getConnectedShipsWeight() : 0);
		ysInput = (Keyboard.isKeyDown(Keyboard.KEY_W) ? -ACCELERATION/getConnectedShipsWeight() : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_S) ? ACCELERATION/getConnectedShipsWeight() : 0);

		if (physicsObject.speed.x >= MAX_TRANSLATE)
			physicsObject.speed.x = MAX_TRANSLATE;
		else if (physicsObject.speed.x <= -MAX_TRANSLATE)
			physicsObject.speed.x = -MAX_TRANSLATE;
		
		if (physicsObject.speed.y >= MAX_TRANSLATE)
			physicsObject.speed.y = MAX_TRANSLATE;
		else if (physicsObject.speed.y <= -MAX_TRANSLATE)
			physicsObject.speed.y = -MAX_TRANSLATE;

		if (!Keyboard.isKeyDown(Keyboard.KEY_A) || !Keyboard.isKeyDown(Keyboard.KEY_D))
			physicsObject.speed.x = HvlMath.stepTowards(physicsObject.speed.x, ACCELERATION / (250+(connectedShips.size()*10)), 0);
		if (!Keyboard.isKeyDown(Keyboard.KEY_W) || !Keyboard.isKeyDown(Keyboard.KEY_S))
			physicsObject.speed.y = HvlMath.stepTowards(physicsObject.speed.y, ACCELERATION / (250+(connectedShips.size()*10)), 0);
		
		physicsObject.speed.add(xsInput * delta, ysInput * delta);

		float angleInput = (Keyboard.isKeyDown(Keyboard.KEY_Q) ? -ROTATION_ACCE/getConnectedShipsWeight() : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_E) ? ROTATION_ACCE/getConnectedShipsWeight() : 0);
	
		if (!Keyboard.isKeyDown(Keyboard.KEY_Q) || !Keyboard.isKeyDown(Keyboard.KEY_E))
			physicsObject.angleSpeed = HvlMath.stepTowards(physicsObject.angleSpeed, ACCELERATION / (500+(connectedShips.size()*5)), 0);

		physicsObject.angleSpeed += angleInput * delta;
		
		physicsObject.update(delta);
		
		connectedShips.removeIf(s -> s.physicsObject.isDead());
	
	}

	public void draw(float delta) {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f,
				physicsObject.radius * 2f, Main.getTexture(Main.INDEX_PLAYER_SHIP), physicsObject.isDead() ? Color.darkGray : Color.white);
		hvlResetRotation();
	}

//	public void connectShip(ShipFriendly shipArg) {
//		connectedShips.add(shipArg);
//		shipArg.physicsObject.connectToParent(physicsObject);
//	}

	public PhysicsObject collidesWith(PhysicsObject physicsObjectArg) {
		if (physicsObject.collidesWith(physicsObjectArg))
			return physicsObject;
		for (ShipFriendly ship : connectedShips) {
			if (ship.physicsObject.collidesWith(physicsObjectArg))
				return ship.physicsObject;
		}
		return null;
	}

	public float distance(float xArg, float yArg) {
		float minimumDistance = HvlMath.distance(physicsObject.location.x, physicsObject.location.y, xArg, yArg)
				- physicsObject.radius;
		for (ShipFriendly ship : connectedShips) {
			float shipDistance = HvlMath.distance(ship.physicsObject.location.x, ship.physicsObject.location.y, xArg,
					yArg) - ship.physicsObject.radius;
			minimumDistance = Math.min(minimumDistance, shipDistance);
		}
		return minimumDistance;
	}

	public HvlCoord2D getBaseLocation() {
		return physicsObject.location;
	}

	public float getConnectedShipsWeight() {
		return ((connectedShips.size()*0.003f) + 1);
	}

}
