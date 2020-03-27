package com.hyprgloo.ssj;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObject {
	
	public HvlCoord2D speed;
	
	private HvlCoord2D location;
	private float angle, radius;
	
	private PhysicsObject parent;
	private float parentDistance, parentAngle;
	
	public PhysicsObject(float xArg, float yArg, float angleArg, float radiusArg){
		location = new HvlCoord2D(xArg, yArg);
		speed = new HvlCoord2D();
		angle = angleArg;
		radius = radiusArg;
	}
	
	public void connectToParent(PhysicsObject parentArg){
		parent = parentArg;
		parentDistance = HvlMath.distance(parent.getPosition(), getPosition());
		parentAngle = HvlMath.fullRadians(parent.getPosition(), getPosition());
	}
	
	public void disconnectFromParent(){
		parent = null;
	}
	
	public boolean hasParent(){
		return parent != null;
	}
	
	public HvlCoord2D getPosition(){
		if(hasParent()){
			float x = parent.getPosition().x + ((float)Math.cos(Math.toRadians(parent.getAngle() + parentAngle)) * parentDistance);
			float y = parent.getPosition().y + ((float)Math.sin(Math.toRadians(parent.getAngle() + parentAngle)) * parentDistance);
			location = new HvlCoord2D(x, y);
			return location;
		}else return location;
	}
	
	public float getAngle(){
		if(hasParent()){
			return parent.getAngle() + angle;
		}else return angle;
	}
	
	public void update(float delta){
		location.add(speed.multNew(delta));
	}
	
	public float getRadius(){
		return radius;
	}
	
}
