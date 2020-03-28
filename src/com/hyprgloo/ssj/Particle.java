package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public abstract class Particle {

	public HvlCoord2D location, speed;
	
	public float life, maxLife, size;
	public int texture, textureEmissive;
	
	public Particle(HvlCoord2D locationArg, HvlCoord2D speedArg, float maxLifeArg, float sizeArg, int textureArg, int textureEmissiveArg){
		location = locationArg;
		speed = speedArg;
		maxLife = maxLifeArg;
		life = maxLifeArg;
		size = sizeArg;
		texture = textureArg;
		textureEmissive = textureEmissiveArg;
		
		Game.particles.add(this);
	}
	
	public abstract float getDirection();
	
	public abstract Color getColor();
	
	public void update(float delta){
		life -= delta;
		location.add(speed.multNew(delta));
	}
	
	public void draw(float delta){
		hvlRotate(location, getDirection());
		hvlDrawQuadc(location.x, location.y, size, size, Main.getTexture(texture), getColor());
		hvlResetRotation();
	}
	
	public void drawEmissive(float delta){
		hvlRotate(location, getDirection());
		hvlDrawQuadc(location.x, location.y, size, size, Main.getTexture(textureEmissive), getColor());
		hvlResetRotation();
	}
	
	public boolean isDead(){
		return life <= 0f;
	}
	
}
