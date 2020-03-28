package com.hyprgloo.ssj.particle;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Particle;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class ParticleSpark extends Particle{

	public static final float DURATION_LIFETIME = 0.4f;
	public static final float SIZE = 8f, SPEED = 300f;
	
	public static void createSparkExplosion(HvlCoord2D locationArg, Color colorArg){
		for(int i = 0; i < 4; i++){
			float direction = HvlMath.randomInt(360);
			HvlCoord2D speed = new HvlCoord2D((float)Math.cos(Math.toRadians(direction)) * SPEED, (float)Math.sin(Math.toRadians(direction)) * SPEED);
			new ParticleSpark(new HvlCoord2D(locationArg), speed, colorArg);
		}
	}
	
	private Color color;
	
	public ParticleSpark(HvlCoord2D locationArg, HvlCoord2D speedArg, Color colorArg){
		super(locationArg, speedArg, DURATION_LIFETIME, SIZE, Main.INDEX_SPARK, Main.INDEX_SPARK_EMISSIVE);
		color = colorArg;
	}

	@Override
	public float getDirection(){
		return (float)Math.toDegrees(HvlMath.fullRadians(new HvlCoord2D(), speed)) - 90f;
	}

	@Override
	public Color getColor(){
		return new Color(color.r, color.g, color.b, (life / maxLife));
	}

}
