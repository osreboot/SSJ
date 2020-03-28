package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public class Asteroid {

	float rotationSpeed;
	
	private PhysicsObject physicsObject;

	public Asteroid(float xArg, float yArg, float xSpeedArg, float ySpeedArg, float rotationArg, float rotationSpeedArg, float sizeArg) {
		
		rotationSpeed = rotationSpeedArg;
		
		physicsObject = new PhysicsObject(xArg, yArg, rotationArg, sizeArg);
		 
	}
	
	public void update(float delta) {
		physicsObject.update(delta);
	}
	
	public void draw() {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, Game.globalTimer * rotationSpeed);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius, physicsObject.radius, Main.getTexture(Main.INDEX_ASTEROID));
		hvlResetRotation();
		
	}
	
	
}
