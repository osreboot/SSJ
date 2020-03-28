package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.Player;
import com.hyprgloo.ssj.ShipEnemy;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.osreboot.ridhvl.HvlMath;

public class ShipEnemyConvoy extends ShipEnemy{

	public static final float SIZE = 30f;
	
	public boolean hasJr = false;
	private int typeHandler;
	private boolean typeAssigned = false;
	ShipEnemyConvoy jr = null;
	
	public ShipEnemyConvoy(float xArg, float yArg, float angleArg, boolean isJr) {
		super(xArg, yArg, angleArg, SIZE, 1000f, 50f, 125f);
		physicsObject.alliance = Alliance.ENEMY;
		physicsObject.damage = 50f;
		
		assignType(isJr);
		
		if(hasJr){
			jr = new ShipEnemyConvoy(physicsObject.location.x, physicsObject.location.y, physicsObject.angleSpeed, true);
			jr.physicsObject.location.x += physicsObject.radius + jr.physicsObject.radius;
			jr.physicsObject.connectToParent(physicsObject);
		}
	}
	
	public void assignType(boolean isJr) {
		if(!hasJr) {
			typeHandler = HvlMath.randomIntBetween(0, 100);
			if(typeHandler < 10){
				hasJr = true;
			}else {
				hasJr = false;
			}
		}
	}

	@Override
	public void update(float delta, Player playerArg) {
		super.update(delta, playerArg);
		physicsObject.setBaseAngle((float)Math.toDegrees(HvlMath.fullRadians(physicsObject.location, playerArg.getBaseLocation())) - 90f);
		
		if(hasJr){
			if(jr.physicsObject.isDead()){
				jr = null;
				hasJr = false;
			}else jr.physicsObject.update(delta);
		}
	}

	@Override
	public void draw(float delta) {
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_CONVOY_ENEMY));
		hvlResetRotation();
		
		if(hasJr){
			hvlRotate(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.getVisualAngle());
			hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius * 2f, jr.physicsObject.radius * 2f, Main.getTexture(Main.INDEX_CONVOY_ENEMY));
			hvlResetRotation();
			
		}
		
	}
	
	
	

}
