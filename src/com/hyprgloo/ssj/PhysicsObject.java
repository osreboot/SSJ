package com.hyprgloo.ssj;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObject {

	private static final float MAX_ROTATE = 100;

	public static enum Alliance{
		NEUTRAL, FRIENDLY, ENEMY
	}
	
	public HvlCoord2D location, speed;
	public float angleSpeed, radius;
	
	private float angle;

	private PhysicsObject parent;
	private float connectionDistance, connectionAngle;
	
	public Alliance alliance;
	public float damage;
	public boolean canDealDamage = true, canReceiveDamage = true, isSolid = true;
	private float health;

	public PhysicsObject(float xArg, float yArg, float angleArg, float radiusArg){
		location = new HvlCoord2D(xArg, yArg);
		speed = new HvlCoord2D();
		angle = angleArg;
		radius = radiusArg;
		health = 100f;
		alliance = Alliance.NEUTRAL;
		
		Game.physicsObjects.add(this);
	}

	public void connectToParent(PhysicsObject parentArg){
		if(!hasParent()){
			parent = parentArg;
			connectionDistance = HvlMath.distance(parent.location, location);
			connectionAngle = (float)Math.toDegrees(HvlMath.fullRadians(location, parent.location)) - parent.getVisualAngle();
			speed.x = 0;
			speed.y = 0;
			angleSpeed = 0;
			angle = -parent.getVisualAngle() - connectionAngle + angle;
		}
	}

	public void disconnectFromParent(){
		if(hasParent()){
			angle = getVisualAngle();
			parent = null;
		}
	}

	public boolean hasParent(){
		return parent != null;
	}

	public float getVisualAngle(){
		if(hasParent())
			return angle + parent.getVisualAngle() + connectionAngle;
		else return angle;
	}
	
	public void setBaseAngle(float angleArg){
		angle = angleArg;
	}

	public void update(float delta){
		if(hasParent()){
			speed.x = 0;
			speed.y = 0;
			angleSpeed = 0;
			location.x = parent.location.x + ((float)Math.cos(Math.toRadians(parent.getVisualAngle() + connectionAngle)) * connectionDistance);
			location.y = parent.location.y + ((float)Math.sin(Math.toRadians(parent.getVisualAngle() + connectionAngle)) * connectionDistance);
		}else{
			location.add(speed.multNew(delta));
			angle += angleSpeed * delta;
			if(angleSpeed >= MAX_ROTATE) angleSpeed = MAX_ROTATE;
			else if(angleSpeed <= -MAX_ROTATE) angleSpeed = -MAX_ROTATE;
		}
	}

	public boolean collidesWith(float xArg, float yArg, float radiusArg){
		return HvlMath.distance(location.x, location.y, xArg, yArg) < radius + radiusArg;
	}
	
	public boolean collidesWith(PhysicsObject physicsObjectArg){
		return collidesWith(physicsObjectArg.location.x, physicsObjectArg.location.y, physicsObjectArg.radius);
	}
	
	public boolean collidesWith(float xArg, float yArg){
		return collidesWith(xArg, yArg, 0f);
	}
	
	public boolean isDead(){
		return health <= 0f;
	}
	
	public void hurt(float damageArg){
		health -= damageArg;
	}
	
	public void onDeath(){}
	
	public void onCollision(PhysicsObject physicsObjectArg){
		hurt(physicsObjectArg.damage);
	}

}
