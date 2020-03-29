package com.hyprgloo.ssj.physics;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Options;
import com.hyprgloo.ssj.PhysicsObject;
import com.hyprgloo.ssj.Player;
import com.hyprgloo.ssj.particle.ParticleScrap;
import com.hyprgloo.ssj.particle.ParticleSpark;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObjectShip extends PhysicsObject{

	public Color[] scrapColors;
	public boolean bigExplosion;
	private float volume;
	
	public PhysicsObjectShip(float xArg, float yArg, float angleArg, float radiusArg, boolean bigExplosionArg, Color... scrapColorsArg){
		super(xArg, yArg, angleArg, radiusArg);
		bigExplosion = bigExplosionArg;
		scrapColors = scrapColorsArg;
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		super.onCollision(physicsObjectArg);
		if(isDead()){
			volume = 0.5f/((HvlMath.distance(location, Game.player.getBaseLocation())/30f));
			if(volume > 0.45f) volume = 0.45f;
			if(HvlMath.distance(location, Game.player.getBaseLocation()) < 500) {
				if(Options.sound)Main.getSound(Main.INDEX_CRASH).playAsSoundEffect(HvlMath.randomFloatBetween(0.8f, 1f), volume, false);
			}
			
			ParticleScrap.createScrapExplosion(location, bigExplosion, scrapColors);
			ParticleSpark.createSparkExplosion(location, Color.yellow);
		}
	}

}
