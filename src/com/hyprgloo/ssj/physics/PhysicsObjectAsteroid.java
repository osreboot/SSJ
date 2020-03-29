package com.hyprgloo.ssj.physics;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.PhysicsObject;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObjectAsteroid extends PhysicsObject{
	
	private float volume;
	
	public PhysicsObjectAsteroid(float xArg, float yArg, float angleArg, float radiusArg){
		super(xArg, yArg, angleArg, radiusArg);
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		super.onCollision(physicsObjectArg);
		if(isDead()){
			volume = 1f/((HvlMath.distance(location, Game.player.getBaseLocation())/100f));
			if(volume > 0.8f) volume = 0.8f;
			if(HvlMath.distance(location, Game.player.getBaseLocation()) < 500) {
				Main.getSound(Main.INDEX_ASTEROID_BOOM).playAsSoundEffect(HvlMath.randomFloatBetween(0.6f, 0.75f), volume, false);
			}
			
			// TODO particles
		}
	}

}
