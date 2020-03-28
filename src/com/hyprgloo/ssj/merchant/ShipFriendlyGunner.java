package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.PhysicsObject.Alliance;
import com.hyprgloo.ssj.ShipFriendly;
import com.hyprgloo.ssj.projectile.ProjectileMerchantGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.HvlCursor;

public class ShipFriendlyGunner extends ShipFriendly{
	
	public static final float SIZE = 16f;
	public static final float DURATION_SHOT_COOLDOWN = 0.2f;
	public static final float SPEED_PROJECTILE = 500f;
	
	private float shotCooldown;
	
	public ShipFriendlyGunner(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE);
		shotCooldown = 0f;
	}

	@Override
	public void updateConnected(float delta){
		shotCooldown = HvlMath.stepTowards(shotCooldown, delta, 0f);
		if(shotCooldown == 0f && Mouse.isButtonDown(0)){
			shotCooldown = DURATION_SHOT_COOLDOWN;
			HvlCoord2D projectileSpeed = HvlCursor.getCursorPosition().addNew(Game.player.getBaseLocation()).add(-Display.getWidth()/2, -Display.getHeight()/2)
					.subtract(physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileMerchantGunner(physicsObject.location, projectileSpeed, 0f);
		}
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_SHIP_0));
		hvlResetRotation();
	}

}
