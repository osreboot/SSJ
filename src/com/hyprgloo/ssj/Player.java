package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;

import org.lwjgl.input.Keyboard;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Player {

	public static final float ACCELERATION = 100f;
	public static final float SPEED_ROTATION = 100f;
	private static final float MAX_TRANSLATE = 100;

	private PhysicsObject physicsObject;

	private ArrayList<ShipFriendly> connectedShips;

	public Player() {
		physicsObject = new PhysicsObject(0f, 0f, 0f, 16f);

		connectedShips = new ArrayList<>();
	}

	public void update(float delta) {
		float xsInput = (Keyboard.isKeyDown(Keyboard.KEY_A) ? -ACCELERATION : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_D) ? ACCELERATION : 0);
		float ysInput = (Keyboard.isKeyDown(Keyboard.KEY_W) ? -ACCELERATION : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_S) ? ACCELERATION : 0);

		physicsObject.speed.add(xsInput * delta, ysInput * delta);

		if (physicsObject.speed.x >= MAX_TRANSLATE)
			physicsObject.speed.x = MAX_TRANSLATE;
		else if (physicsObject.speed.x <= -MAX_TRANSLATE)
			physicsObject.speed.x = -MAX_TRANSLATE;
		
		if (physicsObject.speed.y >= MAX_TRANSLATE)
			physicsObject.speed.y = MAX_TRANSLATE;
		else if (physicsObject.speed.y <= -MAX_TRANSLATE)
			physicsObject.speed.y = -MAX_TRANSLATE;

		if (!Keyboard.isKeyDown(Keyboard.KEY_A) || !Keyboard.isKeyDown(Keyboard.KEY_D))
			physicsObject.speed.x = HvlMath.stepTowards(physicsObject.speed.x, ACCELERATION / 500, 0);
		if (!Keyboard.isKeyDown(Keyboard.KEY_W) || !Keyboard.isKeyDown(Keyboard.KEY_S))
			physicsObject.speed.y = HvlMath.stepTowards(physicsObject.speed.y, ACCELERATION / 500, 0);

		float angleInput = (Keyboard.isKeyDown(Keyboard.KEY_Q) ? -SPEED_ROTATION : 0)
				+ (Keyboard.isKeyDown(Keyboard.KEY_E) ? SPEED_ROTATION : 0);
		physicsObject.angleSpeed += angleInput * delta;
		
		if (!Keyboard.isKeyDown(Keyboard.KEY_Q) || !Keyboard.isKeyDown(Keyboard.KEY_E))
			physicsObject.angleSpeed = HvlMath.stepTowards(physicsObject.angleSpeed, ACCELERATION / 500, 0);

		physicsObject.update(delta);

		for (ShipFriendly ship : connectedShips)
			ship.update(delta);
	}

	public void draw(float delta) {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f,
				physicsObject.radius * 2f, Main.getTexture(Main.INDEX_PLAYER_SHIP));
		hvlResetRotation();

		for (ShipFriendly ship : connectedShips)
			ship.draw(delta);
	}

	public void connectShip(ShipFriendly shipArg) {
		connectedShips.add(shipArg);
		shipArg.physicsObject.connectToParent(physicsObject);
	}

	public boolean collidesWith(PhysicsObject physicsObjectArg) {
		if (physicsObject.collidesWith(physicsObjectArg))
			return true;
		for (ShipFriendly ship : connectedShips) {
			if (ship.physicsObject.collidesWith(physicsObjectArg))
				return true;
		}
		return false;
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

	public int getConnectedShips() {
		return connectedShips.size();
	}

}
