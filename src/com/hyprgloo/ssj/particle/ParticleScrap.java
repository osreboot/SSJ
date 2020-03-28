package com.hyprgloo.ssj.particle;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Particle;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class ParticleScrap extends Particle{

	public static final float DURATION_LIFETIME = 3f;
	public static final float SIZE_MIN = 10f;
	public static final float SIZE_MAX = 16f;
	public static final float SPEED_MIN = 50f;
	public static final float SPEED_MAX = 200f;
	public static final float SPEED_ROTATION = 500f;
	
	public static void createScrapExplosion(HvlCoord2D locationArg, boolean bigArg, Color... colorsArg){
		for(int i = 0; i < (bigArg ? 8 : 4); i++){
			float direction = HvlMath.randomInt(360);
			float speedMagnitude = HvlMath.randomFloatBetween(SPEED_MIN, SPEED_MAX);
			Color color = colorsArg[HvlMath.randomInt(colorsArg.length)];
			HvlCoord2D speed = new HvlCoord2D((float)Math.cos(Math.toRadians(direction)) * speedMagnitude, (float)Math.sin(Math.toRadians(direction)) * speedMagnitude);
			new ParticleScrap(new HvlCoord2D(locationArg), speed, color);
		}
	}
	
	private Color color;
	private float angle, angleSpeed, size;
	
	public ParticleScrap(HvlCoord2D locationArg, HvlCoord2D speedArg, Color colorArg){
		super(locationArg, speedArg, DURATION_LIFETIME, Main.INDEX_SCRAP_0);
		color = colorArg;
		angle = HvlMath.randomInt(360);
		angleSpeed = HvlMath.randomFloatBetween(-SPEED_ROTATION, SPEED_ROTATION);
		size = HvlMath.randomFloatBetween(SIZE_MIN, SIZE_MAX);
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
