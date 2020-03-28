package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public abstract class Particle {

	public HvlCoord2D location, speed;

	public float life, maxLife;
	public int texture, textureEmissive;

	public Particle(HvlCoord2D locationArg, HvlCoord2D speedArg, float maxLifeArg, int textureArg, int textureEmissiveArg){
		location = locationArg;
		speed = speedArg;
		maxLife = maxLifeArg;
		life = maxLifeArg;
		texture = textureArg;
		textureEmissive = textureEmissiveArg;

		Game.particles.add(this);
	}

	public Particle(HvlCoord2D locationArg, HvlCoord2D speedArg, float maxLifeArg, int textureArg){
		this(locationArg, speedArg, maxLifeArg, textureArg, -1);
	}

	public abstract float getDirection();

	public abstract Color getColor();
	
	public abstract float getSize();

	public void update(float delta){
		life -= delta;
		location.add(speed.multNew(delta));
	}

	public void draw(float delta){
		hvlRotate(location, getDirection());
		hvlDrawQuadc(location.x, location.y, getSize(), getSize(), Main.getTexture(texture), getColor());
		hvlResetRotation();
	}

	public void drawEmissive(float delta){
		if(textureEmissive != -1){
			hvlRotate(location, getDirection());
			hvlDrawQuadc(location.x, location.y, getSize(), getSize(), Main.getTexture(textureEmissive), getColor());
			hvlResetRotation();
		}
	}

	public boolean isDead(){
		return life <= 0f;
	}

}
