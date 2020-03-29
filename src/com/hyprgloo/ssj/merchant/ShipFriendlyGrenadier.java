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
	
	public static final Color COLOR_SCRAP = Color.blue;
	
	private float shotCooldown;
	
	public ShipFriendlyGrenadier(float xArg, float yArg, float angleArg){
		super(xArg, yArg, angleArg, SIZE, COLOR_SCRAP);
		shotCooldown = 0f;
	}

	@Override
	public void updateConnected(float delta){
		shotCooldown = HvlMath.stepTowards(shotCooldown, delta, 0f);
		if(shotCooldown == 0f && Mouse.isButtonDown(0)){
			shotCooldown = DURATION_SHOT_COOLDOWN;
			HvlCoord2D projectileSpeed = HvlCursor.getCursorPosition().addNew(Game.player.getBaseLocation()).add(-Display.getWidth()/2, -Display.getHeight()/2)
					.subtract(physicsObject.location).normalize().mult(SPEED_PROJECTILE);
			new ProjectileMerchantGrenadier(physicsObject.location, projectileSpeed, physicsObject.getVisualAngle());
		}
	}

	@Override
	public void draw(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_SHIP));
		if(!physicsObject.hasParent())
			hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 4f, physicsObject.radius * 4f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_LAUNCH));
		hvlResetRotation();
		
		if(physicsObject.hasParent()) {
			hvlRotate(physicsObject.location.x, physicsObject.location.y, HvlMath.fullRadians(HvlCursor.getCursorPosition(), physicsObject.location));
			hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 4f, physicsObject.radius * 4f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_LAUNCH));
			hvlResetRotation();
		}
	}

	@Override
	public void drawEmissive(float delta){
		hvlRotate(physicsObject.location.x, physicsObject.location.y, physicsObject.getVisualAngle());
		hvlDrawQuadc(physicsObject.location.x, physicsObject.location.y, physicsObject.radius * 2f, physicsObject.radius * 2f, Main.getTexture(Main.INDEX_FRIENDLY_MISSILE_SHIP_EMMISSIVE));
		hvlResetRotation();
	}

}
