package com.hyprgloo.ssj;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;

public class Player {

	public static final float ACCELERATION = 100f;
	
	public PhysicsObject physicsObject;
	
	public Player(){
		physicsObject = new PhysicsObject(0f, 0f, 0f, 100f);
	}
	
	public void update(float delta){
		float xsInput = (Keyboard.isKeyDown(Keyboard.KEY_A) ? -ACCELERATION : 0) + (Keyboard.isKeyDown(Keyboard.KEY_D) ? ACCELERATION : 0);
		float ysInput = (Keyboard.isKeyDown(Keyboard.KEY_W) ? -ACCELERATION : 0) + (Keyboard.isKeyDown(Keyboard.KEY_S) ? ACCELERATION : 0);
		physicsObject.speed.add(xsInput * delta, ysInput * delta);
		
		physicsObject.update(delta);
	}
	
	public void draw(float delta){
		hvlRotate(physicsObject.getPosition().x, physicsObject.getPosition().y, physicsObject.getAngle());
		hvlDrawQuadc(physicsObject.getPosition().x, physicsObject.getPosition().y, physicsObject.getRadius() * 2f, physicsObject.getRadius() * 2f, Color.blue);
		hvlResetRotation();
	}
	
}
