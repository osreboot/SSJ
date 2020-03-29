package com.hyprgloo.ssj.physics;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Options;
import com.hyprgloo.ssj.PhysicsObject;
import com.hyprgloo.ssj.particle.ParticleAsteroid;
import com.hyprgloo.ssj.particle.ParticleScrap;
import com.hyprgloo.ssj.particle.ParticleSpark;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObjectAsteroid extends PhysicsObject{
	
	private float volume;
	private float rad;
	
	public PhysicsObjectAsteroid(float xArg, float yArg, float angleArg, float radiusArg){
		super(xArg, yArg, angleArg, radiusArg);
		this.rad = radiusArg;
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		super.onCollision(physicsObjectArg);
		if(isDead()){
			volume = 1f/((HvlMath.distance(location, Game.player.getBaseLocation())/100f));
			if(volume > 0.8f) volume = 0.8f;
			if(HvlMath.distance(location, Game.player.getBaseLocation()) < 500) {
				if(Options.sound) Main.getSound(Main.INDEX_ASTEROID_BOOM).playAsSoundEffect(HvlMath.randomFloatBetween(0.45f, 0.6f), volume, false);
			}
			
			ParticleAsteroid.createScrapExplosion(location, this.rad);
			ParticleSpark.createSparkExplosion(location, Color.red);
		}
	}

}
