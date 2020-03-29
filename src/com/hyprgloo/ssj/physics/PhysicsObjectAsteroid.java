package com.hyprgloo.ssj.physics;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.PhysicsObject;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObjectAsteroid extends PhysicsObject{
	
	public PhysicsObjectAsteroid(float xArg, float yArg, float angleArg, float radiusArg){
		super(xArg, yArg, angleArg, radiusArg);
	}
	
	@Override
	public void onCollision(PhysicsObject physicsObjectArg){
		super.onCollision(physicsObjectArg);
		if(isDead()){
			if(HvlMath.distance(location, Game.player.getBaseLocation()) < 500) {
				Main.getSound(Main.INDEX_CRASH).playAsSoundEffect(HvlMath.randomFloatBetween(0.8f, 1f), 0.5f/((HvlMath.distance(location, Game.player.getBaseLocation())/30f)), false);
			}
			
			// TODO particles
		}
	}

}
