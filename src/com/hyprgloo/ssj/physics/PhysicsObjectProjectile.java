package com.hyprgloo.ssj.physics;

import com.hyprgloo.ssj.PhysicsObject;

public class PhysicsObjectProjectile extends PhysicsObject{

	public PhysicsObjectProjectile(float xArg, float yArg, float angleArg, float radiusArg){
		super(xArg, yArg, angleArg, radiusArg);
		isSolid = false;
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		if(!(physicsObjectArg instanceof PhysicsObjectProjectile)) hurt(100f);
	}

}
