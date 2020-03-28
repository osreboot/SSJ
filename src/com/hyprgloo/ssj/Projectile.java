package com.hyprgloo.ssj;

import com.osreboot.ridhvl.HvlCoord2D;

public abstract class Projectile {

	public PhysicsObject physicsObject;
	
	public boolean friendly;
	
	public Projectile(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg, float radiusArg, boolean friendlyArg){
		physicsObject = new PhysicsObject(locationArg.x, locationArg.y, rotationArg, radiusArg);
		physicsObject.speed = speedArg;
		friendly = friendlyArg;
		
		Game.projectiles.add(this);
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(float delta);
	
}
