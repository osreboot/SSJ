package com.hyprgloo.ssj;

import com.hyprgloo.ssj.physics.PhysicsObjectProjectile;
import com.osreboot.ridhvl.HvlCoord2D;

public abstract class Projectile {

	public PhysicsObjectProjectile physicsObject;
	
	public boolean friendly;
	
	public Projectile(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg, float radiusArg, boolean friendlyArg){
		physicsObject = new PhysicsObjectProjectile(locationArg.x, locationArg.y, rotationArg, radiusArg);
		physicsObject.speed = speedArg;
		friendly = friendlyArg;
		
		Game.projectiles.add(this);
	}
	
	public abstract void update(float delta);
	
	public abstract void draw(float delta);
	
}
