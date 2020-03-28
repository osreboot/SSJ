package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Asteroid {

	public PhysicsObject physicsObject;


	public boolean hasJr = false;
	private int typeHandler;
	Asteroid jr = null;

	public Asteroid(HvlCoord2D pos, boolean isJr, float parSize, ArrayList<Asteroid> toAdd) {
		float rotationArg = HvlMath.randomFloatBetween(0, 3.14f);
		float rotationSpeedArg = HvlMath.randomFloatBetween(-100, 100);
		
		float sizeArg = isJr ? HvlMath.randomFloatBetween(parSize/4, parSize/2) : HvlMath.randomFloatBetween(10, 100);

		assignType(isJr);

		physicsObject = new PhysicsObject(pos.x, pos.y, rotationArg, sizeArg);
		physicsObject.angleSpeed = rotationSpeedArg;
		physicsObject.alliance = Alliance.ENEMY;
		physicsObject.damage = 100f;
		physicsObject.canReceiveDamage = false;
		physicsObject.canDealDamage = false;

		if(hasJr){
			jr = new Asteroid(new HvlCoord2D(physicsObject.location.x, physicsObject.location.y), true, physicsObject.radius, toAdd);
			jr.physicsObject.location.x += physicsObject.radius + jr.physicsObject.radius;
			jr.physicsObject.connectToParent(physicsObject);
		}

		toAdd.add(this);
	}

	public void assignType(boolean isJr) {
		if(!hasJr && !isJr) {
			typeHandler = HvlMath.randomIntBetween(0, 100);
			if(typeHandler < 20){
				hasJr = true;
			}else {
				hasJr = false;
			}
		}
	}

	public void update(float delta) {
		physicsObject.update(delta);
		if(physicsObject.isDead()){
			jr.physicsObject.disconnectFromParent();
		}
		
		if(hasJr){
			if(jr.physicsObject.isDead()){
				jr = null;
				hasJr = false;
			} else jr.physicsObject.update(delta);
		}
	}

	public void draw() {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_ASTEROID));
		hvlResetRotation();

		if(hasJr){
			hvlRotate(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.getVisualAngle());
			hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius * 2f, jr.physicsObject.radius * 2f, Main.getTexture(Main.INDEX_ASTEROID));
			hvlResetRotation();
		}

	}


}
