package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.osreboot.ridhvl.HvlCoord2D;

public class Asteroid {

	private float rotationSpeed;
	public PhysicsObject physicsObject;
	
	private boolean typeAssigned;

	public Asteroid(float xArg, float yArg, float xSpeedArg, float ySpeedArg, float rotationArg, float rotationSpeedArg, float sizeArg) {
		
		rotationSpeed = rotationSpeedArg;
		
		physicsObject = new PhysicsObject(xArg, yArg, rotationArg, sizeArg);
		 
	}
	
	public void assignType() {
		if(!typeAssigned) {
			
			
			
		}
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
