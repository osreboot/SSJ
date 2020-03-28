package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.ShipEnemy;
import com.hyprgloo.ssj.projectile.ProjectileEnemyGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;

public class ShipEnemyGunner extends ShipEnemy{

	public static final float SIZE = 16f;
	public static final float DURATION_SHOT_COOLDOWN = 1f;
	public static final float SPEED_PROJECTILE = 250f;
	
	private float shotCooldown;
	
	public ShipEnemyGunner(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE, 1000f, 300f, 600f);
	}
	
	@Override
	public void update(float delta){
		super.update(delta);
		
		shotCooldown = HvlMath.stepTowards(shotCooldown, delta, 0f);
		if(shotCooldown == 0f){
			shotCooldown = DURATION_SHOT_COOLDOWN;
			HvlCoord2D projectileSpeed = Game.player.getBaseLocation().subtractNew(physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileEnemyGunner(physicsObject.location, projectileSpeed, 0f);
		}
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_PLAYER_SHIP));
		hvlResetRotation();
	}

}
