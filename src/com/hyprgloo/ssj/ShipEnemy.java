package com.hyprgloo.ssj;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public abstract class ShipEnemy {

	public static final float ACCELERATION_TARGET = 200f;
	public static final float SPEED_MAXIMUM = 100f;
	public static final float DISTANCE_TARGET_RESET = 100f;
	public static final int MAX_TARGET_RETRIES = 10;

	public PhysicsObject physicsObject;

	private HvlCoord2D target;
	private float targetAcquisitionMax, radiusOrbitMin, radiusOrbitMax;

	public ShipEnemy(float xArg, float yArg, float angleArg, float radiusArg, float targetAcquisitionMaxArg, float radiusOrbitMinArg, float radiusOrbitMaxArg){
		physicsObject = new PhysicsObject(xArg, yArg, angleArg, radiusArg);
		targetAcquisitionMax = targetAcquisitionMaxArg;
		radiusOrbitMin = radiusOrbitMinArg;
		radiusOrbitMax = radiusOrbitMaxArg;
	}

	public void update(float delta, Player playerArg){
		if(target != null && HvlMath.distance(physicsObject.location, target) < DISTANCE_TARGET_RESET)
			target = null;
		if(target == null){
			for(int i = 0; i < MAX_TARGET_RETRIES; i++){
				HvlCoord2D newTarget = HvlMath.randomPointInCircle(targetAcquisitionMax).add(physicsObject.location);
				float distance = HvlMath.distance(Game.player.getBaseLocation(), newTarget);
				if(distance >= radiusOrbitMin && distance <= radiusOrbitMax){
					target = newTarget;
					break;
				}
			}
		}

		if(target != null){
			HvlCoord2D accelerationTarget = target.subtractNew(physicsObject.location).normalize().mult(ACCELERATION_TARGET);
			physicsObject.speed.add(accelerationTarget.mult(delta));
//			hvlDrawQuadc(target.x, target.y, 20f, 20f, Color.yellow);
			physicsObject.speed.x = HvlMath.limit(physicsObject.speed.x, -SPEED_MAXIMUM, SPEED_MAXIMUM);
			physicsObject.speed.y = HvlMath.limit(physicsObject.speed.y, -SPEED_MAXIMUM, SPEED_MAXIMUM);
		}else{
			physicsObject.speed.x = HvlMath.stepTowards(physicsObject.speed.x, delta * ACCELERATION_TARGET, 0);
			physicsObject.speed.y = HvlMath.stepTowards(physicsObject.speed.y, delta * ACCELERATION_TARGET, 0);
		}
		physicsObject.update(delta);
	}

	public abstract void draw(float delta);

}
