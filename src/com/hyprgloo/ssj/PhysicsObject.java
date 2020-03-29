package com.hyprgloo.ssj;

import java.util.ArrayList;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.particle.ParticleSpark;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class PhysicsObject {

	private static final float MAX_ROTATE = 100;

	public static enum Alliance{
		NEUTRAL, FRIENDLY, ENEMY
	}

	public HvlCoord2D location, speed;
	public float angleSpeed, radius;

	private float angle;

	private PhysicsObject parent;
	private ArrayList<PhysicsObject> children;
	private float connectionDistance, connectionAngle;

	public Alliance alliance;
	public float damage, health;
	public boolean canDealDamage = true, canReceiveDamage = true, isSolid = true;

	public PhysicsObject(float xArg, float yArg, float angleArg, float radiusArg){
		location = new HvlCoord2D(xArg, yArg);
		speed = new HvlCoord2D();
		angle = angleArg;
		radius = radiusArg;
		health = 100f;
		alliance = Alliance.NEUTRAL;

		children = new ArrayList<>();
		
		Game.physicsObjects.add(this);
	}

	public void connectToParent(PhysicsObject parentArg){
		if(!hasParent()){
			parent = parentArg;
			connectionDistance = HvlMath.distance(parent.location, location);
			connectionAngle = (float)Math.toDegrees(HvlMath.fullRadians(location, parent.location)) - parent.getVisualAngle();
			speed.x = 0;
			speed.y = 0;
			angleSpeed = 0;
			angle = -parent.getVisualAngle() - connectionAngle + angle;
			
			parent.children.add(this);
		}
	}

	public void disconnectFromParent(){
		if(hasParent()){
			parent.children.remove(this);
			
			speed = new HvlCoord2D(parent.speed);
			angle = getVisualAngle();
			parent = null;
		}
	}

	public boolean hasParent(){
		return parent != null;
	}

	public float getVisualAngle(){
		if(hasParent())
			return angle + parent.getVisualAngle() + connectionAngle;
		else return angle;
	}

	public void setBaseAngle(float angleArg){
		angle = angleArg;
	}

	public void update(float delta){
		if(hasParent()){
			if(parent.isDead()){
				disconnectFromParent();
			}else{
				speed.x = 0;
				speed.y = 0;
				angleSpeed = 0;
				location.x = parent.location.x + ((float)Math.cos(Math.toRadians(parent.getVisualAngle() + connectionAngle)) * connectionDistance);
				location.y = parent.location.y + ((float)Math.sin(Math.toRadians(parent.getVisualAngle() + connectionAngle)) * connectionDistance);
			}
		}else{
			location.add(speed.multNew(delta));
			angle += angleSpeed * delta;
			if(angleSpeed >= MAX_ROTATE) angleSpeed = MAX_ROTATE;
			else if(angleSpeed <= -MAX_ROTATE) angleSpeed = -MAX_ROTATE;
		}
	}

	public boolean collidesWith(float xArg, float yArg, float radiusArg){
		return HvlMath.distance(location.x, location.y, xArg, yArg) < radius + radiusArg;
	}

	public boolean collidesWith(PhysicsObject physicsObjectArg){
		return collidesWith(physicsObjectArg.location.x, physicsObjectArg.location.y, physicsObjectArg.radius);
	}

	public boolean collidesWith(float xArg, float yArg){
		return collidesWith(xArg, yArg, 0f);
	}

	public boolean isDead(){
		return health <= 0f;
	}

	public void hurt(float damageArg){
		health -= damageArg;
		if(HvlMath.distance(location, Game.player.getBaseLocation())*10 < 5){
			Main.getSound(Main.INDEX_HURTY).playAsSoundEffect(HvlMath.randomFloatBetween(1.3f, 1.5f), 0.3f, false);
			ParticleSpark.createSparkExplosion(Game.player.physicsObject.location, Color.yellow);
		}
		
	}

	public void onDeath(){}

	public void onCollision(PhysicsObject physicsObjectArg){
		hurt(physicsObjectArg.damage);
	}
	
	public ArrayList<PhysicsObject> getChildren(){
		return children;
	}

}
