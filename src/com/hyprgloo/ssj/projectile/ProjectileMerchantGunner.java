package com.hyprgloo.ssj.projectile;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Projectile;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.osreboot.ridhvl.HvlCoord2D;

public class ProjectileMerchantGunner extends Projectile{

	public static final float SIZE = 5f;
	public static final float DURATION_LIFE = 1f;
	
	private float life;
	
	public ProjectileMerchantGunner(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg){
		super(locationArg, speedArg, rotationArg, SIZE / 2f, true);
		physicsObject.alliance = Alliance.FRIENDLY;
		physicsObject.damage = 10f;
		life = DURATION_LIFE;
	}

	@Override
	public void update(float delta){
		physicsObject.update(delta);
		life -= delta;
		if(life == 0) physicsObject.hurt(100f);
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, SIZE, SIZE, Color.white);
		hvlResetRotation();
	}

}
