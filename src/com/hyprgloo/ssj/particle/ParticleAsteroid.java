package com.hyprgloo.ssj.particle;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Particle;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class ParticleAsteroid extends Particle{
	public static final float DURATION_LIFETIME = 3f;
	public static final float SIZE_MIN = 16f;
	public static final float SIZE_MAX = 32f;
	public static final float SPEED_MIN = 50f;
	public static final float SPEED_MAX = 200f;
	public static final float SPEED_ROTATION = 500f;
	
	public static void createScrapExplosion(HvlCoord2D locationArg, float rad){
		for(int i = 0; i < rad/30; i++){
			float direction = HvlMath.randomInt(360);
			float speedMagnitude = HvlMath.randomFloatBetween(SPEED_MIN, SPEED_MAX);
			HvlCoord2D speed = new HvlCoord2D((float)Math.cos(Math.toRadians(direction)) * speedMagnitude, (float)Math.sin(Math.toRadians(direction)) * speedMagnitude);
			new ParticleAsteroid(new HvlCoord2D(locationArg), speed, new Color(1f,1f,1f,(float)Math.random()), rad);
		}
	}
	
	private Color color;
	private float angle, angleSpeed, size;
	
	public ParticleAsteroid(HvlCoord2D locationArg, HvlCoord2D speedArg, Color c, float rad){
		super(locationArg, speedArg, DURATION_LIFETIME, HvlMath.randomIntBetween(Main.INDEX_ASTEROID_FRAG_1, Main.INDEX_ASTEROID_FRAG_3+1));
		angle = HvlMath.randomInt(360);
		color = c;
		angleSpeed = HvlMath.randomFloatBetween(-SPEED_ROTATION, SPEED_ROTATION);
		size = HvlMath.randomFloatBetween(rad/4, rad/2);
	}

	@Override
	public float getDirection(){
		return angle;
	}

	@Override
	public Color getColor(){
		return new Color(color.r, color.g, color.b, (life / maxLife));
	}
	
	@Override
	public float getSize(){
		return size;
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		angle += angleSpeed * delta;
	}
}
