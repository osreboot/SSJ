package com.hyprgloo.ssj.merchant;

import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlDrawQuadc;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlResetRotation;
import static com.osreboot.ridhvl.painter.painter2d.HvlPainter2D.hvlRotate;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import com.hyprgloo.ssj.Game;
import com.hyprgloo.ssj.Main;
import com.hyprgloo.ssj.ShipFriendly;
import com.hyprgloo.ssj.projectile.ProjectileMerchantGunner;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.HvlCursor;

public class ShipFriendlyGunner extends ShipFriendly{

	public static final float SIZE = 16f;
	public static final float DURATION_SHOT_COOLDOWN = 0.2f;
	public static final float SPEED_PROJECTILE = 500f;

	public static final Color COLOR_SCRAP_0 = new Color(0.06f, 0.7f, 0.94f);
	public static final Color COLOR_SCRAP_1 = new Color(1.0f, 0.6f, 0.15f);
	public static final Color COLOR_SCRAP_2 = new Color(0.1f, 1f, 1f);
	public static final Color COLOR_EMISSIVE = new Color(1f, 1f, 1f, 0.5f);

	private float shotCooldown, lastAngle;

	public ShipFriendlyGunner(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE, COLOR_SCRAP_0, COLOR_SCRAP_1, COLOR_SCRAP_2);
		shotCooldown = 0f;
		lastAngle = 0f;
	}

	@Override
	public void updateConnected(float delta){
		shotCooldown = HvlMath.stepTowards(shotCooldown, delta, 0f);
		if(shotCooldown == 0f && Mouse.isButtonDown(0)){
			shotCooldown = DURATION_SHOT_COOLDOWN;
			Main.getSound(Main.INDEX_PEW).playAsSoundEffect(HvlMath.randomFloatBetween(0.85f, 1f), 0.12f, false);
			HvlCoord2D projectileSpeed = HvlCursor.getCursorPosition().addNew(Game.player.getBaseLocation()).add(-Display.getWidth()/2, -Display.getHeight()/2)
					.subtract(physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileMerchantGunner(physicsObject.location, projectileSpeed, 0f);
		}
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_GUN_SHIP));
		hvlResetRotation();

		if(physicsObject.hasParent()){
			HvlCoord2D target = HvlCursor.getCursorPosition().addNew(Game.camera.getX(), Game.camera.getY()).add(Game.camera.getAlignment());
			lastAngle = (float)Math.toDegrees(HvlMath.fullRadians(target, physicsObject.location));
		}

		hvlRotate(physicsObject.location.x, physicsObject.location.y, lastAngle - 90f);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_GUN));
		hvlResetRotation();
	}

	@Override
	public void drawEmissive(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_GUN_SHIP_EMMISSIVE), COLOR_EMISSIVE);
		hvlResetRotation();
	}

}
