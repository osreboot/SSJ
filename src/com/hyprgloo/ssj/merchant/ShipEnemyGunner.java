package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.ShipEnemy;

public class ShipEnemyGunner extends ShipEnemy{

	public static final float SIZE = 16f;
	public static final float DURATION_SHOT_COOLDOWN = 0.2f;
	public static final float SPEED_PROJECTILE = 500f;
	
	private float shotCooldown;
	
	public ShipEnemyGunner(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE, 300f, 100f, 200f);
		shotCooldown = 0f;
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_PLAYER_SHIP));
		hvlResetRotation();
	}

}
