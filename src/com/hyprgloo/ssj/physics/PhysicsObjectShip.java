package com.hyprgloo.ssj.physics;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.PhysicsObject;
import com.hyprgloo.ssj.particle.ParticleScrap;
import com.hyprgloo.ssj.particle.ParticleSpark;

public class PhysicsObjectShip extends PhysicsObject{

	public Color[] scrapColors;
	public boolean bigExplosion;
	
	public PhysicsObjectShip(float xArg, float yArg, float angleArg, float radiusArg, boolean bigExplosionArg, Color... scrapColorsArg){
		super(xArg, yArg, angleArg, radiusArg);
		bigExplosion = bigExplosionArg;
		scrapColors = scrapColorsArg;
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		super.onCollision(physicsObjectArg);
		if(isDead()){
			ParticleScrap.createScrapExplosion(location, bigExplosion, scrapColors);
			ParticleSpark.createSparkExplosion(location, Color.yellow);
		}
	}

}
