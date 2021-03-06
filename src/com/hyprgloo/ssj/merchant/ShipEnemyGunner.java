package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.ShipEnemy;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.Player;
import com.hyprgloo.ssj.projectile.ProjectileEnemyGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class ShipEnemyGunner extends ShipEnemy{

	public static final float SIZE = 16f;
	public static final float DURATION_SHOT_COOLDOWN = 1f;
	public static final float SPEED_PROJECTILE = 250f;
	
	public static final Color COLOR_SCRAP_0 = Color.lightGray;
	public static final Color COLOR_SCRAP_1 = new Color(0.2f, 0.6f, 0.2f);
	
	public boolean hasJr = false;
	private int typeHandler;
	ShipEnemyGunner jr = null;
	
	private float shotCooldown;
	
	public ShipEnemyGunner(float xArg, float yArg, float angleArg, boolean isJr){
		super(xArg, yArg, angleArg, SIZE, 500f, 200f, 300f, false, COLOR_SCRAP_0, COLOR_SCRAP_1);
		physicsObject.alliance = Alliance.ENEMY;
		physicsObject.damage = 50f;
		
		assignType(isJr);
		
		if(hasJr){
			jr = new ShipEnemyGunner(physicsObject.location.x, physicsObject.location.y, physicsObject.angleSpeed, true);
			jr.physicsObject.location.x += physicsObject.radius + jr.physicsObject.radius;
			jr.physicsObject.connectToParent(physicsObject);
		}
		
		
	}
	
	public void assignType(boolean isJr) {
		if(!hasJr) {
			typeHandler = HvlMath.randomIntBetween(0, 100);
			if(typeHandler < 15){
				hasJr = true;
			}else {
				hasJr = false;
			}
		}
	}
	
	@Override
	public void update(float delta, Player playerArg){
		super.update(delta, playerArg);
		physicsObject.setBaseAngle((float)Math.toDegrees(HvlMath.fullRadians(physicsObject.location, playerArg.getBaseLocation())) - 90f);
		shotCooldown = HvlMath.stepTowards(shotCooldown, delta, 0f);
		if(shotCooldown == 0f){
			shotCooldown = DURATION_SHOT_COOLDOWN;
			HvlCoord2D projectileSpeed = Game.player.getBaseLocation().subtractNew(physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileEnemyGunner(physicsObject.location, projectileSpeed, 0f);
		}
		
		
		
		
		
		if(hasJr) {
		jr.shotCooldown = HvlMath.stepTowards(jr.shotCooldown, delta, 0f);
		if(jr.shotCooldown == 0f){
			jr.shotCooldown = DURATION_SHOT_COOLDOWN;
			HvlCoord2D projectileSpeed = Game.player.getBaseLocation().subtractNew(jr.physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileEnemyGunner(jr.physicsObject.location, projectileSpeed, 0f);
		}
		}
		
		
		
		
		
		
		if(hasJr){
			if(jr.physicsObject.isDead()){
				jr = null;
				hasJr = false;
			}else jr.physicsObject.update(delta);
		}
	}
	


	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_ENEMY_GUNNER_SHIP));
		hvlResetRotation();
		
		if(hasJr){
			hvlRotate(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.getVisualAngle());
			hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius * 2f, jr.physicsObject.radius * 2f, Main.getTexture(Main.INDEX_ENEMY_GUNNER_SHIP));
			hvlResetRotation();
			
		}
	}
	
	@Override
	public void drawEmissive(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_ENEMY_GUNNER_SHIP_EMISSIVE));
		hvlResetRotation();
		
		if(hasJr){
			hvlRotate(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.getVisualAngle());
			hvlDrawQuadc(jr.physicsObject.location.x, jr.physicsObject.location.y, jr.physicsObject.radius * 2f, jr.physicsObject.radius * 2f, Main.getTexture(Main.INDEX_ENEMY_GUNNER_SHIP_EMISSIVE));
			hvlResetRotation();
		}
	}

}
