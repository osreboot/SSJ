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
import com.hyprgloo.ssj.projectile.ProjectileMerchantGrenadier;
import com.osreboot.ridhvl.HvlCoord2D;
import com.osreboot.ridhvl.HvlMath;
import com.osreboot.ridhvl.painter.HvlCursor;

public class ShipFriendlyGrenadier extends ShipFriendly{
	
	public static final float SIZE = 16f;
	public static final float DURATION_SHOT_COOLDOWN = 2.5f;
	public static final float SPEED_PROJECTILE = 500f;
	
	public static final Color COLOR_SCRAP_0 = new Color(0.61f, 0.21f, 0.67f);
	public static final Color COLOR_SCRAP_1 = new Color(1.0f, 0.6f, 0.15f);
	public static final Color COLOR_SCRAP_2 = new Color(0f, 1f, 1f);
	public static final Color COLOR_EMISSIVE = new Color(1f, 1f, 1f, 0.5f);
	
	private float shotCooldown, turretAngle;
	private float soundCooldown = 0.1f;
	
	public ShipFriendlyGrenadier(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE, COLOR_SCRAP_0, COLOR_SCRAP_1, COLOR_SCRAP_2);
		shotCooldown = 0f;
		turretAngle = 0f;
	}

	@Override
	public void updateConnected(float delta){
		shotCooldown = HvlMath.stepTowards(shotCooldown, delta, 0f);
		if(soundCooldown > 0.1f) {
			soundCooldown = 0.1f;
		}
		if(shotCooldown == 0f && Mouse.isButtonDown(0)){
			if(soundCooldown >= 0.1f) {
			Main.getSound(Main.INDEX_PEW).playAsSoundEffect(HvlMath.randomFloatBetween(0.85f, 1f),0.05f, false);
			soundCooldown += delta;
			}
			shotCooldown = DURATION_SHOT_COOLDOWN;
			HvlCoord2D projectileSpeed = HvlCursor.getCursorPosition().addNew(Game.player.getBaseLocation()).add(-Display.getWidth()/2, -Display.getHeight()/2)
					.subtract(physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileMerchantGrenadier(physicsObject.location, projectileSpeed, physicsObject.getVisualAngle() + turretAngle);
		}
		
		turretAngle += delta * 50f;
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_SHIP));
		hvlResetRotation();
		
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle() + turretAngle - 90f);
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_LAUNCH));
		hvlResetRotation();
	}

	@Override
	public void drawEmissive(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_SHIP_EMMISSIVE), COLOR_EMISSIVE);
		hvlResetRotation();
	}

}
