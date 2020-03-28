package com.hyprgloo.ssj.projectile;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.Projectile;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.HvlCursor;

public class ProjectileMerchantGrenadier extends Projectile{

	public static final float SIZE = 10f;
	public static final float DURATION_LIFE = 5f;
	public static final float SPEED_ROTATION = 240f;
	public static final float SPEED_TRANSLATION = 400f;
	
	public static final Color COLOR_SPARKS = Color.white;
	
	private float angle, life;
	
	public ProjectileMerchantGrenadier(HvlCoord2D locationArg, HvlCoord2D speedArg, float rotationArg){
		super(locationArg, speedArg, rotationArg, SIZE / 2f, true, COLOR_SPARKS);
		physicsObject.alliance = Alliance.FRIENDLY;
		physicsObject.damage = 50f;
		life = DURATION_LIFE;
		
		angle = rotationArg;
	}

	@Override
	public void update(float delta){
		HvlCoord2D target = HvlCursor.getCursorPosition().addNew(Game.camera.getX(), Game.camera.getY()).add(Game.camera.getAlignment());
		float targetAngle = (float)Math.toDegrees(HvlMath.fullRadians(target, physicsObject.location));
		
		while(Math.abs(targetAngle - angle) > 180f){
			if(Math.abs(targetAngle - 360f - angle) < Math.abs(targetAngle + 360f - angle))
					targetAngle -= 360f;
			else targetAngle += 360f;
		}
		
		angle = HvlMath.stepTowards(angle, delta * SPEED_ROTATION, targetAngle);
		physicsObject.setBaseAngle(angle);
		physicsObject.speed = new HvlCoord2D((float)Math.cos(Math.toRadians(angle)), (float)Math.sin(Math.toRadians(angle))).mult(SPEED_TRANSLATION);
		
		physicsObject.update(delta);
		life -= delta;
		if(life <= 0) physicsObject.hurt(100f);
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location, physicsObject.getVisualAngle() + 90f);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, SIZE, SIZE * 2f, Color.white);
		hvlResetRotation();
	}
	
	@Override
	public void drawEmissive(float delta){
		// TODO
	}

}