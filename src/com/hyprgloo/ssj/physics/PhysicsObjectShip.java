package com.hyprgloo.ssj.physics;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.PhysicsObject;
import com.hyprgloo.ssj.particle.ParticleScrap;
import com.hyprgloo.ssj.particle.ParticleSpark;

public class PhysicsObjectShip extends PhysicsObject{

	public Color[] scrapColors;
	
	public PhysicsObjectShip(float xArg, float yArg, float angleArg, float radiusArg, Color... scrapColorsArg){
		super(xArg, yArg, angleArg, radiusArg);
		scrapColors = scrapColorsArg;
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		super.onCollision(physicsObjectArg);
		if(isDead()){
			ParticleScrap.createScrapExplosion(location, false, scrapColors);
			ParticleSpark.createSparkExplosion(location, Color.yellow);
		}
	}

}
