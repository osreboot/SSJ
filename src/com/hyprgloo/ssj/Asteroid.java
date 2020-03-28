package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public class Asteroid {
	
	public PhysicsObject physicsObject;

	public Asteroid(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg, float sizeArg) {
		
		physicsObject = new PhysicsObject(locationArg.x, locationArg.y, rotationArg, sizeArg);
		
	}
	
	public void update(float delta) {
		physicsObject.update(delta);
	}
	
	public void draw() {
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius, physicsObject.radius, Color.white);
	}
	
	
}
