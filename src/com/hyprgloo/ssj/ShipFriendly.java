package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.osreboot.ridhvl.HvlMath;

public class ShipFriendly {

	public static final float DISTANCE_TRIGGER_BOOST = 100f;
	public static final float SPEED_BOOST = 100f;
	
	public PhysicsObject physicsObject;
	
	private boolean boosted = false;
	
	public ShipFriendly(float xArg, float yArg, float angleArg, float radiusArg){
		physicsObject = new PhysicsObject(xArg, yArg, angleArg, radiusArg);
	}
	
	public void update(float delta, Player playerArg){
		if(playerArg != null){
			physicsObject.setBaseAngle((float)Math.toDegrees(HvlMath.fullRadians(physicsObject.location, playerArg.getBaseLocation())) - 90f);
			
			if(!boosted && playerArg.distance(physicsObject.location.x, physicsObject.location.y) < DISTANCE_TRIGGER_BOOST){
				physicsObject.speed = playerArg.getBaseLocation().subtractNew(physicsObject.location).normalize().mult(SPEED_BOOST);
				boosted = true;
			}
		}
		
		physicsObject.update(delta);
	}
	
	public void update(float delta){
		update(delta, null);
	}
	
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_SHIP_0));
		hvlResetRotation();
	}
	
}
