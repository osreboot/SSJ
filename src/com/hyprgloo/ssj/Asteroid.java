package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Asteroid {
	
	private float rotationSpeed;
	public PhysicsObject physicsObject;
	
	public int asteroidType;
	private int typeHandler;
	private boolean typeAssigned = false;

	public Asteroid(HvlCoord2D pos, boolean jrArg) {
		
		float rotationArg = HvlMath.randomFloatBetween(0, 3.14f);
		float rotationSpeedArg = HvlMath.randomFloatBetween(-200, 200);
		float sizeArg = (jrArg)?HvlMath.randomFloatBetween(50, 500):HvlMath.randomFloatBetween(50, 500);
		
		rotationSpeed = rotationSpeedArg;
		
		physicsObject = new PhysicsObject(pos.x, pos.y, rotationArg, sizeArg);
		physicsObject.alliance = Alliance.ENEMY;
		physicsObject.damage = 50f;
		 
	}
	
	public void assignType() {
		if(!typeAssigned) {
			
			typeHandler = HvlMath.randomIntBetween(0, 100);
			if(typeHandler > 20) {
				asteroidType = 1;
			}else {
				asteroidType = 2;
			}
			typeAssigned = true;
		}
	}
	
	public void update(float delta) {
		
		if(asteroidType == 1) {
			Asteroid jr = new Asteroid(new HvlCoord2D(physicsObject.location.x, physicsObject.location.y), true);
			jr.physicsObject.connectToParent(physicsObject);
		}
		
		physicsObject.update(delta);
		
	}
	
	public void draw() {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, Game.globalTimer * rotationSpeed);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius, physicsObject.radius, Main.getTexture(Main.INDEX_ASTEROID));
		hvlResetRotation();
		
	}
	
	
}
