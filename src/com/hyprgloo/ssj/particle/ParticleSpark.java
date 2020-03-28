package com.hyprgloo.ssj.particle;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Particle;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class ParticleSpark extends Particle{

	public static final float DURATION_LIFETIME = 1f;
	public static final float SIZE = 16f;
	
	public Color color;
	
	public ParticleSpark(HvlCoord2D locationArg, HvlCoord2D speedArg, Color colorArg){
		super(locationArg, speedArg, DURATION_LIFETIME, SIZE, Main.INDEX_SPARK, Main.INDEX_SPARK_EMISSIVE);
		color = colorArg;
	}

	@Override
	public float getDirection(){
		return HvlMath.fullRadians(new HvlCoord2D(), speed);
	}

	@Override
	public Color getColor(){
		return new Color(color.r, color.g, color.b, (life / maxLife));
	}

}
