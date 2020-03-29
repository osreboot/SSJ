package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuad;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.physics.PhysicsObjectAsteroid;
import com.hyprgloo.ssj.projectile.ProjectileMerchantGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class Asteroid {

	public static final Color COLOR_HALO = new Color(0.5f, 0.5f, 0.5f, 0.5f);
	
	public PhysicsObject physicsObject;
	private float tempHealth = 0.00001f;
	private float impactFlash = 0f;

	public boolean hasJr = false;
	private int typeHandler;
	Asteroid jr = null;
	
	private int texture, textureJr;

	public Asteroid(HvlCoord2D pos, boolean isJr, float parSize, ArrayList<Asteroid> toAdd) {
		float rotationArg = HvlMath.randomFloatBetween(0, 3.14f);
		float rotationSpeedArg = HvlMath.randomFloatBetween(-100, 100);
		
		float sizeArg = isJr ? HvlMath.randomFloatBetween(parSize/4, parSize/2) : HvlMath.randomFloatBetween(50, 120);

		assignType(isJr);
		
		texture = Main.INDEX_ASTEROID0 + HvlMath.randomInt(3);
		textureJr = Main.INDEX_ASTEROID0 + HvlMath.randomInt(3);

		physicsObject = new PhysicsObjectAsteroid(pos.x, pos.y, rotationArg, sizeArg);
		physicsObject.angleSpeed = rotationSpeedArg;
		physicsObject.alliance = Alliance.ENEMY;
		physicsObject.damage = 100f;
		physicsObject.health += sizeArg * 2 ;
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

	public void draw(float delta) {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
//		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f * 1.333333f, physicsObject.radius * 2f * 1.333333f, Main.getTexture(Main.INDEX_ASTEROID_HALO), COLOR_HALO);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f * 1.333333f, physicsObject.radius * 2f * 1.333333f, Main.getTexture(texture));
		hvlResetRotation();

		if(hasJr){
			hvlRotate(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.getVisualAngle());
//			hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius * 2f * 1.333333f, jr.physicsObject.radius * 2f * 1.333333f, Main.getTexture(Main.INDEX_ASTEROID_HALO), COLOR_HALO);
			hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius * 2f * 1.333333f, jr.physicsObject.radius * 2f * 1.333333f, Main.getTexture(textureJr));
			hvlResetRotation();
		}
		
		if(physicsObject.health < tempHealth) {
			impactFlash = 0.7f;
		}
		if(impactFlash >= 0f) {
			impactFlash -= delta*2;
			hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
			if(texture == Main.INDEX_ASTEROID0) {
			hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f * 1.333333f, physicsObject.radius * 2f * 1.333333f, Main.getTexture(Main.INDEX_ASTEROID0_HURT), new Color(1f, 1f, 1f, impactFlash));
			}else if(texture == Main.INDEX_ASTEROID1) {
			hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f * 1.333333f, physicsObject.radius * 2f * 1.333333f, Main.getTexture(Main.INDEX_ASTEROID1_HURT), new Color(1f, 1f, 1f, impactFlash));
			}else if(texture == Main.INDEX_ASTEROID2) {
			hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f * 1.333333f, physicsObject.radius * 2f * 1.333333f, Main.getTexture(Main.INDEX_ASTEROID2_HURT), new Color(1f, 1f, 1f, impactFlash));
			}
			hvlResetRotation();
		}
		
		tempHealth = physicsObject.health;

	}


}
