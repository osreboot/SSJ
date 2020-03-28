package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Asteroid {
	
	private float rotationSpeed;
	public PhysicsObject physicsObject;
	
	
	public boolean hasJr = false;
	private int typeHandler;
	private boolean typeAssigned = false;
	private boolean jrSpawned = false;
	private boolean jrType;
	Asteroid jr = null;

	public Asteroid(HvlCoord2D pos, boolean jrArg) {
	    Asteroid jr;
		float rotationArg = HvlMath.randomFloatBetween(0, 3.14f);
		float rotationSpeedArg = HvlMath.randomFloatBetween(-200, 200);
		float sizeArg = (jrArg)?9999:HvlMath.randomFloatBetween(50, 500);
		
		rotationSpeed = rotationSpeedArg;
		jrType = jrArg;
		physicsObject = new PhysicsObject(pos.x, pos.y, rotationArg, sizeArg);
		 
	}
	
	public void assignType() {
		if(!typeAssigned) {
			hasJr = true;
			typeAssigned = true;
		}
	}
	
	public void update(float delta) {
		if(hasJr) {
			if(!jrSpawned) {
				jr = new Asteroid(new HvlCoord2D(physicsObject.location.x + 100, physicsObject.location.y + 100), true);
				jr.physicsObject.connectToParent(physicsObject);
				jrSpawned = true;
			}
		}
	
		jr.physicsObject.update(delta);
		physicsObject.update(delta);
		
		
	}
	
	public void draw() {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, Game.globalTimer * rotationSpeed);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius, physicsObject.radius, Main.getTexture(Main.INDEX_ASTEROID));
		if(jrSpawned) {
		hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius, jr.physicsObject.radius, Main.getTexture(Main.INDEX_ASTEROID));
		}
		hvlResetRotation();
		
	}
	
	
}
