package com.hyprgloo.ssj.physics;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.PhysicsObject;
import com.hyprgloo.ssj.particle.ParticleSpark;

public class PhysicsObjectProjectile extends PhysicsObject{

	public Color sparkColor;
	
	public PhysicsObjectProjectile(float xArg, float yArg, float angleArg, float radiusArg, Color sparkColorArg){
		super(xArg, yArg, angleArg, radiusArg);
		isSolid = false;
		sparkColor = sparkColorArg;
	}
	
//	@Override
//	public void update(float delta){
//		if(isDead())
//			ParticleSpark.createSparkExplosion(location, sparkColor);
//	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		if(!(physicsObjectArg instanceof PhysicsObjectProjectile)){
			hurt(100f);
			ParticleSpark.createSparkExplosion(location, sparkColor);
		}
	}

}
